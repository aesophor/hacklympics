package hacklympics.login;

import com.hacklympics.api.communication.Config;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import hacklympics.common.FXMLTable;
import hacklympics.utility.Utils;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Config.getURL() will invoke the static initializer of Config class,
        // which reads parameters from config.properties (e.g. server hostname and port...etc.)
        System.out.println("You may edit config.properties to set the server hostname:port manually.\n");
        System.out.println("[*] Loaded server hostname:port from config.properties: " + Config.getURL());
        
        // Load and display Login stage.
        String loginFXML = FXMLTable.getInstance().get("Login");
        Utils.loadStage(new FXMLLoader(getClass().getResource(loginFXML)));
    }

    public static void main(String[] args) {
        launch(args);
    }

}