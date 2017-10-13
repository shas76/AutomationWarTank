package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import shas.GlobalVars;

public final class Config {

	private String configPath = "c:/AutomationWarTank.xml";
	private String[] planMineProduction = { "-2-", "-2-", "-2-", "-2-", "-0-",
			"-0-", "-0-", "-3-", "-3-", "-1-" };
	private String[] planArmoryProduction = { "-1-", "-1-", "-1-", "-1-",
			"-3-", "-0-", "-2-" };
	private int sleepInterval = 5;
	private int randomInterval = 2;
	private Date[] battleTimes;
	private String[] battleURLs;
	private String[] generalProcessingURLs;
	private int limitUsingSpecialShell;
	private int enemyDurabilityLimitUsingSpecialShell;
	private String bankProduction;
	private int enemyDurabilityLimitUsingShortTimeReload;
	private int maxDurability4UsingRepaer;
	private String fighterLogFileName;
	private String[] allied;
	private int limitChangeTarget;
	private String alliedExceptions = "";
	private int enableBodyLogging = 0;
	private String userName = "";
	private String password = "";

	public Config() throws ConfigurationException, ParseException {
		loadingConfiguration(this.configPath);
	}
	
	public Config(String configPath) throws ConfigurationException, ParseException {
		this.configPath = configPath;
		loadingConfiguration(this.configPath);
	}

	public void loadingConfiguration() throws ConfigurationException, ParseException{
		loadingConfiguration(configPath);
	}
	
	private void loadingConfiguration(String configFileName)
			throws ConfigurationException, ParseException {
		GlobalVars.logger.Logging("Current Config path:" + configPath);
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
		ArrayList<Date> listOfTimes = new ArrayList<Date>();
		for (String time : arrOfTimes) {
			listOfTimes.add(new SimpleDateFormat("HH:mm").parse(time));
		}
		Date[] tempBattleTime = listOfTimes.toArray(new Date[0]);
		String[] tempBattleURL = configRead.getStringArray("BattleURL");
		String[] tempGeneralProcessingURLs = configRead.getStringArray("GeneralProcessingURLs");
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
		enableBodyLogging = configRead.getInt("Enablelogger.LoggingBody", 0);
		String tempAlliedExceptions = configRead.getString("AlliedExceptions")
				.toLowerCase();

		planArmoryProduction = tempPlanArmoryProduction;
		planMineProduction = tempPlanMineProduction;
		sleepInterval = tempSleepInterval;
		randomInterval = tempRandomInterval;
		battleTimes = tempBattleTime;
		battleURLs = tempBattleURL;
		generalProcessingURLs = tempGeneralProcessingURLs;
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

		GlobalVars.logger.Logging("Configuration loaded.");
		GlobalVars.logger.Logging("planArmoryProduction:"
				+ outArray(planArmoryProduction));
		GlobalVars.logger.Logging("planMineProduction:"
				+ outArray(planMineProduction));
		GlobalVars.logger.Logging("sleepInterval:"
				+ String.valueOf(sleepInterval));
		GlobalVars.logger.Logging("randomInterval:"
				+ String.valueOf(randomInterval));
		GlobalVars.logger.Logging("limitUsingSpecialShell:"
				+ String.valueOf(limitUsingSpecialShell));
		GlobalVars.logger.Logging("enemyDurabilityLimitUsingSpecialShall:"
				+ String.valueOf(enemyDurabilityLimitUsingSpecialShell));
		GlobalVars.logger.Logging("bankProduction:" + bankProduction);
		GlobalVars.logger.Logging("enemyDurabilityLimitUsingShortTimeReload:"
				+ enemyDurabilityLimitUsingShortTimeReload);
		GlobalVars.logger.Logging("maxDurability4UsingRepaer:"
				+ maxDurability4UsingRepaer);
		GlobalVars.logger.Logging("battleTime:" + outArray(battleTimes));
		GlobalVars.logger.Logging("battleURL:" + outArray(battleURLs));
		GlobalVars.logger.Logging("GeneralProcessingURLs:" + outArray(generalProcessingURLs));
		GlobalVars.logger.Logging("FighterLogFileName:" + fighterLogFileName);
		GlobalVars.logger.Logging("Allied:" + outArray(allied) + "|");
		GlobalVars.logger.Logging("LimitChangeTarget:" + limitChangeTarget);
		GlobalVars.logger.Logging("User:" + userName);
		GlobalVars.logger.Logging("enableBodylogger.Logging:"
				+ enableBodyLogging);
		GlobalVars.logger.Logging("alliedExceptions:" + alliedExceptions);
	}

	private String outArray(Object[] objs) {
		String str2print = "";
		for (Object obj : objs) {
			str2print += obj.toString() + ",";
		}
		return str2print;
	}

	public String getConfigPath() {
		return configPath;
	}

	public String[] getPlanMineProduction() {
		return planMineProduction;
	}

	public String[] getPlanArmoryProduction() {
		return planArmoryProduction;
	}

	public int getSleepInterval() {
		return sleepInterval;
	}

	public int getRandomInterval() {
		return randomInterval;
	}

	public Date[] getBattleTimes() {
		return battleTimes;
	}

	public String[] getBattleURLs() {
		return battleURLs;
	}

	public int getLimitUsingSpecialShell() {
		return limitUsingSpecialShell;
	}

	public int getEnemyDurabilityLimitUsingSpecialShell() {
		return enemyDurabilityLimitUsingSpecialShell;
	}

	public String getBankProduction() {
		return bankProduction;
	}

	public int getEnemyDurabilityLimitUsingShortTimeReload() {
		return enemyDurabilityLimitUsingShortTimeReload;
	}

	public int getMaxDurability4UsingRepaer() {
		return maxDurability4UsingRepaer;
	}

	public String getFighterLogFileName() {
		return fighterLogFileName;
	}

	public String[] getAllied() {
		return allied;
	}

	public int getLimitChangeTarget() {
		return limitChangeTarget;
	}

	public String getAlliedExceptions() {
		return alliedExceptions;
	}

	public int getEnableBodyLogging() {
		return enableBodyLogging;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String[] getGeneralProcessingURLs() {
		return generalProcessingURLs;
	}
}
