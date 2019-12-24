package terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import exceptions.CrashException;
import util.Util;

public class ADBComunicator {
	
	private boolean gpsOn;
	private boolean gpsCalibrated;
	private String sentCommands;
	
	private static ADBComunicator instance = null;
	
	private ADBComunicator() {
		this.gpsOn = true;
		this.gpsCalibrated = true;
		this.sentCommands = "";
	}
	
	public static synchronized ADBComunicator getInstance() {
        if (instance == null)
        	instance = new ADBComunicator();
 
        return instance;
    }
	
	public String runADBCommand(String adbCommand, boolean print) throws IOException {
		sentCommands += adbCommand + "\n";
		if(adbCommand.equals(Commands.GPS_ON)) {
			gpsOn = true;
		}
		if(adbCommand.equals(Commands.GPS_OFF)) {
			gpsOn = false;
		}
		if(print) {
			System.out.println(adbCommand);			
		}
        StringBuffer returnValue = new StringBuffer();
        String line;
        InputStream inStream = null;
        try {
            Process process = Runtime.getRuntime().exec(adbCommand);

            // process.waitFor();/
            inStream = process.getInputStream();
            BufferedReader brCleanUp = new BufferedReader(
                    new InputStreamReader(inStream));
            while ((line = brCleanUp.readLine()) != null) {
            	if(print) {
            		System.out.println(line);            		
            	}
                returnValue.append(line).append("\n");
            }

            brCleanUp.close();
            try {


                process.waitFor();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        return returnValue.toString();
    }
	
	public String runADBLogcatCommand() throws IOException, CrashException {
		sentCommands += Commands.GET_LOGCAT_ON_DEMAND + "\n";
        StringBuffer returnValue = new StringBuffer();
        String line;
        Process process = Runtime.getRuntime().exec(Commands.GET_LOGCAT_ON_DEMAND);

        // process.waitFor();/
        InputStream inStream = process.getInputStream();
        BufferedReader brCleanUp = new BufferedReader(
                new InputStreamReader(inStream));
        while ((line = brCleanUp.readLine()) != null) {
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

	public void clearSendCommands() {
		this.sentCommands = "";
		
	}
	
}
