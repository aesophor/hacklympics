package hacklympics.login;

import com.hacklympics.api.event.EventListener;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import hacklympics.utility.FXMLTable;
import hacklympics.utility.Utils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String loginFXML = FXMLTable.getInstance().get("Login");
        Utils.loadStage(new FXMLLoader(getClass().getResource(loginFXML)));
        
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.execute(EventListener.getInstance());
    }

    public static void main(String[] args) {
        launch(args);
    }

}