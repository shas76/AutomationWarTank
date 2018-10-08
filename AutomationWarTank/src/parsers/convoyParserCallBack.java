package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class convoyParserCallBack extends goToURLFinderParserCallBack {
	public convoyParserCallBack(String currentURL) {
		super(currentURL);
		// defaultGoToURL = Consts.siteAddress + Consts.battleTab;
	}

	@Override
	protected void handleStartTagA(Tag tag, MutableAttributeSet attributes, int pos) {
		Object attribute = attributes.getAttribute(Attribute.HREF);

		if (attribute != null) {
			String href = formHREF((String) attribute);
			// Check attack URL
			if (href.contains("findEnemy") || href.contains("startFight") || href.contains("attackRegular")
					|| href.contains("startMasking")) {
				getResponse().setRedirectUrl(href);
				// timeOut = 1000;
				setNoMoreCalculte(true);
				GlobalVars.logger.Logging("Attack Convoy!!!");
			}
		}
	}

}
