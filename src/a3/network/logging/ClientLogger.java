package a3.network.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import a3.network.api.messages.MessageType;

public enum ClientLogger {

	INSTANCE;
	
	private final LogFilter filter = new LogFilter();
	
	public void addFilter(MessageType type) {
		this.filter.addFilter(type);
	}
	
	public void removeFilter(MessageType type) {
		this.filter.removeFilter(type);
	}
	
	public void logln(String msg) {
		if (filter.filter(msg)) return;
		System.out.println(msg);
	}
	
	public void log(String msg) {
		if (filter.filter(msg)) return;
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
