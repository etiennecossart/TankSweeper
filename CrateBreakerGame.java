import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;

/**
 * CrateBreakerGame.java
 * 
 * Novemeber 24, 2015
 * 
 * ƒtienne Cossart
 * 
 * CrateBreaker: 
 * Drive the tank and fire into the crates. 
 * But, WATCH OUT! Some crates have bombs!! 
 * Goop the bombs and destroy the boxes so your convoy can make it through. 
 * You only have limited amount of goop so don't waste it
 * Goop bombs do not open up neighboring boxes however, only normal bombs.
 * 
 * This is the main class of the CrateBreaker files
 * It draws the graphics, sets up the minefield, initializes the tank,
 * contorols the tank, and contorols the minefield.
 * To drive tank, player uses W,A,S,D for up, left, down, and right.
 * Player aims the cannon with the mouse.
 * Player fires bombs by pressing E, and goop by pressing Q.
 * 
 * 
 * 
 * I changed the firing of bombs from the mouse to the keys. 
 * Originally, my program would shout 3 bombs simultaniously. 
 * This would also double the value of cells at random times.
 * To aliviate the problem, I changed it to the key controled.
 * I later found that the bug was caused because I had 2 mouseListeners() method
 * one in the run() and one in the startGame() which was imbedded in the run().
 * 
 * I added a score counter which records whenever a crate is opened. 
 * When the counter has recorded that all the crate are opened and 0,
 * the game is over and the player has won
 * 
 * I added a resetHint() method which, when a goop opens a bomb crate,
 * it resets the hint to 0 instead of -1 and passes it to the main class.
 * The main class reassigns the hint and recalculates the neighboring hints.
 * It passes the updated hints back to the CrateBreakerCrate class.
 * The crate class updates the colors and determines if the former bomb's hint is 0
 * in which case it explores the neighbors, or else it opens it with the reassiged hint.
 **/

public class CrateBreakerGame extends GraphicsProgram {
//constants 
  public static int
    APPLICATION_WIDTH = 650,
    APPLICATION_HEIGHT = 680,
    ROW = 7,
    COL = 7;
  
  private static final double 
    WIDTH = APPLICATION_WIDTH,
    HEIGHT = APPLICATION_HEIGHT,
    TANK_SIZE = 17; 
  
  private static final Color
    TAN = new Color(255, 236, 139),
    BROWN = new Color(139, 90, 0);
  
//instance variables
  private CrateBreakerCrate crate[][] = new CrateBreakerCrate[ROW][COL];
  private int[][] hint = new int[7][7];
  public boolean gameOver = false, gameWon = false;
  private RandomGenerator rand = new RandomGenerator();
  private int countMine = 0;
  private CrateBreakerCrate CrateBreakerCrate;
  private int score = 8, goopRemain = 8;
  private GLabel guessCount, ammo;
  private CrateBreakerTank tank;
  private boolean isStop = true;
  private GLine aimLine;
  private GPoint lastPoint;
  private GLabel message;
  private int up, down, left, right; // indicators of the movement and rotation
  private int cratez = 0, cam = 0;
  
  // set up the scene
  public void init() {
    addMouseListeners(); 
  }
  
  public void run() {
    addMouseListeners(); 
    addKeyListeners(); 
    drawGraphics();
    isStop = false; 
    // create a line to point the gun of the tank
    aimLine = new GLine(tank.getX(), tank.getY(), tank.getX(), tank.getY());
    
    new Thread(tank).start(); //create a line to point the gun of the tank
  }
  
