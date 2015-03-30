package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class armoryParserCallBack
  extends goToURLFinderParserCallBack
{
  private boolean noMoreCalculte = false;
  
  public armoryParserCallBack()
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
      String href = "http://wartank.net/production/" + 
        (String)attribute;
      if ((href.contains("startProduceLink")) && 
        (href.contains(AutomationWarTank.planArmoryProduction[AutomationWarTank.currentArmoryProduction])) && 
        (!href.contains("upgradeLink")))
      {
        AutomationWarTank.currentArmoryProduction += 1;
        if (AutomationWarTank.currentArmoryProduction >= AutomationWarTank.planArmoryProduction.length) {
          AutomationWarTank.currentArmoryProduction = 0;
        }
        this.URL = href;
        this.noMoreCalculte = true;
        AutomationWarTank.Logging("Start Armory Production!!!");
      }
    }
  }
}
