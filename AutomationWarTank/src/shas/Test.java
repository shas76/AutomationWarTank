package shas;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;


import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.text.html.parser.ParserDelegator;

import org.apache.http.Header;
import org.apache.http.util.EntityUtils;

public class Test {

	public static void main(String[] args)  {

		try {
			URL url = new URL("file:///z://keys");

			InputStreamReader isr;
			isr = new InputStreamReader(url.openStream());
			StringWriter sw = new StringWriter();
			char[] charbuffer = new char[1024];
			while (true) {
				int countRead = isr.read(charbuffer);
				if (countRead == -1)
					break;
				if (countRead > 0) {
					sw.write(charbuffer, 0, countRead);
				}
			}
			System.out.print(sw.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

/*		
		Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(2);
		
		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 05);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(calendar.getTime());
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 04);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		System.out.println(calendar.getTime());
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));

		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 05);
		calendar.set(Calendar.DAY_OF_MONTH, 30);
		System.out.println(calendar.getTime());
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 06);
		calendar.set(Calendar.DAY_OF_MONTH, 01);
		System.out.println(calendar.getTime());
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));
		
		calendar.set(Calendar.YEAR, 2014);
		calendar.set(Calendar.MONTH, 00);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(calendar.getTime());
		System.out.println(calendar.get(Calendar.WEEK_OF_YEAR));*/
	}

}
