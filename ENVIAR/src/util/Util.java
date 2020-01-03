package util;

import java.io.IOException;
import java.util.ArrayList;

import terminal.ADBComunicator;
import terminal.Commands;

public class Util {
	
//	net.osmand
//	org.nitri.opentopo
//	fly.speedmeter.grub
//	com.marcospoerl.simplypace
//	ch.bailu.aat
//	com.nextgis.mobile
//	com.mapswithme.maps.pro
//	com.voice.gps.navigator.voice.nearby.places.maps
//	com.mapfactor.navigator
//	com.offline.routemaps.gps.directionfinder.free
//	menion.android.locus
//	gps.maps.navigation.offlinemaps.drivingdirections
//	com.google.android.apps.maps
//	com.EvilChaotic.ModernWarplanes
	public static final String APP_PKG = "com.google.android.apps.maps";
//	public static final String APP_PKG = "net.osmand";
//	public static final String APP_PKG = "com.offline.routemaps.gps.directionfinder.free";
//	public static final String APP_PKG = "hr.mireo.arthur";
//	public static final String APP_PKG = "com.voicenavigation.gps.driving.directions";
	
	public static final String JOGO_1 = "com.skgames.trafficrider";
	public static final String JOGO_2 = "com.EvilChaotic.ModernWarplanes";
	public static final String JOGO_3 = "com.ea.games.r3_row";
	public static final String CAMERA = "android.media.action.IMAGE_CAPTURE";
	
	public static ArrayList<String> getSetup(String setupName){
		ArrayList<String> setup = new ArrayList<>();
		if(setupName.equals("S1")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_PORTRAIT");
			setup.add("GPS_CALIBRATED");
			setup.add("INTERNET_ON");
		}else if(setupName.equals("S2")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_PORTRAIT");
			setup.add("GPS_CALIBRATED");
			setup.add("INTERNET_OFF");
		}else if(setupName.equals("S3")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_PORTRAIT");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_ON");
		}else if(setupName.equals("S4")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_PORTRAIT");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_OFF");
		}else if(setupName.equals("S5")) {
			setup.add("GPS_OFF");
			setup.add("ORIENTATION_PORTRAIT");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_ON");
		}else if(setupName.equals("S6")) {
			setup.add("GPS_OFF");//ESTAVA EM ON
			setup.add("ORIENTATION_PORTRAIT");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_OFF");
		}else if(setupName.equals("S7")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_LANDSCAPE");
			setup.add("GPS_CALIBRATED");
			setup.add("INTERNET_ON");
		}else if(setupName.equals("S8")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_LANDSCAPE");
			setup.add("GPS_CALIBRATED");
			setup.add("INTERNET_OFF");
		}else if(setupName.equals("S9")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_LANDSCAPE");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_OFF");
		}else if(setupName.equals("S10")) {
			setup.add("GPS_ON");
			setup.add("ORIENTATION_LANDSCAPE");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_ON");
		}else if(setupName.equals("S11")) {
			setup.add("GPS_OFF");
			setup.add("ORIENTATION_LANDSCAPE");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_OFF");
		}else if(setupName.equals("S12")) {
			setup.add("GPS_OFF");
			setup.add("ORIENTATION_LANDSCAPE");
			setup.add("GPS_NOT_CALIBRATED");
			setup.add("INTERNET_ON");
		}
		return setup;
	}
	
	public static String retirarExcessoEspacos(String linha) {
		while(linha.contains("\t")) {
			linha = linha.replace("\t", " ");
		}
		while(linha.contains("  ")) {
			linha = linha.replace("  ", " ");			
		}
		return linha;
	}
	
	public static boolean autIsRunning() throws IOException {
//		adb shell pidof net.osmand
		ADBComunicator adbComunicator = ADBComunicator.getInstance();
		String pid = adbComunicator.runADBCommand(Commands.APP_PID);
		if(pid == null || pid.equals("")) {
			return false;
		}else {
			return true;
		}
	}

}
