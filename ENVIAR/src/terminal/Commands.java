package terminal;

import java.text.DecimalFormat;

import util.Util;

public class Commands {
	
	public static final String LIST_AVDS = "emulator -list-avds";
	public static final String START_EMULATOR = "emulator -avd ";
	public static final String WAIT_FOR_DEVICE = "wait-for-device";
	public static final String EMULATOR_STATUS = "adb get-state";
	public static final String PRESS_HOME = "adb shell input keyevent KEYCODE_HOME";
	public static final String INSTALL_APP = "adb -s emulator-5556 install helloWorld.apk";
	public static final String GPS_ON = "adb shell settings put secure location_providers_allowed +gps";
	public static final String GPS_OFF = "adb shell settings put secure location_providers_allowed -gps";
	public static final String RECEIVE_CALL = "adb emu gsm call 8888";
	public static final String ACCEPT_CALL = "adb emu gsm accept 8888";
	public static final String CANCEL_CALL = "adb emu gsm cancel 8888";
	public static final String INTERNET_ON = "adb emu network speed full";
	public static final String INTERNET_OFF = "adb emu network speed 1 1";
	public static final String AUTO_ORIENTATION_ON = "adb shell settings put system accelerometer_rotation 1";
	public static final String AUTO_ORIENTATION_OFF = "adb shell settings put system accelerometer_rotation 0";
	public static final String ORIENTATION_PORTRAIT_DEFAULT = "adb shell settings put system user_rotation 0";
	public static final String ORIENTATION_PORTRAIT_UPSIDE_DOWN = "adb shell settings put system user_rotation 2";
	public static final String ORIENTATION_LANDSCAPE_RIGHT = "adb shell settings put system user_rotation 1";
	public static final String ORIENTATION_LANDSCAPE_LEFT = "adb shell settings put system user_rotation 3";
	public static final String OPEN_CAMERA = "adb shell am start -a android.media.action.IMAGE_CAPTURE";
	public static final String TAKE_A_PICTURE = "adb shell input keyevent 27";
	public static final String GO_HOME = "adb shell am start -a android.intent.action.MAIN -c android.intent.category.HOME";
	public static final String APP_PID = "adb shell pidof " + Util.APP_PKG;
	public static final String CLEAR_LOGCAT = "adb logcat -c";
	public static final String GET_ALL_LOGCAT = "adb logcat -d";
	public static final String GET_LOGCAT_ON_DEMAND = "adb logcat";
	
//	-37.541069 -7.589196
//	-35.8930119 -7.223572
	private final static String OPEN_APP = "adb shell monkey -p";
	private final static String CLOSE_APP = "adb shell am force-stop";
	private final static String SEND_GEO = "adb emu geo fix";
	
	public static String sendGeo(double latitude, double longitude) {
		DecimalFormat df = new DecimalFormat("0.#######################");
		String latStr = df.format(latitude);
		latStr = latStr.replaceAll(",", ".");
		String lngStr = df.format(longitude);
		lngStr = lngStr.replaceAll(",", ".");
		return SEND_GEO + " " + lngStr + " " + latStr;
	}
	
	public static String openApp(String appPackage) {
		return OPEN_APP + " " + appPackage + " -c android.intent.category.LAUNCHER 1";
	}
	
	public static String closeApp(String appPackage) {
		return CLOSE_APP + " " + appPackage;
	}

}
