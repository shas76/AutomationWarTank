package shas;

import java.util.Random;

class angarParserCallBack extends goToURLFinderParserCallBack {
	public angarParserCallBack(String currentURL) {
		super(currentURL);
		defaultGoToURL = Consts.siteAddress
				+ Consts.buildingsTab;
		if (AutomationWarTank.isTakeProductionMode) {
      AutomationWarTank.isTakeProductionMode = false;
			timeOut = 1000;
		} else {
      Random rnd = new Random();
			timeOut = (int) ((AutomationWarTank.sleepInterval + (rnd
					.nextDouble() * AutomationWarTank.randomInterval)) * Consts.msInMinunte);
    }
  }
}
