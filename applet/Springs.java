import processing.core.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; import javax.sound.midi.*; import javax.sound.midi.spi.*; import javax.sound.sampled.*; import javax.sound.sampled.spi.*; import java.util.regex.*; import javax.xml.parsers.*; import javax.xml.transform.*; import javax.xml.transform.dom.*; import javax.xml.transform.sax.*; import javax.xml.transform.stream.*; import org.xml.sax.*; import org.xml.sax.ext.*; import org.xml.sax.helpers.*; public class Springs extends PApplet {//import processing.opengl.*;

int numSprings = 2; 
Spring[] springs = new Spring[numSprings]; 

public void setup()
{
  //size(1280, 1024, OPENGL);
  size(800, 600);
  //noStroke(); 
  noFill();
  smooth();

/*
 * Spring(x, y, size, damping, mass, spring_constant, trail_length)
 *
 * x is starting x-coordinate
 * y is starting y-coordinate
 * size is diameter of ellipse
 * damping affects by how much the velocity reduces for each oscillation (should be less than 1)
 * higher mass means slower acceleration/deceleration
 * spring_constant affects damping force, which affects acceleration/deceleration (damping force is also influenced by current velocity)
 * (should spring_constant be the same for all objects?)
 */

  springs[0] = new Spring(0, 0, 30, 0.98f, 24.0f, 0.1f, 60); 
  springs[1] = new Spring(0, 0, 30, 0.98f, 20.0f, 0.1f, 60); 
}

public void draw() 
{
  background(51); 
  
  for (int i=0; i<numSprings; i++) { 
    springs[i].update(); 
    springs[i].display(); 
  }  
}

class Spring 
{ 
  // Screen values 
  float tempxpos, tempypos; 
  int size = 20; 

  // Spring simulation constants 
  float mass;       // Mass 
  float k = 0.2f;    // Spring constant 
  float damp;       // Damping 
  float rest_posx;  // Rest position X  // x position ball will eventually come to rest at
  float rest_posy;  // Rest position Y  // y position ball will eventually come to rest at

  // Spring simulation variables 
  float velx = 0.0f;   // X Velocity 
  float vely = 0.0f;   // Y Velocity 
  float accel = 0;    // Acceleration 
  float force = 0;    // Force 

  int trail = 60;
  float mx[] = new float[trail];
  float my[] = new float[trail];

  // Constructor
  // Spring(x, y, size, damping, mass, spring_constant, trail_length)
  Spring(float x, float y, int s, float d, float m, float k_in, int t) 
  { 
    tempxpos = x;
    tempypos = y;
    rest_posx = x;
    rest_posy = y;
    size = s;
    damp = d; 
    mass = m; 
    k = k_in;
    trail = t;
  } 

  public void update() 
  { 
    rest_posy = mouseY; 
    rest_posx = mouseX;

    force = -k * (tempypos - rest_posy);  // f=-ky 
    accel = force / mass;                 // Set the acceleration, f=ma == a=f/m 
    vely = damp * (vely + accel);         // Set the velocity 
    tempypos = tempypos + vely;           // Updated position 

    force = -k * (tempxpos - rest_posx);  // f=-kx 
    accel = force / mass;                 // Set the acceleration, f=ma == a=f/m 
    velx = damp * (velx + accel);         // Set the velocity 
    tempxpos = tempxpos + velx;           // Updated position 
  } 
  
  public void display() 
  { 
    // Reads throught the entire array
    // and shifts the values to the left
    for(int i=1; i<trail; i++) {
      mx[i-1] = mx[i];
      my[i-1] = my[i];
    } 
    // Add the new values to the end of the array
    mx[trail-1] = tempxpos;
    my[trail-1] = tempypos;

    for(int i=0; i<trail; i++) {
      //fill(255, 255 * i / (trail-1)); 
      stroke(255, 255 * i / (trail-1)); 
      ellipse(mx[i], my[i], size * (i+1) / trail, size * (i+1) / trail);
    }
  } 
} 

  static public void main(String args[]) {     PApplet.main(new String[] { "Springs" });  }}