package path;

import java.util.ArrayList;

public class LocationsGenerator {
	
	private Location source;
	private Location destination;
	
	public LocationsGenerator(Location source, Location destination) {
		this.source = source;
		this.destination = destination;
	}
	
	public ArrayList<Location> getLocations(double distances[]) {
		ArrayList<Location> locations = new ArrayList<>();
		locations.add(source);
		locations.add(destination);
		int i = 0;
		double distanciaRestante = source.getDistance(destination);
		try {
			for (double distancia : distances) {
				if(distanciaRestante > distancia) {
					Location newLocation = getLocationBetween(locations.get(i), destination, distances[i]);
					locations.add(i + 1, newLocation);
					distanciaRestante -= distancia;
					i++;
				}else {
					System.out.println("Distância inválida!!!");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return locations;
	}
	
	public ArrayList<Location> getLocations(double distancia) {
		ArrayList<Location> locations = new ArrayList<>();
		locations.add(source);
		double distanciaRestante = source.getDistance(destination);
		try {
			while(distanciaRestante > distancia) {
				Location newLocation = getLocationBetween(locations.get(locations.size()-1), destination, distancia);
				locations.add(newLocation);
				distanciaRestante -= distancia;
			}
			locations.add(destination);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return locations;
	}
	
	public static Location getLocationBetween(Location l1, Location l2, double d) throws Exception {
		
		d = d/100000;
//		double distance = l1.getDistance(l2);
		
		if(d == 0) {
			return l1;
		}
		
		double x1 = l1.getLat();
		double x2 = l2.getLat();
		
		double y1 = l1.getLng();
		double y2 = l2.getLng();
		
		if(x2 - x1 != 0) {
			//Equação da reta y = mx + k. 
			double m = (y2 - y1) / (x2 - x1);//Fonte: https://www.todamateria.com.br/equacao-da-reta/ 
			double k = y2 - m*x2;
			
			//0  = (m^2 + 1)x^2 + (-2^i - 2mj + 2mk)x + (i^2 + j^2 + k^2 - r^2 - 2kj) fonte: http://math.mikeyaworski.com/line_intersect_circle
			double a = Math.pow(m, 2) + 1;
			double b = -2*x1 - 2*m*y1 + 2*m*k;
			double c = Math.pow(x1, 2) + Math.pow(y1, 2) + Math.pow(k, 2) - Math.pow(d, 2) - 2*k*y1;
			
			double delta = Math.pow(b, 2) - 4*a*c;
			
			double xp1 = 0, xp2 = 0;
			
			if(delta >= 0) {
				xp1 = (-b + Math.sqrt(delta)) / (2*a);
				xp2 = (-b - Math.sqrt(delta)) / (2*a);
			}else {
				throw new Exception("Delta menor que zero");
			}
			
			double yp1 = m*xp1 + k;
			double yp2 = m*xp2 + k;
			
			Location result = new Location(xp1, yp1);
			if(result.getDistance(l2) <= l1.getDistance(l2)) {
				return result;
			}else {
				return new Location(xp2, yp2);
			}
		}else {
			return new Location(x1, y1 + d);
		}
		
		
		
	}

}
