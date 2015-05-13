package shas;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

class goToURLFinderParserCallBack extends ParserCallback {
	protected String URL;
	protected String Method;
	protected String defaultGoToURL;
	protected String defaultGoToMethod;
	protected long timeOut;

	goToURLFinderParserCallBack() {
		super();
		defaultGoToURL = "";
		defaultGoToMethod = AutomationWarTank.GET_METHOD;
		URL = "";
		Method = "";
		timeOut = 1000;
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
		if (URL.equals("")) {
			URL = defaultGoToURL;

		}
		if (Method.equals("")) {
			Method = defaultGoToMethod;
		}
	}
}
