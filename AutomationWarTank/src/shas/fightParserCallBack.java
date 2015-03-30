package shas;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

class fightParserCallBack
  extends goToURLFinderParserCallBack
{
  private boolean noMoreCalculte = false;
  private Date timeLeft = null;
  private int myDurability;
  private int enemyDurability;
  private int countSpecialShells;
  private int levelNestedTables = 0;
  private int calculatingParameter = 0;
  private boolean inRepairLink = false;
  private boolean inManeuverLink = false;
  private boolean inAttackSpecialShellLink = false;
  private int[] tableColumnsNumber = new int[20];
  private String attackRegularShellLink = "";
  private String attackSpecialShellLink = "";
  private String repairLink;
  private String repairLinkBody;
  private String maneuverLink;
  private String maneverLinkBody;
  private String changeTargetLink;
  private String enemyName;
  private StringWriter fighterLog = new StringWriter();
  
  public fightParserCallBack()
  {
    this.defaultGoToURL = "http://wartank.net/angar";
    
    this.timeOut = 7050L;
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
      if (href.contains("currentOverview"))
      {
        this.URL = href;
        if (this.timeLeft != null) {
          this.timeOut = 
          
            (this.timeLeft.getTime() - 20000L + TimeZone.getDefault().getOffset(
            this.timeLeft.getTime()));
        }
        this.noMoreCalculte = true;
        
        AutomationWarTank.Logging("Applay Button 'Platoon, lets roll! Attack!' !!!");
      }
      if (href.contains("refresh"))
      {
        this.URL = href;
        if (this.timeLeft != null) {
          this.timeOut = 
          
            (this.timeLeft.getTime() - 20000L + TimeZone.getDefault().getOffset(
            this.timeLeft.getTime()));
        }
        this.noMoreCalculte = true;
        AutomationWarTank.Logging("refresh");
      }
      if (href.contains("attackRegularShellLink")) {
        this.attackRegularShellLink = href;
      }
      if (href.contains("changeTargetLink")) {
        this.changeTargetLink = href;
      }
      if (href.contains("attackSpecialShellLink"))
      {
        this.inAttackSpecialShellLink = true;
        this.attackSpecialShellLink = href;
      }
      if (href.contains("repairLink"))
      {
        this.repairLink = href;
        this.inRepairLink = true;
      }
      if (href.contains("maneuverLink"))
      {
        this.maneuverLink = href;
        this.inManeuverLink = true;
      }
    }
    if (tag == HTML.Tag.TABLE) {
      this.levelNestedTables += 1;
    }
    if (tag == HTML.Tag.TR) {
      this.tableColumnsNumber[this.levelNestedTables] = 0;
    }
    if (tag == HTML.Tag.TD) {
      this.tableColumnsNumber[this.levelNestedTables] += 1;
    }
  }
  
  public void handleSimpleTag(HTML.Tag arg0, MutableAttributeSet arg1, int arg2)
  {
    if (arg0 == HTML.Tag.BR) {
      this.fighterLog.write("\r\n");
    }
    super.handleSimpleTag(arg0, arg1, arg2);
  }
  
  public void handleText(char[] data, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    String bodyText = new String(data);
    if (this.levelNestedTables == 0)
    {
      this.fighterLog.write(bodyText);
      if (bodyText.contains(":")) {
        this.fighterLog.write("\r\n");
      }
    }
    if (bodyText.contains("starts in"))
    {
      String[] wordsInBody = bodyText.split(" ");
      try
      {
        AutomationWarTank.Logging("Time Left on Page:" + wordsInBody[2]);
        this.timeLeft = new SimpleDateFormat("HH:mm:ss")
          .parse(wordsInBody[2]);
        AutomationWarTank.Logging("Time Left:" + 
          String.valueOf(this.timeLeft.getTime()));
      }
      catch (ParseException e)
      {
        e.printStackTrace();
      }
    }
    if (this.tableColumnsNumber[2] == 2)
    {
      if (this.calculatingParameter == 1)
      {
        try
        {
          this.enemyDurability = Integer.parseInt(bodyText);
        }
        catch (NumberFormatException e)
        {
          this.enemyDurability = 1000;
        }
        AutomationWarTank.Logging("Enemy Durability:" + this.enemyDurability);
      }
      if (this.calculatingParameter == 0)
      {
        this.calculatingParameter += 1;
        try
        {
          this.myDurability = Integer.parseInt(bodyText);
        }
        catch (NumberFormatException e)
        {
          this.myDurability = 1000;
        }
        AutomationWarTank.Logging("My Durability:" + this.myDurability);
      }
    }
    if ((this.tableColumnsNumber[1] == 2) && (this.enemyName == null))
    {
      this.enemyName = bodyText;
      AutomationWarTank.Logging("EnemyName:" + this.enemyName);
    }
    if (this.inRepairLink) {
      this.repairLinkBody = bodyText;
    }
    if (this.inManeuverLink) {
      this.maneverLinkBody = bodyText;
    }
    if (this.inAttackSpecialShellLink)
    {
      String[] splitedBody = bodyText.split("[()]");
      try
      {
        this.countSpecialShells = Integer.parseInt(splitedBody[1]);
      }
      catch (NumberFormatException e)
      {
        this.countSpecialShells = 0;
      }
      AutomationWarTank.Logging(
        "Count Special Shells:" + this.countSpecialShells);
    }
  }
  
  public void handleEndTag(HTML.Tag tag, int pos)
  {
    if (this.noMoreCalculte) {
      return;
    }
    if (tag == HTML.Tag.A)
    {
      this.inRepairLink = false;
      this.inManeuverLink = false;
      this.inAttackSpecialShellLink = false;
    }
    if (tag == HTML.Tag.TABLE) {
      this.levelNestedTables -= 1;
    }
    if (tag == HTML.Tag.TR) {
      this.tableColumnsNumber[this.levelNestedTables] -= 1;
    }
  }
  
  public void afterParse()
  {
    if (this.URL.equals("")) {
      if ((this.repairLinkBody.contains("Repair kit")) && 
        (this.myDurability < AutomationWarTank.maxDurability4UsingRepaer))
      {
        this.URL = this.repairLink;
        this.timeOut = 0L;
        AutomationWarTank.skipWaiting = true;
        AutomationWarTank.Logging("Use Repair!");
      }
      else if (this.maneverLinkBody.contains("Maneuver"))
      {
        this.URL = this.maneuverLink;
        this.timeOut = 0L;
        AutomationWarTank.skipWaiting = true;
        AutomationWarTank.Logging("Maneuver!");
      }
      else
      {
        boolean allied = false;
        boolean isAlliedException = false;
        if (AutomationWarTank.alliedExceptions.contains(this.enemyName.toLowerCase()))
        {
          isAlliedException = true;
          this.fighterLog.write("\r\nAllied Exception:" + 
            this.enemyName + "\r\n");
        }
        if (!isAlliedException) {
          for (String alliance : AutomationWarTank.allied) {
            if (this.enemyName.toLowerCase().contains(alliance))
            {
              allied = true;
              break;
            }
          }
        }
        if (allied)
        {
          if (AutomationWarTank.countSkippedPlayers == AutomationWarTank.limitChangeTarget)
          {
            this.URL = this.maneuverLink;
            this.fighterLog
              .write("\r\ncountSkippedPlayers == limitChangeTarget. Friend= " + 
              this.enemyName + 
              ". Go to maneuver\r\n");
            this.timeOut = 1000L;
          }
          else
          {
            this.URL = this.changeTargetLink;
            this.timeOut = 1000L;
            this.fighterLog.write("\r\nSkip our friend! " + 
              this.enemyName + "\r\n");
            this.fighterLog.write("\r\nCountSkippedPlayers: " + 
              AutomationWarTank.countSkippedPlayers + 
              "\r\n");
            
            AutomationWarTank.countSkippedPlayers += 1;
            AutomationWarTank.Logging("Skip our friend! " + 
              this.enemyName);
            AutomationWarTank.Logging("CountSkippedPlayers: " + 
              AutomationWarTank.countSkippedPlayers);
          }
        }
        else
        {
          AutomationWarTank.countSkippedPlayers = 0;
          AutomationWarTank.Logging("CountSkippedPlayers: " + 
            AutomationWarTank.countSkippedPlayers);
          if ((!this.attackSpecialShellLink.equals("")) && 
            (this.countSpecialShells > AutomationWarTank.limitUsingSpecialShell) && 
            (this.enemyDurability > AutomationWarTank.enemyDurabilityLimitUsingSpecialShell))
          {
            this.URL = this.attackSpecialShellLink;
            if (AutomationWarTank.skipWaiting)
            {
              this.timeOut = 0L;
              AutomationWarTank.skipWaiting = false;
            }
          }
          else if (!this.attackRegularShellLink.equals(""))
          {
            this.URL = this.attackRegularShellLink;
            if (AutomationWarTank.skipWaiting)
            {
              this.timeOut = 0L;
              AutomationWarTank.skipWaiting = false;
            }
            else if ((this.enemyDurability < AutomationWarTank.enemyDurabilityLimitUsingShortTimeReload) && 
              (this.enemyDurability > 0))
            {
              this.timeOut = 4000L;
            }
          }
        }
      }
    }
    try
    {
      PrintStream printStream = new PrintStream(new FileOutputStream(
        AutomationWarTank.fighterLogFileName, true));
      printStream.print(this.fighterLog.toString());
      printStream.close();
    }
    catch (FileNotFoundException e)
    {
      AutomationWarTank.Logging(e);
    }
    if (Math.abs(this.timeOut) > 300000L) {
      this.timeOut = 10000L;
    }
    if (this.timeOut < 0L) {
      this.timeOut = 1000L;
    }
    super.afterParse();
  }
}
