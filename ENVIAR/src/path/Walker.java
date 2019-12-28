package path;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import terminal.ADBComunicator;
import terminal.Commands;
import testCase.TestCase;
import util.CommandExecutor;

public class Walker implements Observer{
	
	private final int sleepTime = 1000;
	private ADBComunicator adb;
	private boolean interromper;
	private boolean eventosAcabaram;
	private int progress;
	
	public Walker() {
		adb = ADBComunicator.getInstance();
		interromper = false;
		progress = -1;
	}
	
	public int getProgress() {
		return progress;
	}
	
	public void walk(TestCase testCase, int index) {
		eventosAcabaram = false;
		sendEvents(testCase);
		ArrayList<Location> locations = testCase.getLocations();
		int size = locations.size();
		int i = 0;
		try {
			for (Location location : locations) {
				i++;
				if(progress != (i*100)/size) {
					progress = (i*100)/size;
					System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
					System.out.println(progress + "%");
					System.out.println("Setup: " + testCase.getSetupString());
					System.out.println("Caso de teste " + index);
					System.out.println(testCase.toString());
					if(eventosAcabaram) {
						System.out.println("ACABARAM OS EVENTOS");
					}
					System.out.println("Press A to Accept or F to Fail");
				}
				if(adb.isGpsOn() && adb.isGpsCalibrated()) {
					adb.runADBCommand(Commands.sendGeo(location.getLat(), location.getLng()), false);
				}
				dormir(sleepTime);
				if(interromper) {
					break;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void dormir(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void sendEvents(TestCase testCase) {
		
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	CommandExecutor commandExecutor = new CommandExecutor();
            	System.out.println("Sending events");
            	try {
            		for (String cmd : testCase.getDelayEvents()) {
            			commandExecutor.executeCmd(cmd);
        				if(interromper) {
        					break;
        				}
        			}
            		eventosAcabaram = true;
        			System.out.println("ACABARAM OS EVENTOS");
        		} catch (IOException | InterruptedException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
            }
 
        };
        t1.start();	
		
	}

	@Override
	public void update(Observable o, Object arg) {
		interromper = true;
		
	}
	

}
