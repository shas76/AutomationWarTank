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
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		String href;

		if (tag == Tag.FORM) {
			if ("id1".equals((String) attributes.getAttribute(Attribute.ID))) {
				URL = Consts.siteAddress + "/"
						+ (String) attributes.getAttribute(Attribute.ACTION);
				Method = (String) attributes.getAttribute(Attribute.METHOD);
			}
			;
		}
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);

			if (attribute != null) {
				href = Consts.siteAddress + "/" + (String) attribute;

				if (href.contains(Consts.SHOW_SIGNIN_LINK)) {
					URL = href;
					timeOut = 0;
				}
			}
		}
	}
}
