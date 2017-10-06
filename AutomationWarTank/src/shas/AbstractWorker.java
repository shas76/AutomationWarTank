package shas;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.parser.ParserDelegator;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public abstract class AbstractWorker implements Runnable {

	private HttpClient httpclient;
	private String method;
	private long timeOut;
	private String responseBody;
	
	protected String goToURL;
	
	private goToURLFinderParserCallBack parseHTML(
			goToURLFinderParserCallBack parser) throws IOException {
		if (parser == null) {
			AutomationWarTank.Logging("Parser is NULL: ", this);
			goToURL = "";
			method = "GET";
			timeOut = 0;
			return null;
		}
		AutomationWarTank.Logging("Parse using: " + parser.toString(), this);
		ParserDelegator parserDelegator = new ParserDelegator();
		parserDelegator.parse(new StringReader(responseBody), parser, true);
		parser.afterParse();
		goToURL = parser.getURL();
		method = parser.getMethod();
		timeOut = parser.getTimeOut();
		AutomationWarTank.Logging("goToURL=" + goToURL + "   method=" + method,
				this);
		return parser;
	}

	private void readContent(HttpResponse siteResponse)
			throws UnsupportedEncodingException, IllegalStateException,
			IOException {
		HttpEntity entity = siteResponse.getEntity();
		Header contentEncoding = entity.getContentEncoding();
		String charSet = "UTF-8";
		if (contentEncoding != null) {
			charSet = contentEncoding.getValue();
		}
		InputStreamReader isr = new InputStreamReader(entity.getContent(),
				charSet);
		StringWriter sw = new StringWriter();
		char[] charbuffer = new char[1024];
		while (true) {
			int countRead = isr.read(charbuffer);
			if (countRead == -1)
				break;
			if (countRead > 0) {
				sw.write(charbuffer, 0, countRead);
			}
		}
		EntityUtils.consume(entity);
		responseBody = sw.toString();
		AutomationWarTank.Logging(siteResponse.toString(), this);
		if (AutomationWarTank.enableBodyLogging == 1) {
			AutomationWarTank.Logging(sw.toString(), this);
		}
	}

	private int executeHttpGet(String URL) throws Exception {
		AutomationWarTank.Logging("Get Response from:" + URL, this);
		if (("".equals(URL)) || (URL == null)) {
			throw new Exception("Empty URL !!!");
		}
		HttpResponse response = httpclient.execute(new HttpGet(URL));
		if (response.getStatusLine().getStatusCode() == Consts.request_redirected_302) {
			goToURL = getHeaderItem(response.getAllHeaders(),
					Consts.LocationHeader);
		} else {
			readContent(response);
		}
		return response.getStatusLine().getStatusCode();
	}

	private void executeHttpGetAndParse(String URL) throws Exception {

		if (executeHttpGet(URL) == Consts.request_redirected_302) {
			executeHttpGetAndParse(goToURL);
		} else {
			parseHTML(getParserByURL(URL));
		}
	}

	@SuppressWarnings("unused")
	private int executeHttpPost(String URL) throws Exception {
		return executeHttpPost(URL, null);
	}

	private int executeHttpPost(String URL, List<NameValuePair> requestParams)
			throws Exception {
		AutomationWarTank.Logging("Get Response from:" + URL, this);
		if (("".equals(URL)) || (URL == null)) {
			throw new Exception("Empty URL !!!");
		}
		HttpPost httpPost = new HttpPost(URL);
		if (requestParams != null) {
			httpPost.setEntity(new UrlEncodedFormEntity(requestParams));
		}
		HttpResponse response = httpclient.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == Consts.request_redirected_302) {
			goToURL = getHeaderItem(response.getAllHeaders(),
					Consts.LocationHeader);
		} else {
			readContent(response);
		}
		return response.getStatusLine().getStatusCode();
	}

	private void executeHttpPostAndParse(String URL) throws Exception {
		executeHttpPostAndParse(URL, null);
	}

	private void executeHttpPostAndParse(String URL,
			List<NameValuePair> requestParams) throws Exception {
		if (executeHttpPost(URL, requestParams) == Consts.request_redirected_302) {
			executeHttpPostAndParse(goToURL, requestParams);
		} else {
			parseHTML(getParserByURL(goToURL));
		}
	}

	private String getHeaderItem(Header[] headers, String name) {
		for (Header head : headers) {
			if (head.getName().equals(name)) {
				AutomationWarTank.Logging(name + "=" + head.getValue(), this);
				return head.getValue();
			}
		}
		return "";
	}

	private goToURLFinderParserCallBack getParserByURL(String URL) {
		if (URL.toLowerCase().contains(Consts.angarTab.toLowerCase()))
			return new angarParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.battleTab.toLowerCase()))
			return new battleParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.buildingsTab.toLowerCase()))
			return new buildingsParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.convoyTab.toLowerCase()))
			return new convoyParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.mineTab.toLowerCase()))
			return new mineParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.armoryTab.toLowerCase()))
			return new armoryParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.polygonTab.toLowerCase()))
			return new polygonParserCallBack(URL);
		if (URL.toLowerCase().contains(Consts.bankTab.toLowerCase()))
			return new bankParserCallBack(URL);
		if (URL.equals(Consts.siteAddress)
				|| URL.toLowerCase().contains(
						Consts.SHOW_SIGNIN_LINK.toLowerCase()))
			return new loginPageParserCallBack(URL);
		if (isURLBattle(URL))
			return new fightParserCallBack(URL);
		return null;
	}

	protected boolean isURLBattle(String URL) {
		for (String battlePath : AutomationWarTank.battleURLs) {
			if (URL.contains(battlePath)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void run() {
		while (true) {
			httpclient = HttpClients.createDefault();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

			try {

				executeHttpGetAndParse(Consts.siteAddress);
				executeHttpGetAndParse(goToURL);
				// Login for registered users
				nameValuePairs.add(new BasicNameValuePair("id1_hf_0", ""));
				nameValuePairs.add(new BasicNameValuePair("login",
						AutomationWarTank.userName));
				nameValuePairs.add(new BasicNameValuePair("password",
						AutomationWarTank.password));
				executeHttpPostAndParse(goToURL, nameValuePairs);
				if (goToURL.equals("")) {
					goToURL = Consts.siteAddress + Consts.angarTab;
				}
				while (true) {
					AutomationWarTank.Logging("Start next Iteration.", this);
					doWork();
					switch (method) {
					case Consts.POST_METHOD:
						executeHttpPostAndParse(goToURL);
					case Consts.GET_METHOD:
						executeHttpGetAndParse(goToURL);
					}

					AutomationWarTank.Logging("Wating "
							+ (int) (timeOut / 1000) + " seconds.", this);
					Thread.sleep(timeOut);
				}
			} catch (Exception e) {
				AutomationWarTank.Logging(e, this);
				try {
					Thread.sleep(5 * Consts.msInMinunte);
				} catch (InterruptedException e1) {
					AutomationWarTank.Logging(e1, this);
				}
			}
		}
	}

	public abstract void doWork() throws ParseException;
	public abstract void doBeforeWhile() ;

}
