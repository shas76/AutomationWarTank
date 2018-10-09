package parsers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.Consts;
import shas.GlobalVars;

public class fightParserCallBack extends goToURLFinderParserCallBack {

	private static final String MANEUVER_LINK = "maneuverLink";
	private static final String REPAIR_LINK = "repairLink";
	private static final String ATTACK_SPECIAL_SHELL_LINK = "attackSpecialShellLink";
	private static final String CHANGE_TARGET_LINK = "changeTargetLink";
	private static final String ATTACK_REGULAR_SHELL_LINK = "attackRegularShellLink";

	private List<String> linksIDs = Arrays.asList(MANEUVER_LINK, REPAIR_LINK, ATTACK_SPECIAL_SHELL_LINK,
			CHANGE_TARGET_LINK, ATTACK_REGULAR_SHELL_LINK);
	private Date timeLeft = null;
	private int myDurability;
	private int enemyDurability;
	private int countSpecialShells;
	private int levelNestedTables = 0;
	private int calculatingParameter = 0;
	// private boolean inRepairLink = false;
	// private boolean inManeuverLink = false;
	// // private boolean inAttackSpecialShellLink = false;
	private int[] tableColumnsNumber = new int[20];
	private long timeOut;

	private HashMap<String, String> links = new HashMap<String, String>();
	private HashMap<String, String> linksBody = new HashMap<String, String>();
	private String currentLinkID = null;

	private String enemyName;
	private StringWriter fighterLog = new StringWriter();

	public fightParserCallBack(String currentURL) {
		super(currentURL);
		// defaultGoToURL = Consts.siteAddress + Consts.angarTab;
		getResponse().setDelay(500);
		if (GlobalVars.afterShot) {
			GlobalVars.afterShot = false;
			GlobalVars.timeOfShot = getCurrentTime();
		}
	}

