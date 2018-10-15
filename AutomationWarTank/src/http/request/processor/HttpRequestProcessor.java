package http.request.processor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import javax.swing.text.html.parser.ParserDelegator;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import parsers.CoinsParserCallBack;
import parsers.angarParserCallBack;
import parsers.armoryParserCallBack;
import parsers.bankParserCallBack;
import parsers.battleParserCallBack;
import parsers.convoyParserCallBack;
import parsers.fightParserCallBack;
import parsers.goToURLFinderParserCallBack;
import parsers.loginPageParserCallBack;
import parsers.mineParserCallBack;
import parsers.polygonParserCallBack;
import shas.Consts;
import shas.GlobalVars;
import utils.URLsConvertor;

public class HttpRequestProcessor {

	private HttpClient httpclient = null;

	private HttpClient getHttpclient() throws Exception {
		if (httpclient != null) {
			return httpclient;
		} else {
			httpclient = HttpClients.createDefault();
			// Login for registered users

			Response responce = processRequestInternal(new Request(Consts.siteAddress));
			responce = processRequestInternal(new Request(responce.getRedirectUrl()));
			// login
			processRequestInternal(new Request(responce.getRedirectUrl(), Consts.POST_METHOD, Arrays.asList(
					new BasicNameValuePair("id1_hf_0", ""),
					new BasicNameValuePair("login", GlobalVars.config.getUserName()), new BasicNameValuePair(
							"password", GlobalVars.config.getPassword())), responce));
			return httpclient;

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

	private HttpResponse executeRequest(final Request request) throws Exception {
		GlobalVars.logger.Logging("Get Response from:" + request.getUrl());
		if ("".equals(request.getUrl()) || request.getUrl() == null)
			throw new Exception("Empty URL !!!");
		HttpRequestBase httpRequest = null;
		String uRL = request.getUrl();
		Map<String, String> vURL = URLsConvertor.getURL2VURLByVUrl(uRL);
		if (!vURL.isEmpty()) {
			String key = (String) (vURL.keySet().toArray())[0];
			GlobalVars.logger.Logging("    Replace VURL:" + vURL.get(key) + " by " + key + " in " + uRL);
			uRL = uRL.replace(vURL.get(key), key);
		}
		if (Consts.GET_METHOD.equals(request.getMethod())) {
			httpRequest = new HttpGet(uRL);
		} else {
			if (Consts.POST_METHOD.equals(request.getMethod().toUpperCase())) {
				httpRequest = new HttpPost(uRL);
				if (request.getParameters() != null) {
					((HttpPost) httpRequest).setEntity(new UrlEncodedFormEntity(request.getParameters()));
				}
			}
		}
		return getHttpclient().execute(httpRequest);
	}

	private String readContent(HttpResponse siteResponse) throws UnsupportedEncodingException, IllegalStateException,
			IOException {
		HttpEntity entity = siteResponse.getEntity();
		Header contentEncoding = entity.getContentEncoding();
		String charSet = "UTF-8";
		if (contentEncoding != null) {
			charSet = contentEncoding.getValue();
		}
		InputStreamReader isr = new InputStreamReader(entity.getContent(), charSet);
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
		String responseBody = sw.toString();
		GlobalVars.logger.Logging(siteResponse.toString());
		if (GlobalVars.config.getEnableBodyLogging() == 1) {
			GlobalVars.logger.Logging(sw.toString());
		}
		return responseBody;
	}

	private Response parseHTML(String responseBody, goToURLFinderParserCallBack parser) throws IOException {
		if (parser == null) {
			GlobalVars.logger.Logging("Parser is NULL: ", this);
			return null;
		}
		GlobalVars.logger.Logging("Parse using: " + parser.toString(), this);
		ParserDelegator parserDelegator = new ParserDelegator();
		parserDelegator.parse(new StringReader(responseBody), parser, true);
		parser.afterParse();
		GlobalVars.logger.Logging("Response =" + parser.getResponse(), this);
		return parser.getResponse();
	}

	private goToURLFinderParserCallBack getParserByURL(String URL) {
		String lURL = URL.toLowerCase();
		if (lURL.contains(Consts.angarTab.toLowerCase()))
			return new angarParserCallBack(URL);
		if (lURL.contains(Consts.battleTab.toLowerCase()))
			return new battleParserCallBack(URL);
		if (lURL.contains(Consts.convoyTab.toLowerCase()))
			return new convoyParserCallBack(URL);
		if (lURL.contains(Consts.mineTab.toLowerCase()))
			return new mineParserCallBack(URL);
		if (lURL.contains(Consts.armoryTab.toLowerCase()))
			return new armoryParserCallBack(URL);
		if (lURL.contains(Consts.polygonTab.toLowerCase()))
			return new polygonParserCallBack(URL);
		if (lURL.contains(Consts.bankTab.toLowerCase()))
			return new bankParserCallBack(URL);
		if (lURL.contains(Consts.coinsTab.toLowerCase()))
			return new CoinsParserCallBack(URL);
		if (lURL.equals(Consts.siteAddress) || lURL.contains(Consts.SHOW_SIGNIN_LINK.toLowerCase()))
			return new loginPageParserCallBack(URL);
		if (isURLBattle(URL))
			return new fightParserCallBack(URL);
		return new goToURLFinderParserCallBack(URL);
	}

	protected boolean isURLBattle(String URL) {
		for (String battlePath : GlobalVars.config.getBattleURLs()) {
			if (URL.toLowerCase().contains(battlePath.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private Response processRequestInternal(final Request request) throws Exception {
		HttpResponse response = executeRequest(request);
		if (response.getStatusLine().getStatusCode() == Consts.request_redirected_302) {
			request.setUrl(Consts.siteAddress + "/" + getHeaderItem(response.getAllHeaders(), Consts.LOCATION_HEADER));
			return processRequestInternal(request);
		} else {
			return parseHTML(readContent(response), getParserByURL(request.getUrl()));
		}
	}

	public Request processRequest(final Request request) throws Exception {
		return new Request(processRequestInternal(request));
	}

}