  //initializes graphics
  public void drawGraphics(){
    //draw background
    setBackground(TAN);
    
    for (int i = 0; i < 1200; i++) {
      // location of the gravel
      double x = rand.nextDouble(0, 650);
      double y = rand.nextDouble(0, 650);
      
      double size = rand.nextDouble(1,5); 
      
      //draws sandy gravel
      GOval gravel = new GOval(x, y, size, size);
      add(gravel);
      gravel.setFilled(true);
      gravel.setColor(BROWN);
    }
    
    //create hint[][]
    for (int a = 0; a < 7; a++) {
      for (int b = 0; b < 7; b++) {
        hint[a][b] = getRand();
      }
    }
    //check neighbors of cell and calculating hint
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        if (hint[i][j] == -1){ //if bomb
          for (int k = -1; k < 2; k++) {
            for (int l = -1; l < 2; l++) {
              if (inBound(i + k, j + l) && hint[i + k][j + l] != -1) {
                hint[i + k][j + l] = hint[i + k][j + l] + 1; 
              }
            }
          }
        }
      }
    }
    
    //create crate
    for (int e = 0; e < 7; e++) {
      for (int f = 0; f < 7; f++) {
        crate[e][f] = new CrateBreakerCrate(hint[e][f], e, f, this);
        add(crate[e][f], 100*e+25, 100*f+25); //draws the cells
      }
    }
    
    //draws vertical lines
    for (int g = 0; g < 7; g++){
      GLine vert = new GLine (g, 0, g, 0);
      add (vert);
      vert.setColor(Color.BLACK);
    }
    
    //draws Horizontal lines
    for (int h = 0; h < 7; h++){
      GLine hor = new GLine (h, 0, h, 0);
      add (hor);
      hor.setColor(Color.BLACK);
    }
    
    
    //adds the goop counter
    ammo = new GLabel ("Goop Ammo:" + goopRemain); //displays message
    ammo.setFont(new Font("Sanserif", Font.BOLD, 20));
    ammo.setColor (Color.BLACK);
    add (ammo, 460, 670);
    
    //calls upon tank class to draw tank
    tank = new CrateBreakerTank(50, 50, TANK_SIZE, Color.YELLOW, 3, this);
    add(tank, 75, 75); 
    lastPoint = new GPoint(200, 300);
  }
  
  //checks collision of bomb with crates
  public void checkCollision (CrateBreakerProjectile bomb, boolean isGoop){
    if (gameOver == true) return;//when gameOver, click to restart
    
    //if the bomb is not goop, open()
    if (isGoop == false){
      for (int e = 0; e < 7; e++) {
        for (int f = 0; f < 7; f++) {
          if(crate[e][f].isOpen() == false){
            if (bomb.getBounds().intersects(crate[e][f].getBounds())) {
              bomb.explode();
              crate[e][f].open();//open cell
            }
          }
        }
      }
    }else { //if bomb is goop, openGoop()
      for (int e = 0; e < 7; e++) {
        for (int f = 0; f < 7; f++) {
          if(crate[e][f].isOpen() == false){
            if (bomb.getBounds().intersects(crate[e][f].getBounds())) {
              bomb.explode();
              crate[e][f].openGoop();//open cell
            }
          }
        }
      }
    }
  }
  
  ///checks for the bound of the cell
  public boolean inBound(int s, int t) {
    return s >= 0 && s < ROW && t >= 0 && t< COL;
  }
  
  //checks neighbors of opened cell
  public void explore(int row, int col){
    for (int u = -1; u < 2; u++) {
      for (int v = -1; v < 2; v++) {
        if (inBound ((row + u), (col + v))) {
          crate[row + u][col + v].open();
          if (hint[row + u][col + v] == -1) return;
        }
      }
    }
  }
  
  //iterates random numbers for bombs
  private int getRand(){
    while (countMine < 15){
      int d = rand.nextInt(0, 3);//each number has 10% chance of being -1
      if (d == 0){
        countMine++;
        return -1;
      } else {
        return 0; 
      }
    }
    return 0; //after reaching bomb count, all other cells are 0
  }
  
  
  /// update the line
  public void mouseMoved(GPoint point) {
    if (isStop) return;
    lastPoint = point;
  }
  
  // return the point of the mouse
  public GPoint getMousePoint() {
    return lastPoint;  
  }
  
  // set the starting point of the line
  public void setLine(double x, double y) {
    aimLine.setStartPoint(x,y);
  }
  
  // turn on the indicator if the key associated with it is pressed
  public void keyPressed(KeyEvent e) {
    if (gameOver == true) return;
    if (e.getKeyCode() == KeyEvent.VK_W) up = 1;    //W drives tank up 
    if (e.getKeyCode() == KeyEvent.VK_S) down = 1;  //S drives tank down  
    if (e.getKeyCode() == KeyEvent.VK_A) left = 1;  //A drives tank left  
    if (e.getKeyCode() == KeyEvent.VK_D) right = 1; //D drives tank right
    
    // set the direction of the tank
    tank.setDirection(down - up, right - left);
    
    //E fires bomb
    if (e.getKeyCode() == KeyEvent.VK_E) { 
      tank.fire();
    }
    
    //if player still has ammo
    if (goopRemain == 0) return;
    if (e.getKeyCode() == KeyEvent.VK_Q) {
      tank.fireGoop(); //Q fires goop
      updateGoopCount();
    }
  }
  
  // turn off the indicator if the key associated with it is released
  public void keyReleased(KeyEvent e) {    
    if (e.getKeyCode() == KeyEvent.VK_W) up = 0;    
    if (e.getKeyCode() == KeyEvent.VK_S) down = 0;    
    if (e.getKeyCode() == KeyEvent.VK_A) left = 0;    
    if (e.getKeyCode() == KeyEvent.VK_D) right = 0;
    
    
    // set the direction of the tank
    tank.setDirection(down - up, right - left);
  }
  
  private void updateGoopCount(){
    //udates goop label
    goopRemain--;
    ammo.setLabel ("Goop Ammo:" + goopRemain); 
  }
  
  //checks collision between crates and tank
  public void checkCollision(CrateBreakerTank tank){
    for (int e = 0; e < 7; e++) {
      for (int f = 0; f < 7; f++) {
        if(crate[e][f].isOpen() == false){ //if crates are closed, collide
          
          //x axis distance between tank and crate
          double xDist = tank.getX() - crate[e][f].getX();
          
          //x axis minimum distance possible between tank and crate
          double xSize = TANK_SIZE + crate[e][f].getWidth()  / 2;
          
          //x axis offset of tank and crate in both directions
          double xOffset = xSize - Math.abs(xDist);
          
          //y axis distance between tank and crate
          double yDist = tank.getY() - crate[e][f].getY();
          
          //y axis minimum distance possible between tank and crate
          double ySize = TANK_SIZE + crate[e][f].getHeight() / 2;
          
          //y axis offset of tank and crate in both directions
          double yOffset = ySize - Math.abs(yDist);
          
          if (xOffset > 0 && yOffset > 0) {
            if (up == 1 || down == 1) //move tank in y direction
              tank.move(0, yOffset * yDist / Math.abs(yDist));
            
            if (left == 1 || right == 1) //move tank in x direction
              tank.move(xOffset * xDist / Math.abs(xDist), 0);
          }
        }
      }
    }
  }
  
  //resets hint after goop has opened bomb crate
  public void resetHint(int newHint, int row, int col){
    hint[row][col] = newHint;
    //reassigns hints to 0 and -1
    for (int e = 0; e < 7; e++) {
      for (int f = 0; f < 7; f++) {
        if(hint[e][f] > -1){
          hint[e][f] = 0;
        }
      }
    }
    
    //recalculates hint
    for (int i = 0; i < ROW; i++) {
      for (int j = 0; j < COL; j++) {
        if (hint[i][j] == -1){ //if bomb
          for (int s = -1; s < 2; s++) {
            for (int t = -1; t < 2; t++) {
              if (inBound(i + s, j + t) && hint[i + s][j + t] != -1) {
                hint[i + s][j + t] = hint[i + s][j + t] + 1;
              }
            }
          }
        }
      }
    }
    //passes new hint to CrateBreakerCrate class
    for (int q = 0; q < 7; q++) {
      for (int r = 0; r < 7; r++) {
        crate[q][r].setHint(hint[q][r]);
      }
    }
  }
  
  //records how many bombs are left
  public void scoreCounter(){
    score--;
    System.out.println(score);
    if (score == 0) {
      gameWon();
      return;
    }
  }
  
  
  //Loses game
  public void gameOver() {
    gameOver = true;//stops game
    GLabel label = new GLabel ("Fatality"); //displays message
    label.setFont(new Font("Sanserif", Font.BOLD, 160));
    label.setColor (Color.RED);
    add (label, 325 - label.getWidth()/2, 360);
  }
  
  //Loses game
  public void gameWon() {
    gameOver = true;//stops game
    GLabel congrats = new GLabel ("Good Job Soldier"); //displays message
    congrats.setFont(new Font("Sanserif", Font.BOLD, 60));
    congrats.setColor (Color.GREEN);
    add (congrats, 325 - congrats.getWidth()/2, 260);
    
    GLabel label = new GLabel ("Perimeter Cleared"); //displays message
    label.setFont(new Font("Sanserif", Font.BOLD, 60));
    label.setColor (Color.GREEN);
    add (label, 325 - label.getWidth()/2, 460);
  }
}