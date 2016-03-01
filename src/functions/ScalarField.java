/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import complex.DEC_Complex;
import complex.DEC_Iterator;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class ScalarField {
 public ScalarField(){
  
 }
 public float calculate(float x, float y, float z){
  float u = x / 600;
  float v = y / 400;
  float w = z /400;
  return new Float(Math.sin(Math.PI*u)*Math.cos(Math.PI*v)*Math.cos(3*Math.PI*w)).floatValue();
 }
 public float sampleValue(DEC_Object o, DEC_GeometricContainer container, int numSamples) throws DEC_Exception{
  ArrayList<PVector> verts = o.getGeometry(container);
  return sample(verts,numSamples);
 }
 public PImage createScalarPlot(DEC_Complex complex, DEC_GeometricContainer container, int numSamples,int dimension,PApplet parent) throws DEC_Exception{
  PGraphics resultingImage = parent.createGraphics(512, 512, PApplet.P2D);
  DEC_Iterator iterator = complex.createIterator(dimension, 'p');
  DEC_Iterator faceIterator = complex.createIterator(2,'p');
  
  resultingImage.beginDraw();
  resultingImage.colorMode(PApplet.HSB);
  while(faceIterator.hasNext()){
   DEC_PrimalObject object = (DEC_PrimalObject) faceIterator.next();
   float value = sampleValue(object,container,numSamples);
   //get texture vertices
   PVector t0 = object.getVectorContent(2);
   PVector t1 = object.getVectorContent(3);
   PVector t2 = object.getVectorContent(4);
   resultingImage.noStroke();
   resultingImage.fill(value*255,255,255);
   resultingImage.beginShape();
    resultingImage.vertex(t0.x*resultingImage.width,t0.y*resultingImage.height);
    resultingImage.vertex(t1.x*resultingImage.width,t1.y*resultingImage.height);
    resultingImage.vertex(t2.x*resultingImage.width,t2.y*resultingImage.height);
   resultingImage.endShape();
  }
  resultingImage.endDraw();
  return resultingImage.get();
 }
 float random(float a, float b){
  float t = (float) Math.random();
  return a*(1-t)+b*t;
 }
 public ArrayList<Float> convexRandomNumbers(int numNumbers){
  if(numNumbers == 1){
   ArrayList<Float> numbers = new ArrayList<Float>();
   numbers.add(new Float(random(0,1)));
   return numbers;
  }else{
   ArrayList<Float> subRandom = convexRandomNumbers(numNumbers-1);
   float sum = 0;
   for(int i=0;i<subRandom.size();i++){
    sum += subRandom.get(i).floatValue();
   }
   float lastRandom = random(0,1-sum);
   subRandom.add(new Float(lastRandom));
   return subRandom;
  }
 }
 public PVector randomVectorInPolygon(ArrayList<PVector> p){
  ArrayList<Float> randomCoefficients = convexRandomNumbers(p.size()-1);
  ArrayList<PVector> centered = new ArrayList<PVector>();
  for(int i=1;i<p.size();i++){
   centered.add(PVector.sub(p.get(i),p.get(0)));
  }
  PVector r = new PVector();
  for(int i=0;i<centered.size();i++){
   r.add(PVector.mult(centered.get(i),randomCoefficients.get(i).floatValue()));
  }
  r.add(p.get(0));
  return r;
 }
 public float sample(ArrayList<PVector> p, int numSamples){
  float sample = 0;
  for(int i=0;i<numSamples;i++){
   PVector v = randomVectorInPolygon(p);
   sample += calculate(v.x,v.y,v.z);
  }
  sample /= (float) numSamples;
  return sample;
 }
}
