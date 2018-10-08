package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;
import shas.GlobalVars;

public class armoryParserCallBack extends goToURLFinderParserCallBack {

	private boolean noMoreCalculte = false;

	public armoryParserCallBack(String currentURL) {
		super(currentURL);
		// defaultGoToURL = Consts.siteAddress + Consts.buildingsTab;
	}

	@Override
	protected void handleStartTagA(Tag tag, MutableAttributeSet attributes, int pos) {
		Object attribute = attributes.getAttribute(Attribute.HREF);
		String href = formHREF(Consts.ProductionPath + (String) attribute);
		// Check convoy URL

		// Check attack URL
		if (href.contains("startProduceLink")
				&& href.contains(GlobalVars.config.getPlanArmoryProduction()[GlobalVars.currentArmoryProduction])
				&& !href.contains("upgradeLink")) {
			GlobalVars.currentArmoryProduction++;
			if (GlobalVars.currentArmoryProduction >= GlobalVars.config.getPlanArmoryProduction().length) {
				GlobalVars.currentArmoryProduction = 0;
			}
			getResponse().setRedirectUrl(href);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Start Armory Production!!!");
		}
	}

}
