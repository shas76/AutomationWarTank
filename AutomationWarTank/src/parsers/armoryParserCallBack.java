package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class armoryParserCallBack extends goToURLFinderParserCallBack {
	private boolean noMoreCalculte = false;

	public armoryParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress + Consts.buildingsTab;
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);
			String href = Consts.siteAddress + Consts.ProductionPath
					+ (String) attribute;
			// Check convoy URL

			// Check attack URL
			if (href.contains("startProduceLink")
					&& href.contains(GlobalVars.config
							.getPlanArmoryProduction()[AutomationWarTank.currentArmoryProduction])
					&& !href.contains("upgradeLink")) {
				AutomationWarTank.currentArmoryProduction++;
				if (AutomationWarTank.currentArmoryProduction >= GlobalVars.config
						.getPlanArmoryProduction().length) {
					AutomationWarTank.currentArmoryProduction = 0;
				}
				URL = href;
				noMoreCalculte = true;
				GlobalVars.logger.Logging("Start Armory Production!!!");
			}
		}
	}
}
