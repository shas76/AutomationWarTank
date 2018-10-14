package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import shas.Consts;

public class URLsConvertor {
	public static Map<String, String> urlToPathOfPage = new HashMap<String, String>();
	static {
		urlToPathOfPage.put(Consts.MINE, Consts.ProductionPath);
		urlToPathOfPage.put(Consts.ARMORY, Consts.ProductionPath);
		urlToPathOfPage.put(Consts.BANK, Consts.ProductionPath);
		urlToPathOfPage.put(Consts.MISSIONS, "/"+Consts.MISSIONS+"/");
		urlToPathOfPage.put(Consts.PROFILE, "/"+Consts.PROFILE+"/");
	}
	public static Map<String, String> backUrls4Page = new HashMap<String, String>();
	static {
		backUrls4Page.put(Consts.MINE, Consts.buildingsTab);
		backUrls4Page.put(Consts.ARMORY, Consts.buildingsTab);
		backUrls4Page.put(Consts.BANK, Consts.buildingsTab);
		backUrls4Page.put(Consts.POLYGON, Consts.buildingsTab);
		backUrls4Page.put(Consts.buildingsTab, Consts.angarTab);
		backUrls4Page.put(Consts.convoyTab, Consts.angarTab);
		backUrls4Page.put(Consts.MISSIONS+"/", Consts.angarTab);
		backUrls4Page.put(Consts.ADVANCED, Consts.angarTab);
	}

	private static Map<String, String> url2vurl = new HashMap<String, String>();
	static {
		url2vurl.put(Consts.cwTab, Consts.cwTab + "VirtualLink");
	}

	public static Map<String, String> getURL2VURLByUrl(String uRL) {
		return url2vurl.entrySet().stream().filter(entry -> uRL.contains(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static Map<String, String> getURL2VURLByVUrl(String vURL) {
		return url2vurl.entrySet().stream().filter(entry -> vURL.contains(entry.getValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	public static String getPath4URL(String url, Map<String, String> listOfUrls4Page, String defaultValue) {
		return listOfUrls4Page.entrySet().stream().filter(entry -> url.contains(entry.getKey()))
				.map(Map.Entry::getValue).findFirst().orElse(defaultValue);
	}
}
