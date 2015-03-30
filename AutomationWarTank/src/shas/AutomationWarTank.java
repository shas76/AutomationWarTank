package shas;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.text.html.parser.ParserDelegator;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AutomationWarTank
{
  private static final String RMI_SEVER_LOCATOR = "//localhost/AutomationWarTank";
  static final String GET_METHOD = "GET";
  static final String POST_METHOD = "POST";
  static final int request_redirected_302 = 302;
  static final String siteAddress = "http://wartank.net";
  static final String ProductionPath = "/production/";
  static final String timeFormat = "%tF %tT ";
  static final String battleTab = "/battle";
  static final String angarTab = "/angar";
  static final String convoyTab = "/convoy";
  static final String buildingsTab = "/buildings";
  static final String mineTab = "/production/Mine";
  static final String polygonTab = "/polygon";
  static final String armoryTab = "/production/Armory";
  static final String bankTab = "/production/Bank";
  static final String pveTab = "/pve";
  static final String cwTab = "/cw";
  static final String REFRESH = "refresh";
  static final int msInMinunte = 60000;
  static final String SHOW_SIGNIN_LINK = "-showSigninLink";
  static final String LocationHeader = "Location";
  static HttpClient httpclient;
  static String configPath;
  static String[] planMineProduction = { "-2-", "-2-", "-2-", "-2-", "-0-", 
    "-0-", "-0-", "-3-", "-3-", "-1-" };
  static String[] planArmoryProduction = { "-1-", "-1-", "-1-", "-1-", "-3-", 
    "-0-", "-2-" };
  static int sleepInterval = 5;
  static int randomInterval = 2;
  static Date[] battleTimes;
  static String[] battleURLs;
  static int limitUsingSpecialShell;
  static int enemyDurabilityLimitUsingSpecialShell;
  static String bankProduction;
  static int enemyDurabilityLimitUsingShortTimeReload;
  static int maxDurability4UsingRepaer;
  static String fighterLogFileName;
  static String[] allied;
  static int limitChangeTarget;
  static String alliedExceptions = "";
  static int currentMineProduction = 0;
  static int currentArmoryProduction = 0;
  static boolean isTakeProductionMode = false;
  static boolean skipWaiting = false;
  static int countSkippedPlayers = 0;
  static int enableBodyLogging = 0;
  static String userName = "";
  static String password = "";
  static int printResponseBoby = 0;
  private static String responseBody;
  private static String goToURL;
  private static String method;
  private static long timeOut;
  
  public static void Logging(String message)
  {
    System.out.printf("%tF %tT %s\n", new Object[] { new Date(), new Date(), message });
  }
  
  public static void Logging(Exception e)
  {
    System.out.printf("%tF %tT ", new Object[] { new Date(), new Date() });
    e.printStackTrace();
  }
  
  public static Date extractTime(Date dateToExtract)
    throws ParseException
  {
    return 
      new SimpleDateFormat("HH:mm").parse(new SimpleDateFormat("HH:mm").format(dateToExtract));
  }
  
  public static String outArray(Object[] objs)
  {
    String str2print = "";
    Object[] arrayOfObject = objs;int j = objs.length;
    for (int i = 0; i < j; i++)
    {
      Object obj = arrayOfObject[i];
      str2print = str2print + obj.toString() + ",";
    }
    return str2print;
  }
  
  private static void loadingConfiguration(String configFileName)
    throws ConfigurationException, ParseException
  {
    Logging("Current Config path:" + configPath);
    XMLConfiguration configRead = new XMLConfiguration(configFileName);
    String[] tempPlanArmoryProduction = configRead
      .getStringArray("ShellsProduction");
    String[] tempPlanMineProduction = configRead
      .getStringArray("MineProduction");
    int tempSleepInterval = configRead.getInt("SleepingInterval", 5);
    int tempRandomInterval = configRead.getInt("RandomInterval", 5);
    
    int tempLimitUsingSpecialShell = configRead.getInt(
      "LimitUsingSpecialShell", 30);
    int tempEnemyDurabilityLimitUsingSpecialShall = configRead.getInt(
      "EnemyDurabilityLimitUsingSpecialShall", 200);
    String tempBankProduction = configRead.getString("BankProduction", 
      "-1-");
    int tempEnemyDurabilityLimitUsingShortTimeReload = configRead.getInt(
      "EnemyDurabilityLimitUsingShortTimeReload", 200);
    int tempMaxDurability4UsingRepaer = configRead.getInt(
      "MaxDurability4UsingRepaer", 250);
    
    String[] arrOfTimes = configRead.getStringArray("BattleTime");
    ArrayList<Date> listOfTimes = new ArrayList();
    for (String time : arrOfTimes) {
      listOfTimes.add(new SimpleDateFormat("HH:mm").parse(time));
    }
    Date[] tempBattleTime = (Date[])listOfTimes.toArray(new Date[0]);
    String[] tempBattleURL = configRead.getStringArray("BattleURL");
    String tempFighterLogFileName = configRead.getString(
      "FighterLogFileName", "d:\\reset.txt");
    String[] tempAllied = configRead.getStringArray("Allied");
    for (int i = 0; i < tempAllied.length; i++) {
      tempAllied[i] = tempAllied[i].substring(1, 
        tempAllied[i].length() - 1);
    }
    int tempLimitChangeTarget = configRead.getInt("LimitChangeTarget", 10);
    String tempUserName = configRead.getString("UserName");
    String tempPassword = configRead.getString("Password");
    enableBodyLogging = configRead.getInt("EnableLoggingBody", 0);
    String tempAlliedExceptions = configRead.getString("AlliedExceptions")
      .toLowerCase();
    
    planArmoryProduction = tempPlanArmoryProduction;
    planMineProduction = tempPlanMineProduction;
    sleepInterval = tempSleepInterval;
    randomInterval = tempRandomInterval;
    battleTimes = tempBattleTime;
    battleURLs = tempBattleURL;
    limitUsingSpecialShell = tempLimitUsingSpecialShell;
    enemyDurabilityLimitUsingSpecialShell = tempEnemyDurabilityLimitUsingSpecialShall;
    bankProduction = tempBankProduction;
    enemyDurabilityLimitUsingShortTimeReload = tempEnemyDurabilityLimitUsingShortTimeReload;
    maxDurability4UsingRepaer = tempMaxDurability4UsingRepaer;
    fighterLogFileName = tempFighterLogFileName;
    allied = tempAllied;
    limitChangeTarget = tempLimitChangeTarget;
    userName = tempUserName;
    password = tempPassword;
    alliedExceptions = tempAlliedExceptions;
    
    currentMineProduction = 0;
    currentArmoryProduction = 0;
    Logging("Configuration loaded.");
    Logging("planArmoryProduction:" + outArray(planArmoryProduction));
    Logging("planMineProduction:" + outArray(planMineProduction));
    Logging("sleepInterval:" + String.valueOf(sleepInterval));
    Logging("randomInterval:" + String.valueOf(randomInterval));
    Logging("limitUsingSpecialShell:" + 
      String.valueOf(limitUsingSpecialShell));
    Logging("enemyDurabilityLimitUsingSpecialShall:" + 
      String.valueOf(enemyDurabilityLimitUsingSpecialShell));
    Logging("bankProduction:" + bankProduction);
    Logging("enemyDurabilityLimitUsingShortTimeReload:" + 
      enemyDurabilityLimitUsingShortTimeReload);
    Logging("maxDurability4UsingRepaer:" + maxDurability4UsingRepaer);
    Logging("battleTime:" + outArray(battleTimes));
    Logging("battleURL:" + outArray(battleURLs));
    Logging("FighterLogFileName:" + fighterLogFileName);
    Logging("Allied:" + outArray(allied) + "|");
    Logging("LimitChangeTarget:" + limitChangeTarget);
    Logging("User:" + userName);
    Logging("enableBodyLogging:" + enableBodyLogging);
    Logging("alliedExceptions:" + alliedExceptions);
  }
  
  private static goToURLFinderParserCallBack parseHTML(goToURLFinderParserCallBack parser)
    throws IOException
  {
    if (parser == null)
    {
      Logging("Parser is NULL: ");
      goToURL = "";
      method = "GET";
      timeOut = 0L;
      return null;
    }
    Logging("Parse using: " + parser.toString());
    ParserDelegator parserDelegator = new ParserDelegator();
    parserDelegator.parse(new StringReader(responseBody), parser, true);
    parser.afterParse();
    goToURL = parser.getURL();
    method = parser.getMethod();
    timeOut = parser.getTimeOut();
    Logging("goToURL=" + goToURL + "   method=" + method);
    return parser;
  }
  
  private static void readContent(HttpResponse siteResponse)
    throws UnsupportedEncodingException, IllegalStateException, IOException
  {
    HttpEntity entity = siteResponse.getEntity();
    Header contentEncoding = entity.getContentEncoding();
    String charSet = "UTF-8";
    if (contentEncoding != null) {
      charSet = contentEncoding.getValue();
    }
    InputStreamReader isr = new InputStreamReader(entity.getContent(), 
      charSet);
    StringWriter sw = new StringWriter();
    char[] charbuffer = new char[1024];
    for (;;)
    {
      int countRead = isr.read(charbuffer);
      if (countRead == -1) {
        break;
      }
      if (countRead > 0) {
        sw.write(charbuffer, 0, countRead);
      }
    }
    EntityUtils.consume(entity);
    responseBody = sw.toString();
    Logging(siteResponse.toString());
    if (enableBodyLogging == 1) {
      Logging(sw.toString());
    }
  }
  
  private static int executeHttpGet(String URL)
    throws Exception
  {
    Logging("Get Response from:" + URL);
    if (("".equals(URL)) || (URL == null)) {
      throw new Exception("Empty URL !!!");
    }
    HttpResponse response = httpclient.execute(new HttpGet(URL));
    if (response.getStatusLine().getStatusCode() == 302) {
      goToURL = getHeaderItem(response.getAllHeaders(), "Location");
    } else {
      readContent(response);
    }
    return response.getStatusLine().getStatusCode();
  }
  
  private static void executeHttpGetAndParse(String URL)
    throws Exception
  {
    if (executeHttpGet(URL) == 302) {
      executeHttpGetAndParse(goToURL);
    } else {
      parseHTML(getParserByURL(URL));
    }
  }
  
  private static int executeHttpPost(String URL)
    throws Exception
  {
    return executeHttpPost(URL, null);
  }
  
  private static int executeHttpPost(String URL, List<NameValuePair> requestParams)
    throws Exception
  {
    Logging("Get Response from:" + URL);
    if (("".equals(URL)) || (URL == null)) {
      throw new Exception("Empty URL !!!");
    }
    HttpPost httpPost = new HttpPost(URL);
    if (requestParams != null) {
      httpPost.setEntity(new UrlEncodedFormEntity(requestParams));
    }
    HttpResponse response = httpclient.execute(httpPost);
    if (response.getStatusLine().getStatusCode() == 302) {
      goToURL = getHeaderItem(response.getAllHeaders(), "Location");
    } else {
      readContent(response);
    }
    return response.getStatusLine().getStatusCode();
  }
  
  private static boolean isURLBattle(String URL)
  {
    for (String battlePath : battleURLs) {
      if (URL.contains(battlePath)) {
        return true;
      }
    }
    return false;
  }
  
  private static void executeHttpPostAndParse(String URL)
    throws Exception
  {
    executeHttpPostAndParse(URL, null);
  }
  
  private static void executeHttpPostAndParse(String URL, List<NameValuePair> requestParams)
    throws Exception
  {
    if (executeHttpPost(URL, requestParams) == 302) {
      executeHttpPostAndParse(goToURL, requestParams);
    } else {
      parseHTML(getParserByURL(goToURL));
    }
  }
  
  private static String getHeaderItem(Header[] headers, String name)
  {
    Header[] arrayOfHeader = headers;int j = headers.length;
    for (int i = 0; i < j; i++)
    {
      Header head = arrayOfHeader[i];
      if (head.getName().equals(name))
      {
        Logging(name + "=" + head.getValue());
        return head.getValue();
      }
    }
    return "";
  }
  
  private static goToURLFinderParserCallBack getParserByURL(String URL)
  {
    if (URL.toLowerCase().contains("/angar".toLowerCase())) {
      return new angarParserCallBack();
    }
    if (URL.toLowerCase().contains("/battle".toLowerCase())) {
      return new battleParserCallBack();
    }
    if (URL.toLowerCase().contains("/buildings".toLowerCase())) {
      return new buildingsParserCallBack();
    }
    if (URL.toLowerCase().contains("/convoy".toLowerCase())) {
      return new convoyParserCallBack();
    }
    if (URL.toLowerCase().contains("/production/Mine".toLowerCase())) {
      return new mineParserCallBack();
    }
    if (URL.toLowerCase().contains("/production/Armory".toLowerCase())) {
      return new armoryParserCallBack();
    }
    if (URL.toLowerCase().contains("/polygon".toLowerCase())) {
      return new polygonParserCallBack();
    }
    if (URL.toLowerCase().contains("/production/Bank".toLowerCase())) {
      return new bankParserCallBack();
    }
    if ((URL.equals("http://wartank.net")) || (URL.toLowerCase().contains("-showSigninLink".toLowerCase()))) {
      return new loginPageParserCallBack();
    }
    if (isURLBattle(URL)) {
      return new fightParserCallBack();
    }
    return null;
  }
  
  public static void main(String[] args)
  {
    boolean recivedStopSignal = false;
    
    configPath = "c:/AutomationWarTank.xml";
    if (args.length != 0) {
      try
      {
        if (args[0].toLowerCase().equals("stop"))
        {
          StopInterface stopProgram = (StopInterface)
            Naming.lookup("//localhost/AutomationWarTank");
          Logging("Send signal for stopping");
          stopProgram.stop();
          Logging("Signal was sended");
          return;
        }
        if (args[0].toLowerCase().equals("reloadconfig"))
        {
          StopInterface stopProgram = (StopInterface)
            Naming.lookup("//localhost/AutomationWarTank");
          Logging("Send signal for reload config");
          stopProgram.reloadConfiguration();
          Logging("Signal was sended");
          return;
        }
        configPath = args[0];
      }
      catch (Exception e)
      {
        Logging(e);
      }
    }
    Logging("Programm started.");
    try
    {
      loadingConfiguration(configPath);
    }
    catch (Exception e)
    {
      Logging(e);
      return;
    }
    Logging("RMI server starting");
    try
    {
      LocateRegistry.createRegistry(1099);
      Logging("RMI registry created.");
    }
    catch (RemoteException e)
    {
      Logging("RMI registry already exists.");
    }
    try
    {
      StopListener rmiServer = new StopListener();
      Naming.rebind("//localhost/AutomationWarTank", rmiServer);
      Logging("PeerServer bound in registry");
    }
    catch (Exception e)
    {
      Logging(e); return;
    }
    do
    {
      StopListener rmiServer;
      httpclient = HttpClients.createDefault();
      List<NameValuePair> nameValuePairs = new ArrayList();
      try
      {
        executeHttpGetAndParse("http://wartank.net");
        executeHttpGetAndParse(goToURL);
        
        nameValuePairs.add(new BasicNameValuePair("id1_hf_0", ""));
        nameValuePairs.add(new BasicNameValuePair("login", userName));
        nameValuePairs
          .add(new BasicNameValuePair("password", password));
        executeHttpPostAndParse(goToURL, nameValuePairs);
        if (goToURL.equals("")) {
          goToURL = "http://wartank.net/angar";
        }
        Date battleTime = null;
        for (;;)
        {
          Logging("Start next Iteration.");
          Logging("Check WarTime.");
          Date currentTime = 
            extractTime(new Date());
          boolean TimeForWar = false;
          String battleUrl = "";
          
          int index = 0;
          Date time;
          for (time : battleTimes)
          {
            if ((time.getTime() > currentTime.getTime()) && 
              (time.getTime() - currentTime.getTime() < 360000L))
            {
              battleUrl = "http://wartank.net" + battleURLs[index];
              TimeForWar = true;
              battleTime = time;
              Logging("Goto battle!!! URL" + battleURLs[index]);
              break;
            }
            index++;
          }
          if (TimeForWar)
          {
            if (!isURLBattle(goToURL))
            {
              countSkippedPlayers = 0;
              goToURL = battleUrl;
            }
          }
          else if ((battleTime != null) && 
            (isURLBattle(goToURL))) {
            if (currentTime.getTime() - battleTime.getTime() > 120000L) {
              if (goToURL.contains("refresh"))
              {
                goToURL = "http://wartank.net/angar";
                battleTime = null;
              }
            }
          }
          switch ((time = method).hashCode())
          {
          case 70454: 
            if (time.equals("GET")) {
              break;
            }
          case 2461856: 
            if ((goto 614) && (time.equals("POST")))
            {
              executeHttpPostAndParse(goToURL);
              
              executeHttpGetAndParse(goToURL);
            }
            break;
          }
          if (rmiServer.isStop())
          {
            recivedStopSignal = true;
            break;
          }
          if (rmiServer.isNeedReloadConfig())
          {
            loadingConfiguration(configPath);
            rmiServer.setNeedReloadConfig(false);
          }
          Logging("Wating " + (int)(timeOut / 1000L) + " seconds.");
          Thread.sleep(timeOut);
        }
      }
      catch (Exception e)
      {
        Logging(e);
        try
        {
          Thread.sleep(300000L);
        }
        catch (InterruptedException e1)
        {
          Logging(e1);
        }
      }
    } while (!recivedStopSignal);
    Logging("Programm stopped.");
  }
}
