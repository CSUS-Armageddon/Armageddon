package a3.network.logging;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import a3.network.api.messages.MessageType;

public enum ServerLogger {

	INSTANCE;
	
	private JTextArea logWindow;
	private JScrollPane scrollPane;
	private JScrollBar scrollBar;
	
	private final LogFilter filter = new LogFilter();
	
	public void addFilter(MessageType type) {
		this.filter.addFilter(type);
	}
	
	public void removeFilter(MessageType type) {
		this.filter.removeFilter(type);
	}
	
	public void setLogWindow(JTextArea jta, JScrollPane sp) {
		this.logWindow = jta;
		this.scrollPane = sp;
		this.scrollBar = this.scrollPane.getVerticalScrollBar();
	}
	
	public void logln(String msg) {
		if (filter.filter(msg)) return;
		System.out.println(msg);
		this.logWindow.append(msg + "\n");
		scrollToBottom();
	}
	
	public void log(String msg) {
		if (filter.filter(msg)) return;
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
