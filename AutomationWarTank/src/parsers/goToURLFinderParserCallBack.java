package parsers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

import http.request.processor.Response;
import shas.Consts;
import shas.GlobalVars;
import utils.URLsConvertor;

public class goToURLFinderParserCallBack extends ParserCallback {

	private Response response = new Response("", Consts.GET_METHOD, 0);
	private boolean noMoreCalculte = false;
	private boolean checkActive = false;

	private List<String> linksToCheckIsActive = Arrays.asList(Consts.MINE, Consts.POLYGON, Consts.ARMORY, Consts.BANK,
			Consts.AWARD_LINK, Consts.MARKET, Consts.BUY_GOLD, Consts.FREE_BOOST_LINK, Consts.TAKE_PRODUCTION_LINK,
			Consts.buildingsTab, Consts.convoyTab, Consts.MISSIONS + "/", Consts.ADVANCED, Consts.PROFILE + "/",
			Consts.SKILLS, Consts.TAKE_FUEL_LINK, Consts.cwTab, Consts.PROVISION_LINK);
	private List<String> pagesNotCheckIsActive = Arrays.asList(Consts.MINE, Consts.POLYGON, Consts.ARMORY, Consts.BANK);

	private String currentActiveHREF = "";

	protected String currentURL;
	protected String pathToPage;
	protected boolean doCheckActive;

	public goToURLFinderParserCallBack(String currentURL) {
		super();
		this.currentURL = currentURL;
		doCheckActive = !pagesNotCheckIsActive.stream().anyMatch(lnk -> currentURL.contains(lnk));
		pathToPage = URLsConvertor.getPath4URL(currentURL, URLsConvertor.urlToPathOfPage, "/");
		String backPath = URLsConvertor.getPath4URL(currentURL, URLsConvertor.backUrls4Page, "");
		getResponse().setRedirectUrl("".equals(backPath) ? backPath : Consts.siteAddress + backPath);
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
		String relativePathToPage;
		if (relativeHREF.contains("..")) {
			relativePathToPage = relativeHREF.replace("..", "");
		} else {
			relativePathToPage = pathToPage + relativeHREF;
		}
		GlobalVars.logger.Logging("\r\n               relativeHREF: " + relativeHREF
				+ "\r\n    >>>> relativePathToPage: " + relativePathToPage);
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
			if (doCheckActive && linksToCheckIsActive.stream().anyMatch(lnk -> hREF.contains(lnk))
					&& !hREF.contains(Consts.MISSIONS + "/.")) {
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
			Map<String, String> vURL = URLsConvertor.getURL2VURLByUrl(currentActiveHREF);
			if (!vURL.isEmpty()) {
				String key = (String) (vURL.keySet().toArray())[0];
				GlobalVars.logger.Logging("    Replace VURL:" + key + " by " + vURL.get(key) + " in "
						+ currentActiveHREF);
				currentActiveHREF = currentActiveHREF.replace(key, vURL.get(key));
			}
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
