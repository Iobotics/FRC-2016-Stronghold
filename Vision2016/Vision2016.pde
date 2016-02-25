import ipcapture.*;

// ***NOTE*** Requires installing (through Processing) three libraries: Minim, Video, and BlobDetection //
import processing.video.*; //Used for cameras connected to this computer
import processing.net.*;

// Sound library //
import ddf.minim.*;
import ddf.minim.analysis.*;
import ddf.minim.effects.*;
import ddf.minim.signals.*;
import ddf.minim.spi.*;
import ddf.minim.ugens.*;

import blobDetection.*;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.image.*;

// Target physical constants //
float targetWidth = 20.0; //Inches
float targetHeight = 14.0;

// Image constants //
//TODO: Tune
float hueMax = 360.0;
float satMax = 100.0;
float valMax = 100.0;
float targetHueMin = 50; //130
float targetHueMax = 220; //200
float targetSatMin = 20.0; //20
float targetSatMax = 100.0;
float targetValMin = 20.0;
float targetValMax = 100.0;
float targetBoundingBoxAreaMin = 0.0005;
float targetAreaMin = 0.003;
//Aspect ratio: Width / height... ideal = 1.6
float targetAspectRatioMin = 0.25;  
float targetAspectRatioMax = 1.5;
float targetAspectRatioIdeal = 1.6;
//Rectangularity: Area / bounding box area... ideal = 0.3
float targetRectangularityMin = 0.1;
float targetRectangularityMax = 0.6;

float imageCenterX = 0.5; //What should the program consider the "center" of the screen (as a proportion of its width and height)?
float imageCenterY = 0.5;

// IPCapture //
//Based on mjpeg-streamer script running on RoboRio to turn the output from a USB camera into an mjpg
//mjpeg-streamer Readme: https://github.com/robotpy/roborio-packages/blob/2016/ipkg/mjpg-streamer/README.md
//Important mjpeg-streamer settings (set through SSH config): 
//-bk 0 (disable backlight compensation), -gain 0 (disable brightness correction), -cagc 0 (disable color correction), -ex 70 (high exposure), -sa 100 (very high saturation)
//Other settings don't seem to change anything (they're dependent on the camera model)
IPCapture camera;
String mjpegURL = "http://10.24.38.227:5800/?action=stream";

// Camera parameters //
int cameraWidth = 640;
int cameraHeight = 480;
String cameraName = "name=Logitech HD Pro Webcam C920,size=800x600,fps=30";
float cameraFocalLength = 0.1444882; //Inches
float cameraSensorWidth = 0.188976; //Inches
float cameraSensorHeight = 0.141732;

// Distance calibration constants //
float calibrationDistance = 96; //Inches
float calibrationWidth = denormalize(0.091364205, cameraWidth);
float calibrationHeight = denormalize(0.22871456, cameraHeight);
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
float lockedErrorMax = 0.2; //Maximum error to play the "target locked" sound... TODO: Make this depend on actual likely successful shot region

void setup() {
  // Set up Processing constants //  
  colorMode(HSB, hueMax, satMax, valMax);
  rectMode(CENTER);
  ellipseMode(CENTER);
  noFill();
  strokeWeight(1);
  stroke(0.0, 100.0, 100.0); //Red stroke
  size(640, 480);
  frameRate(10);
  
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
  
  frame = new PImage();
}

void draw() {
  // Get image from streamed mjpg //
  if (camera.isAvailable()) {
    camera.read();
  }
  
  frame = camera;
  
  // Apply color filter //
  PImage filteredFrame = filterImageHSBRange(frame, targetHueMin, targetHueMax, targetSatMin, targetSatMax, targetValMin, targetValMax);
  
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
      //println("Aspect ratio: " + getBlobAspectRatio(blob));
      //println("Rectangularity: " + getBlobRectangularity(blob));
      //println("Area: " + getBlobArea(blob));
      //println("X error: " + getTargetXError(blob));
      //println("Y error: " + getTargetYError(blob));
    }
  }
  strokeWeight(1);
  
  // Show bounding boxes and target locations //
  for (Blob blob : filteredBlobs) {
    stroke(240.0, 100.0, 100.0); //Blue stroke
    noFill();
    rect(denormalize(blob.x, frame.width), denormalize(blob.y, frame.height), denormalize(blob.w, frame.width), denormalize(blob.h, frame.height));
    stroke(200.0, 100.0, 100.0); //Blue stroke
    fill(200.0, 100.0, 100.0); //Blue fill
    ellipse(denormalize(blob.x, frame.width), denormalize(blob.y, frame.height), 4, 4);
    stroke(0.0, 100.0, 100.0); //Red stroke
    noFill();
  }
  
  float leastError = -1; //Least error seen so far
  float error;
  
  // Display image //
  image(frame, 0, 0);
  
  // Show target reports and play sounds //
  textSize(23);
  fill(255);
  int displayBufferIncrement = 30;
  int displayBuffer = 30;
  Blob target;
  for (int i = 0; i < filteredBlobs.size(); i++) {
    target = filteredBlobs.get(i);
    
    // Text //
    float rounder = 1000.0; //Quick and dirty rounding to this number's digits
    noStroke();
    text("Target " + (i + 1) + " :", 20, displayBuffer);
    displayBuffer += displayBufferIncrement;
    text("X error: " + round(getTargetXError(target) * rounder) / rounder, 60, displayBuffer);
    displayBuffer += displayBufferIncrement;
    text("Y error: " + round(getTargetYError(target) * rounder) / rounder, 60, displayBuffer);
    displayBuffer += displayBufferIncrement;
    text("Estimated distance: " + round(getTargetDistance(target) * rounder) / rounder, 60, displayBuffer);
    displayBuffer += displayBufferIncrement;
    
    // Target crosshair //
    strokeWeight(1);
    stroke(110, 100, 100); //Green
    line(target.x * cameraWidth, (target.y * cameraHeight) + 50, target.x * cameraWidth, (target.y * cameraHeight) - 50);
    line((target.x * cameraWidth) - 50, target.y * cameraHeight, (target.x * cameraWidth) + 50, target.y * cameraHeight);
    
    // Detect lock for audio //
    error = sqrt(pow(getTargetXError(target), 2) + pow(getTargetYError(target), 2));
    if (leastError == -1 || error <= leastError) {
      leastError = error;
    }
  }
  
  // Camera crosshair //
  stroke(0, 100, 100);
  line(imageCenterX * cameraWidth, 0, imageCenterX * cameraWidth, cameraHeight);
  line(0, imageCenterY * cameraHeight, cameraWidth, imageCenterY * cameraHeight);
  
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

