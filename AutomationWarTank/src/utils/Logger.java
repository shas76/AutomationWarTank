package utils;

import java.util.Date;

public final class Logger {
	public void Logging(String message) {
		System.out.printf("%tF %tT %s\n", new Object[] { new Date(),
				new Date(), message });
	}

	public void Logging(String message, Object object) {
		Logging(message);
	}
	
	public void Logging(Exception e) {
		System.out.printf("%tF %tT ", new Object[] { new Date(), new Date() });
		e.printStackTrace();
	}

	public void Logging(Exception e, Object object) {
		Logging(e);
	}
}
