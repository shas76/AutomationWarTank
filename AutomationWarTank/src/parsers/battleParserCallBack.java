package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class battleParserCallBack extends goToURLFinderParserCallBack {
	private int fuel;
	private boolean isFuel = false;
	private String tagBody;
	private boolean noMoreCalculte = false;
	private String href;

	public battleParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress + Consts.angarTab;
	}

	@Override
	public void handleSimpleTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;

		if (tag == Tag.IMG) {
			Object attribute = attributes.getAttribute(Attribute.TITLE);
			if (attribute != null) {
				if (((String) attribute).equals("Fuel")) {
					isFuel = true;
				}

			}
		}
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);

			if (attribute != null) {
				href = Consts.siteAddress + "/" + (String) attribute;
				// Check attack URL

				if ((href.contains("-opponents-") && fuel >= 105)
						|| href.contains("-lastOpponentPanel-")) {
					URL = href;
					noMoreCalculte = true;
					GlobalVars.logger.Logging("Attack!!!");
				}

				if (href.contains("showOtherOpps")) {
					URL = href;
					noMoreCalculte = true;
					GlobalVars.logger.Logging("New opponents");
				}
			}
		}
	}

	@Override
	public void handleText(char[] data, int pos) {
		if (noMoreCalculte)
			return;
		tagBody = new String(data);
	}

	@Override
	public void handleEndTag(Tag tag, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.TD) {
			if (isFuel) {
				fuel = Integer.parseInt(tagBody);
				isFuel = false;
				GlobalVars.logger.Logging("Fuel:" + fuel);
			}
		}
	}
}
