package shas;

import java.util.Random;

class angarParserCallBack extends goToURLFinderParserCallBack {
	public angarParserCallBack() {
		super();
		defaultGoToURL = AutomationWarTank.siteAddress
				+ AutomationWarTank.buildingsTab;
		if (AutomationWarTank.isTakeProductionMode) {
			AutomationWarTank.isTakeProductionMode = false;
			timeOut = 1000;
		} else {
			Random rnd = new Random();
			timeOut = (int) ((AutomationWarTank.sleepInterval + (rnd
					.nextDouble() * AutomationWarTank.randomInterval)) * AutomationWarTank.msInMinunte);
		}
	}
}
