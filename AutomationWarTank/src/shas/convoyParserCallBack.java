package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class convoyParserCallBack extends goToURLFinderParserCallBack {
	private boolean noMoreCalculte = false;

	public convoyParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress
				+ Consts.battleTab;
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);

			if (attribute != null) {
				String href = Consts.siteAddress + "/"
						+ (String) attribute;
				// Check attack URL
				if (href.contains("findEnemy") || href.contains("startFight")
						|| href.contains("attackRegular")
						|| href.contains("startMasking")) {
					URL = (String) href;
					timeOut = 1000;
					noMoreCalculte = true;
					AutomationWarTank.Logging("Attack Convoy!!!");
				}
			}
		}
	}
}
