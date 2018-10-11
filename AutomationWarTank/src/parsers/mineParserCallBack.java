package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class mineParserCallBack extends goToURLFinderParserCallBack {

	public mineParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {
		if (hREF.contains("startProduceLink")
				&& hREF.contains(GlobalVars.config.getPlanMineProduction()[GlobalVars.currentMineProduction])
				&& !hREF.contains("upgradeLink")) {
			GlobalVars.currentMineProduction++;
			if (GlobalVars.currentMineProduction >= GlobalVars.config.getPlanMineProduction().length) {
				GlobalVars.currentMineProduction = 0;
			}
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Start Production!!!");
		}
	}

}
