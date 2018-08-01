import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;
import java.util.*;


public class CrateBreakerEnemy extends GCompound implements Runnable {
  //Constance
  private static final double
    DELAY = 20;
  
  //instance variables
  private double size, xSpeed, ySpeed;
  private boolean isAlive = true;
  private CrateBreakerGame game;
  private CrateBreakerEnemy enemy;
  
  //constructor for balls
  public CrateBreakerEnemy(double size, double xSpeed, double ySpeed, 
                      CrateBreakerGame game) {
    //set parameeters through instance variables
    this.size = size;
    this.xSpeed = xSpeed;
    this.ySpeed = ySpeed;
    this.game = game;
    
    
      GImage bee = new GImage ("enemy.gif");
      add(enemy);
    } 
  
  public void run() {
    while(isAlive == true){
      //animate bomb/bullets
      oneTimeStep();
      //pause between frames
      pause(DELAY);
    }
    explode();//explode after animation ends
  }
  
  public boolean isAlive() {
    return isAlive; 
  }
  
  public double getXSpeed() {
    return xSpeed;
  }
  
  public double getYSpeed() {
    return ySpeed;
  }
   
  public double setXSpeed (double x) {
    xSpeed = -xSpeed;
    return xSpeed;
  }
  
  public double setYSpeed (double y) {
    ySpeed = -ySpeed;
    return ySpeed;
  }
  
  private void oneTimeStep() {
    move(xSpeed, ySpeed);
    //game.checkCollision(this);
  }
  
   public void die() {
    isAlive = false;
    removeAll();
  }
   
  public void explode(){
    removeAll();
}
  }
