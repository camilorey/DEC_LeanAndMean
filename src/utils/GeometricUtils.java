/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class GeometricUtils {
 public static PVector midPoint(PVector p1, PVector p2){
  return centroid(new PVector[]{p1,p2});
 }
 public static PVector centroid(PVector[] p){
  PVector c = new PVector();
  for(int i=0;i<p.length;i++){
   c.add(p[i]);
  }
  c.div((float)p.length);
  return c;
 }
 public static PVector centroid(ArrayList<PVector> p){
  PVector c = new PVector();
  for(int i=0;i<p.size();i++){
   c.add(p.get(i));
  }
  c.div((float)p.size());
  return c;
 }
 public static float volume(ArrayList<PVector> v){
  if(v.size() == 1){
   return 1;
  }else if(v.size() == 2){
   return v.get(0).dist(v.get(1));
  }else if(v.size() == 3){
   PVector w1 = PVector.sub(v.get(1), v.get(0));
   PVector w2 = PVector.sub(v.get(2),v.get(0));
   return w1.cross(w2).mag()/2.0f;
  }else if(v.size() == 4){
   PVector w1 = PVector.sub(v.get(1), v.get(0));
   PVector w2 = PVector.sub(v.get(2),v.get(0));
   PVector w3 = PVector.sub(v.get(3),v.get(0));
   return determinant3x3(new float[][]{{w1.x,w2.x,w3.x},
                                       {w1.y,w2.y,w3.y},
                                       {w1.z,w2.z,w3.z}});
  }else{
   return 0;
  }
 }
 public static float angleBetweenVectors(PVector a, PVector b){
  PVector c = PVector.sub(a,b);
  float angle = PApplet.atan2(c.y,c.x);
  if(angle<0){
   angle += PApplet.PI;
  }
  return angle;
 }
public static void swapAngles(ArrayList<Float> l, int i, int j){
 float tempI = l.get(i).floatValue();
 float tempJ = l.get(j).floatValue();
 l.set(i,new Float(tempJ));
 l.set(j,new Float(tempI));
}
public static void swapVectors(ArrayList<PVector> l, int i, int j){
 PVector tempI = new PVector(l.get(i).x,l.get(i).y,l.get(i).z);
 PVector tempJ = new PVector(l.get(j).x,l.get(j).y,l.get(j).z);
 l.set(i,tempJ);
 l.set(j,tempI);
}
 public static float surfaceArea(ArrayList<PVector> v){
  return 0;
 }
 public static float cellVolume(ArrayList<PVector> v){
  return 0;
 }
 public static float determinant2x2(float[][] A){
  return A[0][0]*A[1][1]-A[0][1]*A[1][0];
 }
 public static float determinant3x3(float[][] A){
  return A[0][0]*determinant2x2(new float[][]{{A[1][1],A[2][1]},
                                              {A[1][2],A[2][2]}})
        -A[1][0]*determinant2x2(new float[][]{{A[0][1],A[2][1]},
                                              {A[0][2],A[2][2]}})
        +A[2][0]*determinant2x2(new float[][]{{A[0][1],A[1][1]},
                                              {A[0][2],A[1][2]}});  
 }
}
