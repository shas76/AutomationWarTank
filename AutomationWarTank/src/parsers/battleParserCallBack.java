package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class battleParserCallBack extends goToURLFinderParserCallBack {
	private int fuel;
	private boolean isFuel = false;
	private String tagBody;

	public battleParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleSimpleTagIMG(Tag tag, MutableAttributeSet attributes, int pos) {
		Object attribute = attributes.getAttribute(Attribute.TITLE);
		if (attribute != null) {
			if ("Fuel".equals(attribute)) {
				isFuel = true;
			}

		}
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {

		if ((hREF.contains("-opponents-") && fuel >= 90) || hREF.contains("-lastOpponentPanel-")) {
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Attack!!!");
		}

		if (hREF.contains("showOtherOpps")) {
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("New opponents");
		}
	}

	@Override
	protected void handleTextOfNode(char[] data, int pos) {
		tagBody = new String(data);
	}

	@Override
	protected void handleEndTagTD(Tag tag, int pos) {
		if (isFuel) {
			fuel = Integer.parseInt(tagBody);
			isFuel = false;
			GlobalVars.logger.Logging("Fuel:" + fuel);
		}
	}

}
