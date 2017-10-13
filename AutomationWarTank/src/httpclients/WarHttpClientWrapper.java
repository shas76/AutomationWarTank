package httpclients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.parser.ParserDelegator;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
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

public class WarHttpClientWrapper {
	
	private HttpClient httpclient = HttpClients.createDefault();
	public List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
	public String goToURL;
	public String method;
	public long timeOut;
	private String responseBody;
	
	public WarHttpClientWrapper() {
		super();

	}

	private String getHeaderItem(Header[] headers, String name) {
		for (Header head : headers) {
			if (head.getName().equals(name)) {
				GlobalVars.logger.Logging(name + "=" + head.getValue());
				return head.getValue();
			}
		}
		return "";
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
	
	public boolean isURLBattle(String URL) {
		for (String battlePath : GlobalVars.config.getBattleURLs()) {
			if (URL.contains(battlePath)) {
				return true;
			}
		}
		return false;
	}

	private goToURLFinderParserCallBack parseHTML(
			goToURLFinderParserCallBack parser) throws IOException {
		if (parser == null) {
			GlobalVars.logger.Logging("Parser is NULL: ");
			goToURL = "";
			method = "GET";
			timeOut = 0;
			return null;
		}
		GlobalVars.logger.Logging("Parse using: " + parser.toString());
		ParserDelegator parserDelegator = new ParserDelegator();
		parserDelegator.parse(new StringReader(responseBody), parser, true);
		parser.afterParse();
		goToURL = parser.getURL();
		method = parser.getMethod();
		timeOut = parser.getTimeOut();
		GlobalVars.logger.Logging("goToURL=" + goToURL + "   method=" + method);
		return parser;
	}
	

}
