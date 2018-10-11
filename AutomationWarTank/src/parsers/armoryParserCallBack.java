package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class armoryParserCallBack extends goToURLFinderParserCallBack {

	public armoryParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleStartTagA(final String hREF, final Tag tag, final MutableAttributeSet attributes, final int pos) {

		// Check attack URL
		if (hREF.contains("startProduceLink")
				&& hREF.contains(GlobalVars.config.getPlanArmoryProduction()[GlobalVars.currentArmoryProduction])
				&& !hREF.contains("upgradeLink")) {
			GlobalVars.currentArmoryProduction++;
			if (GlobalVars.currentArmoryProduction >= GlobalVars.config.getPlanArmoryProduction().length) {
				GlobalVars.currentArmoryProduction = 0;
			}
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Start Armory Production!!!");
		}
	}

}
