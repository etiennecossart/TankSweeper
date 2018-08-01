import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
  * CrateBreakerCrate.java
  * 
  * ƒtienne Cossart
  * 
  * December 11, 2015
  * 
  * This is a subclass of CrateBreakerGame.java
  * It draws the crates, controls the open method and goop method.
  * It also controls the explosion of crates and makes the open cells 
  * gradient colors to warn player of nearby mines
  * 
  * 
  * I added a newHint variable which changes a -1 (bomb) to a 0 if its hit by goop.
  * The newHint is passed to the main class which iterates through the array the hints
  * using the newHint. 
  * After recalulating the hints, it passes back to the new hints to the Crate class.
  * The setHint method iterates throught the crate array to see if the crate is open.
  * If it is open, it updates the color of the crates using the updateColor() method.
  * 
  * I also added a color coordination for each hint value when the cell is open
  * for aesthetic purposes :)
  * */

public class CrateBreakerCrate extends GCompound {
  //instance viariables 
  public boolean isMine, isOpen = false;
  private GImage crate; 
  private GRect cell;
  private int row, col, hint, newHint;
  private CrateBreakerGame game;
  private GLabel hintNum;
  
  private static final Color
    //defining colors for the cells
    YELLOW1 = new Color(255, 255, 0),
    GOLD1 = new Color(255, 215, 0),
    ORANGE1 = new Color(255, 165, 0),
    DARKORANGE1 = new Color(255, 127, 0),
    ORANGERED1 = new Color(255, 69, 0),
    BROWN1 = new Color(255, 64, 64),
    RED1 = new Color(255, 0, 0),
    RED2 = new Color(205, 0, 0);
  
  
  public CrateBreakerCrate (int hint, int row, int col, CrateBreakerGame game){
    //save parameters to instance variables
    this.row = row;
    this.col = col;
    this.hint = hint;
    this.game = game;
    
    //create GRect
    cell = new GRect(-25, -25, 50, 50);
    cell.setFilled(true);
    cell.setFillColor(Color.GRAY);
    add(cell);
    
    //crate image
    crate = new GImage ("crate.gif");
    add (crate, -25, -25);
    
    //add the number hints
    hintNum = new GLabel (Integer.toString(hint), 25, 25);
    add(hintNum, -6.75, 6.75);
    hintNum.setVisible(false);
  }
  
  //return isOpen
  public boolean isOpen(){
    return isOpen;
  }
  
