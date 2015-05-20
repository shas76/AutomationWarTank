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

class fightParserCallBack extends goToURLFinderParserCallBack {
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

	public fightParserCallBack() {
		super();
		defaultGoToURL = AutomationWarTank.siteAddress
				+ AutomationWarTank.angarTab;
		timeOut = 6050;
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			Object attribute = attributes.getAttribute(Attribute.HREF);
			String href = AutomationWarTank.siteAddress + "/"
					+ (String) attribute;

			if (href.contains("currentOverview")) {
				URL = href;
				if (timeLeft != null) {
					timeOut = timeLeft.getTime()
							- 20000 // Applay before 20 sec
							+ TimeZone.getDefault().getOffset(
									timeLeft.getTime());
				}
				noMoreCalculte = true;
				AutomationWarTank
						.Logging("Applay Button 'Platoon, lets roll! Attack!' !!!");
			}
			if (href.contains(AutomationWarTank.REFRESH)) {
				URL = href;
				if (timeLeft != null) {
					timeOut = timeLeft.getTime()
							- 20000 // Applay before 20 sec
							+ TimeZone.getDefault().getOffset(
									timeLeft.getTime());
				}

				noMoreCalculte = true;
				AutomationWarTank.Logging(AutomationWarTank.REFRESH);

			}
			if (href.contains("attackRegularShellLink")) {
				attackRegularShellLink = href;
			}
			if (href.contains("changeTargetLink")) {
				changeTargetLink = href;
			}
			if (href.contains("attackSpecialShellLink")) {
				inAttackSpecialShellLink = true;
				attackSpecialShellLink = href;
			}
			if (href.contains("repairLink")) {
				repairLink = href;
				inRepairLink = true;
			}
			if (href.contains("maneuverLink")) {
				maneuverLink = href;
				inManeuverLink = true;
			}
		}

