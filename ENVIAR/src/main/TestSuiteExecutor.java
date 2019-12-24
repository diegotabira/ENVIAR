package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import exceptions.SintaxException;
import percurso.Walker;
import terminal.ADBComunicator;
import testCase.TestCase;
import util.CommandExecutor;
import util.LogManager;
import util.Monitor;
import util.Util;

public class TestSuiteExecutor implements Observer{

	private static boolean executandoCasoDeTeste;
	private static Monitor monitor;
	private static CommandExecutor commandExecutor;
	
	private final static String TEST_SUIT = "testSuite/JustWalk.txt";
//	private final static String TEST_SUIT = "testSuite/TS01_Ordem1.txt";
	
	private static ArrayList<TestCase> testSuite;
	private static LogManager log;

	public static void main(String[] args) {
		log = new LogManager(Util.APP_PKG);
		try {
			commandExecutor = new CommandExecutor();
			loadTestSuite();
			executeTestSuite();
		} catch (SintaxException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	private static void executeTestSuite() {
		int i = 0;
		int qte = testSuite.size();
		monitor = new Monitor();
		monitor.startTesterMonitor();
		for (TestCase testCase : testSuite) {
			i++;
			if(!log.alreadyTested(testCase)) {
				printTestCase(testCase, i, qte);
				prepareLogsTestCase();
				setupTestCase(testCase);
				executeTestCase(testCase, i);				
			}
		}
		System.exit(0);
		
	}

	private static void prepareLogsTestCase() {
		try {
			monitor.startLogcatMonitor(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void setupTestCase(TestCase testCase) {
		try {
			commandExecutor.executeCmd("GO_HOME");
			System.out.println("Setting up...");
			dormir(1000);
			System.out.println("3");
			dormir(1000);
			System.out.println("2");
			dormir(1000);
			System.out.println("1");
			commandExecutor.executeCmd("CANCEL_CALL");
			commandExecutor.executeCmd("CLEAR_RAM");
			commandExecutor.executeCmd("AUTO_ORIENTATION_OFF");
			
			for (String cmd : testCase.getSetup()) {
				commandExecutor.executeCmd(cmd);
			}
			commandExecutor.executeCmd("OPEN_APP");
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void executeTestCase(TestCase testCase, int i) {		
		System.out.println("Executar caso de teste em...");
		dormir(1000);
		System.out.println("3");
		dormir(1000);
		System.out.println("2");
		dormir(1000);
		System.out.println("1");
		
		executandoCasoDeTeste = true;
		Walker walker = new Walker();
		monitor.addObserver(walker);
		monitor.setPassed(true);
//		contadorTempo();
		long now = Calendar.getInstance().getTimeInMillis();
//		sendEvents(testCase);
		ADBComunicator adb = ADBComunicator.getInstance();
		try {
			adb.runADBCommand("", false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		walker.walk(testCase, i);
		long sleptTime = Calendar.getInstance().getTimeInMillis() - now;
		sleptTime /= 1000;
		if(monitor.hasPassed()) {
			System.out.println("Caso de teste finalizou com sucesso em " + sleptTime + " segundos");
			if(walker.getProgress() < 100) {//TODO APAGAR ESSE IF DEPOIS
				sleptTime = (sleptTime*100)/walker.getProgress();
			}
			log.saveTestCase("Accept", sleptTime, testCase, i);
		}else if(monitor.hasCrashed()){
			System.out.println("Caso de teste travou em " + sleptTime + " segundos");
			log.saveTestCase("Crash", sleptTime, testCase, i);
			System.exit(0);
		}else {
			System.out.println("Caso de teste falhou em " + sleptTime + " segundos");
			log.saveTestCase("Fail", sleptTime, testCase, i);
		}
		executandoCasoDeTeste = false;
		monitor.deleteObservers();
	}

	private static void printTestCase(TestCase testCase, int i, int qte) {
		System.out.println("Caso de teste " + i + " de " + qte);
		System.out.println();
		System.out.println(testCase.toString());
	}

	

	@SuppressWarnings("resource")
	private static void loadTestSuite() throws SintaxException, IOException {
		testSuite = new ArrayList<>();
		FileReader arq = new FileReader(TEST_SUIT);
		BufferedReader lerArq = new BufferedReader(arq);
		String linha = lerArq.readLine();
		if(linha != null) {
			linha = Util.retirarExcessoEspacos(linha);
			linha = lerArq.readLine();
		}
		while(linha != null) {
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
			linha = lerArq.readLine();
		}		
	}

//	private static String get(String cmd, String linha) throws IOException, SintaxException {
//		
//		String[] pkg = linha.split(" ");
//		if(pkg[0].equals(cmd)) {
//			return pkg[1];
//		}else {
//			throw new SintaxException("app_pck not found");
//		}
//	}

	

	private static void dormir(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
//	private static void contadorTempo() {
//
//		Thread t1 = new Thread() {
//			@Override
//			public void run() {
//				int tempo = 0;
//				do {
//					try {
//						Thread.sleep(1000);
//						tempo++;
//						System.out.println(tempo + " segundos");
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//				} while (executandoCasoDeTeste);
//			}
//
//		};
//		t1.start();
//
//	}

	@Override
	public void update(Observable observable, Object arg1) {
		if(observable instanceof Monitor){
//			Monitor interruptor = (Monitor) observable;
//			passou = interruptor.hasPassed();
			executandoCasoDeTeste = false;
		}		
	}

	

}
