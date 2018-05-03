package hacklympics.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.Utils;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String loginFXML = FXMLTable.getInstance().get("Login");
        Utils.loadStage(new FXMLLoader(getClass().getResource(loginFXML)));
    }

    public static void main(String[] args) {
        launch(args);
    }

}