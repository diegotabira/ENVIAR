package testCase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import path.Line;
import path.Location;
import path.Path;
import terminal.Commands;
import util.Util;

public class TestCase {
	
	private String pathName;
	private ArrayList<Location> locations;
	private double maxSpeed;
	private ArrayList<String> setup;
	private String setupName;
	private ArrayList<String> delayEvents;
	
	public TestCase(String pathName, double maxSpeed, String setupName, ArrayList<String> delayEvents) throws IOException {
		this.maxSpeed = maxSpeed;
		this.pathName = pathName;
		
		this.setupName = setupName;
		this.setup = Util.getSetup(setupName);		
		Path path = getCaminho(pathName);
		locations = path.getLocationsPath();
		Location startingLocation = locations.get(0);
		setup.add(Commands.sendGeo(startingLocation.getLat(), startingLocation.getLng()));
		
		this.delayEvents = delayEvents;
	}
	
	@SuppressWarnings("resource")
	private Path getCaminho(String pathName) throws IOException {
		ArrayList<Line> lines = new ArrayList<>();
		FileReader arq = new FileReader("paths/" + pathName + ".txt");
		BufferedReader lerArq = new BufferedReader(arq);
		String linha1 = lerArq.readLine();
		String linha2 = lerArq.readLine();
		while(linha1 != null && linha2 != null) {
			Scanner scanner = new Scanner(linha1);
			scanner.useLocale(Locale.US);
			double lat1 = scanner.nextDouble();
			double lng1 = scanner.nextDouble();
			
			scanner = new Scanner(linha2);
			scanner.useLocale(Locale.US);
			double lat2 = scanner.nextDouble();
			double lng2 = scanner.nextDouble();
			
			lines.add(new Line(new Location(lat1, lng1), new Location(lat2, lng2), maxSpeed));
			linha1 = linha2;
			linha2 = lerArq.readLine();
		}

		Path p = new Path(lines);
		return p;
	}
	
	public String getPathName() {
		return this.pathName;
	}
	
	public String getSetupName() {
		return this.setupName;
	}
	
	public String getSetupString() {
		if(this.setupName.equals("S1")) {
			return "GPS_ON, ORIENTATION_PORTRAIT, GPS_CALIBRATED, INTERNET_ON";
		}
		if(this.setupName.equals("S2")) {
			return "GPS_ON, ORIENTATION_PORTRAIT, GPS_CALIBRATED, INTERNET_OFF";
		}
		if(this.setupName.equals("S3")) {
			return "GPS_ON, ORIENTATION_PORTRAIT, GPS_NOT_CALIBRATED, INTERNET_ON";
		}
		if(this.setupName.equals("S4")) {
			return "GPS_ON, ORIENTATION_PORTRAIT, GPS_NOT_CALIBRATED, INTERNET_OFF";
		}
		if(this.setupName.equals("S5")) {
			return "GPS_OFF, ORIENTATION_PORTRAIT, GPS_NOT_CALIBRATED, INTERNET_ON";
		}
		if(this.setupName.equals("S6")) {
			return "GPS_ON, ORIENTATION_PORTRAIT, GPS_NOT_CALIBRATED, INTERNET_OFF";
		}
		if(this.setupName.equals("S7")) {
			return "GPS_ON, ORIENTATION_LANDSCAPE, GPS_CALIBRATED, INTERNET_ON";
		}
		if(this.setupName.equals("S8")) {
			return "GPS_ON, ORIENTATION_LANDSCAPE, GPS_CALIBRATED, INTERNET_OFF";
		}
		if(this.setupName.equals("S9")) {
			return "GPS_ON, ORIENTATION_LANDSCAPE, GPS_NOT_CALIBRATED, INTERNET_OFF";
		}
		if(this.setupName.equals("S10")) {
			return "GPS_ON, ORIENTATION_LANDSCAPE, GPS_NOT_CALIBRATED, INTERNET_ON";
		}
		if(this.setupName.equals("S11")) {
			return "GPS_OFF, ORIENTATION_LANDSCAPE, GPS_NOT_CALIBRATED, INTERNET_OFF";
		}
		if(this.setupName.equals("S12")) {
			return "GPS_OFF, ORIENTATION_LANDSCAPE, GPS_NOT_CALIBRATED, INTERNET_ON";
		}
		return "";		

	}

	public ArrayList<String> getDelayEvents() {
		return delayEvents;
	}

	public ArrayList<Location> getLocations() {
		return locations;
	}

	public double getMaxSpeed() {
		return maxSpeed;
	}

	public ArrayList<String> getSetup() {
		return setup;
	}
	
	public String toString() {
		String result = pathName + "\t\t" + maxSpeed + "\t\t" + setupName;
		for (String delayEvent : delayEvents) {
			result += "\t\t" + delayEvent;
		}
		return result;
	}
	

}
