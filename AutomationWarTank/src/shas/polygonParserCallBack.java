package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class polygonParserCallBack extends goToURLFinderParserCallBack {
  private boolean noMoreCalculte = false;
  
	public polygonParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress
				+ Consts.buildingsTab;
  }
  
	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
      return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);
			String href = Consts.siteAddress + "/"
					+ (String) attribute;

			if (href.contains("-0-") && !href.contains("upgradeLink")) {
				URL = href;
				noMoreCalculte = true;
        AutomationWarTank.Logging("Attack increase!!!");
      }
    }
  }
}
