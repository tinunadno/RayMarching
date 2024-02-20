package root.Render;

import root.Objects.Cube;
import root.Objects.Cylinder;
import root.Objects.Floor;
import root.RayMarching;
import root.Objects.Sphere;
import root.Vector.vec3;
import root.MathAdditions.mat;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Map{
	//sphere params
	private static Sphere sph=new Sphere(new vec3(2, 2, 0), 1.0);
	//=============
	//cylinder params
	private static Cylinder cyl=new Cylinder(new vec3(4, 0, 3), 2, 2);
	//=============
	//cube params
	private static Cube cub=new Cube(new vec3(), 0.8);
	//===========
	//surface params
	private static Floor flr=new Floor(new vec3(), -2);
	
	private static double scale=2;
	private static BufferedImage img=null;
	static{
		try{
			img = ImageIO.read(new File("./src/root/Render/SKY_TEXTURE.jpg"));
		}
		catch(Exception e){
			System.out.println("No texture found!");
			System.exit(0);
		}
	}
	//==============
	//light sourve vector
	private static vec3 lightSource=new vec3(2,5, 6);
	//===================
	//its calling fom RayMarching class, changing fields, depends on time or frame
	public static void upgradeMap(){
		sph.setPosition(new vec3(Math.sin(RayMarching.getTime())/2, Math.abs((RayMarching.getTime()%10.0-5.0))-2.5, Math.sin(RayMarching.getTime())));
		cub.setZRotation((Math.PI/4)*RayMarching.getTime());
		cyl.setYRotation((Math.PI/4)*RayMarching.getTime());
	}
	public static double map(vec3 ray){
		return Math.min(Math.min(mat.smin(sph.map(ray), cub.map(ray), 0.4), cyl.map(ray)), flr.map(ray));
	}
	//shader block, ill do this better later
	public static vec3 colorMap(Ray ray_, int recDepth){
		if(recDepth>4)return new vec3(0.95);
		vec3 ray=ray_.getRay();
		double smap=sph.map(ray);
		double smap1=cyl.map(ray);
		double cmap=cub.map(ray);
		double fmap=flr.map(ray);
		if(fmap<=RayMarching.getTrashHold()){
			vec3 texCol=new vec3(0.9,0.8,0.5);
			if(Math.abs(ray.x()*5)%10<5 == (Math.abs(ray.y()*5)%10>5)){
				texCol=new vec3(0.4, 0.3, 0.2);
			}
			return getShadowColor(texCol, ray_);
		}
		if(smap1<=RayMarching.getTrashHold()){
			return getShadowColor(mirrorRay(ray, cyl.getPosition(), recDepth), ray_);
		}

		if(smap<=0.4 && cmap<=0.4){
			return getShadowColor(mat.mixColor(mirrorRay(ray, sph.getPosition(), recDepth), mat.mixColor(mirrorRay(ray, cub.getPosition(), recDepth), new vec3(0, 1, 0), 0.3), 1.0-cmap/0.3), ray_);
		}
		if(cmap<=RayMarching.getTrashHold()){
			return getShadowColor(mat.mixColor(mirrorRay(ray, cub.getPosition(), recDepth), new vec3(0, 1, 0), 0.3), ray_);
		}
		if(smap<=RayMarching.getTrashHold()){
			return getShadowColor(mirrorRay(ray, sph.getPosition(), recDepth), ray_);
		}
		
		double xAngle=mat.getAngle(ray.x(), ray.y())*1.5;
		double yAngle=mat.getAngle(mat.length(ray.x(), ray.y()), ray.z())/2;
		return mat.intToRgb(img.getRGB(Math.abs(((int)(yAngle/(Math.PI)*(img.getHeight()-1)*scale*4))%(img.getWidth()-1)), Math.abs(((int)(xAngle/(Math.PI)*(img.getWidth()-1)))%(img.getHeight()-1))));
	}
	
	private static vec3 getShadowColor(vec3 outVec, Ray ray_){
		ray_.setOrigin(ray_.getRay().add(new vec3(Math.random()/50)));
		ray_.setRotation(lightSource.sub(ray_.getOrigin()).normalize().mult(0.01));
		ray_.march();
		if((Map.map(ray_.getRay()))<=RayMarching.getTrashHold())outVec=mat.mixColor(outVec, new vec3(), 0.5);
		return outVec;
	}
	
	private static vec3 mirrorRay(vec3 ray, vec3 objectPos, int recDepth){
		Ray ray1=(new Ray(ray, ray.sub(objectPos).normalize()));
		ray1.march();
		return Map.colorMap(ray1, recDepth+1);
	}
	//=================================
}