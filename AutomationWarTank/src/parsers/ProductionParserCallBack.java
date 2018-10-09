package parsers;

import shas.Consts;

public class ProductionParserCallBack extends goToURLFinderParserCallBack {

	public ProductionParserCallBack(String currentURL) {
		super(currentURL);
		pathToPage = Consts.ProductionPath;
		getResponse().setRedirectUrl(Consts.buildingsTab);
	}

}