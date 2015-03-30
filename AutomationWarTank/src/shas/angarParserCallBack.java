package shas;

import java.util.Random;

class angarParserCallBack
  extends goToURLFinderParserCallBack
{
  public angarParserCallBack()
  {
    this.defaultGoToURL = "http://wartank.net/buildings";
    if (AutomationWarTank.isTakeProductionMode)
    {
      AutomationWarTank.isTakeProductionMode = false;
      this.timeOut = 1000L;
    }
    else
    {
      Random rnd = new Random();
      this.timeOut = 
        ((int)((AutomationWarTank.sleepInterval + rnd.nextDouble() * AutomationWarTank.randomInterval) * 60000.0D));
    }
  }
}
