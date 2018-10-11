package shas;

import utils.Config;
import utils.Logger;

public class GlobalVars {
	// public static String lastEnemyName = "";
	// public static boolean skipWaiting = false;
	public static Logger logger = new Logger();
	public static Config config;
	public final static String monitor = "";
	public static String command = "";
	public static int currentArmoryProduction = 0;
	public static int countSkippedPlayers = 0;
	public static int currentMineProduction = 0;
//	public static boolean isTakeProductionMode = false;
	public static boolean afterShot = false;
	public static long timeOfShot = 0;
}
