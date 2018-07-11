package hacklympics.utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.embed.swing.SwingFXUtils;
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
    
    
    public static byte[] bufferedImage2ByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
    
    public static BufferedImage byteArray2BufferedImage(byte[] bytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
    
    public static Image BufferedImage2FXImage(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    
    
    public static String formatTime(int seconds) {
        return String.format("%02d:%02d", (seconds / 60), (seconds % 60));
    }
    
}