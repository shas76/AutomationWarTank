package shas;

import java.util.Arrays;
import java.util.List;

public final class Consts {
	public static final String RMI_SEVER_LOCATOR = "//localhost/AutomationWarTank";
	public static final String GET_METHOD = "GET";
	public static final String POST_METHOD = "POST";
	public static final int request_redirected_302 = 302;
	public static final String siteAddress = "http://wartank.net";
	public static final String ProductionPath = "/production/";
	public static final String timeFormat = "%tF %tT ";
	public static final String battleTab = "/battle";
	public static final String angarTab = "/angar";
	public static final String convoyTab = "/convoy";
	public static final String buildingsTab = "/buildings";
	public static final String mineTab = ProductionPath + "Mine";
	public static final String polygonTab = "/polygon";
	public static final String armoryTab = ProductionPath + "Armory";
	public static final String bankTab = ProductionPath + "Bank";
	public static final String pveTab = "/pve";
	public static final String cwTab = "/cw";
	public static final String dmTab = "/dm";
	public static final String REFRESH = "refresh";
	public static final int ONE_SECOND = 1000;
	public static final int msInMinunte = 60 * ONE_SECOND;
	public static final String SHOW_SIGNIN_LINK = "-showSigninLink";
	public static final String LOCATION_HEADER = "Location";
	public static final List<String> COMMANDS = Arrays.asList("stop", "reloadconfig");
	public static final int RMI_SERVER_PORT = 2099;
	// static int printResponseBoby = 0;
	public static final int ENEMY_DURABILITY_TO_SHOT = 2000;
	public static final long MAX_INTERVAL_BETWEEN_SHOT = 6050;
	public static final long MIN_INTERVAL_BETWEEN_SHOT = 4000;
}
