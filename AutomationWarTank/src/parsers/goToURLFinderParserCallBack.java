package parsers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import http.request.processor.Response;
import shas.Consts;
import shas.GlobalVars;

public class goToURLFinderParserCallBack extends ParserCallback {
	private Response response = new Response("", Consts.GET_METHOD, 0);
	private boolean noMoreCalculte = false;
	private boolean checkActive = false;

	private List<String> linksToCheckIsActive = Arrays.asList("Mine", "polygon", "Armory", "Bank", "awardLink",
			"market", "buyGold", "freeBoostLink", "takeProductionLink", Consts.buildingsTab, Consts.convoyTab,
			"missions/", "Advanced");
	private List<String> pagesNotCheckIsActive = Arrays.asList("Mine", "polygon", "Armory", "Bank");

	private String currentActiveHREF = "";
	private Map<String, String> urlToPathOfPage = new HashMap<String, String>();
	{
		urlToPathOfPage.put("Mine", Consts.ProductionPath);
		urlToPathOfPage.put("Armory", Consts.ProductionPath);
		urlToPathOfPage.put("Bank", Consts.ProductionPath);
		urlToPathOfPage.put("missions", "/missions/");
	}
	private Map<String, String> backUrls4Page = new HashMap<String, String>();
	{
		backUrls4Page.put("Mine", Consts.buildingsTab);
		backUrls4Page.put("Armory", Consts.buildingsTab);
		backUrls4Page.put("Bank", Consts.buildingsTab);
		backUrls4Page.put("polygon", Consts.buildingsTab);
		backUrls4Page.put(Consts.buildingsTab, Consts.angarTab);
		backUrls4Page.put(Consts.convoyTab, Consts.angarTab);
		backUrls4Page.put("missions/", Consts.angarTab);
		backUrls4Page.put("Advanced", Consts.angarTab);
	}
	protected String currentURL;
	protected String pathToPage;
	protected boolean doCheckActive;

	public goToURLFinderParserCallBack(String currentURL) {
		super();
		this.currentURL = currentURL;
		doCheckActive = !pagesNotCheckIsActive.stream().anyMatch(lnk -> currentURL.contains(lnk));
		pathToPage = getPath4URL(currentURL, urlToPathOfPage, "/");
		String backPath = getPath4URL(currentURL, backUrls4Page, "");
		getResponse().setRedirectUrl("".equals(backPath) ? backPath : Consts.siteAddress + backPath);
	}

	protected String getPath4URL(String url, Map<String, String> listOfUrls4Page, String defaultValue) {
		return listOfUrls4Page.entrySet().stream().filter(entry -> currentURL.contains(entry.getKey()))
				.map(Map.Entry::getValue).findFirst().orElse(defaultValue);
	}

	public Response getResponse() {
		return response;
	}

	public boolean isNoMoreCalculte() {
		return noMoreCalculte;
	}

	public void setNoMoreCalculte(boolean noMoreCalculte) {
		this.noMoreCalculte = noMoreCalculte;
	}

	protected String formHREF(String relativeHREF) {
		String relativePathToPage = pathToPage + relativeHREF;
		return Consts.siteAddress + relativePathToPage.replace("//", "/");
	}

	protected String getHREF(Object attribute) {
		return attribute != null ? formHREF((String) attribute) : "";
	}

	// Start Tags
	protected void handleStartTagA(final String hREF, final Tag tag, final MutableAttributeSet attributes, final int pos) {

	};

	protected void handleStartTagFORM(final Tag tag, final MutableAttributeSet attributes, final int pos) {

	};

	protected void handleStartTagTD(final Tag tag, final MutableAttributeSet attributes, final int pos) {

	}

	protected void handleStartTagTR(final Tag tag, final MutableAttributeSet attributes, final int pos) {
	}

	protected void handleStartTagTABLE(final Tag tag, final MutableAttributeSet attributes, final int pos) {
	}

	// Text Tags
	protected void handleTextOfNode(final char[] data, final int pos) {

	}

	// End Tags
	protected void handleEndTagDIV(final Tag tag, final int pos) {

	};

	protected void handleEndTagTD(final Tag tag, final int pos) {

	};

	protected void handleEndTagA(final Tag tag, final int pos) {

	}

	protected void handleEndTagTABLE(final Tag tag, final int pos) {

	}

	protected void handleEndTagTR(final Tag tag, final int pos) {

	}

	// Simple Tags
	protected void handleSimpleTagIMG(final Tag tag, final MutableAttributeSet attributes, final int pos) {

	}

	// /////////////////////////////////////////////////////////////////////////////////

	@Override
	public void handleSimpleTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (isNoMoreCalculte()) {
			return;
		}

		if (tag == Tag.IMG) {
			handleSimpleTagIMG(tag, attributes, pos);
		}
	}

	@Override
	public void handleStartTag(Tag tag, MutableAttributeSet attributes, int pos) {
		if (isNoMoreCalculte()) {
			return;
		}
		if (tag == Tag.A) {
			String hREF = getHREF(attributes.getAttribute(Attribute.HREF));
			handleStartTagA(hREF, tag, attributes, pos);
			if (doCheckActive && linksToCheckIsActive.stream().anyMatch(lnk -> hREF.contains(lnk))) {
				currentActiveHREF = hREF;
				checkActive = true;
			}
		}
		if (tag == Tag.FORM) {
			handleStartTagFORM(tag, attributes, pos);
		}
		if (tag == Tag.TABLE) {
			handleStartTagTABLE(tag, attributes, pos);
		}
		if (tag == Tag.TR) {
			handleStartTagTR(tag, attributes, pos);
		}
		if (tag == Tag.TD) {
			handleStartTagTD(tag, attributes, pos);
		}
	}

	@Override
	public void handleText(char[] data, int pos) {
		if (noMoreCalculte)
			return;
		handleTextOfNode(data, pos);
		if (checkActive && new String(data).equals("+")) {
			getResponse().setRedirectUrl(currentActiveHREF);
			setNoMoreCalculte(true);
			GlobalVars.logger.Logging("Goto to active URL:" + getResponse());
			checkActive = false;
		}
	}

	@Override
	public void handleEndTag(Tag tag, int pos) {
		if (noMoreCalculte)
			return;
		if (tag == Tag.DIV) {
			handleEndTagDIV(tag, pos);
			if (checkActive) {
				checkActive = false;
			}
		}
		if (tag == Tag.TD) {
			handleEndTagTD(tag, pos);
			if (checkActive) {
				checkActive = false;
			}
		}
		if (tag == Tag.A) {
			handleEndTagA(tag, pos);
		}

		if (tag == Tag.TABLE) {
			handleEndTagTABLE(tag, pos);
		}
		if (tag == Tag.TR) {
			handleEndTagTR(tag, pos);
		}
	}

	public void afterParse() {

	}

}
