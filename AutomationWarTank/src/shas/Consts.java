package shas;

import java.util.Arrays;
import java.util.List;

public final class Consts {
	public static final String RMI_SEVER_LOCATOR = "//localhost/AutomationWarTank";
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";
	public static final int request_redirected_302 = 302;

	public static final String PROVISION_LINK = "provisionLink";
	public static final String TAKE_FUEL_LINK = "takeFuelLink";
	public static final String SKILLS = "/skills/";
	public static final String PROFILE = "profile";
	public static final String ADVANCED = "Advanced";
	public static final String MISSIONS = "missions";
	public static final String TAKE_PRODUCTION_LINK = "takeProductionLink";
	public static final String FREE_BOOST_LINK = "freeBoostLink";
	public static final String BUY_GOLD = "buyGold";
	public static final String MARKET = "market";
	public static final String AWARD_LINK = "awardLink";
	
	public static final String BANK = "Bank";
	public static final String ARMORY = "Armory";
	public static final String POLYGON = "polygon";
	public static final String MINE = "Mine";	
	
	public static final String siteAddress = "http://wartank.net";
	public static final String ProductionPath = "/production/";
	public static final String timeFormat = "%tF %tT ";
	public static final String battleTab = "/battle";
	public static final String angarTab = "/angar";
	public static final String convoyTab = "/convoy";
	public static final String buildingsTab = "/buildings";
	public static final String mineTab = ProductionPath + MINE;
	public static final String polygonTab = "/polygon";
	public static final String armoryTab = ProductionPath + Consts.ARMORY;
	public static final String bankTab = ProductionPath + BANK;
	public static final String coinsTab = "/coins/";
	public static final String pveTab = "/pve"; // Campaigns
	public static final String cwTab = "/cw"; // war
	public static final String dmTab = "/dm"; // Skirmish
	public static final String REFRESH = "refresh";
	public static final int ONE_SECOND = 1000;
	public static final int msInMinunte = 60 * ONE_SECOND;
	public static final String SHOW_SIGNIN_LINK = "-showSigninLink";
	public static final String LOCATION_HEADER = "Location";
	public static final List<String> COMMANDS = Arrays.asList("stop", "reloadconfig");
	public static final int RMI_SERVER_PORT = 2099;
	public static final int ENEMY_DURABILITY_TO_SHOT = 2250;
	public static final long MAX_INTERVAL_BETWEEN_SHOT = 7050;
	public static final long MIN_INTERVAL_BETWEEN_SHOT = 4050;

}
