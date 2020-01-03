package util;

public class TerminalManagerLogger {
	
	private static String allSentCommands = "";
	private static String lastSentCommands = "";
	
	public static String getAllSentCommands() {
		return allSentCommands;
	}
	
	public static String getLastSentCommands() {
		String response = lastSentCommands;
		lastSentCommands = "";
		return response;
	}
	
	public static void appendSentCommands(String command) {
		TerminalManagerLogger.lastSentCommands.concat(command + "\n");
		allSentCommands.concat(command + "\n");
	}
	
	
}
