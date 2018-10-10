package parsers;

import shas.Consts;

public class ProductionParserCallBack extends goToURLFinderParserCallBack {

	public ProductionParserCallBack(String currentURL) {
		super(currentURL);
		getResponse().setRedirectUrl(Consts.siteAddress+Consts.buildingsTab);
		doCheckActive = false;
	}

}