		if (tag == Tag.TABLE) {
			levelNestedTables++;
		}
		if (tag == Tag.TR) {
			tableColumnsNumber[levelNestedTables] = 0;
		}
		if (tag == Tag.TD) {
			tableColumnsNumber[levelNestedTables]++;
		}
	}

	@Override
	public void handleSimpleTag(Tag arg0, MutableAttributeSet arg1, int arg2) {
		if (arg0 == Tag.BR) {
			fighterLog.write("\r\n");
		}
		super.handleSimpleTag(arg0, arg1, arg2);
	}

	@Override
	public void handleText(char[] data, int pos) {
		if (noMoreCalculte)
			return;

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
				AutomationWarTank
						.Logging("Time Left on Page:" + wordsInBody[2]);
				timeLeft = new SimpleDateFormat("HH:mm:ss")
						.parse(wordsInBody[2]);
				AutomationWarTank.Logging("Time Left:"
						+ String.valueOf(timeLeft.getTime()));
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
				AutomationWarTank
						.Logging("Enemy Durability:" + enemyDurability);
			}
			if (calculatingParameter == 0) {
				calculatingParameter++;
				try {
					myDurability = Integer.parseInt(bodyText);
				} catch (NumberFormatException e) {
					myDurability = 1000;
				}
				AutomationWarTank.Logging("My Durability:" + myDurability);
			}
		}
		if (tableColumnsNumber[1] == 2 && enemyName == null) {
			enemyName = bodyText;
			AutomationWarTank.Logging("EnemyName:" + enemyName);
		}

		if (inRepairLink) {
			repairLinkBody = bodyText;
		}

		if (inManeuverLink) {
			maneverLinkBody = bodyText;
		}
		if (inAttackSpecialShellLink) {
			String[] splitedBody = bodyText.split("[()]");
			try {
				countSpecialShells = Integer.parseInt(splitedBody[1]);
			} catch (NumberFormatException e) {
				countSpecialShells = 0;
			}
			AutomationWarTank.Logging("Count Special Shells:"
					+ countSpecialShells);
		}
	}

	@Override
	public void handleEndTag(Tag tag, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.A) {
			inRepairLink = false;
			inManeuverLink = false;
			inAttackSpecialShellLink = false;
		}
		if (tag == Tag.TABLE) {
			levelNestedTables--;
		}
		if (tag == Tag.TR) {
			tableColumnsNumber[levelNestedTables]--;
		}
	}

	@Override
	public void afterParse() {

		if (URL.equals("")) {
			if (repairLinkBody.contains("Repair kit")
					&& myDurability < AutomationWarTank.maxDurability4UsingRepaer) {
				URL = repairLink;
				timeOut = 0;
				AutomationWarTank.skipWaiting = true;
				AutomationWarTank.Logging("Use Repair!");
			} else {
				if (maneverLinkBody.contains("Maneuver")) {
					URL = maneuverLink;
					timeOut = 0;
					AutomationWarTank.skipWaiting = true;
					AutomationWarTank.Logging("Maneuver!");
				} else {
					boolean allied = false;
					boolean isAlliedException = false;
					if (AutomationWarTank.alliedExceptions.contains(enemyName
							.toLowerCase())) {
						isAlliedException = true;
						fighterLog.write("\r\n" + "Allied Exception:"
								+ enemyName + "\r\n");
					}
					if (!isAlliedException) {
						for (String alliance : AutomationWarTank.allied) {
							if (enemyName.toLowerCase().contains(alliance)) {
								allied = true;
								break;
							}
						}
					}
					if (allied) {
						if (AutomationWarTank.countSkippedPlayers == AutomationWarTank.limitChangeTarget) {
							URL = maneuverLink;
							fighterLog
									.write("\r\ncountSkippedPlayers == limitChangeTarget. Friend= "
											+ enemyName
											+ ". Go to maneuver\r\n");
							timeOut = 1000;
						} else {
							URL = changeTargetLink;
							timeOut = 1000;
							fighterLog.write("\r\n" + "Skip our friend! "
									+ enemyName + "\r\n");
							fighterLog.write("\r\n" + "CountSkippedPlayers: "
									+ AutomationWarTank.countSkippedPlayers
									+ "\r\n");

							AutomationWarTank.countSkippedPlayers++;
							AutomationWarTank.Logging("Skip our friend! "
									+ enemyName);
							AutomationWarTank.Logging("CountSkippedPlayers: "
									+ AutomationWarTank.countSkippedPlayers);
						}
					} else {
						AutomationWarTank.countSkippedPlayers = 0;
						AutomationWarTank.Logging("CountSkippedPlayers: "
								+ AutomationWarTank.countSkippedPlayers);
						if (!attackSpecialShellLink.equals("")
								&& countSpecialShells > AutomationWarTank.limitUsingSpecialShell
								&& enemyDurability > AutomationWarTank.enemyDurabilityLimitUsingSpecialShell
								&& AutomationWarTank.goToURL.contains(AutomationWarTank.dmTab)) {
							URL = attackSpecialShellLink;
							if (AutomationWarTank.skipWaiting) {
								timeOut = 0;
								AutomationWarTank.skipWaiting = false;
							}
							;
						} else {
							if (!AutomationWarTank.goToURL.contains(AutomationWarTank.dmTab)){
								
							}
							if (!attackRegularShellLink.equals("")) {
								URL = attackRegularShellLink;
								if (AutomationWarTank.skipWaiting) {
									timeOut = 0;
									AutomationWarTank.skipWaiting = false;
								} else {
									if (enemyDurability < AutomationWarTank.enemyDurabilityLimitUsingShortTimeReload
											&& enemyDurability > 0) {
										timeOut = 4000;
									}
								}
							}
						}
					}

				}
			}
		}
		PrintStream printStream;
		try {
			printStream = new PrintStream(new FileOutputStream(
					AutomationWarTank.fighterLogFileName, true));
			printStream.print(fighterLog.toString());
			printStream.close();
		} catch (FileNotFoundException e) {
			AutomationWarTank.Logging(e);
		}
		if (Math.abs(timeOut) > 5 * AutomationWarTank.msInMinunte) {
			timeOut = 10000;
		}
		if (timeOut < 0)
			timeOut = 1000;
		super.afterParse();
	}
}
