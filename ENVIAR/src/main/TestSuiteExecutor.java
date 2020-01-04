package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.JOptionPane;

import exceptions.SintaxException;
import path.Walker;
import testCase.TestCase;
import testCase.TestSuiteFiles;
import util.CommandExecutor;
import util.GuiUpdater;
import util.LogManager;
import util.Monitor;
import util.Util;
import view.ExecutionWindow;

public class TestSuiteExecutor implements Observer{

	private Monitor monitor;
	private CommandExecutor commandExecutor;
	
	private ExecutionWindow exectionWindow;
	private TestSuiteFiles testSuiteFiles;
	private GuiUpdater guiUpdater;
	
	private ArrayList<TestCase> testSuite;
	private LogManager log;
	
	public TestSuiteExecutor(ExecutionWindow exectionWindow, TestSuiteFiles testSuiteFiles) {
		this.exectionWindow = exectionWindow;
		this.testSuiteFiles = testSuiteFiles;
		guiUpdater = GuiUpdater.getInstance();
	}

	public void execute() throws SintaxException, IOException, InterruptedException {
		log = new LogManager(this.testSuiteFiles);
		commandExecutor = new CommandExecutor();
		loadTestSuite();
		executeTestSuite();
	}
	
	private void executeTestSuite() throws IOException, InterruptedException {
		new Thread() {
		     
		    @Override
		    public void run() {		    	
		    	int i = 0;
				monitor = Monitor.getInstance();
				for (TestCase testCase : testSuite) {
					if(Monitor.getInstance().hasStopped()) {
						break;
					}
					i++;
					if(!log.alreadyTested(testCase)) {
						guiUpdater.clearSentCommands();
						try {
							exectionWindow.addTestCase(testCase.toString());
							prepareLogsTestCase();
							setupTestCase(testCase);
							executeTestCase(testCase, i);				
						} catch (IOException | InterruptedException | SintaxException e) {
							JOptionPane.showMessageDialog(TestSuiteExecutor.this.exectionWindow, e.getMessage(), "ERROR",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}

		    }
		  }.start();		
	}

	private void prepareLogsTestCase() throws IOException, InterruptedException {
		monitor.startLogcatMonitor(log);		
	}

	private void setupTestCase(TestCase testCase) throws IOException, InterruptedException {
		commandExecutor.executeCmd("GO_HOME");
		guiUpdater.updateSentCommands("Setting up...");
		dormir(1000);
		guiUpdater.updateSentCommands("3");
		dormir(1000);
		guiUpdater.updateSentCommands("2");
		dormir(1000);
		guiUpdater.updateSentCommands("1");
		if(monitor.hasStopped()) {
			guiUpdater.updateSentCommands("Test case stopped");
			return;
		}
		commandExecutor.executeCmd("CANCEL_CALL");
		commandExecutor.executeCmd("CLEAR_RAM");
		commandExecutor.executeCmd("AUTO_ORIENTATION_OFF");
		
		for (String cmd : testCase.getSetup()) {
			commandExecutor.executeCmd(cmd);
		}
		commandExecutor.executeCmd("OPEN_APP");
	}

	private void executeTestCase(TestCase testCase, int i) throws IOException, InterruptedException, SintaxException {	
		if(monitor.hasStopped()) {
			guiUpdater.updateSentCommands("Test case stopped");
			return;
		}
		guiUpdater.updateSentCommands("Execute test case on...");
		dormir(1000);
		guiUpdater.updateSentCommands("3");
		dormir(1000);
		guiUpdater.updateSentCommands("2");
		dormir(1000);
		guiUpdater.updateSentCommands("1");
		
		Walker walker = new Walker();
		monitor.addObserver(walker);
		monitor.setPassed(true);
		long now = Calendar.getInstance().getTimeInMillis();
		walker.walk(testCase, i);
		long sleptTime = Calendar.getInstance().getTimeInMillis() - now;
		sleptTime /= 1000;
		if(monitor.hasStopped()) {
			guiUpdater.updateSentCommands("Test case stopped in " + sleptTime + " seconds");
		}else if(monitor.hasPassed()) {
			guiUpdater.updateSentCommands("Test case successfully completed in " + sleptTime + " seconds");
			log.saveTestCase("Accept", sleptTime, testCase, i);
		}else if(monitor.hasCrashed()){
			guiUpdater.updateSentCommands("Test case crashed in " + sleptTime + " seconds");
			log.saveTestCase("Crash", sleptTime, testCase, i);
//			System.exit(0);
		}else {
			guiUpdater.updateSentCommands("Test case failed in " + sleptTime + " seconds");
			log.saveTestCase("Fail", sleptTime, testCase, i);
		}
		monitor.deleteObservers();
		commandExecutor.executeCmd("GPS_CALIBRATED");
		exectionWindow.updateResults();
		exectionWindow.updateResults();
	}	

	@SuppressWarnings("resource")
	private void loadTestSuite() throws SintaxException, IOException {
		String testSuiteStr = testSuiteFiles.getTestSuite();
		testSuite = new ArrayList<>();		
		Scanner scanner = new Scanner(testSuiteStr);
		while(scanner.hasNextLine()) {
			String linha = scanner.nextLine();
			linha = Util.retirarExcessoEspacos(linha);
			String[] colunas = linha.split(" ");
			String pathName = colunas[0];
			Double speed = Double.valueOf(colunas[1]);
			String setupName = colunas[2];
			ArrayList<String> delayEvents = new ArrayList<>();
			for(int i = 3; i < colunas.length; i++) {
				delayEvents.add(colunas[i]);
			}
			testSuite.add(new TestCase(pathName, speed, setupName, delayEvents));			
		}
		scanner.close();
	}

	private void dormir(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Observable observable, Object arg1) {
		if(observable instanceof Monitor){
//			Monitor interruptor = (Monitor) observable;
//			passou = interruptor.hasPassed();
		}		
	}

}