boolean filterBlobBoundingBoxAreaMin(Blob bl, float minSize) {
  return (getBlobBoundingBoxArea(bl) > minSize);
}

boolean filterBlobAreaMin(Blob bl, float minSize) {
  return (getBlobArea(bl) > minSize);
}

boolean filterBlobAspectRatioRange(Blob blob, float minAspectRatio, float maxAspectRatio) {
  return withinRange(getBlobAspectRatio(blob), minAspectRatio, maxAspectRatio);
}

boolean filterBlobRectangularityRange(Blob blob, float minRectangularity, float maxRectangularity) {
  return withinRange(getBlobRectangularity(blob), minRectangularity, maxRectangularity);
}

PImage filterImageHSBRange(PImage img, float minHue, float maxHue, float minSat, float maxSat, float minValue, float maxValue) {
  PImage output = createImage(img.width, img.height, HSB);
  output.loadPixels();
  img.loadPixels();
  int imgPixelCount = img.width * img.height;
  color examinedPixel; 
  float hue;
  float sat;
  float val;
  
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
  }
  
  output.updatePixels();
  img.updatePixels();
  return output;
}

float getBlobArea(Blob blob) { //Assumes computeTriangles() has been called
  int triangleCount = blob.getTriangleNb();
  BlobTriangle triangle;
  EdgeVertex vertexA, vertexB, vertexC;
  //Side lengths
  float len1, len2, len3;
  float area = 0.0;
  for (int i = 0; i < triangleCount; i++) {
    triangle = blob.getTriangle(i);
    area += getTriangleArea(blob, triangle);
  }
  return area;
}

float getBlobBoundingBoxArea(Blob blob) { //Gets a blob's bounding box area (not denormalized!)
  return blob.w * blob.h;
}

float getBlobRectangularity(Blob blob) {
  return getBlobArea(blob) / getBlobBoundingBoxArea(blob);
}

float getBlobAspectRatio(Blob blob) { //Width / height
  return blob.w / blob.h;
}

float getTargetDistance(Blob target) { //Assumes blob is a potential target; returns inches
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

float getTargetXError(Blob target) { //Returns an "aim error" value the control loop should try to zero; normalized based on height so that distance doesn't change the results that much
  return (imageCenterX - target.x) / target.h;
}

float getTargetYError(Blob target) {
  return (imageCenterY - target.y) / target.h;
}

boolean withinRange (float number, float min, float max) { //Utility function that checks if a number is within a range
  if (number > max || number < min) return false;
  return true;
}

float denormalize (float number, float factor) { //Reverses BlobDetector's normalizing
  return number * factor;
}

float getTriangleArea(Blob blob, BlobTriangle triangle) {
  EdgeVertex vertexA = blob.getTriangleVertexA(triangle);
  EdgeVertex vertexB = blob.getTriangleVertexB(triangle);
  EdgeVertex vertexC = blob.getTriangleVertexC(triangle);
  float len1 = sqrt(pow((vertexA.x - vertexB.x), 2) + pow((vertexA.y - vertexB.y), 2));
  float len2 = sqrt(pow((vertexA.x - vertexC.x), 2) + pow((vertexA.y - vertexC.y), 2));
  float len3 = sqrt(pow((vertexB.x - vertexC.x), 2) + pow((vertexB.y - vertexC.y), 2));
  return getTriangleArea(len1, len2, len3);
}

float getTriangleArea(float edge1, float edge2, float edge3) { //Compute the area of a triangle with Hero's formula
  float s = (edge1 + edge2 + edge3);
  return sqrt(s * (s - edge1) * (s - edge2) * (s - edge3));
}