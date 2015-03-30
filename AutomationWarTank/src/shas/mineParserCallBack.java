package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class MineParserCallBack extends GoToURLFinderParserCallBack {

	private boolean noMoreCalculte = false;

	public MineParserCallBack() {
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
					&& href.contains(AutomationWarTank.planMineProduction[AutomationWarTank.currentMineProduction])
					&& !href.contains("upgradeLink")) {
				AutomationWarTank.currentMineProduction++;
				if (AutomationWarTank.currentMineProduction >= AutomationWarTank.planMineProduction.length) {
					AutomationWarTank.currentMineProduction = 0;
				}
				URL = href;
				noMoreCalculte = true;
				AutomationWarTank.Logging("Start Production!!!");

			}

		}

	}

}