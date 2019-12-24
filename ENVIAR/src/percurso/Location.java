package percurso;

public class Location {
	
	private double lat;
	private double lng;
	
	public Location(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public double getDistance(Location location) {
//		double aux1 = Math.pow(this.lat - location.getLat(), 2);
//		double aux2 = Math.pow(this.lng - location.getLng(), 2);
//		return Math.sqrt(aux1 + aux2);
		
		double earthRadius = 6371;//kilometers
	    double dLat = Math.toRadians(location.getLat() - this.lat);
	    double dLng = Math.toRadians(location.getLng() - this.lng);
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(this.lat))
	            * Math.cos(Math.toRadians(location.getLat()));
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    double dist = earthRadius * c;
	 
	    return dist * 1000; //em metros
	}
	
	public String toString() {
		return "(" + this.lat + " : " + this.lng + ")";
	}

}
