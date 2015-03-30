package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class battleParserCallBack
  extends goToURLFinderParserCallBack
{
  private int fuel;
  private boolean isFuel = false;
  private String tagBody;
  private boolean noMoreCalculte = false;
  private String href;
  
  public battleParserCallBack()
  {
    this.defaultGoToURL = "http://wartank.net/angar";
  }
  
  public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if (tag == HTML.Tag.IMG)
    {
      Object attribute = attributes.getAttribute(HTML.Attribute.TITLE);
      if ((attribute != null) && 
        (((String)attribute).equals("Fuel"))) {
        this.isFuel = true;
      }
    }
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
        if (((this.href.contains("-opponents-")) && (this.fuel >= 105)) || 
          (this.href.contains("-lastOpponentPanel-")))
        {
          this.URL = this.href;
          this.noMoreCalculte = true;
          AutomationWarTank.Logging("Attack!!!");
        }
        if (this.href.contains("showOtherOpps"))
        {
          this.URL = this.href;
          this.noMoreCalculte = true;
          AutomationWarTank.Logging("New opponents");
        }
      }
    }
  }
  
  public void handleText(char[] data, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    this.tagBody = new String(data);
  }
  
  public void handleEndTag(HTML.Tag tag, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if ((tag == HTML.Tag.TD) && 
      (this.isFuel))
    {
      this.fuel = Integer.parseInt(this.tagBody);
      this.isFuel = false;
      AutomationWarTank.Logging("Fuel:" + this.fuel);
    }
  }
}
