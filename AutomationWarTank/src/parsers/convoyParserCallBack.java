package parsers;

import java.util.Arrays;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Tag;

import shas.GlobalVars;

public class convoyParserCallBack extends goToURLFinderParserCallBack {

	private List<String> linksOfActions = Arrays.asList("findEnemy", "startFight", "attackRegular", "startMasking");

	public convoyParserCallBack(String currentURL) {
		super(currentURL);
	}

	@Override
	protected void handleStartTagA(String hREF, Tag tag, MutableAttributeSet attributes, int pos) {

		if (linksOfActions.stream().anyMatch(lnk -> hREF.contains(lnk))) {
			getResponse().setRedirectUrl(hREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Attack Convoy!!!");
		}
	}

}
