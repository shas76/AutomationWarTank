package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class buildingsParserCallBack
  extends goToURLFinderParserCallBack
{
  private boolean noMoreCalculte = false;
  private boolean checkActive = false;
  private String href;
  
  public void afterParse()
  {
    super.afterParse();
    if ((AutomationWarTank.isTakeProductionMode) && 
      (this.URL.equals(this.defaultGoToURL)))
    {
      this.URL = "http://wartank.net/buildings";
      
      AutomationWarTank.isTakeProductionMode = false;
    }
  }
  
  public buildingsParserCallBack()
  {
    this.defaultGoToURL = "http://wartank.net/convoy";
  }
  
  public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if (tag == HTML.Tag.A)
    {
      Object attribute = attributes.getAttribute(HTML.Attribute.HREF);
      if (attribute != null)
      {
        this.href = ("http://wartank.net/" + (String)attribute);
        if (this.href.contains("takeProductionLink"))
        {
          this.URL = this.href;
          this.noMoreCalculte = true;
          AutomationWarTank.isTakeProductionMode = true;
          AutomationWarTank.Logging("Take Production!!!");
        }
        if (this.href.contains("freeBoostLink"))
        {
          this.URL = this.href;
          this.noMoreCalculte = true;
          AutomationWarTank.Logging("Boost upgrading!!!");
        }
        if ((this.href.contains("Mine")) || (this.href.contains("polygon")) || 
          (this.href.contains("Armory")) || (this.href.contains("Bank"))) {
          this.checkActive = true;
        }
      }
    }
  }
  
  public void handleText(char[] data, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if ((this.checkActive) && (new String(data).equals("+")))
    {
      this.URL = this.href;
      this.noMoreCalculte = true;
      AutomationWarTank.Logging("Need Goto select to production");
      this.checkActive = false;
    }
  }
  
  public void handleEndTag(HTML.Tag tag, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if (tag == HTML.Tag.DIV) {
      if (this.checkActive) {
        this.checkActive = false;
      }
    }
  }
}
