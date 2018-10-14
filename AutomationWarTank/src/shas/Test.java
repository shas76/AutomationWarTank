package shas;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Test {
	public static void main(String[] args) {
		
		String ffff = "../sfsfzsfzs".replace("..", "");
		System.out.println(ffff);
		Map<String, String> urlToPathOfPage = new HashMap<String, String>();

		urlToPathOfPage.put("Mine", Consts.ProductionPath + "1");
		urlToPathOfPage.put("Armory", Consts.ProductionPath + "2");
		urlToPathOfPage.put("Bank", Consts.ProductionPath + "3");
		urlToPathOfPage.put("missions", "/missions/");

		System.out.println(urlToPathOfPage.entrySet().stream().filter(entry -> "Armory".contains(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
		System.out.println(urlToPathOfPage.entrySet().stream()
				.filter(entry -> (Consts.ProductionPath + "2").contains(entry.getValue())).map(Map.Entry::getKey)
				.findFirst().get());
	}

}
