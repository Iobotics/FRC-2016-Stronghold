import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import gab.opencv.*; 
import ipcapture.*; 
import edu.wpi.first.networktables.NetworkTable; 
import edu.wpi.first.networktables.NetworkTableEntry; 
import edu.wpi.first.networktables.NetworkTableInstance; 
import processing.video.*; 
import processing.net.*; 
import ddf.minim.*; 
import ddf.minim.analysis.*; 
import ddf.minim.effects.*; 
import ddf.minim.signals.*; 
import ddf.minim.spi.*; 
import ddf.minim.ugens.*; 
import blobDetection.*; 

import edu.wpi.first.wpilibj.tables.*; 
import edu.wpi.first.wpilibj.networktables.*; 
import edu.wpi.first.networktables.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Vision2016 extends PApplet {









// ***NOTE*** Requires installing (through Processing) three libraries: Minim, Video, and BlobDetection //
 //Used for cameras connected to this computer


// Sound library //









// Target physical constants //
float targetWidth = 20.0f; //Inches
float targetHeight = 12.0f;

// Image constants //
//TODO: Tune
float hueMax = 360.0f;
float satMax = 100.0f;
float valMax = 100.0f;
float targetHueMin = 110; //130
float targetHueMax = 190; //200
float targetSatMin = 40.0f; //20
float targetSatMax = 120.0f;
float targetValMin = 20.0f;
float targetValMax = 100.0f;
float targetBoundingBoxAreaMin = 0.0001f;
float targetAreaMin = 0.001f;
//Aspect ratio: Width / height... ideal = 1.6
float targetAspectRatioMin = 0.25f;  
float targetAspectRatioMax = 3.0f;
float targetAspectRatioIdeal = 1.6f;
//Rectangularity: Area / bounding box area... ideal = 0.3
float targetRectangularityMin = 0.0f;
float targetRectangularityMax = 5.0f;

float imageCenterX = 0.5f; //What should the program consider the "center" of the screen (as a proportion of its width and height)?
float imageCenterY = 0.5f;

// IPCapture //
//Based on mjpeg-streamer running on RoboRio to turn the output from a USB camera into an mjpg
//Important mjpeg-streamer settings (set through SSH config): 
//-bk 0 (disable backlight compensation), -gain 0 (disable brightness correction), -cagc 0 (disable color correction), -ex 20 (low exposure), -sa 200 (very high saturation), -co 100 (high contrast)
//Other settings don't seem to change anything (they're dependent on the camera model)
IPCapture camera;
String mjpegURL = "http://roborio-2438-frc.local:1181/?action=stream";

// Camera parameters //
int cameraWidth = 320;
int cameraHeight = 240;
String cameraName = "name=Logitech HD Pro Webcam C920,size=800x600,fps=30";
float cameraFocalLength = 0.1444882f; //Inches
float cameraSensorWidth = 0.188976f; //Inches
float cameraSensorHeight = 0.141732f;

// Distance calibration constants //
float calibrationDistance = 122; //Inches
float calibrationWidth = denormalize(0.091364205f, cameraWidth);
float calibrationHeight = denormalize(0.22871456f, cameraHeight);
float calibrationAspectRatio = calibrationWidth / calibrationHeight;
float calibrationCameraHeight = 16; //Height the camera was off the ground during calibration; currently unused

// Blob detector //
BlobDetection blobDetector;
ArrayList<Blob> blobs;

PImage frame; 

// Sound //
Minim minim;
AudioPlayer audioTargetingBeep;
AudioPlayer audioLockedPitch;
int targetingBeepDelayFrames = 10; //Frames to wait for targeting beep, on average
float targetingBeepRemainingFrames = targetingBeepDelayFrames;
float lockedErrorMax = 0.2f; //Maximum error to play the "target locked" sound... TODO: Make this depend on actual likely successful shot region

// OpenCV //
OpenCV opencv;

// Network tables //
final String networkTableAddress = "roborio-2438-frc.local";//"10.24.38.227";
NetworkTable networkTable;
NetworkTableInstance inst = NetworkTableInstance.getDefault();
final String networkTableName = "SmartDashboard";

public void setup() {
  // Set up Processing constants //  
  colorMode(HSB, hueMax, satMax, valMax);
  rectMode(CENTER);
  ellipseMode(CENTER);
  noFill();
  strokeWeight(1);
  stroke(0.0f, 100.0f, 100.0f); //Red stroke
  
  frameRate(30);
  
  // Initialize network table //
  networkTable = inst.getTable(networkTableName);
  inst.startClientTeam(2438);
  inst.startDSClient();
  
  // Initialize sound //
  minim = new Minim(this);
  audioTargetingBeep = minim.loadFile("targeting-beep.mp3");
  audioLockedPitch = minim.loadFile("locked-beep-2.mp3");
  audioLockedPitch.setLoopPoints(100, 4000); //Produces a cleaner loop
  audioLockedPitch.loop();
  audioLockedPitch.mute();
 
  // Initialize IPCapture connection //
  camera = new IPCapture(this, mjpegURL, "", ""); //No username / password
  camera.start();
  
  // Initialize blob detector //
  blobDetector = new BlobDetection(cameraWidth, cameraHeight);
  blobDetector.setPosDiscrimination(true); //Detector will search for bright areas
  blobs = new ArrayList<Blob>();
  
  // Initialize OpenCV //
  frame = new PImage();
  opencv = new OpenCV(this, camera);
  //Contour.setPolygonApproximationFactor(2); //TODO: Parameterize
}

public void draw() {
  // Get image from streamed mjpg //
  if (camera.isAvailable()) {
    camera.read();
  }
  
  frame = camera;
  
  // Apply color filter //
  PImage filteredFrame = filterImageHSBRange(frame, targetHueMin, targetHueMax, targetSatMin, targetSatMax, targetValMin, targetValMax);
  //image(filteredFrame, 0, 0);
  
  //if (true) return;
  
  // Find blobs //
  filteredFrame.loadPixels();
  blobDetector.computeTriangles();
  blobDetector.computeBlobs(filteredFrame.pixels);
  filteredFrame.updatePixels();
  
  // Build ArrayList for easy blob access //
  blobs = new ArrayList<Blob>(); //Clear old list
  for (int i = 0; i < blobDetector.getBlobNb(); i++) {
    blobs.add(blobDetector.getBlob(i));
  }
  
  // Filter blobs //
  ArrayList<Blob> filteredBlobs = new ArrayList<Blob>();
  for (Blob blob : blobs) {
    if (
    filterBlobBoundingBoxAreaMin(blob, targetBoundingBoxAreaMin)
    && filterBlobAreaMin(blob, targetAreaMin)
    && filterBlobAspectRatioRange(blob, targetAspectRatioMin, targetAspectRatioMax)
    && filterBlobRectangularityRange(blob, targetRectangularityMin, targetRectangularityMax)
    ) {
      filteredBlobs.add(blob);
      //println("Distance: " + getTargetDistance(blob));
      println("Aspect ratio: " + getBlobAspectRatio(blob) + " " + getBlobRectangularity(blob));
      //println("Area: " + getBlobArea(blob));
      //println("X error: " + getTargetXError(blob));
      //println("Y error: " + getTargetYError(blob));
    }
  }
  strokeWeight(1);
  
  // Show bounding boxes and target locations //
  for (Blob blob : filteredBlobs) {
    stroke(240.0f, 100.0f, 100.0f); //Blue stroke
    noFill();
    rect(denormalize(blob.x, frame.width)*2, denormalize(blob.y, frame.height)*2, denormalize(blob.w, frame.width)*2, denormalize(blob.h, frame.height)*2);
    stroke(200.0f, 100.0f, 100.0f); //Blue stroke
    fill(200.0f, 100.0f, 100.0f); //Blue fill
    ellipse(denormalize(blob.x, frame.width)*2, denormalize(blob.y, frame.height)*2, 4, 4);
    stroke(0.0f, 100.0f, 100.0f); //Red stroke
    noFill();
  }
  
  float leastError = -1; //Least error seen so far
  float error;
  
  // Display image //
  image(frame, 0, 0, 640, 480);
  
  // Find bar height with OpenCV //
  // Find largest estimated polygon
  opencv.loadImage(filteredFrame);
  //image(opencv.getOutput(), 0, 0);
  Contour polygonApproximation;
  Contour largestPolygon = null; //Largest polygon detected; used to estimate distance
  for (Contour contour : opencv.findContours()) {
    contour.setPolygonApproximationFactor(3); //TODO: Parameterize, tune... this determines how "strict" the polygon approximation process is
    polygonApproximation = contour.getPolygonApproximation();
    if (largestPolygon == null || polygonApproximation.area() > largestPolygon.area()) {
      largestPolygon = polygonApproximation;
    }
    
    //polygonApproximation.draw();
    fill(30, 100, 100);
    stroke(30, 100, 100);
    strokeWeight(2);
    beginShape(LINES);
    for(PVector pv :  polygonApproximation.getPoints()) {
      vertex(pv.x * 2, pv.y * 4);
    }
    endShape();
  }
  
  float barLength = -1.0f; //Used for distance calculation; if height = -1, then bar height calc failed
  
  if (largestPolygon != null) {
    ArrayList<PVector> polygonPoints = largestPolygon.getPoints();
    ArrayList<PVector> upperLeftCandidates = new ArrayList<PVector>();
    ArrayList<PVector> lowerLeftCandidates = new ArrayList<PVector>();
    
    // Find upper left and bottom left corners //
    PVector upperLeft = null;
    PVector bottomLeft = null;
    
    for (PVector point : polygonPoints) {
      if (point.y < largestPolygon.getBoundingBox().getLocation().y + (largestPolygon.getBoundingBox().getHeight() / 2)) { //This point is in the upper half of the rectangle
        upperLeftCandidates.add(point);
      } else if (point.y > largestPolygon.getBoundingBox().getLocation().y - (largestPolygon.getBoundingBox().getHeight() / 2)) { //This point is in the lower half of the rectangle
        lowerLeftCandidates.add(point);
      }
    }
    
    for (PVector point : upperLeftCandidates) { //Locate leftmost candidate
      if (upperLeft == null || upperLeft.x > point.x) upperLeft = point;
    }
    
    for (PVector point : lowerLeftCandidates) { //Locate leftmost candidate
      if (bottomLeft == null || bottomLeft.x > point.x) bottomLeft = point;
    }
    
    if (upperLeft != null && bottomLeft != null) {
      ellipseMode(CENTER);
      ellipse(upperLeft.x * 2, upperLeft.y * 4, 4, 4);
      ellipse(bottomLeft.x * 2, bottomLeft.y * 4, 4, 4);
      
      // Find bar height //
      barLength = dist(upperLeft.x, upperLeft.y, bottomLeft.x, bottomLeft.y);
    }
  }
  
  float estimatedDistance = getDistance(barLength);
  
  //println("Bar length: " + barLength);
  //println("Estimated distance: " + estimatedDistance);
 
  // Find closest blob by azimuth //
  Blob closestTarget = null;
  
  for (Blob blob : filteredBlobs) {
    if (closestTarget == null) {
      closestTarget = blob;
    } else if (abs(closestTarget.x - imageCenterX) > abs(blob.x - imageCenterX)) {
      closestTarget = blob;
    }
  }
  
  // Sort target list by nearness to screen center (azimuth) //
  //ArrayList<Blob> orderedBlobs = new ArrayList<Blob>();
  //Blob examinedBlob;
  
  /*
  for (int i = 0; i < filteredBlobs.size(); i++) {
    examinedBlob = filteredBlobs.get(i);
    if (orderedBlobs.size() == 0) {
      orderedBlobs.add(examinedBlob);
    } else {
      boolean addedBlob = false;
      for (int j = 0; j < orderedBlobs.size(); j++) {
        if (abs(examinedBlob.x - imageCenterX) < abs(orderedBlobs.get(j).x - imageCenterX)) { //If the examined blob is closer to the center, insert it before this one in the list
          orderedBlobs.add(j, examinedBlob);
          addedBlob = true;
          break;
        }
        
        if (!addedBlob) orderedBlobs.add(examinedBlob); //If the target doesn't belong earlier in the list, add it to the end
      }
    }
  }
  */
  
  float rounder = 1000.0f; //Quick and dirty rounding to this number's digits
  
  if (closestTarget != null) {
    // Target report / sound of closest target //
    textSize(23);
    fill(255);
    
    // Display text //
    noStroke();
    text("e-X: " + round(getTargetXError(closestTarget) * rounder) / rounder, 60, 30);
    text("e-Y: " + round(getTargetYError(closestTarget) * rounder) / rounder, 60, 60);
    text("D: " + round(estimatedDistance * rounder) / rounder, 60, 90);
    text("h-Y: " + round((cameraHeight - (closestTarget.y * cameraHeight)) * rounder) / rounder, 60, 120);
  
    // Push data to the network table //
    double closestXError = getTargetXError(closestTarget);
    double closestYError = getTargetYError(closestTarget);
    networkTable.getEntry("vision-x-error").setDouble(closestXError);
    networkTable.getEntry("vision-y-error").setDouble(closestYError);
    networkTable.getEntry("vision-distance").setDouble(estimatedDistance);
  }
  
  // Show targets and play sounds //
  Blob target;
  for (int i = 0; i < filteredBlobs.size(); i++) {
    target = filteredBlobs.get(i);
    // Target crosshair //
    strokeWeight(1);
    if (target == closestTarget) stroke(110, 100, 100);
    else stroke(50, 100, 70);
    line((target.x * cameraWidth) * 2, ((target.y * cameraHeight) + 50)*2, (target.x * cameraWidth)*2, ((target.y * cameraHeight) - 50)*2);
    line(((target.x * cameraWidth) - 50)*2, (target.y * cameraHeight)*2, ((target.x * cameraWidth) + 50)*2, (target.y * cameraHeight)*2);
    
    // Detect lock for audio //
    error = sqrt(pow(getTargetXError(target), 2) + pow(getTargetYError(target), 2));
    if (leastError == -1 || error <= leastError) {
      leastError = error;
    }
  }
  
  // Set rounder //
  rounder = 10;
  
  // Get network table data //
  float elevationAngle = (float) networkTable.getEntry("gimbal-elevation-position").getDouble(0.0f);
  float elevationAngleRounded = round(elevationAngle * rounder) / rounder;
  
  // Display non-target-based data //
  text("Elevation Angle: " + elevationAngleRounded, cameraWidth * 2 - 250, cameraHeight * 2 - 15);
  
  // Camera crosshair //
  stroke(0, 100, 100);
  line(imageCenterX * cameraWidth * 2, 0, imageCenterX * cameraWidth * 2, cameraHeight * 2);
  line(0, imageCenterY * cameraHeight * 2 , cameraWidth * 2, imageCenterY * cameraHeight * 2);
  
  if (leastError != -1) { //If we detect targets, play a sound
    if (leastError <= lockedErrorMax) { //Target locked!
      audioLockedPitch.unmute();
    } else { //No locked target, play a sound
      audioLockedPitch.mute();
      targetingBeepRemainingFrames--;
      if (targetingBeepRemainingFrames <= 0) { //Play a beep
        audioTargetingBeep.rewind();
        audioTargetingBeep.play();
        targetingBeepRemainingFrames = targetingBeepDelayFrames * leastError;
      }
    }
  } else {
    audioLockedPitch.mute(); //Otherwise, do nothing
  }
}

public boolean filterBlobBoundingBoxAreaMin(Blob bl, float minSize) {
  return (getBlobBoundingBoxArea(bl) > minSize);
}

public boolean filterBlobAreaMin(Blob bl, float minSize) {
  return (getBlobArea(bl) > minSize);
}

public boolean filterBlobAspectRatioRange(Blob blob, float minAspectRatio, float maxAspectRatio) {
  return withinRange(getBlobAspectRatio(blob), minAspectRatio, maxAspectRatio);
}

public boolean filterBlobRectangularityRange(Blob blob, float minRectangularity, float maxRectangularity) {
  return withinRange(getBlobRectangularity(blob), minRectangularity, maxRectangularity);
}

public PImage filterImageHSBRange(PImage img, float minHue, float maxHue, float minSat, float maxSat, float minValue, float maxValue) {
  PImage output = createImage(img.width, img.height, HSB);
  output.loadPixels();
  img.loadPixels();
  int imgPixelCount = img.width * img.height;
  int examinedPixel; 
  float hue;
  float sat;
  float val;
  
  int mousePixel = mouseY/2 * img.width + mouseX/2;
  
  for (int i = 0; i < imgPixelCount; i++) {
    examinedPixel = img.pixels[i];
    hue = hue(examinedPixel);
    sat = saturation(examinedPixel);
    val = brightness(examinedPixel);
    if (withinRange(hue, minHue, maxHue) && withinRange(sat, minSat, maxSat) && withinRange(val, minValue, maxValue)) { //Good pixel
      output.pixels[i] = color(0, 0, 100); //Add white pixel
    } else { //Bad pixel
      output.pixels[i] = color(0, 0, 0); //Add black pixel
    }
    /*if(i == mousePixel) {
      println("mouse HSB", hue, sat, val);
    }*/
  }
  
  output.updatePixels();
  img.updatePixels();
  return output;
}

public float getBlobArea(Blob blob) { //Assumes computeTriangles() has been called
  int triangleCount = blob.getTriangleNb();
  BlobTriangle triangle;
  EdgeVertex vertexA, vertexB, vertexC;
  //Side lengths
  float len1, len2, len3;
  float area = 0.0f;
  for (int i = 0; i < triangleCount; i++) {
    triangle = blob.getTriangle(i);
    area += getTriangleArea(blob, triangle);
  }
  return area;
}

public float getBlobBoundingBoxArea(Blob blob) { //Gets a blob's bounding box area (not denormalized!)
  return blob.w * blob.h;
}

public float getBlobRectangularity(Blob blob) {
  return getBlobArea(blob) / getBlobBoundingBoxArea(blob);
}

public float getBlobAspectRatio(Blob blob) { //Width / height
  return blob.w / blob.h;
}

public float getTargetDistance(Blob target) { //Assumes blob is a potential target; returns inches
  // Correct for angle... consider removing this bit? //
  /*float measuredWidth = denormalize(target.w, cameraWidth);
  float measuredHeight = denormalize(target.h, cameraHeight);
  float correctedWidth = (measuredWidth * calibrationAspectRatio) / getBlobAspectRatio(target);
  float correctedHeight = (measuredHeight * getBlobAspectRatio(target)) / calibrationAspectRatio;
  return (calibrationWidth * calibrationDistance) / measuredWidth;
  */
  float observedHeightPixels = denormalize(target.h, cameraHeight);
  return (cameraFocalLength * targetHeight * cameraHeight) / (observedHeightPixels  * cameraSensorHeight);
}

public float getDistance(float barHeight) { //Returns inches
  // TODO: Make this more readable, parameterize //
  //return 12 * (55.7 - (1.28 * barHeight));
  return (-9.271f * barHeight) + 517.683f;
}

public float getTargetXError(Blob target) { //Returns an "aim error" value the control loop should try to zero; normalized based on height so that distance doesn't change the results that much
  return (imageCenterX - target.x) / target.h;
}

public float getTargetYError(Blob target) {
  return (imageCenterY - target.y) / target.h;
}

public boolean withinRange (float number, float min, float max) { //Utility function that checks if a number is within a range
  if (number > max || number < min) return false;
  return true;
}

public float denormalize (float number, float factor) { //Reverses BlobDetector's normalizing
  return number * factor;
}

public float getTriangleArea(Blob blob, BlobTriangle triangle) {
  EdgeVertex vertexA = blob.getTriangleVertexA(triangle);
  EdgeVertex vertexB = blob.getTriangleVertexB(triangle);
  EdgeVertex vertexC = blob.getTriangleVertexC(triangle);
  float len1 = sqrt(pow((vertexA.x - vertexB.x), 2) + pow((vertexA.y - vertexB.y), 2));
  float len2 = sqrt(pow((vertexA.x - vertexC.x), 2) + pow((vertexA.y - vertexC.y), 2));
  float len3 = sqrt(pow((vertexB.x - vertexC.x), 2) + pow((vertexB.y - vertexC.y), 2));
  return getTriangleArea(len1, len2, len3);
}

public float getTriangleArea(float edge1, float edge2, float edge3) { //Compute the area of a triangle with Hero's formula
  float s = (edge1 + edge2 + edge3);
  return sqrt(s * (s - edge1) * (s - edge2) * (s - edge3));
}
  public void settings() {  size(640, 480); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Vision2016" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
