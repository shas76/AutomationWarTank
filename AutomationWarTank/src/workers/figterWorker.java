package workers;

import java.text.ParseException;
import java.util.Date;

import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class figterWorker extends AbstractWorker {

	private Date battleTime =null;

	private Date getNextButtle;
	
	@Override
	public void doWork() throws ParseException  {
		GlobalVars.logger.Logging("Check WarTime.", this);
		Date currentTime = AutomationWarTank.extractTime(new Date());
		boolean TimeForWar = false;
		String battleUrl = "";
		// Check Battle
		int index = 0;
		for (Date time : GlobalVars.config.getBattleTimes()) {
			if (time.getTime() > currentTime.getTime()
					&& (time.getTime() - currentTime.getTime() < 6 * Consts.msInMinunte)) {
				battleUrl = Consts.siteAddress + GlobalVars.config.getBattleURLs()[index];
				TimeForWar = true;
				battleTime = time;
				GlobalVars.logger.Logging("Goto battle!!! URL" + GlobalVars.config.getBattleURLs()[index], this);
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

	@Override
	public void doAfterWork() {
		// TODO Auto-generated method stub
		
	}

}
