package shas;

import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringWriter;
import java.net.URL;

@SuppressWarnings("unused")
public class Test {
	public static void main(String[] args) {
		try {
      URL url = new URL("file:///z://keys");
      
      InputStreamReader isr = new InputStreamReader(url.openStream());
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
      e.printStackTrace();
    }
  }
}
