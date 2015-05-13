package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class bankParserCallBack extends goToURLFinderParserCallBack {
	private boolean noMoreCalculte = false;

	public bankParserCallBack() {
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
			String href = AutomationWarTank.siteAddress
					+ AutomationWarTank.ProductionPath + (String) attribute;

			if (href.contains(AutomationWarTank.bankProduction)
					&& !href.contains("upgradeLink")) {
				URL = href;
				noMoreCalculte = true;
				AutomationWarTank.Logging("Bank producing started!!!");
			}
		}
	}
}
