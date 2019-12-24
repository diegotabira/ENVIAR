package percurso;

public class Line {
	
	private Location source;
	private Location destation;
	private double speed;
	
	public Line(Location source, Location destination, double speed) {
		this.source = source;
		this.destation = destination;
		this.speed = speed;
	}	
	
	public Location getSource() {
		return source;
	}
	
	public Location getDestation() {
		return destation;
	}
	
	public double getSpeed() {
		return speed;
	}


}
