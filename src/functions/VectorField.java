/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package functions;

import complex.DEC_Object;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import java.util.ArrayList;
import processing.core.PVector;

/**
 *
 * @author laptop
 */
public class VectorField {
 public VectorField(){
  
 }
 public PVector calculate(float x, float y, float z){
  float u = x / 600;
  float v = y / 400;
  float w = z / 400;
  return new PVector(-y,-z,x);
 }
 public PVector sampleValue(DEC_Object o, DEC_GeometricContainer container, int numSamples) throws DEC_Exception{
  ArrayList<PVector> verts = o.getGeometry(container);
  return sample(verts,numSamples);
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
 public PVector randomHullPoint(ArrayList<PVector> p){
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
 public PVector sample(ArrayList<PVector> p, int numSamples){
  PVector sample = new PVector();
  for(int i=0;i<numSamples;i++){
   PVector v = randomHullPoint(p);
   sample.add(calculate(v.x,v.y,v.z));
  }
  sample.div((float) numSamples);
  return sample;
 }
}
