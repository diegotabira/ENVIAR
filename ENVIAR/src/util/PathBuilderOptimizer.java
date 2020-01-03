package util;

import java.util.ArrayList;
import java.util.HashMap;

import path.Location;

public class PathBuilderOptimizer {
	
	private static HashMap<String, ArrayList<Location>> paths = new HashMap<String, ArrayList<Location>>();

	public static ArrayList<Location> getPath(String pathName){
		return paths.get(pathName);
	}
	
	public static void putPath(String pathName, ArrayList<Location> path) {
		paths.put(pathName, path);
	}	
	
	
}
