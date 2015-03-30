package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class polygonParserCallBack
  extends goToURLFinderParserCallBack
{
  private boolean noMoreCalculte = false;
  
  public polygonParserCallBack()
  {
    this.defaultGoToURL = "http://wartank.net/buildings";
  }
  
  public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if (tag == HTML.Tag.A)
    {
      Object attribute = attributes.getAttribute(HTML.Attribute.HREF);
      String href = "http://wartank.net/" + 
        (String)attribute;
      if ((href.contains("-0-")) && (!href.contains("upgradeLink")))
      {
        this.URL = href;
        this.noMoreCalculte = true;
        AutomationWarTank.Logging("Attack increase!!!");
      }
    }
  }
}
