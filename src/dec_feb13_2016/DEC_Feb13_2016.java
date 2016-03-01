/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dec_feb13_2016;

import complex.DEC_Complex;
import complex.DEC_DualObject;
import complex.DEC_Iterator;
import complex.DEC_PrimalObject;
import complex.IndexSet;
import containers.DEC_GeometricContainer;
import exceptions.DEC_Exception;
import functions.ScalarField;
import functions.VectorField;
import processing.core.PApplet;
import processing.core.PImage;
import processing.event.MouseEvent;
import readers.OBJMeshReader;
import viewers.MeshViewer;

/**
 *
 * @author laptop
 */
public class DEC_Feb13_2016 extends PApplet{

 float rotX=0;
 float rotY=0;
 float zTranslation = 0;
 float yTranslation = 0;
 float xTranslation = 0;
 PImage modelTexture;
 PImage scalarField;
 VectorField myField;
 OBJMeshReader myReader;
 DEC_GeometricContainer myContainer;
 DEC_Complex myComplex;
 MeshViewer myViewer;
 boolean[] showComplexOption;
 int selectedVertex = 0;
 int selectedEdge = 0;
 int selectedDualVertex = 0;
 int selectedDualEdge = 0;
 int selectedFace = 0;
 int selectedDualFace = 0;
 long startTime,endTime;
 public void setup(){
  size(1200,800,P3D);
  colorMode(HSB);
  textureMode(NORMAL);
  myViewer = new MeshViewer(this);
  myViewer.setVertexRadius(2);
  myViewer.setPrimalColor(255, 255, 255);
  myViewer.setDualColor(255, 255, 0);
  startTime = System.currentTimeMillis();
  System.out.println("------------------------------------------------------");
  System.out.println("----OBJ model loading using OBJMeshReader-----");
  System.out.println("------------------------------------------------------");
  myReader = new OBJMeshReader("hombre2Triangulado.obj", this, 100);
  myReader.loadModel();
  endTime = System.currentTimeMillis();
  System.out.println(" OBJ loading finished. Elapsed time: "+(endTime-startTime));
  System.out.println("------------------------------------------------------");
  myContainer = new DEC_GeometricContainer();
  System.out.println("------------------------------------------------------");
  System.out.println("---Geometric Container creation from OBJMeshReader-----");
  startTime = System.currentTimeMillis();
  myContainer.setContent(myReader);
  endTime = System.currentTimeMillis();
  System.out.println("------------------------------------------------------");
  myContainer.printContainerInfo();
  System.out.println("------------------------------------------------------");
  System.out.println(" Geometric Container finished. Elapsed time: "+(endTime-startTime));
  System.out.println("------------------------------------------------------");
  try{
  System.out.println("------------------------------------------------------"); 
  myComplex = new DEC_Complex(); 
  startTime = System.currentTimeMillis();
  System.out.println("---DEC Complex creation from Geometric Container------");
  myComplex.setComplex(myContainer,myReader);
  endTime = System.currentTimeMillis();
  myComplex.printComplexInformation();
  System.out.println("DEC Complex creation finished. Elapsed time: "+(endTime-startTime));
  System.out.println("------------------------------------------------------"); 
  }catch(Exception ex){
    System.out.println("Exception caught: "+ex);
    ex.printStackTrace();
  }
  showComplexOption = new boolean[6];
 }
 public void mouseDragged(){
  rotX += (mouseX-pmouseX)*0.01f;
  rotY -= (mouseY-pmouseY)*0.01f;
 }
 public void mouseWheel(MouseEvent event){
  float e = event.getCount();
  zTranslation += 10*e;
 }
 public void keyPressed(){
  char[] keyOptions = new char[]{'1','2','3','4','5','6'};
  for(int i=0;i<keyOptions.length;i++){
   if(key==keyOptions[i]){
    if(!showComplexOption[i]){
     showComplexOption[i] = true;
    }else{
     showComplexOption[i] = false;
    }
   }
  }
  if(keyCode == RIGHT){
   selectedDualEdge++;
   if(selectedDualEdge>= myComplex.numDualEdges()){
    selectedDualEdge = 0;
   }
  }
  if(keyCode == LEFT){
   selectedDualEdge--;
   if(selectedDualEdge <0){
    selectedDualEdge = myComplex.numDualEdges()-1;
   }
  }
  if(key=='z' || key=='Z'){
   saveFrame("pruebaDEC.png");
  }
  if(key == 'w' || key == 'W'){
   yTranslation -=10;
  }
  if(key == 's' || key == 'S'){
   yTranslation +=10;
  }
  if(key == 'a' || key == 'A'){
   xTranslation -=10;
  }
  if(key =='d' || key == 'D'){
   xTranslation +=10;
  }
 }
 public void draw(){
  background(255);
  lights();
  translate(width/2+xTranslation,height/2+yTranslation,zTranslation);
  rotateX(rotY);
  rotateY(rotX);
  try{
    drawComplex();
    drawSelectedDualEdge(selectedDualEdge);
  }catch(Exception ex){
   System.out.println("something is wrong when plotting complex element:");
   ex.printStackTrace();
  }
 }
 void drawSelectedDualFace(int numSelected){
  try{
  DEC_DualObject f = myComplex.getDualObject(2,numSelected);
  if(f!=null){
   myViewer.drawSelectedObject(f, myContainer);
   DEC_Iterator eIterator = myComplex.createIterator(f);
   while(eIterator.hasNext()){
    myViewer.drawSelectedObject(eIterator.next(), myContainer);
   }
  }
  }catch(DEC_Exception ex){
   System.out.println("something went wrong trying to plot selected vertex");
   ex.printStackTrace();
  }
 }
 void drawSelectedFace(int numSelected){
  try{
  DEC_PrimalObject f = myComplex.getPrimalObject(2,numSelected);
  if(f!=null){
   myViewer.drawSelectedObject(f, myContainer);
   DEC_Iterator eIterator = myComplex.createIterator(f);
   while(eIterator.hasNext()){
    myViewer.drawSelectedObject(eIterator.next(), myContainer);
   }
  }
  }catch(DEC_Exception ex){
   System.out.println("something went wrong trying to plot selected vertex");
   ex.printStackTrace();
  }
 }
 void drawSelectedEdge(int numSelected){
  try{
  DEC_PrimalObject e = myComplex.getPrimalObject(1,numSelected);
  if(e!=null){
   myViewer.drawSelectedObject(e, myContainer);
   DEC_Iterator eIterator = myComplex.createIterator(e);
   while(eIterator.hasNext()){
    myViewer.drawSelectedObject(eIterator.next(), myContainer);
   }
  }
  }catch(DEC_Exception ex){
   System.out.println("something went wrong trying to plot selected vertex");
   ex.printStackTrace();
  }
 }
 void drawSelectedDualEdge(int numSelected){
  try{
  DEC_DualObject e = myComplex.getDualObject(1,numSelected);
  if(e!=null){
   myViewer.drawSelectedObject(e, myContainer);
   DEC_Iterator eIterator = myComplex.createIterator(e);
   while(eIterator.hasNext()){
    myViewer.drawSelectedObject(eIterator.next(), myContainer);
   }
  }
  }catch(DEC_Exception ex){
   System.out.println("something went wrong trying to plot selected vertex");
   ex.printStackTrace();
  }
 }
 void drawSelectedVertex(int numSelected){
  try{
  DEC_PrimalObject v = myComplex.getPrimalObject(0,numSelected);
  if(v!=null){
   myViewer.drawSelectedObject(v, myContainer);
   DEC_Iterator vIterator = myComplex.createIterator(v);
   while(vIterator.hasNext()){
    myViewer.drawSelectedObject(vIterator.next(), myContainer);
   }
  }
  }catch(DEC_Exception ex){
   System.out.println("something went wrong trying to plot selected vertex");
   ex.printStackTrace();
  }
 }
 void drawSelectedDualVertex(int numSelected){
  try{
  DEC_DualObject w = myComplex.getDualObject(0,numSelected);
  if(w!=null){
   myViewer.drawSelectedObject(w, myContainer);
   DEC_Iterator wIterator = myComplex.createIterator(w);
   while(wIterator.hasNext()){
    myViewer.drawSelectedObject(wIterator.next(), myContainer);
   }
  }
  }catch(DEC_Exception ex){
   System.out.println("something went wrong trying to plot selected vertex");
   ex.printStackTrace();
  }
 }
 void drawComplex(){
  if(showComplexOption[2]){
   drawComplexElement(2,'p');
  }
  if(showComplexOption[5]){
   drawComplexElement(2,'d');
  }
  if(showComplexOption[0]){
   drawComplexElement(0,'p');
  }
  if(showComplexOption[1]){
   drawComplexElement(1,'p');
  }
  if(showComplexOption[3]){
   drawComplexElement(0,'d');
  }
  if(showComplexOption[4]){
   drawComplexElement(1,'d');
  }
 }
 void drawComplexElement(int dimension, char type){
  DEC_Iterator iter = myComplex.createIterator(dimension, type);
  while(iter.hasNext()){
   try{
    if(type == 'p'){
     DEC_PrimalObject op = (DEC_PrimalObject) iter.next();
     myViewer.drawObject(op, myContainer);
    }else{
     DEC_DualObject od = (DEC_DualObject) iter.next();
     myViewer.drawObject(od,myContainer);
    }
   }catch(DEC_Exception ex){
     System.out.println("something went wrong plotting object");
   }
  }
 }
 /**
  * @param args the command line arguments
  */
 public static void main(String[] args) {
  //PApplet.main(new String[]{dec_indexbased.DEC_indexBased.class.getName()});
  PApplet.main(new String[]{dec_feb13_2016.DEC_Feb13_2016.class.getName()});
 }
 
}
