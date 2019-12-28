package path;

import java.util.ArrayList;

public class Path {
	
	private ArrayList<Line> lines;
	
	public Path(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}
	
	public ArrayList<Location> getLocationsPath(){
		ArrayList<Location> locationsPath = new ArrayList<>();
		
		for(int i = 0; i < lines.size(); i++) {
			Line line = lines.get(i);
			Location ls = line.getSource();
			Location ld = line.getDestation();
			double distancia = getDistancia(line.getSpeed());
			
			LocationsGenerator lg = new LocationsGenerator(ls, ld);
			
			locationsPath.addAll(lg.getLocations(distancia));
		}
		
		locationsPath.add(lines.get(lines.size() - 1).getDestation());

		
		
		return locationsPath;
	}

	/*
	 * v = s/t
	 * t = s/v
	 * 
	 * t ---- s
	 * 0,5 -- x
	 * 
	 * x = (0,5*s) / (s/v)
	 * x = 0,5*v
	 */
	private double getDistancia(double velocidade) {
		return velocidade/3.6;//Converter para metro por segundo
	}

}
