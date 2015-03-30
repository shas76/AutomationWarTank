package shas;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;

class goToURLFinderParserCallBack
  extends HTMLEditorKit.ParserCallback
{
  protected String URL;
  protected String Method;
  protected String defaultGoToURL;
  protected String defaultGoToMethod;
  protected long timeOut;
  
  goToURLFinderParserCallBack()
  {
    this.defaultGoToURL = "";
    this.defaultGoToMethod = "GET";
    this.URL = "";
    this.Method = "";
    this.timeOut = 1000L;
  }
  
  public long getTimeOut()
  {
    return this.timeOut;
  }
  
  public String getURL()
  {
    return this.URL;
  }
  
  public String getMethod()
  {
    return this.Method;
  }
  
  public void afterParse()
  {
    if (this.URL.equals("")) {
      this.URL = this.defaultGoToURL;
    }
    if (this.Method.equals("")) {
      this.Method = this.defaultGoToMethod;
    }
  }
}
