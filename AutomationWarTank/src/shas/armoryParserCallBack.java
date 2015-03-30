package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class ArmoryParserCallBack extends GoToURLFinderParserCallBack {

	private boolean noMoreCalculte = false;

	public ArmoryParserCallBack() {
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
			// Check convoy URL

			// Check attack URL
			if (href.contains("startProduceLink")
					&& href.contains(AutomationWarTank.planArmoryProduction[AutomationWarTank.currentArmoryProduction])
					&& !href.contains("upgradeLink")) {
				AutomationWarTank.currentArmoryProduction++;
				if (AutomationWarTank.currentArmoryProduction >= AutomationWarTank.planArmoryProduction.length) {
					AutomationWarTank.currentArmoryProduction = 0;
				}
				URL = href;
				noMoreCalculte = true;
				AutomationWarTank.Logging("Start Armory Production!!!");

			}

		}

	}

}