package a3.network.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JTextArea;

public enum Logger {

	INSTANCE;
	
	private JTextArea logWindow;
	
	public void setLogWindow(JTextArea jta) {
		this.logWindow = jta;
	}
	
	public void logln(String msg) {
		System.out.println(msg);
		this.logWindow.append(msg + "\n");
	}
	
	public void log(String msg) {
		System.out.print(msg);
		this.logWindow.append(msg);
	}
	
	public void log(Exception e) {
		final StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		System.out.println(sw.toString());
		this.logWindow.append(sw.toString() + "\n");
		pw.close();
		try {
			sw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
