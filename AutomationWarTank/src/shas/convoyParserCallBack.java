package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class convoyParserCallBack
  extends goToURLFinderParserCallBack
{
  private boolean noMoreCalculte = false;
  
  public convoyParserCallBack()
  {
    this.defaultGoToURL = "http://wartank.net/battle";
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
        String href = "http://wartank.net/" + 
          (String)attribute;
        if ((href.contains("findEnemy")) || (href.contains("startFight")) || 
          (href.contains("attackRegular")) || 
          (href.contains("startMasking")))
        {
          this.URL = href;
          this.timeOut = 1000L;
          this.noMoreCalculte = true;
          AutomationWarTank.Logging("Attack Convoy!!!");
        }
      }
    }
  }
}
