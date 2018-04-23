package hacklympics.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.Utils;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Utils.loadStage(new FXMLLoader(), FXMLTable.getInstance().get("Login"), LoginController.class);
    }

    public static void main(String[] args) {
        launch(args);
    }

}