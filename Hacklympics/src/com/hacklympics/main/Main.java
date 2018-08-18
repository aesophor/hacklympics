package com.hacklympics.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import com.hacklympics.api.preference.Config;
import com.hacklympics.utility.FXMLTable;
import com.hacklympics.utility.Utils;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Config.getInstance().getURL() will invoke the constructor of Config class,
        // which reads parameters from config.properties (e.g. server hostname and port...etc.)
        System.out.println("You may edit config.properties to set the server hostname:port manually.\n");
        System.out.println("[*] Loaded server hostname:port from config.properties: " + Config.getInstance().getURL());
        
        // Load and display Login stage.
        String loginFXML = FXMLTable.getInstance().get("Login");
        Utils.showStage(new FXMLLoader(getClass().getResource(loginFXML)));
    }

    public static void main(String[] args) {
        launch(args);
    }

}