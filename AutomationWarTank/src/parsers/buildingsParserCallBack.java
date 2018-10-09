package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;
import shas.GlobalVars;

public class buildingsParserCallBack extends goToURLFinderParserCallBack {

	public buildingsParserCallBack(String currentURL) {
		super(currentURL);
		if (GlobalVars.isTakeProductionMode) {
			getResponse().setRedirectUrl(Consts.siteAddress + Consts.buildingsTab);
			GlobalVars.isTakeProductionMode = false;
		}
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {

		if (hREF.contains("takeProductionLink")) {

			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.isTakeProductionMode = true;
			GlobalVars.logger.Logging("Take Production!!!");
		}
		if (hREF.contains("freeBoostLink")) {

			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Boost upgrading!!!");
		}
	}

}
