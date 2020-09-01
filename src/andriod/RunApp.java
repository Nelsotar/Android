/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package andriod;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * A class that implements runnable, sets the stage of an Application subclass.
 * @author TN
 */
public class RunApp implements Runnable{
    Stage stage;
    RunApp(Stage stage){
        this.stage = stage; 
    }
    
    /**
     * Sets the stage of an Application subclass.
     */
    @Override
    public void run(){
        try {
            //When the x button is clicked, both windows will close
            stage.onCloseRequestProperty().set(e->{ System.exit(0);});
            Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene = new Scene(root);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(RunApp.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
