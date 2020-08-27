/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andriod;

import becker.robots.Direction;
import becker.robots.MazeCity;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

/**
 *Controller for FXML document. Contains button click handlers and supporting methods.
 * @author TN
 */
public class FXMLDocumentController implements Initializable{
    
    private Label label;
    @FXML
    private Label lblMazeAvenues;
    @FXML
    private Label lblStreet;
    @FXML
    private Label lblAvenue;
    @FXML
    private Label lblMazeStreets;
    @FXML
    private Label lblMazeSize;
    @FXML
    private Label lblNav;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnExit;
    @FXML
    private Text txtTitle;
    @FXML
    private Text txtDesc1;
    @FXML
    private Text txtDesc2;
    @FXML
    private Text txtDesc3;
    @FXML
    private Text txtMessage;
    @FXML
    private TextField txtfAvenue;
    @FXML
    private TextField txtfStreet;
    @FXML
    private TextField txtfMazeStreet;
    @FXML
    private TextField txtfMazeAvenue;
    @FXML
    private AnchorPane rootPane;
    private MazeCity city;
    private Andriod data;
    private int streetNum;
    private int avenueNum;
    private int mazeStreetNum;
    private int mazeAvenueNum;
    private Thread t;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    /**
     * Checks validity of user inputs and launches Maze/Android if valid. Launches 
     * Maze/Android in a separate thread.
     * @param event 
     */
    @FXML
    private void handleStart(ActionEvent event){
        if(isValidInputs()){ 
            txtMessage.setText("Navigating to destination.");
            t = new Thread(new Runnable() {
               @Override
                public void run(){
                    freezeDestinationInputs();
                    launchAndriod();
                    unfreezeDestinationInputs();
                }
            });
            t.start();  
        }
    }
    
    /**
     * Tests all user inputs to see if they are valid.
     * @return boolean showing if inputs are valid or not.
     */
    private boolean isValidInputs(){
        TextField[] txtFields = new TextField[4];
        txtFields[0] = txtfStreet;
        txtFields[1] = txtfAvenue;
        txtFields[2] = txtfMazeAvenue;
        txtFields[3] = txtfMazeStreet;
        
        //Checks to see if text values are too long before attempting parsing.
        for(TextField field : txtFields){
            if(field.getText().length() > 3){
                setMessage("Invalid input.");
                return false;
            }
        }
        
        boolean success = parseInputs();
        setMessage();
        
        return success && !((streetNum < 0 || streetNum > mazeStreetNum - 1) || (avenueNum < 0 || avenueNum > mazeAvenueNum - 1) || (mazeAvenueNum > 80 || mazeAvenueNum < 2) || (mazeStreetNum > 80 || mazeStreetNum < 2));
    }
    
    /**
     * Attempts to parse user inputs as integers.
     * @return Whether all inputs could be parsed or not.
     */
    private boolean parseInputs(){
        boolean success = true;
        if(txtfAvenue.getText().matches("\\d+")){
           avenueNum = Integer.parseInt(txtfAvenue.getText()); 
        }
        else{
            avenueNum = -1;
            success = false;
        }
        if(txtfStreet.getText().matches("\\d+")){
           streetNum = Integer.parseInt(txtfStreet.getText()); 
        }
        else{
            streetNum = -1;
            success = false;
        }
        if(txtfMazeAvenue.getText().matches("\\d+")){
           mazeAvenueNum = Integer.parseInt(txtfMazeAvenue.getText());
        }
        else{
            mazeAvenueNum = -1;
            success = false;
        }
        if(txtfMazeStreet.getText().matches("\\d+")){
           mazeStreetNum = Integer.parseInt(txtfMazeStreet.getText());
        }   
        else{
            mazeStreetNum = -1;
            success = false;
        }
        return success;
    }
    
    /**
     * Sets error messages based on values inputted by user.
     */
    private void setMessage(){
        String strMessage = "";
        if(!txtfStreet.getText().matches("\\d+") || streetNum < 0 || streetNum > mazeStreetNum - 1){
            strMessage += "Street number to navigate to must be greater than 0 and less than the # of streets in the maze - 1.\n\n";
        }
        if(!txtfAvenue.getText().matches("\\d+") || avenueNum < 0 || avenueNum > mazeAvenueNum - 1){
            strMessage += "Avenue number to navigate to must be greater than 0 and less than the # of avenues in the maze - 1.\n\n";
        }
        if(!txtfMazeAvenue.getText().matches("\\d+") || mazeAvenueNum > 80 || mazeAvenueNum < 2){
            strMessage += "The # of avenues of the maze must be greater than 1 and less than 80.\n\n";
        }
        if(!txtfMazeStreet.getText().matches("\\d+") || mazeStreetNum > 80 || mazeStreetNum < 2){
            strMessage += "The # of streets of the maze must be greater than 1 and less than 80.\n\n";
        }
        txtMessage.setText(strMessage);
    }
    
    /**
     * Sets error message to provided message.
     * @param msg String to set message text to.
     */
    private void setMessage(String msg){
        txtMessage.setText(msg);
    }
    
    /**
     * Initializes (if not already initialized) the maze and android. Prompts 
     * android to solve maze with entered values.
     */
    private void launchAndriod(){
        if(!(city instanceof MazeCity)){
            freezeMazeInputs();
            city = new MazeCity(mazeStreetNum, mazeAvenueNum);
            data = new Andriod(city, 0, 0, Direction.EAST);
        }   
        data.solveMaze(streetNum, avenueNum);
        txtMessage.setText("Enter new coordinates to navigate to.");
    }
    
    /**
     * Disables the two text fields for setting maze size, changes their appearance.
     */
    private void freezeMazeInputs(){
        txtfMazeStreet.setDisable(true);
        txtfMazeStreet.setStyle("-fx-background-color: lightgray");
        txtfMazeAvenue.setDisable(true);
        txtfMazeAvenue.setStyle("-fx-background-color: lightgray");
    }
    
    /**
     * Disables the two text fields for setting destination and the start button
     * , changes their appearance.
     */
    private void freezeDestinationInputs(){
        txtfStreet.setDisable(true);
        txtfStreet.setStyle("-fx-background-color: lightgray");
        txtfAvenue.setDisable(true);
        txtfAvenue.setStyle("-fx-background-color: lightgray");
        btnStart.setDisable(true);
    }
    
    /**
     * Enables the two text fields for setting destination and the start button,
     * changes their appearance back to default.
     */
    private void unfreezeDestinationInputs(){
       txtfStreet.setDisable(false);
       txtfStreet.setStyle("-fx-background-color: white");
       txtfAvenue.setDisable(false);
       txtfAvenue.setStyle("-fx-background-color: white"); 
       btnStart.setDisable(false);
    }

    /**
     * Exits the application.
     * @param event 
     */
    @FXML
    private void handleExit(ActionEvent event) {
        System.exit(0);
    }
    
}
