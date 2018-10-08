package parsers;

import shas.GlobalVars;

public class angarParserCallBack extends goToURLFinderParserCallBack {
	public angarParserCallBack(String currentURL) {
		super(currentURL);
//		defaultGoToURL = Consts.siteAddress + Consts.buildingsTab;
		if (GlobalVars.isTakeProductionMode) {
			GlobalVars.isTakeProductionMode = false;
//			timeOut = 1000;
		}
	}
}
