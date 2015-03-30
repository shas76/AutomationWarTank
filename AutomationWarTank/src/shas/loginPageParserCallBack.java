package shas;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class loginPageParserCallBack
  extends goToURLFinderParserCallBack
{
  public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes, int pos)
  {
    if ((tag == HTML.Tag.FORM) && 
      ("id1".equals((String)attributes.getAttribute(HTML.Attribute.ID))))
    {
      this.URL = 
        ("http://wartank.net/" + (String)attributes.getAttribute(HTML.Attribute.ACTION));
      this.Method = ((String)attributes.getAttribute(HTML.Attribute.METHOD));
    }
    if (tag == HTML.Tag.A)
    {
      Object attribute = attributes.getAttribute(HTML.Attribute.HREF);
      if (attribute != null)
      {
        String href = "http://wartank.net/" + (String)attribute;
        if (href.contains("-showSigninLink"))
        {
          this.URL = href;
          this.timeOut = 0L;
        }
      }
    }
  }
}
