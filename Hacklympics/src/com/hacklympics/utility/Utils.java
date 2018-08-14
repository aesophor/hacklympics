package com.hacklympics.utility;

import java.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
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
    
    
    public static String formatTime(int seconds) {
        return String.format("%02d:%02d", (seconds / 60), (seconds % 60));
    }
    
    
    public static String serialize(Serializable o) throws IOException {
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ObjectOutputStream oos = new ObjectOutputStream(baos);
    	oos.writeObject(o);
    	oos.close();
    	
    	return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
    
    public static Object deserialize(String s) throws IOException, ClassNotFoundException {
    	byte[] data = Base64.getDecoder().decode(s);
    	ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
    	Object o = ois.readObject();
    	ois.close();
    	
    	return o;
    }
    
}