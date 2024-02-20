package root.Objects;

import root.MathAdditions.mat;
import root.Vector.vec2;
import root.Vector.vec3;

public class Cube extends Object {
	private double size;
	//rotation along Z axis params PS Im to lazy to do normal Q rotation, if u 'wanna fix it
	private vec2 zRot;
	private double tetta=0;
	//====================
	
	public Cube(vec3 position, double size){
		this.position=position;
		this.size=size;
		zRot=new vec2(0, 1);
	}
	@Override
	public double map(vec3 ray){
		ray=new vec3(zRot.x()*ray.x()-zRot.y()*ray.y(), zRot.y()*ray.x()+zRot.x()*ray.y(), ray.z());
		vec3 d=new vec3(Math.abs(ray.x()-position.x())-size, Math.abs(ray.y()-position.y())-size, Math.abs(ray.z()-position.z())-size);
		return Math.min(Math.max(d.x(), Math.max(d.y(), d.z())), 0.0)+(mat.clamp(0, Integer.MAX_VALUE, d)).length();
	}
	
	public double getSize(){return size;}
	public double getZAngle(){return tetta;}
	
	public void setPosition(vec3 position){this.position=position;}
	public void setSize(double size){this.size=size;}
	public void setZRotation(double angle){
		tetta=angle;
		zRot=new vec2(Math.cos(tetta), Math.sin(tetta));
	}
}