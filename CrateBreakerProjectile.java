import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.awt.*;
import java.awt.event.*;

/**
 * CrateBreakerProjectile.java
 * 
 * ƒtienne Cossart
 * 
 * December 11, 2015
 * 
 * Defines a new animated GObject class for the projectiles
 * This is a subclass of CrateBreakerGame.java
 * It controls the projectiles which the tank fires
 * It creates both bullets and goop using GOvals
 * 
 * The only big change I made to this class was the black and green assignment
 * in the constructor. 
 * This class was less changed from the original design because the class itself
 * is simple and straight forward.
 */
public class CrateBreakerProjectile extends GCompound implements Runnable{ 

  private static final double 
    DELAY = 30;
  
  private double xSpeed, ySpeed;
  private CrateBreakerGame game;
  public boolean isAlive = true;
  private double size;
  private boolean isGoop = false;
  private CrateBreakerCrate crate;
  private CrateBreakerProjectile bomb;
  
  /** constructor */
  public CrateBreakerProjectile(double size, double xSpeed, double ySpeed, boolean isGoop, 
                                CrateBreakerGame game) {
    this.game = game;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    this.isGoop = isGoop;
    
    
    //construct both bullets and bombs
    GOval bomb = new GOval (-size / 2, -size / 2, size, size);
    add(bomb);
    bomb.setFilled(true);
    if (isGoop == true) {
      bomb.setFillColor(Color.GREEN);
    } else {
      bomb.setFillColor(Color.BLACK);
    }
  }
  
  /** run the animation of the projectile */
  public void run() {
    while(isAlive) {      
      oneTimeStep();
      pause(DELAY);
    }
    explode();
  }
  
  // move and check for collision
  private void oneTimeStep() {
    move(xSpeed,ySpeed);
    game.checkCollision(this, isGoop); // check hit anything
  }
  
  //explode ball
  public void explode(){
    isAlive = false;
    removeAll();
    } 
  }
