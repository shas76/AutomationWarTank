package parsers;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;
import shas.GlobalVars;

public class mineParserCallBack extends goToURLFinderParserCallBack {
	private boolean noMoreCalculte = false;

	public mineParserCallBack(String currentURL) {
		super(currentURL);
//		defaultGoToURL = Consts.siteAddress + Consts.buildingsTab;
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);
			String href = Consts.siteAddress + Consts.ProductionPath
					+ (String) attribute;
			// Check convoy URL

			// Check attack URL
			if (href.contains("startProduceLink")
					&& href.contains(GlobalVars.config.getPlanMineProduction()[GlobalVars.currentMineProduction])
					&& !href.contains("upgradeLink")) {
				GlobalVars.currentMineProduction++;
				if (GlobalVars.currentMineProduction >= GlobalVars.config
						.getPlanMineProduction().length) {
					GlobalVars.currentMineProduction = 0;
				}
				URL = href;
				noMoreCalculte = true;
				GlobalVars.logger.Logging("Start Production!!!");
			}
		}
	}
}
