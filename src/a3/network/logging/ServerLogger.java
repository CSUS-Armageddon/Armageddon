package a3.network.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public enum ServerLogger {

	INSTANCE;
	
	private JTextArea logWindow;
	private JScrollPane scrollPane;
	private JScrollBar scrollBar;
	
	public void setLogWindow(JTextArea jta, JScrollPane sp) {
		this.logWindow = jta;
		this.scrollPane = sp;
		this.scrollBar = this.scrollPane.getVerticalScrollBar();
	}
	
	public void logln(String msg) {
		System.out.println(msg);
		this.logWindow.append(msg + "\n");
		scrollToBottom();
	}
	
	public void log(String msg) {
		System.out.print(msg);
		this.logWindow.append(msg);
		scrollToBottom();
	}
	
	public void log(Exception e) {
		final StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		System.out.println(sw.toString());
		this.logWindow.append(sw.toString() + "\n");
		scrollToBottom();
		pw.close();
		try {
			sw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private void scrollToBottom() {
		scrollPane.validate();
		scrollBar.setValue(scrollBar.getMaximum());
	}
	
}
