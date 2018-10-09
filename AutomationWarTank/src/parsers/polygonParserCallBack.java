package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;
import shas.GlobalVars;

public class polygonParserCallBack extends goToURLFinderParserCallBack {

	public polygonParserCallBack(String currentURL) {
		super(currentURL);
		getResponse().setRedirectUrl(Consts.buildingsTab);
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {
		if (hREF.contains("-0-") && !hREF.contains("upgradeLink")) {
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Attack increase!!!");
		}
	}

}
