package terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import exceptions.CrashException;
import util.GuiUpdater;
import util.TerminalManagerLogger;
import util.Util;

public class ADBComunicator {
	
	private boolean gpsOn;
	private boolean gpsCalibrated;
	private String sentCommands;
	private GuiUpdater guiUpdater;
	
	private static ADBComunicator instance = null;
	
	private ADBComunicator() {
		this.gpsOn = true;
		this.gpsCalibrated = true;
		this.sentCommands = "";
		guiUpdater = GuiUpdater.getInstance();
	}
	
	public static synchronized ADBComunicator getInstance() {
        if (instance == null)
        	instance = new ADBComunicator(); 
        return instance;
    }
	
	public String runADBCommand(String adbCommand, boolean waitForProcess) throws IOException, InterruptedException {
		guiUpdater.updateSentCommands(adbCommand);
		sentCommands += adbCommand + "\n";
		TerminalManagerLogger.appendSentCommands(adbCommand);
		if(adbCommand.equals(Commands.GPS_ON)) {
			gpsOn = true;
		}
		if(adbCommand.equals(Commands.GPS_OFF)) {
			gpsOn = false;
		}
        StringBuffer returnValue = new StringBuffer();
        String line;
        Process process = Runtime.getRuntime().exec(adbCommand);
        if(waitForProcess) {
        	process.waitFor();            	
        }
        InputStream inStream = process.getInputStream();
        BufferedReader brCleanUp = new BufferedReader(
        		new InputStreamReader(inStream));
        while ((line = brCleanUp.readLine()) != null) {
        	returnValue.append(line).append("\n");
        }
        
        brCleanUp.close();
        process.waitFor();
        
        return returnValue.toString();
	}
	
	public String runADBCommand(String adbCommand) throws IOException, InterruptedException {
        return runADBCommand(adbCommand, false);
    }
	
	public String runADBLogcatCommand() throws IOException, CrashException {
		guiUpdater.updateSentCommands(Commands.GET_LOGCAT_ON_DEMAND);
		sentCommands += Commands.GET_LOGCAT_ON_DEMAND + "\n";
		TerminalManagerLogger.appendSentCommands(Commands.GET_LOGCAT_ON_DEMAND);
        StringBuffer returnValue = new StringBuffer();
        String line;
        Process process = Runtime.getRuntime().exec(Commands.GET_LOGCAT_ON_DEMAND);

        // process.waitFor();/
        InputStream inStream = process.getInputStream();
        BufferedReader brCleanUp = new BufferedReader(
                new InputStreamReader(inStream));
        while ((line = brCleanUp.readLine()) != null) {
        	guiUpdater.updateLogcat(line);
        	if(line.contains("beginning of crash")) {
        		returnValue.append(line).append("\n");
				line = brCleanUp.readLine();
				if(line != null && line.contains("AndroidRuntime: FATAL EXCEPTION")) {
					returnValue.append(line).append("\n");
    				line = brCleanUp.readLine();
					if(line != null && line.contains("AndroidRuntime: Process: " + Util.APP_PKG)) {
						brCleanUp.close();
						throw new CrashException("Crashed");
					}
				}
			}
            returnValue.append(line).append("\n");
        }

        brCleanUp.close();
        try {


            process.waitFor();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnValue.toString();
    }

	public boolean isGpsOn() {
		return gpsOn;
	}

	public void setGpsOn(boolean gpsOn) {
		this.gpsOn = gpsOn;
	}

	public boolean isGpsCalibrated() {
		return gpsCalibrated;
	}

	public void setGpsCalibrated(boolean gpsCalibrated) {
		this.gpsCalibrated = gpsCalibrated;
	}

	public String getSentCommands() {
		return sentCommands;
	}
	
	public void clear() {
		this.sentCommands = "";
	}
	
}
