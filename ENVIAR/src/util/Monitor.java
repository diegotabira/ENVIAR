package util;

import java.io.IOException;
import java.util.Observable;
import java.util.Scanner;

import exceptions.CrashException;

public class Monitor extends Observable{
	
	private boolean hasPassed;
	private boolean hasCrashed;
	
	@SuppressWarnings("resource")
	public void startTesterMonitor() {
		
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	do {
        			Scanner in = new Scanner(System.in);
        			String response = in.next();
        			if(response.equalsIgnoreCase("A")) {
        				hasPassed = true;
        			}else if(response.equalsIgnoreCase("F")) {
        				hasPassed = false;
        			}else {
        				
        			}
        			setChanged();
        			notifyObservers();
        		}while(true);
            }
 
        };
        t1.start();	
				
	}
	
	public void startLogcatMonitor(LogManager log) throws IOException {
		log.clearLogcat();
		hasCrashed = false;
		
		Thread t1 = new Thread() {
            @Override
            public void run() {
            	try {
					log.monitorLogcat();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CrashException e) {
					hasCrashed = true;
					hasPassed = false;
					setChanged();
        			notifyObservers();
				}
            }
 
        };
        t1.start();	
				
	}
	
//	public void startLogcatMonitor(LogManager log) throws IOException {
//		log.clearLogcat();
//		hasCrashed = false;
//		
//		Thread t1 = new Thread() {
//            @Override
//            public void run() {
//            	do {
//        			try {
//						String logcat = log.getLogcat();
//						if(hasCrashed(logcat)) {
//							hasPassed = false;
//							hasCrashed = true;
//							setChanged();
//		        			notifyObservers();
//						}else {
//							Thread.sleep(1000);
//						}
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//        		}while(true);
//            }
// 
//        };
//        t1.start();	
//				
//	}

//	private boolean hasCrashed(String logcat) {
//		Scanner scan = new Scanner(logcat);
//		while(scan.hasNextLine()) {
//			String line = scan.nextLine();
//			if(line.contains("beginning of crash")) {
//				line = scan.nextLine();
//				if(line.contains("AndroidRuntime: FATAL EXCEPTION")) {
//					line = scan.nextLine();
//					if(line.contains("AndroidRuntime: Process: " + Util.APP_PKG)) {
//						return true;
//					}
//				}
//			}
//		}
//		scan.close();
//		return false;
//	}
	
	public boolean hasCrashed() {
		return hasCrashed;
	}

	public boolean hasPassed() {
		return hasPassed;
	}
	
	public void setPassed(boolean passed) {
		this.hasPassed = passed;
	}
	

}
