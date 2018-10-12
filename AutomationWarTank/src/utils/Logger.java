package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	public void Logging(String message) {
		System.out.printf("%s %s\n", new Object[] { dateFormat.format(new Date()), message });
	}

	public void Logging(String message, Object object) {
		Logging(message);
	}

	public void Logging(Exception e) {
		System.out.printf("%s ", new Object[] { dateFormat.format(new Date()) });
		e.printStackTrace();
	}

	public void Logging(Exception e, Object object) {
		Logging(e);
	}
}
