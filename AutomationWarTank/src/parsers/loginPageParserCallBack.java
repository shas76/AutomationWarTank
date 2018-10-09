package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;

public class loginPageParserCallBack extends goToURLFinderParserCallBack {

	public loginPageParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {
		if (hREF.contains(Consts.SHOW_SIGNIN_LINK)) {
			getResponse().setRedirectUrl(hREF);
		}

	}

	@Override
	protected void handleStartTagFORM(Tag tag, MutableAttributeSet attributes, int pos) {
		if ("id1".equals((String) attributes.getAttribute(Attribute.ID))) {
			getResponse().setRedirectUrl(formHREF((String) attributes.getAttribute(Attribute.ACTION)));
			getResponse().setRedirectMethod((String) attributes.getAttribute(Attribute.METHOD));
		}
	}

}
