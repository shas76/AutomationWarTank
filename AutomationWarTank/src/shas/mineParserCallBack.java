package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class mineParserCallBack
  extends goToURLFinderParserCallBack
{
  private boolean noMoreCalculte = false;
  
  public mineParserCallBack()
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
        (href.contains(AutomationWarTank.planMineProduction[AutomationWarTank.currentMineProduction])) && 
        (!href.contains("upgradeLink")))
      {
        AutomationWarTank.currentMineProduction += 1;
        if (AutomationWarTank.currentMineProduction >= AutomationWarTank.planMineProduction.length) {
          AutomationWarTank.currentMineProduction = 0;
        }
        this.URL = href;
        this.noMoreCalculte = true;
        AutomationWarTank.Logging("Start Production!!!");
      }
    }
  }
}