	private long getCurrentTime() {
		return ZonedDateTime.now().toInstant().toEpochMilli();
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {

		if (hREF.contains("currentOverview")) {
			setDelayBeforeFight(hREF);
			GlobalVars.logger.Logging("Applay Button 'Platoon, lets roll! Attack!' !!!");
		}
		if (hREF.contains(Consts.REFRESH)) {
			setDelayBeforeFight(hREF);
			GlobalVars.logger.Logging(Consts.REFRESH);

		}
		currentLinkID = linksIDs.stream().filter(linkID -> hREF.contains(linkID)).findFirst().orElse(null);
		if (currentLinkID != null) {
			links.put(currentLinkID, hREF);
		}

	}

	@Override
	protected void handleStartTagTD(Tag tag, MutableAttributeSet attributes, int pos) {
		tableColumnsNumber[levelNestedTables]++;
	}

	@Override
	protected void handleStartTagTR(Tag tag, MutableAttributeSet attributes, int pos) {
		tableColumnsNumber[levelNestedTables] = 0;
	}

	@Override
	protected void handleStartTagTABLE(Tag tag, MutableAttributeSet attributes, int pos) {
		levelNestedTables++;
	}

	private void setDelayBeforeFight(String hREF) {
		getResponse().setRedirectUrl(hREF);
		if (timeLeft != null) {
			getResponse().setDelay(timeLeft.getTime() - 20000 // Apply before 20
																// sec
					+ TimeZone.getDefault().getOffset(timeLeft.getTime()));
		}
		setNoMoreCalculte(true);
	}

	@Override
	public void handleSimpleTag(Tag arg0, MutableAttributeSet arg1, int arg2) {
		if (arg0 == Tag.BR) {
			fighterLog.write("\r\n");
		}
		super.handleSimpleTag(arg0, arg1, arg2);
	}

	@Override
	protected void handleTextOfNode(char[] data, int pos) {
		String bodyText = new String(data);
		if (levelNestedTables == 0) {
			fighterLog.write(bodyText);
			if (bodyText.contains(":")) {
				fighterLog.write("\r\n");
			}
		}
		if (bodyText.contains("starts in")) {
			String[] wordsInBody = bodyText.split(" ");
			try {
				GlobalVars.logger.Logging("Time Left on Page:" + wordsInBody[2]);
				timeLeft = new SimpleDateFormat("HH:mm:ss").parse(wordsInBody[2]);
				GlobalVars.logger.Logging("Time Left:" + String.valueOf(timeLeft.getTime()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (tableColumnsNumber[2] == 2) {
			if (calculatingParameter == 1) {
				try {
					enemyDurability = Integer.parseInt(bodyText);
				} catch (NumberFormatException e) {
					enemyDurability = 1000;
				}
				GlobalVars.logger.Logging("Enemy Durability:" + enemyDurability);
			}
			if (calculatingParameter == 0) {
				calculatingParameter++;
				try {
					myDurability = Integer.parseInt(bodyText);
				} catch (NumberFormatException e) {
					myDurability = 1000;
				}
				GlobalVars.logger.Logging("My Durability:" + myDurability);
			}
		}
		if (tableColumnsNumber[1] == 2 && enemyName == null) {
			enemyName = bodyText.toLowerCase();
			GlobalVars.logger.Logging("EnemyName:" + enemyName);
		}

		if (currentLinkID != null) {
			linksBody.put(currentLinkID, bodyText);
			if (ATTACK_SPECIAL_SHELL_LINK.equals(currentLinkID)) {
				String[] splitedBody = bodyText.split("[()]");
				try {
					countSpecialShells = Integer.parseInt(splitedBody[1]);
				} catch (NumberFormatException e) {
					countSpecialShells = 0;
				}
				GlobalVars.logger.Logging("Count Special Shells:" + countSpecialShells);
			}

		}
	}

	@Override
	protected void handleEndTagA(Tag tag, int pos) {
		currentLinkID = null;
	}

	@Override
	protected void handleEndTagTABLE(Tag tag, int pos) {
		levelNestedTables--;
	}

	@Override
	protected void handleEndTagTR(Tag tag, int pos) {
		tableColumnsNumber[levelNestedTables]--;
	}

	@Override
	public void afterParse() {

		if ("".equals(getResponse().getRedirectUrl())) {
			if (linksBody.get(REPAIR_LINK).contains("Repair kit")
					&& myDurability < GlobalVars.config.getMaxDurability4UsingRepaer()) {
				getResponse().setRedirectUrl(links.get(REPAIR_LINK));
				getResponse().setDelay(0);
				GlobalVars.logger.Logging("Use Repair!");
			} else {
				if (linksBody.get(MANEUVER_LINK).contains("Maneuver")) {
					getResponse().setRedirectUrl(links.get(MANEUVER_LINK));
					getResponse().setDelay(0);
					GlobalVars.logger.Logging("Maneuver!");
				} else {

					if (isEnemyAllied()) {
						skipFriend();
					} else {
						prepareForShot();
					}
				}
			}
		}

		PrintStream printStream;
		try {
			printStream = new PrintStream(new FileOutputStream(GlobalVars.config.getFighterLogFileName(), true));
			printStream.print(fighterLog.toString());
			printStream.close();
		} catch (FileNotFoundException e) {
			GlobalVars.logger.Logging(e);
		}
		if (Math.abs(timeOut) > 5 * Consts.msInMinunte) {
			timeOut = 10000;
		}
		if (timeOut < 0)
			timeOut = 1000;
		super.afterParse();
	}

	private void prepareForShot() {
		GlobalVars.countSkippedPlayers = 0;
		GlobalVars.logger.Logging("CountSkippedPlayers: " + GlobalVars.countSkippedPlayers);
		if (enemyDurability > Consts.ENEMY_DURABILITY_TO_SHOT && !currentURL.contains(Consts.dmTab)) {
			getResponse().setRedirectUrl(links.get(CHANGE_TARGET_LINK));
		} else {
			long intervalBetweenShots = Consts.MAX_INTERVAL_BETWEEN_SHOT;
			if (enemyDurability < GlobalVars.config.getEnemyDurabilityLimitUsingShortTimeReload()
					&& enemyDurability > 0) {
				intervalBetweenShots = Consts.MIN_INTERVAL_BETWEEN_SHOT;
			}
			if (getCurrentTime() - GlobalVars.timeOfShot > intervalBetweenShots) {
				if (canUseSpecialShell()) {
					getResponse().setRedirectUrl(links.get(ATTACK_SPECIAL_SHELL_LINK));
				} else {
					if (links.get(ATTACK_REGULAR_SHELL_LINK) != null) {
						getResponse().setRedirectUrl(links.get(ATTACK_REGULAR_SHELL_LINK));
					}
				}
				GlobalVars.afterShot = true;
			} else {
				getResponse().setRedirectUrl(links.get(MANEUVER_LINK));
				GlobalVars.logger.Logging("Do maneuver by default!");
			}
		}
	}

	private boolean canUseSpecialShell() {
		return links.get(ATTACK_SPECIAL_SHELL_LINK) != null
				&& countSpecialShells > GlobalVars.config.getLimitUsingSpecialShell()
				&& enemyDurability > GlobalVars.config.getLimitUsingSpecialShell()
				&& (currentURL.contains(Consts.dmTab) || (countSpecialShells > GlobalVars.config
						.getLimitUsingSpecialShell() + 100));
	}

	private void skipFriend() {
		if (GlobalVars.countSkippedPlayers == GlobalVars.config.getLimitChangeTarget()) {
			getResponse().setRedirectUrl(links.get(MANEUVER_LINK));
			fighterLog.write("\r\ncountSkippedPlayers == limitChangeTarget. Friend= " + enemyName
					+ ". Go to maneuver\r\n");
			timeOut = 1000;
		} else {
			getResponse().setRedirectUrl(links.get(CHANGE_TARGET_LINK));
			timeOut = 1000;
			fighterLog.write("\r\n" + "Skip our friend! " + enemyName + "\r\n");
			fighterLog.write("\r\n" + "CountSkippedPlayers: " + GlobalVars.countSkippedPlayers + "\r\n");

			GlobalVars.countSkippedPlayers++;
			GlobalVars.logger.Logging("Skip our friend! " + enemyName);
			GlobalVars.logger.Logging("CountSkippedPlayers: " + GlobalVars.countSkippedPlayers);
		}
	}

	private boolean isEnemyAllied() {

		if (GlobalVars.config.getAlliedExceptions().contains(enemyName)) {
			fighterLog.write("\r\n" + "Allied Exception:" + enemyName + "\r\n");
			return false;
		}

		for (String alliance : GlobalVars.config.getAllied()) {
			if (enemyName.contains(alliance)) {
				return true;
			}
		}

		return false;
	}
}
