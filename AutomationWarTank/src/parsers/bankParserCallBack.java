package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class bankParserCallBack extends ProductionParserCallBack {

	public bankParserCallBack(String currentURL) {
		super(currentURL);
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
