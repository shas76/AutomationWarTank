package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class PolygonParserCallBack extends GoToURLFinderParserCallBack {

	private boolean noMoreCalculte = false;

	public PolygonParserCallBack() {
		super();
		defaultGoToURL = AutomationWarTank.siteAddress
				+ AutomationWarTank.buildingsTab;
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);
			String href = AutomationWarTank.siteAddress + "/"
					+ (String) attribute;
			// Check convoy URL

			// Check attack URL
			if (href.contains("-0-") && !href.contains("upgradeLink")) {
				URL = href;
				noMoreCalculte = true;
				AutomationWarTank.Logging("Attack increase!!!");

			}

		}

	}

}