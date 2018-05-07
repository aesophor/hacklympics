package hacklympics.utility;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.hacklympics.api.session.Session;

public class Utils {

    private Utils() {
        
    }
    
    public static void loadStage(FXMLLoader loader) {
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Hacklympics");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    public static void loadUserStage(FXMLLoader loader) {
        loadStage(loader);
        Session.getInstance().setMainController(loader.getController());
    }
    
}
