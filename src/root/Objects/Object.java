package root.Objects;

import root.Vector.vec3;

public abstract class Object{
	protected vec3 position;
	
	public abstract double map(vec3 map);
	public vec3 getPosition(){return position;}
}