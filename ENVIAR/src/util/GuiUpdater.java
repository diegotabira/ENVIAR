package util;

import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JTextArea;

import view.ExecutionWindow;

public class GuiUpdater {
	
	private static ExecutionWindow exWindow = null;
	private static GuiUpdater instance = null;
	
	private GuiUpdater() {
		
	}
	
	public static synchronized GuiUpdater getInstance() {
        if (instance == null)
        	instance = new GuiUpdater(); 
        return instance;
    }
	
	public void setExecutionWindow(ExecutionWindow exWindow) {
		this.exWindow = exWindow;
	}
	
	public void updateSentCommands(String text) {
		JTextArea sentCommands = this.exWindow.getSentCommandsTextArea();
		sentCommands.append(text + "\n");		
		JScrollBar vertical = exWindow.getSentCommandsScrollPane().getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public void clearSentCommands() {
		JTextArea sentCommands = this.exWindow.getSentCommandsTextArea();
		sentCommands.setText("");
		JScrollBar vertical = exWindow.getSentCommandsScrollPane().getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public void updateSentCommands() {
		JTextArea sentCommands = this.exWindow.getSentCommandsTextArea();
		String lastCommands = TerminalManagerLogger.getLastSentCommands();
		sentCommands.append(lastCommands);
		JScrollBar vertical = exWindow.getSentCommandsScrollPane().getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	public void setProgressBar(int percentage) {
		JProgressBar progressBar = this.exWindow.getProgressBar();
		progressBar.setValue(percentage);
	}

	public void updateLogcat(String response) {
		JTextArea logcatTextArea = this.exWindow.getLogcatTextArea();
		logcatTextArea.append(response + "\n");
		JScrollBar vertical = exWindow.getLogcateScrollPane().getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}

}
