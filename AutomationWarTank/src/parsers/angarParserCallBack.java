package parsers;

import java.util.Random;

import shas.AutomationWarTank;
import shas.Consts;
import shas.GlobalVars;

public class angarParserCallBack extends goToURLFinderParserCallBack {
	public angarParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress + Consts.buildingsTab;
		if (AutomationWarTank.isTakeProductionMode) {
			AutomationWarTank.isTakeProductionMode = false;
			timeOut = 1000;
		} else {
			Random rnd = new Random();
			timeOut = (int) ((GlobalVars.config.getSleepInterval() + (rnd
					.nextDouble() * GlobalVars.config.getRandomInterval())) * Consts.msInMinunte);
		}
	}
}
