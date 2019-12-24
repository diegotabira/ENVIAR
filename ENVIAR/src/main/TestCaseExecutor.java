package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import exceptions.SintaxException;
import percurso.Line;
import percurso.Location;
import percurso.Path;
import percurso.Walker;
import terminal.ADBComunicator;
import terminal.Commands;
import util.Util;

public class TestCaseExecutor {

	private static boolean executandoTeste;
	private static ADBComunicator adb;
	private static final String appPackage = "com.google.android.apps.maps";
	private static String path;
	private static double maxSpeed;
	
	private final static String TEST_CASE = "useCases/order1/UC05.txt";
	
	private static ArrayList<Location> points;
	
	private static ArrayList<String> setup;
	private static ArrayList<String> events;

	public static void main(String[] args) {
		
//		try {
//			adb = new ADBComunicator();
//			loadTestCase();
//			printTestCase();
//			setup();
//			executeTestCase();
//		} catch (SintaxException | IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

	}
	
//	private static void executeTestCase() {
//		System.out.println("Executar caso de teste em:");
//		dormir(1000);
//		System.out.println("3");
//		dormir(1000);
//		System.out.println("2");
//		dormir(1000);
//		System.out.println("1");
//		executandoTeste = true;
//		Walker walker = new Walker();
//		contadorTempo();
//		long now = Calendar.getInstance().getTimeInMillis();				
//		sendEvents();
//		walker.walk(points);
//		long sleptTime = Calendar.getInstance().getTimeInMillis() - now;
//		sleptTime /= 1000;
//		System.out.println("Passaram-se " + sleptTime + " segundos");
//		executandoTeste = false;
//		
//	}
//
//	private static void printTestCase() {
//		System.out.println("appPackage " + appPackage);
//		System.out.println("path " + path);
//		System.out.println("maxSpeed " + maxSpeed);
//		System.out.println();
//		System.out.println("begin_setup");
//		for (String string : setup) {
//			System.out.println(string);
//		}
//		System.out.println("end_setup");
//		System.out.println();
//		System.out.println("begin_events");
//		for (String string : events) {
//			System.out.println(string);
//		}
//		System.out.println("end_events");
//		System.out.println();
//	}
//
//	private static void setup() {
//		System.out.println("Setting up...");
//		try {
//			for (String cmd : setup) {
//				executeCmd(cmd);				
//			}
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//	}
//
//	private static void executeCmd(String cmd) throws IOException, InterruptedException {
//		if(cmd.contains("SLEEP")) {
//			String timeStr = cmd.split(" ")[1];
//			int time = Integer.valueOf(timeStr); 
//			Thread.sleep(time);
//			return;
//		}
//		if(cmd.contains("GPS_CALIBRATED")) {
//			ADBComunicator.gpsCalibrated = true;
//			return;
//		}
//		if(cmd.contains("GPS_NOT_CALIBRATED")) {
//			ADBComunicator.gpsCalibrated = false;
//			simulateNotCalibrationGPS();
//			return;
//		}
//		if(cmd.equals("SIMULATE_LONG_BACKGROUND")) {
//			adb.runADBCommand(Commands.PRESS_HOME, true);
//			dormir(5000);
//			adb.runADBCommand(Commands.startApp(Util.JOGO_1), true);
//			dormir(15000);
//			adb.runADBCommand(Commands.startApp(Util.JOGO_2), true);
//			dormir(15000);
//			adb.runADBCommand(Commands.startApp(Util.JOGO_3), true);
//			dormir(15000);
//			adb.runADBCommand(Commands.PRESS_HOME, true);
//			dormir(5000);
//			adb.runADBCommand(Commands.startApp(appPackage), true);
//			return;
//		}
//		String command = "";
//		if(cmd.equals("LIST_AVDS")){
//			command = Commands.LIST_AVDS;
//		}else if(cmd.equals("START_EMULATOR")){
//			command = Commands.START_EMULATOR;
//		}else if(cmd.equals("WAIT_FOR_DEVICE")){
//			command = Commands.WAIT_FOR_DEVICE;
//		}else if(cmd.equals("EMULATOR_STATUS")){
//			command = Commands.EMULATOR_STATUS;
//		}else if(cmd.equals("PRESS_HOME")){
//			command = Commands.PRESS_HOME;
//		}else if(cmd.equals("INSTALL_APP")){
//			command = Commands.INSTALL_APP;
//		}else if(cmd.equals("GPS_ON")){
//			command = Commands.GPS_ON;
//		}else if(cmd.equals("GPS_OFF")){
//			command = Commands.GPS_OFF;
//		}else if(cmd.equals("RECEIVE_CALL")){
//			command = Commands.RECEIVE_CALL;
//		}else if(cmd.equals("ACCEPT_CALL")){
//			command = Commands.ACCEPT_CALL;
//		}else if(cmd.equals("CANCEL_CALL")){
//			command = Commands.CANCEL_CALL;
//		}else if(cmd.equals("INTERNET_ON")){
//			command = Commands.INTERNET_ON;
//		}else if(cmd.equals("INTERNET_OFF")){
//			command = Commands.INTERNET_OFF;
//		}else if(cmd.equals("AUTO_ORIENTATION_ON")){
//			command = Commands.AUTO_ORIENTATION_ON;
//		}else if(cmd.equals("AUTO_ORIENTATION_OFF")){
//			command = Commands.AUTO_ORIENTATION_OFF;
//		}else if(cmd.equals("ORIENTATION_PORTRAIT_DEFAULT")){
//			command = Commands.ORIENTATION_PORTRAIT_DEFAULT;
//		}else if(cmd.equals("ORIENTATION_PORTRAIT_UPSIDE_DOWN")){
//			command = Commands.ORIENTATION_PORTRAIT_UPSIDE_DOWN;
//		}else if(cmd.equals("ORIENTATION_LANDSCAPE_RIGHT")){
//			command = Commands.ORIENTATION_LANDSCAPE_RIGHT;
//		}else if(cmd.equals("ORIENTATION_LANDSCAPE_LEFT")){
//			command = Commands.ORIENTATION_LANDSCAPE_LEFT;
//		}else if(cmd.equals("OPEN_CAMERA")){
//			command = Commands.OPEN_CAMERA;
//		}else if(cmd.equals("TAKE_A_PICTURE")){
//			command = Commands.TAKE_A_PICTURE;
//		}else if(cmd.equals("GO_HOME")){
//			command = Commands.GO_HOME;
//		}
//		else if(cmd.contains("SEND_GEO")){
//			String location[] = cmd.split(" ");
//			double latitude = Double.valueOf(location[1]);
//			double longitude = Double.valueOf(location[2]);
//			command = Commands.sendGeo(latitude, longitude);
//		}else if(cmd.equals("START_APP")){
//			command = Commands.startApp(appPackage);
//		}
//		adb.runADBCommand(command, true);		
//	}
//
//	private static void simulateNotCalibrationGPS() {
//		Thread t1 = new Thread() {
//            @Override
//            public void run() {
//            	try {
//            		do {
//            			adb.runADBCommand(Commands.sendGeo(-7.589196, -37.541069), true);
//            			dormir(1000);            			
//            		}while(!ADBComunicator.gpsCalibrated);
//        		} catch (IOException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
// 
//        };
//        t1.start();	
//		
//	}
//
//	private static void loadTestCase() throws SintaxException, IOException {
//		setup = new ArrayList<>();
//		events = new ArrayList<>();
//		FileReader arq = new FileReader(TEST_CASE);
//		BufferedReader lerArq = new BufferedReader(arq);
//		String linha = lerArq.readLine();
//		if(linha.equals("begin_description")) {
//			linha = lerArq.readLine();
//			while(!linha.equals("end_description")) {
//				System.out.println(linha);
//				linha = lerArq.readLine();
//			}
//			System.out.println();
//		}else {
//			throw new SintaxException("begin_description not found");
//		}
//		
//		linha = lerArq.readLine();
////		appPackage = get("app_pck", linha);
//		linha = lerArq.readLine();
//		path = get("path", linha);
//		linha = lerArq.readLine();
//		String maxSpeedStr = get("max_speed", linha);
//		maxSpeed = Double.valueOf(maxSpeedStr);
//		
//		linha = lerArq.readLine();
//		if(linha.equals("begin_setup")) {
//			linha = lerArq.readLine();
//			while(!linha.equals("end_setup")) {
//				setup.add(linha);
//				linha = lerArq.readLine();
//			}
//			Path path = getCaminho();
//			points = path.getLocationsPath();
//			Location startingLocation = points.get(0);
//			setup.add(Commands.sendGeo(startingLocation.getLat(), startingLocation.getLng()));
//		}else {
//			throw new SintaxException("begin_setup not found");
//		}
//		
//		linha = lerArq.readLine();
//		if(linha.equals("begin_events")) {
//			linha = lerArq.readLine();
//			while(!linha.equals("end_events")) {
//				events.add(linha);				
//				linha = lerArq.readLine();
//			}
//		}else {
//			throw new SintaxException("begin_events not found");
//		}
//		
//		
//		
//	}
//
//	private static String get(String cmd, String linha) throws IOException, SintaxException {
//		
//		String[] pkg = linha.split(" ");
//		if(pkg[0].equals(cmd)) {
//			return pkg[1];
//		}else {
//			throw new SintaxException("app_pck not found");
//		}
//	}
//
//	private static void sendEvents() {
//		
//		Thread t1 = new Thread() {
//            @Override
//            public void run() {
//            	System.out.println("Sending events");
//            	try {
//            		for (String cmd : events) {
//        				executeCmd(cmd);				
//        			}
//        			System.out.println("ACABARAM OS EVENTOS");
//        		} catch (IOException | InterruptedException e) {
//        			// TODO Auto-generated catch block
//        			e.printStackTrace();
//        		}
//            }
// 
//        };
//        t1.start();	
//		
//	}
//
//	private static void dormir(int time) {
//		try {
//			Thread.sleep(time);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
//	
////	private static void iniciarEmulador() {
////		ADBComunicator adb = new ADBComunicator();
////		String result = "";
////		try {
////			result = adb.runADBCommand(Commands.GO_HOME, false);
////			if (result.equals("")) {
////				Thread t1 = new Thread() {
////					@Override
////					public void run() {
////						try {
////							adb.runADBCommand(Commands.START_EMULATOR, false);
////						} catch (IOException e) {
////							// TODO Auto-generated catch block
////							e.printStackTrace();
////						}
////					}
////
////				};
////				t1.start();
////
////			}
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////		do {
////			try {
////				Thread.sleep(1000);
////				adb.runADBCommand(Commands.startApp("com.mapswithme.maps.pro"), false);
////				System.out.println("Esperando Emulador...");
////			} catch (InterruptedException | IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////		} while (result.equals(""));
////		try {
////			adb.runADBCommand(Commands.GPS_ON, false);
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
////
////	}
//
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
//				} while (executandoTeste);
//			}
//
//		};
//		t1.start();
//
//	}
//
//	private static Path getCaminho() throws IOException {
//		ArrayList<Line> lines = new ArrayList<>();
//		FileReader arq = new FileReader("paths/" + path);
//		BufferedReader lerArq = new BufferedReader(arq);
//		String linha1 = lerArq.readLine();
//		String linha2 = lerArq.readLine();
//		while(linha1 != null && linha2 != null) {
//			Scanner scanner = new Scanner(linha1);
//			scanner.useLocale(Locale.US);
//			double lat1 = scanner.nextDouble();
//			double lng1 = scanner.nextDouble();
//			
//			scanner = new Scanner(linha2);
//			scanner.useLocale(Locale.US);
//			double lat2 = scanner.nextDouble();
//			double lng2 = scanner.nextDouble();
//			
//			lines.add(new Line(new Location(lat1, lng1), new Location(lat2, lng2), maxSpeed));
//			linha1 = linha2;
//			linha2 = lerArq.readLine();
//		}
//
//		Path p = new Path(lines);
//		return p;
//	}

}
