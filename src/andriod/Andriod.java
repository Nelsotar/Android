/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andriod;

import becker.robots.*;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

/**
 *Class for a maze-crawling robot called Android.
 * @author TN
 */
public class Andriod extends Robot{
    
    Stack<Direction> validDirections;
    ArrayList<Direction> finalDirections;
    Stack<int[]> hasVisited;
    Andriod realAndriod;
    
    /**
     * 
     * @param city The city the android will be in.
     * @param street Initial street the android is on.
     * @param avenue Initial avenue the android is on.
     * @param d Initial direction the android is facing.
     */
    public Andriod(City city, int street, int avenue, Direction d){
        super(city,street,avenue,d);
    }
    
    /**
     * First invisible android (probe) crawls through maze and finds direct directions to
     * destination. Then creates a second visible android that goes directly to
     * destination.
     * @param street Street to navigate to.
     * @param avenue Avenue to navigate to.
     */
    public void solveMaze(int street, int avenue){
        validDirections = new Stack<>();
        finalDirections = new ArrayList<>();
        hasVisited = new Stack<>();
        setTransparency(1.0);
        setSpeed(1000);
        realAndriod = new Andriod(getCity(),getStreet(), getAvenue(), getDirection());
        
        if(probe(street, avenue)){
            finishMaze();
            realAndriod.setColor(Color.blue);
            System.out.println("Destination reached.");
        }
        else{
            System.out.println("Destination cannot be reached.");
        }
    }
    
    /**
     * Leads the visible android through the maze with directions collected from
     * probe.
     */
    private void finishMaze(){
        while(!finalDirections.isEmpty()){
           realAndriod.moveDirection(finalDirections.remove(0));
        }
        if(realAndriod.canPickThing()){
            realAndriod.pickThing();
        }
    }
    
    /**
     * Allows probe to crawl through maze, collects direct directions to destination.
     * @param street Street to navigate to.
     * @param avenue Avenue to navigate to.
     * @return boolean indicating success or failure.
     */
    private boolean probe(int street, int avenue){
        Direction nextDirection;
        boolean backTrack = false;
        while(!isAtDestination(street, avenue)){     
            if(!isVisited()){  
                hasVisited.push(new int[]{getStreet(), getAvenue()});  
                getValidDirections();
            }
            
            if(validDirections.isEmpty()){
                return false;
            }   
            
            nextDirection = validDirections.pop();
            backTrack = isNextVisited(nextDirection);
  
            if(backTrack){
               if(finalDirections.size() > 0){
                   finalDirections.remove(finalDirections.size() - 1);
               }
            }
            else{
                finalDirections.add(nextDirection);
            }
            moveDirection(nextDirection);
            
        }
        return true;
    }
    
    /**
     * Takes in a direction and returns a boolean indicating whether the next 
     * space in that direction has been visited or not.
     * @param d Direction probe is heading in next.
     * @return boolean indicating if next spot has been visited on not.
     */
    private boolean isNextVisited(Direction d){
        int street = getStreet();
        int avenue = getAvenue();
        switch (d) {
            case NORTH:
                return isVisited(street - 1, avenue);
            case SOUTH:
                return isVisited(street + 1, avenue);
            case EAST:
                return isVisited(street, avenue + 1);
            default:
                return isVisited(street, avenue - 1);
        }
    }
    
    /**
     * Searches array of visited coordinates and returns if current coordinates 
     * have been visited or not.
     * @return boolean indicating if visited or not.
     */
    private boolean isVisited(){      
        return containsArray(getStreet(), getAvenue());
    }
    
    /**
     * Searches array of visited coordinates and returns if provided coordinates 
     * have been visited or not.
     * @param street Street part of coordinate to check,
     * @param avenue Avenue part of coordinate to check.
     * @return boolean indicating if visited or not.
     */
    private boolean isVisited(int street, int avenue){      
        return containsArray(street, avenue);
    }
    
    /**
     * Iterates through the hasVisited array and returns if coordinates are a 
     * match for any entry.
     * @param street Street part of coordinate to check,
     * @param avenue Avenue part of coordinate to check.
     * @return boolean indicating if entry in array is matched.
     */
    private boolean containsArray(int street, int avenue){
        if(!hasVisited.isEmpty()){
            for(int[] element: hasVisited){
                if(element[0] ==  street && element[1] == avenue){
                 return true;
                }   
            }
        }   
        return false;
    }
    
    /**
     * Moves android in provided direction.
     * @param d Direction to move in.
     */
    private void moveDirection(Direction d){
        faceDirection(d);
        move();       
    }
    
    /**
     * Checks all directions an android may move, pushes clear paths into 
     * validDirections stack.
     */
    private void getValidDirections(){
        
        turnLeft();
        turnLeft();
        if(frontIsClear()){
            validDirections.push(getDirection());
        }
        turnRight();
        if(frontIsClear()){
            validDirections.push(getDirection());
        }
        turnLeft();
        turnLeft();
        if(frontIsClear()){
            validDirections.push(getDirection());
        }
        turnLeft();
        if(frontIsClear()){
            validDirections.push(getDirection());
        }
    }
    
    /**
     * Determines if the android is at its destination.
     * @param street Destination street
     * @param avenue Destination avenue
     * @return boolean indicating if android is at destination.
     */
    private boolean isAtDestination(int street, int avenue){
        return getStreet() == street && getAvenue() == avenue;
    }
    
    /**
     * Turns the android to the right.
     */
    public void turnRight(){
        turnLeft();
        turnLeft();
        turnLeft();
    }
    
    /**
     * Moves the android forward, but only if the way is clear.
     */
    @Override
    public void move(){
        if(frontIsClear()){
            super.move();
        }
    }
    
    /**
     * Turns the android until it faces East.
     */
    public void faceEast(){
      while(getDirection() != Direction.EAST){
         turnLeft(); 
      }  
    }

    /**
     * Turns the android until it faces West.
     */
    public void faceWest(){
      while(getDirection() != Direction.WEST){
         turnLeft(); 
      }  
    }

    /**
     * Turns the android until it faces North.
     */
    public void faceNorth(){
      while(getDirection() != Direction.NORTH){
         turnLeft(); 
      }  
    }
    
    /**
     * Turns the android until it faces South.
     */
    public void faceSouth(){
      while(getDirection() != Direction.SOUTH){
         turnLeft(); 
      }  
    }
    
    /**
     * Turns the android until it faces the specified direction.
     * @param d Direction for android to face.
     */
    public void faceDirection(Direction d){
        switch (d) {
            case NORTH:
                faceNorth();
                break;
            case SOUTH:
                faceSouth();
                break;
            case EAST:
                faceEast();
                break;
            case WEST:
                faceWest();
                break;
            default:
                break;
        }
    }
}
