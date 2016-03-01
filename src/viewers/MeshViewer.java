/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewers;

import complex.DEC_Complex;
import complex.DEC_DualObject;
import complex.DEC_Iterator;
import complex.DEC_Object;
import complex.DEC_PrimalObject;
import complex.IndexSet;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import functions.VectorField;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import readers.OBJMeshReader;
import utils.GeometricUtils;

/**
 *
 * @author laptop
 */
public class MeshViewer {

 protected PApplet parent;
 protected int[] primalColor;
 protected int[] dualColor;
 protected float vertexRadius;
 public MeshViewer(PApplet parent) {
  this.parent = parent;
 }

 public void drawComplex(DEC_Complex complex, DEC_GeometricContainer container) {

 }
 public void setVertexRadius(float vertexRadius){
  this.vertexRadius = vertexRadius;
 }
 public float getVertexRadius(){
  return this.vertexRadius;
 }
 public void setPrimalColor(int r, int g, int b){
  this.primalColor = new int[]{r,g,b};
 }
 public int[] getPrimalColor(){
  return this.primalColor;
 }
 public void setDualColor(int r, int g, int b){
  this.dualColor = new int[]{r,g,b};
 }
 public int[] getDualColor(){
  return this.dualColor;
 }
 public void drawSelectedObject(DEC_Object object, DEC_GeometricContainer container) throws DEC_Exception{
  ArrayList<PVector> verts = object.getGeometry(container);
  if(object instanceof DEC_PrimalObject){
   if(object.getOrientation()>0){
    parent.stroke(0.75f*primalColor[0],0.75f*primalColor[1],0.75f*primalColor[2]);
   }else{
    parent.stroke(255-0.75f*primalColor[0],255-0.75f*primalColor[1],255-0.75f*primalColor[2]);
   }
   parent.fill(primalColor[0],primalColor[1],primalColor[2]);
  }else if(object instanceof DEC_DualObject){
   if(object.getOrientation()>0){
    parent.stroke(0.75f*dualColor[0],0.75f*dualColor[1],0.75f*dualColor[2]);
   }else{
    parent.stroke(255-0.75f*dualColor[0],255-0.75f*dualColor[1],255-0.75f*dualColor[2]);
   }
   parent.fill(dualColor[0],dualColor[1],dualColor[2]);
  }
  if(verts.size()==1){
    parent.noFill();
    parent.strokeWeight(2);
    parent.pushMatrix();
     parent.translate(verts.get(0).x,verts.get(0).y,verts.get(0).z);
     if(object instanceof DEC_PrimalObject){
      parent.box(1.5f*vertexRadius);
     }else{
      parent.box(vertexRadius);
     }
    parent.popMatrix();
   }else if(verts.size()==2){
    parent.stroke(255,0,0);
    parent.strokeWeight(5);
    parent.line(verts.get(0).x,verts.get(0).y,verts.get(0).z,verts.get(1).x,verts.get(1).y,verts.get(1).z);
   }else if(verts.size()>2){
    if(object.dimension() == 2){
     if(object instanceof DEC_PrimalObject){
      parent.fill(255,0,0,150);
      parent.beginShape();
      for(int i=0;i<verts.size();i++){
       parent.vertex(verts.get(i).x,verts.get(i).y,verts.get(i).z);
      }
      parent.endShape(PApplet.CLOSE);
     }else if(object instanceof DEC_DualObject){
      PVector faceCenter = object.getVectorContent(0);
      ArrayList<PVector> sorted = sortPointsInPolygon(verts,faceCenter);
      PVector centroid = GeometricUtils.centroid(sorted);
      parent.fill(150,0,0,150);
      for(int i=1;i<sorted.size();i++){
       parent.beginShape();
       parent.vertex(centroid.x,centroid.y,centroid.z);
       parent.vertex(sorted.get(i).x,sorted.get(i).y,sorted.get(i).z);
       parent.vertex(sorted.get((i+1)%sorted.size()).x,sorted.get((i+1)%sorted.size()).y,sorted.get((i+1)%sorted.size()).z);
       parent.endShape(PApplet.CLOSE);      
      }
     }
    }
   }
 }
 public void drawVector(DEC_Object object,DEC_GeometricContainer container) throws DEC_Exception{
  ArrayList<PVector> verts = object.getGeometry(container);
 }
 public void drawTextureOnComplex(DEC_Complex complex,DEC_GeometricContainer container,OBJMeshReader objModel,PImage texture) throws DEC_Exception{
  DEC_Iterator iterator = complex.createIterator(2, 'p');
  parent.noStroke();
  while(iterator.hasNext()){
   DEC_PrimalObject face = (DEC_PrimalObject) iterator.next();
   ArrayList<PVector> faceVerts = face.getGeometry(container);
   ArrayList<PVector> texVertices = new ArrayList<PVector>();
   texVertices.add(face.getVectorContent(2));
   texVertices.add(face.getVectorContent(3));
   texVertices.add(face.getVectorContent(4));
   if(texVertices.size()==faceVerts.size()){
    parent.beginShape();
    parent.texture(texture);
    for(int i=0;i<texVertices.size();i++){
     parent.vertex(faceVerts.get(i).x,faceVerts.get(i).y,faceVerts.get(i).z, 
                   texVertices.get(i).x,texVertices.get(i).y);
    }
    parent.endShape();
   }
  }
 }
 public void drawVectorOnObject(DEC_Object object,PVector v, float minLength, float maxLength) throws DEC_Exception{
  float length = v.mag();
  PVector w = new PVector(v.x,v.y,v.z);
  w.limit(45);
  float u = (length-maxLength)/(maxLength-minLength);
  PVector c = object.getVectorContent(0);
  parent.colorMode(PApplet.HSB);
  parent.stroke(255*u,255,255);
  parent.pushMatrix();
   parent.translate(c.x,c.y,c.z);
   parent.line(0,0,0,w.x,w.y,w.z);
  parent.popMatrix();
  parent.colorMode(PApplet.RGB);
 }
 public void drawVectorField(DEC_Complex complex, DEC_GeometricContainer container,int dimension,VectorField vField,int numSamples ,float minLength,float maxLength) throws DEC_Exception{
  DEC_Iterator iterator = complex.createIterator(dimension, 'p');
  while(iterator.hasNext()){
   DEC_PrimalObject op = (DEC_PrimalObject) iterator.next();
   PVector v = vField.sampleValue(op, container, numSamples);
   drawVectorOnObject(op,v,minLength,maxLength);
  }
 }
 public void drawVertex(PVector v, char type, int orientation){
   parent.noStroke();
   if(type == 'p'){
    if(orientation>0){
     parent.fill(primalColor[0],primalColor[1],primalColor[2]);
    }else{
     parent.fill(primalColor[0],0.5f*primalColor[1],primalColor[2]);
    }
   }else{
    if(orientation>0){
     parent.fill(dualColor[0],dualColor[1],dualColor[2]);
    }else{
     parent.fill(dualColor[0],0.5f*dualColor[1],dualColor[2]);
    }
   }
   parent.pushMatrix();
    parent.translate(v.x,v.y,v.z);
    parent.box(vertexRadius);
   parent.popMatrix();
 }
 public void drawEdge(PVector v0, PVector v1, char type, int orientation){
  if(type == 'p'){
    if(orientation>0){
     parent.stroke(primalColor[0],primalColor[1],primalColor[2]);
    }else{
     parent.stroke(primalColor[0],0.5f*primalColor[1],primalColor[2]);
    }
   }else{
    if(orientation>0){
     parent.stroke(dualColor[0],dualColor[1],dualColor[2]);
    }else{
     parent.stroke(dualColor[0],0.5f*dualColor[1],dualColor[2]);
    }
   }
  parent.strokeWeight(1);
  parent.line(v0.x,v0.y,v0.z,v1.x,v1.y,v1.z);
 }
 public void drawPrimalFace(ArrayList<PVector> verts, ArrayList<PVector> normals, int orientation){
  if(orientation>0){
   parent.fill(250,180);
  }else{
   parent.fill(250,180);
  }
  parent.noStroke();
  parent.beginShape();
  for(int i=0;i<verts.size();i++){
   parent.normal(normals.get(i).x,normals.get(i).y,normals.get(i).z);
   parent.vertex(verts.get(i).x,verts.get(i).y,verts.get(i).z);
  }
  parent.endShape(PApplet.CLOSE);
 }
 public void drawDualFace(ArrayList<PVector> verts, ArrayList<PVector> normals,PVector faceCenter,PVector faceNormal, int orientation){
  if(orientation>0){
   parent.fill(150);
  }else{
   parent.fill(150,150);
  }
  parent.noStroke();
  if(verts.size()==normals.size()){
   for(int i=0;i<verts.size();i++){
    parent.beginShape();
     parent.normal(faceNormal.x,faceNormal.y,faceNormal.z);
     parent.vertex(faceCenter.x,faceCenter.y,faceCenter.z);
     parent.normal(normals.get(i).x,normals.get(i).y,normals.get(i).z);
     parent.vertex(verts.get(i).x,verts.get(i).y,verts.get(i).z);
     parent.normal(normals.get((i+1)%verts.size()).x,normals.get((i+1)%verts.size()).y,normals.get((i+1)%verts.size()).z);
     parent.vertex(verts.get((i+1)%verts.size()).x,verts.get((i+1)%verts.size()).y,verts.get((i+1)%verts.size()).z);
    parent.endShape(PApplet.CLOSE);
   }
  }
 }
 public void drawObject(DEC_Object object, DEC_GeometricContainer container) throws DEC_Exception{
  ArrayList<PVector> verts = container.getGeometricContent(object);
  if(verts.size()==1){
   if(object instanceof DEC_PrimalObject){
     drawVertex(verts.get(0), 'p',object.getOrientation());
   }else if(object instanceof DEC_DualObject){
     drawVertex(verts.get(0), 'd',object.getOrientation());
   }
  }else if(verts.size()==2){
   if(object instanceof DEC_PrimalObject){
     drawEdge(verts.get(0),verts.get(1), 'p',object.getOrientation());
   }else if(object instanceof DEC_DualObject){
     drawEdge(verts.get(0),verts.get(1), 'd',object.getOrientation());
   }
  }else if(verts.size()>2){
    if(object instanceof DEC_PrimalObject){
     if(object.dimension()==2){
      ArrayList<PVector> normals = new ArrayList<PVector>();
      normals.add(object.getVectorContent(2));
      normals.add(object.getVectorContent(3));
      normals.add(object.getVectorContent(4));
      drawPrimalFace(verts, normals,object.getOrientation());
     }
   }else if(object instanceof DEC_DualObject){
    if(object.dimension()==2){
     PVector faceCenter = object.getVectorContent(0);
     PVector faceNormal = object.getVectorContent(1);
     ArrayList<PVector> normals = new ArrayList<PVector>();
     drawDualFace(verts, normals, faceCenter, faceNormal, object.getOrientation());
    }
   }
  }
 }
 public ArrayList<PVector> sortPointsInPolygon(ArrayList<PVector> p, PVector apex){
  ArrayList<PVector> sorted = new ArrayList<PVector>();
  for(int i=0;i<p.size();i++){
   sorted.add(new PVector(p.get(i).x,p.get(i).y,p.get(i).z));
  }
  //create flat frustum points
  ArrayList<PVector> frustumPoints = createFlatFrustum(sorted, apex);
  PVector centroid = GeometricUtils.centroid(frustumPoints);
  //create plane coordinate system
  PVector xAxis = PVector.sub(frustumPoints.get(0),centroid).normalize(null);
  PVector yAxis = xAxis.cross(PVector.sub(apex,centroid)).normalize(null);
  ArrayList<PVector> planePoints = new ArrayList<PVector>();
  for(int i=0;i<frustumPoints.size();i++){
   float xComponent = frustumPoints.get(i).dot(xAxis);
   float yComponent = frustumPoints.get(i).dot(yAxis);
   planePoints.add(new PVector(xComponent,yComponent));
  }
  ArrayList<Float> angles = new ArrayList<Float>();
  for(int i=0;i<planePoints.size();i++){
   float angle = GeometricUtils.angleBetweenVectors(planePoints.get(i),xAxis);
   angles.add(new Float(angle));
  }
  for(int i=0;i<angles.size();i++){
   for(int j=1;j<angles.size()-i;j++){
    float angleJMinus = angles.get(j-1).floatValue();
    float angleJ = angles.get(j).floatValue();
    PVector tempJMinus = new PVector(sorted.get(j-1).x,sorted.get(j-1).y,sorted.get(j-1).z);
    PVector tempJ = new PVector(sorted.get(j).x,sorted.get(j).y,sorted.get(j).z);
    if(angleJMinus>angleJ){
     angles.set(j-1,new Float(angleJ));
     angles.set(j,new Float(angleJMinus));
     sorted.set(j-1,tempJ);
     sorted.set(j,tempJMinus);
     
    }
   }
  }
  return sorted;
 }
 public ArrayList<PVector> createFlatFrustum(ArrayList<PVector> p, PVector apex) {
  //construct normal plane to points using centroid as basis
  PVector centroid = GeometricUtils.centroid(p);
  PVector normalVector = PVector.sub(centroid, apex).normalize(null);
  normalVector.normalize();
  //project poins onto plane generated by centroid and normal vector
  ArrayList<PVector> flatPoints = new ArrayList<PVector>();
  for (int i=0; i<p.size (); i++) {
    PVector q = PVector.sub(p.get(i),centroid);
    float dotProduct = normalVector.dot(q);
    PVector normalComponent = PVector.mult(normalVector,dotProduct);
    PVector p_proj = PVector.sub(p.get(i),normalComponent);
    flatPoints.add(p_proj);
  }
  return flatPoints;
 }
}
