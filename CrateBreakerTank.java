import acm.graphics.*;
import acm.util.*;
import java.awt.*;
import java.applet.*;

/**
 * CrateBreakerTank.java
 * 
 * ƒtiene Cossart
 * 
 * December 11, 2015
 * 
 * This is a subclass of CrateBreakerGame.java
 * It draws the tank which is comprised of a GOval dome and body, 
 * an array of GRect for the treds and a GPolygon for the cannon
 * It moves the tank and sets its direciton
 * It rotates the cannon and fires using CrateBreakerProjectile projectiles
 * 
 * 
 * This class stayed close to what was orginally designed. 
 * The only big change was adding the fire() and fireGoop() methods.
 * This passed the information of whether the bomb was goop or not 
 * to the CrateBreakerProjectile.java class.
 * I tried to put the fire() and fireGoop() directly in 
 * the CrateBreakerGame.java keyPressed method.
 * I changed it back becasue it cuased more problems than before.
 */

public class CrateBreakerTank extends GCompound implements Runnable{
  private static final int TRACK_SECTION = 10;
  
  private static final double 
    INCRE = 0.2,
    DELAY = 20;
  
  private GOval body;
  private GPolygon[] leftBelt, rightBelt;
  private GOval dome;
  private GPolygon gun;
  private Color color;
  public double lastAngle, lastGunAngle, size;
  private double speed, angle; // polar speed and angle
  private int upDown, leftRight; // directions as determined by the game
  private CrateBreakerGame game;
  
  //private AudioClip fireSound = MediaTools.loadAudioClip("fire.wav");
  
  // constructor
  public CrateBreakerTank (double size, Color color, double speed, CrateBreakerGame game) {
    // save the parameters to instance vars
    this.color = color;
    this.size = size;
    this.speed = speed;
    this.game = game;
    
    // draw the belts
    leftBelt = drawBelt(-size, -size, size*2, size/2, 8);
    rightBelt = drawBelt(-size, size/2, size*2, size/2, 8);
    
    // draw the gun
    drawGun();
    
    // draw the body
    body = new GOval(size*1.2, size*1.2);
    body.setFilled(true);
    body.setFillColor(color);
    add(body, -size*0.6, -size*0.6);
    
    // draw the dome
    dome = new GOval(-size*0.2, -size*0.2, size*0.4, size*0.4);
    add(dome);
    dome.setFilled(true);
    dome.setFillColor(Color.GRAY);
  }
  
  // second constructor with location
  public CrateBreakerTank(double x, double y, double size, Color color, double speed, 
                          CrateBreakerGame game) {
    this(size, color, speed, game);
    setLocation(x, y);
  }
  
  // use animation to control the tank
  public void run() {
    while(true) {
      oneTimeStep();
      pause(DELAY);
      game.checkCollision(this);
    }        
  }
  
  // set the direction of the movement of the tank
  public void setDirection(int upDown, int leftRight) {
    this.upDown = upDown;
    this.leftRight = leftRight;
  }
  
  private void oneTimeStep() { 
    angle = GMath.angle(0, 0, leftRight, upDown);
    
    // move the tank according to the polar speed and angle
    if (leftRight!=0 || upDown!=0) moveTank();
    
    // set the angle of the gun
    setGunAngle();
  }
  
  // rotate the tank
  public void rotate(double a) {
    // rotate and left and right belts
    for (GPolygon piece : leftBelt) {
      piece.rotate(a);
    }
    for (GPolygon piece : rightBelt) {
      piece.rotate(a);
    }
  }
  
  // move the tank according to the plar speed and angle
  public void moveTank() { 
    setAngle(); // set the angle of the tank
    
    movePolar(speed,angle); // move the tank
    
    // change the color of the pieces in the belts 
    changeColor(leftBelt);
    changeColor(rightBelt);
  }
  
  // change the color of the belt
  private void changeColor(GPolygon[] belt) {
    // colors of the pieces alternate
    for (GPolygon piece : belt) { 
      if (piece.getFillColor().equals(Color.BLACK)) {
        piece.setFillColor(color);
      } else {
        piece.setFillColor(Color.BLACK);
      }
    }
  }
  
  // set the angle of the tank
  private void setAngle() {
    // rotate to align with the direction of movement
    rotate(angle-lastAngle);
    lastAngle = angle;
  }
  
  // set the angle of the gun
  private void setGunAngle() {
    // compute the angle from the tank to the mouse point
    double gunAngle = GMath.angle(getX(), getY(), 
                                  game.getMousePoint().getX(), 
                                  game.getMousePoint().getY());
    
    gun.rotate(gunAngle-lastGunAngle);
    lastGunAngle = gunAngle;
  }
  
  // draw a belt, (x, y) is upper left conor, n is the number of pieces
  private GPolygon[] drawBelt(double x, double y, 
                              double width, double height, 
                              int n) {
    GPolygon[] belt = new GPolygon[n];
    // compute the width of each piece 
    double pieceWidth = width/n;
    
    // use a for loop to draw pieces of the belt
    for (int i = 0; i< n; i++) {
      GPolygon piece = new GPolygon();
      piece.addVertex(x + i*pieceWidth, y);
      piece.addVertex(x + (i+1)*pieceWidth, y);
      piece.addVertex(x + (i+1)*pieceWidth, y+height);
      piece.addVertex(x + i*pieceWidth, y+height);
      piece.setFilled(true);
      
      // set color of the piece alternatively
      if (i%2 == 0) {
        piece.setFillColor(color);
      } else {
        piece.setFillColor(Color.black);
      }
      
      add(piece);
      belt[i] = piece;
    }
    return belt;
  }
  
  // fire the gun
  public void fire() {
    
    
    double xSpeed = 30*GMath.cosDegrees(lastGunAngle);
    
    double ySpeed = -30*GMath.sinDegrees(lastGunAngle);
    
    CrateBreakerProjectile bomb = new CrateBreakerProjectile(10, xSpeed, ySpeed, false, game);
    game.add(bomb, getX(), getY());
    // move the shell to the tip of the gun
    
    bomb.movePolar(size*1.5, lastGunAngle);

    new Thread(bomb).start();
    System.out.println("tank.fire()" );
  }
  
    public void fireGoop() {
    
    double xSpeed = 30*GMath.cosDegrees(lastGunAngle);
    
    double ySpeed = -30*GMath.sinDegrees(lastGunAngle);
    
    CrateBreakerProjectile bomb = new CrateBreakerProjectile(10, xSpeed, ySpeed, true, game);
    game.add(bomb, getX(), getY());
    // move the shell to the tip of the gun
    bomb.movePolar(size*1.5, lastGunAngle);
    
    new Thread(bomb).start();
    System.out.println("tank.fireGoop()" );
  }
  
  // draw the tank gun
  private void drawGun() {
    gun = new GPolygon();
    
    gun.addVertex(size*1.5, size/8);
    gun.addVertex(0, size/8);
    gun.addVertex(0, -size/8);
    gun.addVertex(size*1.5, -size/8);    
    
    add(gun);
    gun.setFilled(true);
    gun.setFillColor(color);
  }
} 
