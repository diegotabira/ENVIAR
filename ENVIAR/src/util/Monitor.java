package util;

import java.io.IOException;
import java.util.Observable;

import exceptions.CrashException;

public class Monitor extends Observable{
	
	private boolean hasPassed;
	private boolean hasCrashed;
	private boolean hasStopped;
	
	private static Monitor instance = null;
	
	private Monitor() {
		this.hasPassed = false;
		this.hasCrashed = false;
		this.hasStopped = false;
	}
	
	public static synchronized Monitor getInstance() {
        if (instance == null)
        	instance = new Monitor(); 
        return instance;
    }
	
	public void start() {
		this.hasStopped = false;
	}
	
	public void accept() {
//		this.hasStopped = true;
		this.hasPassed = true;
		setChanged();
		notifyObservers();
	}
	
	public void reject() {
//		this.hasStopped = true;
		this.hasPassed = false;
		setChanged();
		notifyObservers();
	}
	
	public void stop() {
		this.hasStopped = true;
		setChanged();
		notifyObservers();
	}
	
//	@SuppressWarnings("resource")
//	public void startTesterMonitor() {
//		
//		Thread t1 = new Thread() {
//            @Override
//            public void run() {
//            	do {
//        			Scanner in = new Scanner(System.in);
//        			String response = in.next();
//        			if(response.equalsIgnoreCase("A")) {
//        				hasPassed = true;
//        			}else if(response.equalsIgnoreCase("F")) {
//        				hasPassed = false;
//        			}else {
//        				
//        			}
//        			setChanged();
//        			notifyObservers();
//        		}while(true);
//            }
// 
//        };
//        t1.start();	
//				
//	}
	
	public void startLogcatMonitor(LogManager log) throws IOException, InterruptedException {
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
	
	public boolean hasStopped() {
		return hasStopped;
	}
	
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
