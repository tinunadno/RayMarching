package root.Objects;

import root.Objects.Object;
import root.Vector.vec3;

public class Sphere extends Object {
	private double radius;
	
	public Sphere(vec3 position, double radius){
		this.position=position;
		this.radius=radius;
	}
	@Override
	public double map(vec3 ray){
		return (ray.sub(position)).length()-radius;
	}
	
	public double getRadius(){return radius;}
	
	public void setPosition(vec3 position){this.position=position;}
	public void setRadius(double radius){this.radius=radius;}
}