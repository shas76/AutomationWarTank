package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class BattleParserCallBack extends GoToURLFinderParserCallBack {

	private int fuel;

	private boolean isFuel = false;
	private String tagBody;
	private boolean noMoreCalculte = false;
	// private boolean needCheckConvoy = false;
	// private boolean firstAttack = false;
	private String href;

	public BattleParserCallBack() {
		super();
		defaultGoToURL = AutomationWarTank.siteAddress
				+ AutomationWarTank.angarTab;
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
				href = AutomationWarTank.siteAddress + "/" + (String) attribute;
				// Check convoy URL
				// if (href.contains("convoy")) {
				// needCheckConvoy = true;
				// firstAttack = true;
				// }

				// Check attack URL

				if ((href.contains("-opponents-") && fuel >= 105)
						|| href.contains("-lastOpponentPanel-")) {
					// if ((firstAttack && fuel >= 105) || !firstAttack) {
					URL = href;
					noMoreCalculte = true;
					AutomationWarTank.Logging("Attack!!!");
					// }
				}

				if (href.contains("showOtherOpps")) {
					URL = href;
					noMoreCalculte = true;
					AutomationWarTank.Logging("New opponents");
				}

			}

		}

	}

	@Override
	public void handleText(char[] data, int pos) {
		if (noMoreCalculte)
			return;
		tagBody = new String(data);
		// if (needCheckConvoy && tagBody.equals("+")) {
		// URL = href;
		// noMoreCalculte = true;
		
		// AutomationWarTank.Logging("Need Goto Escort");
		// needCheckConvoy = false;
		// }
	}

	@Override
	public void handleEndTag(Tag tag, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.TD) {
			if (isFuel) {
				fuel = Integer.parseInt(tagBody);
				isFuel = false;
				AutomationWarTank.Logging("Fuel:" + fuel);
			}
		}
		// if (tag == Tag.DIV) {
		// Check convoy URL
		// if (needCheckConvoy) {
		// needCheckConvoy = false;
		// }
		// }
	}
}