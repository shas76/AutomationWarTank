package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class buildingsParserCallBack extends goToURLFinderParserCallBack {
	private boolean noMoreCalculte = false;
	private boolean checkActive = false;
	private String href;

	@Override
	public void afterParse() {
		super.afterParse();
		if (AutomationWarTank.isTakeProductionMode
				&& URL.equals(defaultGoToURL)) {
			URL = Consts.siteAddress + Consts.buildingsTab;
			AutomationWarTank.isTakeProductionMode = false;

		}
	}

	public buildingsParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress + Consts.convoyTab;
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);

			if (attribute != null) {
				href = Consts.siteAddress + "/" + (String) attribute;

				if (href.contains("takeProductionLink")) {

					URL = href;
					noMoreCalculte = true;
					AutomationWarTank.isTakeProductionMode = true;
					GlobalVars.logger.Logging("Take Production!!!");
				}
				if (href.contains("freeBoostLink")) {

					URL = href;
					noMoreCalculte = true;
					GlobalVars.logger.Logging("Boost upgrading!!!");
				}

				if (href.contains("Mine") || href.contains("polygon")
						|| href.contains("Armory") || href.contains("Bank")) {
					checkActive = true;
				}
			}
		}
	}

	@Override
	public void handleText(char[] data, int pos) {
		if (noMoreCalculte)
			return;
		if (checkActive && new String(data).equals("+")) {
			URL = href;
			noMoreCalculte = true;
			GlobalVars.logger.Logging("Need Goto select to production");
			checkActive = false;
		}
	}

	@Override
	public void handleEndTag(Tag tag, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.DIV) {
			// Check convoy URL
			if (checkActive) {
				checkActive = false;
			}
		}
	}
}