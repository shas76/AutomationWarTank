package workers;

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
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import parsers.angarParserCallBack;
import parsers.armoryParserCallBack;
import parsers.bankParserCallBack;
import parsers.battleParserCallBack;
import parsers.buildingsParserCallBack;
import parsers.convoyParserCallBack;
import parsers.fightParserCallBack;
import parsers.goToURLFinderParserCallBack;
import parsers.loginPageParserCallBack;
import parsers.mineParserCallBack;
import parsers.polygonParserCallBack;
import shas.Consts;
import shas.GlobalVars;

public abstract class AbstractWorker implements Runnable {

	private HttpClient httpclient;
	private List<NameValuePair> nameValuePairs;

	private String method;

	private String responseBody;

	private String goToURL;

	private boolean hasToStop = false;

	public String getGoToURL() {
		return goToURL;
	}

	public void setGoToURL(String goToURL) {
		this.goToURL = goToURL;
	}

	public String getMethod() {
		return method;
	}

	public boolean isHasToStop() {
		return hasToStop;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setHasToStop(boolean hasToStop) {
		this.hasToStop = hasToStop;
	}

	private goToURLFinderParserCallBack parseHTML(
			goToURLFinderParserCallBack parser) throws IOException {
		if (parser == null) {
			GlobalVars.logger.Logging("Parser is NULL: ", this);
			goToURL = "";
			method = "GET";
			return null;
		}
		GlobalVars.logger.Logging("Parse using: " + parser.toString(), this);
		ParserDelegator parserDelegator = new ParserDelegator();
		parserDelegator.parse(new StringReader(responseBody), parser, true);
		parser.afterParse();
		goToURL = parser.getURL();
		method = parser.getMethod();
		GlobalVars.logger.Logging("goToURL=" + goToURL + "   method=" + method,
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
		GlobalVars.logger.Logging(siteResponse.toString());
		if (GlobalVars.config.getEnableBodyLogging() == 1) {
			GlobalVars.logger.Logging(sw.toString());
		}
	}

	private String getHeaderItem(Header[] headers, String name) {
		for (Header head : headers) {
			if (head.getName().equals(name)) {
				GlobalVars.logger.Logging(name + "=" + head.getValue(), this);
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

	private HttpResponse executeHttpRequest(String URL, String method,
			List<NameValuePair> requestParams) throws Exception {
		GlobalVars.logger.Logging("Get Response from:" + URL);
		if ("".equals(URL) || URL == null)
			throw new Exception("Empty URL !!!");
		HttpRequestBase httpRequest = null;
		if (Consts.GET_METHOD.equals(method.toUpperCase())) {
			httpRequest = new HttpGet(URL);
		} else {
			if (Consts.POST_METHOD.equals(method.toUpperCase())) {
				httpRequest = new HttpPost(URL);
				if (requestParams != null) {
					((HttpPost) httpRequest)
							.setEntity(new UrlEncodedFormEntity(requestParams));
				}
			}
		}
		return httpclient.execute(httpRequest);
	}

	private void executeHttpRequestAndParse(String URL, String method,
			List<NameValuePair> requestParams) throws Exception {
		HttpResponse response = executeHttpRequest(URL, method, requestParams);
		if (response.getStatusLine().getStatusCode() == Consts.request_redirected_302) {
			executeHttpRequestAndParse(
					Consts.siteAddress
							+ "/"
							+ getHeaderItem(response.getAllHeaders(),
									Consts.LOCATION_HEADER), method,
					requestParams);
		} else {
			readContent(response);
			parseHTML(getParserByURL(URL));
		}

	}

	private void executeHttpRequestAndParse(String URL, String method)
			throws Exception {
		executeHttpRequestAndParse(URL, method, null);
	}

	protected boolean isURLBattle(String URL) {
		for (String battlePath : GlobalVars.config.getBattleURLs()) {
			if (URL.contains(battlePath)) {
				return true;
			}
		}
		return false;
	}

	protected void init() throws Exception {
		httpclient = HttpClients.createDefault();
		nameValuePairs = new ArrayList<NameValuePair>();
		// Login for registered users
		nameValuePairs.add(new BasicNameValuePair("id1_hf_0", ""));
		nameValuePairs.add(new BasicNameValuePair("login", GlobalVars.config
				.getUserName()));
		nameValuePairs.add(new BasicNameValuePair("password", GlobalVars.config
				.getPassword()));

		executeHttpRequestAndParse(Consts.siteAddress, Consts.GET_METHOD);
		executeHttpRequestAndParse(goToURL, Consts.GET_METHOD);
		// login
		executeHttpRequestAndParse(goToURL, Consts.POST_METHOD, nameValuePairs);
		if (goToURL.equals("")) {
			goToURL = Consts.siteAddress + Consts.angarTab;
		}

	}

	@Override
	public void run() {
		int countOfIdleIteraction = 0;
		while (!isHasToStop()) {
			if (countOfIdleIteraction > 0) {
				countOfIdleIteraction--;
			} else {
				try {
					init();
					while (!isHasToStop() ) {
						GlobalVars.logger
								.Logging("Start next Iteration.", this);
						doWork();
						GlobalVars.logger.Logging("The Iteration was ended.",
								this);
						doAfterWork();
					}
				} catch (Exception e) {
					GlobalVars.logger.Logging(e, this);
				}
			}
			if (countOfIdleIteraction == 0) {
				countOfIdleIteraction = getCountOfIdleSeconds()/5; 
				GlobalVars.logger.Logging("Wating "
						+ (int) (countOfIdleIteraction * 5 / 1000)
						+ " seconds.", this);
			}

			threadPause(5 * Consts.ONE_SECOND);
		}
	}

	public void doWork() throws Exception {
		executeHttpRequestAndParse(goToURL, method);
	};

	protected abstract void doAfterWork();

	protected abstract int getCountOfIdleSeconds();

	// public abstract void doBeforeWhile();

	public void threadPause(long sleepInterval) {
		try {
			Thread.sleep(sleepInterval);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
