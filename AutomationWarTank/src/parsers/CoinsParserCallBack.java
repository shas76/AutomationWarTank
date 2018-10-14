package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class CoinsParserCallBack extends goToURLFinderParserCallBack {

	public CoinsParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {
		if (hREF.contains("exchangeLink")) {
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Get coins!!!");
		}

	}

}
