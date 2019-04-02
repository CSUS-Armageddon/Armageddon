package a3.network.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public enum ClientLogger {

	INSTANCE;
	
	public void logln(String msg) {
		System.out.println(msg);
	}
	
	public void log(String msg) {
		System.out.print(msg);
	}
	
	public void log(Exception e) {
		final StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		System.out.println(sw.toString());
		pw.close();
		try {
			sw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