  //Controls if crate is opened with normal bomb
  public void open(){
    if (isOpen()) {
      return; 
    } else if (isOpen() == false) { //if crate is not open
      
      //if the crate opened is a bomb (-1), explode
      if (hint == -1) {
        explode();
        hintNum.setVisible(true);
        remove(crate);
        game.gameOver(); //ends game
      }
     
      //if the crate opened is 1, remove crate image, color cell YELLOW1
      if (hint == 1) {
        remove(crate); 
        cell.setFillColor(YELLOW1); 
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if the crate opened is 2, remove crate image, color cell GOLD1
      if (hint == 2) {
        remove(crate);
        cell.setFillColor(GOLD1);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
    
      //if the crate opened is 3, remove crate image, color cell ORANGE1
      if (hint == 3) {
        remove(crate);
        cell.setFillColor(ORANGE1);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if the crate opened is 4, remove crate image, color cell DARKORANGE1
      if (hint == 4) {
        remove(crate);
        cell.setFillColor(DARKORANGE1);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if the crate opened is 5, remove crate image, color cell ORANGERED1
      if (hint == 5) {
        remove(crate);
        cell.setFillColor(ORANGERED1);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if the crate opened is 6, remove crate image, color cell BROWN1
      if (hint == 6) {
        remove(crate);
        cell.setFillColor(BROWN1);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if the crate opened is 7, remove crate image, color cell RED1
      if (hint == 7) {
        remove(crate);
        cell.setFillColor(RED1);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if the crate opened is 8, remove crate image, color cell RED2
      if (hint == 8) {
        remove(crate);
        cell.setFillColor(RED2);
        hintNum.setVisible(true);
        isOpen = true;
        return;
      }
     
      //if crate opened is 0, it clears crate and explores neighboring ones
      if (hint == 0) {
        isOpen = true;
        hintNum.setVisible(false);
        game.explore(row, col);//calls explore method when cell is opened
        remove(crate);
        remove(cell);
        return;
      }
    }
  }
  
  public void openGoop(){
    if (isOpen()) {
      return;
      
    } else if (isOpen() == false) {
      
      //if crate opened with goop is -1, resets crate hint and explores neighbors
      if (hint == -1) {
        remove(crate);
        newHint = 0;
        isOpen = true;
        game.resetHint(newHint, row, col);
        game.scoreCounter();
        return;
      }
    }
    
    //if the crate opened with goop is 1, remove crate image, color cell YELLOW1
    if (hint == 1) {
      remove(crate);
      cell.setFillColor(YELLOW1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 2, remove crate image, color cell GOLD1
    if (hint == 2) {
      remove(crate);
      cell.setFillColor(GOLD1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 3, remove crate image, color cell ORANGE1
    if (hint == 3) {
      remove(crate);
      cell.setFillColor(ORANGE1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened is 4 with goop, remove crate image, color cell DARKORNAGE1
    if (hint == 4) {
      remove(crate);
      cell.setFillColor(DARKORANGE1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 5, remove crate image, color cell ORANGERED1
    if (hint == 5) {
      remove(crate);
      cell.setFillColor(ORANGERED1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 6, remove crate image, color cell BROWN1
    if (hint == 6) {
      remove(crate);
      cell.setFillColor(BROWN1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 7, remove crate image, color cell RED1
    if (hint == 7) {
      remove(crate);
      cell.setFillColor(RED1);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 8, remove crate image, color cell RED2
    if (hint == 8) {
      remove(crate);
      cell.setFillColor(RED2);
      hintNum.setVisible(true);
      isOpen = true;
      return;
    }
    
    //if the crate opened with goop is 0, remove crate, no exploring though
    if (hint == 0) {
      isOpen = true;
      hintNum.setVisible(false);
      remove(crate);
      remove(cell);
      return;
    }
  }
  
  //updates hint and images of crates after goop exploration
  public void setHint(int hint){
    this.hint = hint;
    //add the number hints
    remove(hintNum);
    hintNum = new GLabel (Integer.toString(hint), 25, 25);
    add(hintNum, -4, 8);
    
    //if updated hint is 0 or crate is unopened, the hint is not visible
    if(hint == 0 || isOpen == false){
      hintNum.setVisible(false);
    
    }else{
      //if updated hint is not 0 and is opened, the hint is visible to user
      hintNum.setVisible(true);
    } 
    
    //after udated hint, update cell color
    updateColor();
  }
  
  //udate color of cell after hint update from goop exploration
  public void updateColor() {
    //if updated hint is 1, cell color is YELLOW1
    if (hint == 1) {
      cell.setFillColor(YELLOW1);
      return;
    }
    
    //if updated hint is 2, cell color is GOLD1
    if (hint == 2) {
      cell.setFillColor(GOLD1);
      return;
    }
    
    //if updated hint is 3, cell color is ORANGE1
    if (hint == 3) {
      cell.setFillColor(ORANGE1);
      return;
    }
    
    //if updated hint is 4, cell color is DARKORNAGE1
    if (hint == 4) {
      cell.setFillColor(DARKORANGE1);
      return;
    }
    
    //if updated hint is 5, cell color is ORANGERED1
    if (hint == 5) {
      cell.setFillColor(ORANGERED1);
      return;
    }
    
    //if updated hint is 6, cell color is BROWN1
    if (hint == 6) {
      cell.setFillColor(BROWN1);
      return;
    }
    
    //if updated hint is 7, cell color is RED1
    if (hint == 7) {
      cell.setFillColor(RED1);
      return;
    }
    
    //if updated hint is 8, cell color is RED2
    if (hint == 8) {
      cell.setFillColor(RED2);
      return;
    }
    //if updated hint is 0, clear crate, explore
    if (hint == 0) {
      isOpen = true;
      hintNum.setVisible(false);
      game.explore(row, col);
      remove(crate);
      remove(cell);
      return;
    }
  }
  
  //controls bomb cell opened graphic (nuclear explosion! BADABOOOOM!!!!)
  public void explode(){
    //display GImage explosion
    GImage explosion = new GImage ("nuke.gif");
    add (explosion, -25, -25);
    game.gameOver = true;
  }
}