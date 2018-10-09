package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;
import shas.GlobalVars;

public class bankParserCallBack extends goToURLFinderParserCallBack {

	public bankParserCallBack(String currentURL) {
		super(currentURL);
		pathToPage = Consts.ProductionPath;
		getResponse().setRedirectUrl(Consts.buildingsTab);
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {
		if (hREF.contains(GlobalVars.config.getBankProduction()) && !hREF.contains("upgradeLink")) {
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Bank producing started!!!");
		}
	}

}
