package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import http.request.processor.Response;
import shas.Consts;

public class loginPageParserCallBack extends goToURLFinderParserCallBack {

	public loginPageParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleStartTagA(Tag tag, MutableAttributeSet attributes, int pos) {
		Object attribute = attributes.getAttribute(Attribute.HREF);

		if (attribute != null) {
			String href =formHREF((String) attribute);

			if (href.contains(Consts.SHOW_SIGNIN_LINK)) {
				getResponse().setRedirectUrl( href);
			}
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
