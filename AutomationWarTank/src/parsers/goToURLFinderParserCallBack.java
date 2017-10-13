package parsers;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import shas.Consts;

public class goToURLFinderParserCallBack extends ParserCallback {
	protected String URL;
	protected String Method;
	protected String defaultGoToURL;
	protected String defaultGoToMethod;
	protected long timeOut;
	protected String currentURL;

	goToURLFinderParserCallBack(String currentURL) {
		super();
		defaultGoToURL = "";
		defaultGoToMethod = Consts.GET_METHOD;
		URL = "";
		Method = "";
		timeOut = 1000;
		this.currentURL = currentURL;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public String getURL() {
		return URL;
	}

	public String getMethod() {
		return Method;
	}

	public void afterParse() {
/*		if (URL.equals("")) {
			URL = defaultGoToURL;

		}
		if (Method.equals("")) {
			Method = defaultGoToMethod;
		}*/
	}
}
