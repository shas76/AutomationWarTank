package shas;

import java.text.ParseException;
import java.util.Date;

public class figterWorker extends AbstractWorker {

	private Date battleTime =null;

	private Date getNextButtle
	@Override
	public void doWork() throws ParseException  {
		AutomationWarTank.Logging("Check WarTime.", this);
		Date currentTime = AutomationWarTank.extractTime(new Date());
		boolean TimeForWar = false;
		String battleUrl = "";
		// Check Battle
		int index = 0;
		for (Date time : AutomationWarTank.battleTimes) {
			if (time.getTime() > currentTime.getTime()
					&& (time.getTime() - currentTime.getTime() < 6 * Consts.msInMinunte)) {
				battleUrl = Consts.siteAddress + AutomationWarTank.battleURLs[index];
				TimeForWar = true;
				battleTime = time;
				AutomationWarTank.Logging("Goto battle!!! URL" + AutomationWarTank.battleURLs[index], this);
				break;
			}
			index++;
		}
		// TO DO check URL contains IN ARRAY
		if (TimeForWar) {
			if (!isURLBattle(goToURL)) {
				AutomationWarTank.countSkippedPlayers = 0;
				goToURL = battleUrl;
			}
		} else {
			if (battleTime != null) {
				if (isURLBattle(goToURL)
						&& (currentTime.getTime() - battleTime.getTime()) > 2 * Consts.msInMinunte
						&& goToURL.contains(Consts.REFRESH)) {
					goToURL = Consts.siteAddress + Consts.angarTab;
					battleTime = null;
				}
			}
		}

	}

	@Override
	public void doBeforeWhile() {
		battleTime =null;		
	}

}
