package util;

import java.io.IOException;

import terminal.ADBComunicator;
import terminal.Commands;

public class CommandExecutor {
	
	private ADBComunicator adb;
	
	public CommandExecutor() {
		adb = ADBComunicator.getInstance();
	}
	
	public void executeCmd(String cmd) throws IOException, InterruptedException {
		if(cmd.equals("WAIT")) { 
			Thread.sleep(15000);
			return;
		}else if(cmd.equals("NOT_WAIT")) {
			return;
		}
		if(cmd.contains("WAIT")) {//Para esperar um determinado tempo
			String timeStr = cmd.split(" ")[1];
			int time = Integer.valueOf(timeStr); 
			Thread.sleep(time);
			return;
		}
		if(cmd.equals("CLEAR_RAM")) {
			adb.runADBCommand(Commands.closeApp(Util.JOGO_1));
			adb.runADBCommand(Commands.closeApp(Util.JOGO_2));
			adb.runADBCommand(Commands.closeApp(Util.JOGO_3));
			adb.runADBCommand(Commands.closeApp(Util.CAMERA));
			return;
		}
		if(cmd.equals("CLEAR_LOGCAT")) {
			adb.runADBCommand(Commands.CLEAR_LOGCAT);
			return;
		}
		if(cmd.contains("GPS_CALIBRATED")) {
			adb.setGpsCalibrated(true);
			return;
		}
		if(cmd.contains("GPS_NOT_CALIBRATED")) {
			adb.setGpsCalibrated(false);
			simulateNotCalibrationGPS();
			return;
		}
		if(cmd.equals("SIMULATE_LONG_BACKGROUND")) {
			adb.runADBCommand(Commands.PRESS_HOME);
			dormir(5000);
			adb.runADBCommand(Commands.openApp(Util.JOGO_1));
			dormir(15000);
			adb.runADBCommand(Commands.openApp(Util.JOGO_2));
			dormir(15000);
			adb.runADBCommand(Commands.openApp(Util.JOGO_3));
			dormir(15000);
			adb.runADBCommand(Commands.PRESS_HOME);
			dormir(5000);
			adb.runADBCommand(Commands.openApp(Util.APP_PKG));
			return;
		}
		if(cmd.equals("TAKE_A_PICTURE")){
			adb.runADBCommand(Commands.PRESS_HOME);
			dormir(5000);
			adb.runADBCommand(Commands.OPEN_CAMERA);
			dormir(5000);
			adb.runADBCommand(Commands.TAKE_A_PICTURE);
			dormir(5000);
			adb.runADBCommand(Commands.openApp(Util.APP_PKG));
			return;
		}
		String command = "";
		if(cmd.equals("LIST_AVDS")){
			command = Commands.LIST_AVDS;
		}else if(cmd.equals("START_EMULATOR")){
			command = Commands.START_EMULATOR;
		}else if(cmd.equals("WAIT_FOR_DEVICE")){
			command = Commands.WAIT_FOR_DEVICE;
		}else if(cmd.equals("EMULATOR_STATUS")){
			command = Commands.EMULATOR_STATUS;
		}else if(cmd.equals("PRESS_HOME")){
			command = Commands.PRESS_HOME;
		}else if(cmd.equals("INSTALL_APP")){
			command = Commands.INSTALL_APP;
		}else if(cmd.equals("GPS_ON")){
			command = Commands.GPS_ON;
		}else if(cmd.equals("GPS_OFF")){
			command = Commands.GPS_OFF;
		}else if(cmd.equals("RECEIVE_CALL")){
			command = Commands.RECEIVE_CALL;
		}else if(cmd.equals("ACCEPT_CALL")){
			command = Commands.ACCEPT_CALL;
		}else if(cmd.equals("CANCEL_CALL")){
			command = Commands.CANCEL_CALL;
		}else if(cmd.equals("INTERNET_ON")){
			command = Commands.INTERNET_ON;
		}else if(cmd.equals("INTERNET_OFF")){
			command = Commands.INTERNET_OFF;
		}else if(cmd.equals("AUTO_ORIENTATION_ON")){
			command = Commands.AUTO_ORIENTATION_ON;
		}else if(cmd.equals("AUTO_ORIENTATION_OFF")){
			command = Commands.AUTO_ORIENTATION_OFF;
		}else if(cmd.equals("ORIENTATION_PORTRAIT_DEFAULT") || cmd.equals("ORIENTATION_PORTRAIT")){
			command = Commands.ORIENTATION_PORTRAIT_DEFAULT;
		}else if(cmd.equals("ORIENTATION_PORTRAIT_UPSIDE_DOWN")){
			command = Commands.ORIENTATION_PORTRAIT_UPSIDE_DOWN;
		}else if(cmd.equals("ORIENTATION_LANDSCAPE_RIGHT")){
			command = Commands.ORIENTATION_LANDSCAPE_RIGHT;
		}else if(cmd.equals("ORIENTATION_LANDSCAPE_LEFT") || cmd.equals("ORIENTATION_LANDSCAPE")){
			command = Commands.ORIENTATION_LANDSCAPE_LEFT;
		}else if(cmd.equals("OPEN_CAMERA")){
			command = Commands.OPEN_CAMERA;
		}else if(cmd.equals("GO_HOME")){
			command = Commands.GO_HOME;
		}else if(cmd.contains("SEND_GEO")){
			String location[] = cmd.split(" ");
			double latitude = Double.valueOf(location[1]);
			double longitude = Double.valueOf(location[2]);
			command = Commands.sendGeo(latitude, longitude);
		}else if(cmd.equals("OPEN_APP")){
			command = Commands.openApp(Util.APP_PKG);
		}else if(cmd.contains("adb emu geo fix ")){
			command = cmd;
		}
		adb.runADBCommand(command);		
	}

	private void simulateNotCalibrationGPS() {
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	try {
            		Monitor monitor = Monitor.getInstance();
            		do {
            			adb.runADBCommand(Commands.sendGeo(-7.589196, -37.541069));
            			dormir(1000);            			
            		}while(!adb.isGpsCalibrated() && !monitor.hasStopped());
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
 
        };
        t1.start();	
		
	}
	
	private void dormir(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
