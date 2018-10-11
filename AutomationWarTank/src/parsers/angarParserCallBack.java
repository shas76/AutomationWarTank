package parsers;

import shas.GlobalVars;

public class angarParserCallBack extends goToURLFinderParserCallBack {
	public angarParserCallBack(String currentURL) {
		super(currentURL);
/*		if (GlobalVars.isTakeProductionMode) {
			GlobalVars.isTakeProductionMode = false;
		}*/
	}
}
