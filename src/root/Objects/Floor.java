package root.Objects;

import root.Vector.vec3;

public class Floor extends Object {
	private double heigh;
	public Floor(vec3 position, double heigh){
		this.position=position;
		this.heigh=heigh;
	}
	
	@Override
	public double map(vec3 ray){
		return (ray.sub(position)).z()-heigh;
	}
}