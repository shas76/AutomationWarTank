package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import http.request.processor.Response;
import shas.Consts;
import shas.GlobalVars;
import sun.net.www.protocol.https.AbstractDelegateHttpsURLConnection;

public class goToURLFinderParserCallBack extends ParserCallback {
	private Response response = new Response("", Consts.GET_METHOD, 0);
	private boolean noMoreCalculte = false;
	protected String currentURL;

	goToURLFinderParserCallBack(String currentURL) {
		super();
		this.currentURL = currentURL;
	}

	public Response getResponse() {
		return response;
	}

	public boolean isNoMoreCalculte() {
		return noMoreCalculte;
	}

	public void setNoMoreCalculte(boolean noMoreCalculte) {
		this.noMoreCalculte = noMoreCalculte;
	}

	protected String formHREF(String relativeURL) {
		return Consts.siteAddress + "/" + relativeURL;
	}

	protected void handleStartTagA(Tag tag, MutableAttributeSet attributes, int pos) {

	};

	protected void handleStartTagFORM(Tag tag, MutableAttributeSet attributes, int pos) {

	};

	protected void handleTextOfNode(char[] data, int pos) {

	}

	protected void handleEndTagDIV(Tag tag, int pos) {

	};

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (isNoMoreCalculte()) {
			return;
		}
		if (tag == Tag.A)
			handleStartTagA(tag, attributes, pos);
		if (tag == Tag.FORM)
			handleStartTagFORM(tag, attributes, pos);

	}

	@Override
	public void handleText(char[] data, int pos) {
		if (noMoreCalculte)
			return;
		handleTextOfNode(data, pos);
	}

	@Override
	public void handleEndTag(Tag tag, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.DIV) {
			handleEndTagDIV(tag, pos);
		}
	}

	public void afterParse() {

	}
}
