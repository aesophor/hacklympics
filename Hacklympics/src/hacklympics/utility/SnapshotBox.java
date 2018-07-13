package hacklympics.utility;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.jfoenix.controls.JFXCheckBox;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.proctor.Snapshot;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.application.Platform;

public class SnapshotBox extends Group {
    
    private static final int SNAPSHOT_WIDTH = 155;
    private static final int SNAPSHOT_HEIGHT = 93;
    private static final int SNAPSHOT_LAYOUT_X = 0;
    private static final int SNAPSHOT_LAYOUT_Y = 20;
    
    private static final int TIMELABEL_LAYOUT_X = 15;
    private static final int TIMELABEL_LAYOUT_Y = 115;
    
    private final Student student;
    private final JFXCheckBox checkbox;
    private final ImageView snapshot;
    private final Label timestamp;

    public SnapshotBox(Student student) {
        this.student = student;
        
        this.checkbox = new JFXCheckBox(student.getFullname());
        
        this.snapshot = new ImageView(new Image("/resources/Images/BlankSnapshot.jpg"));
        this.snapshot.setFitWidth(SNAPSHOT_WIDTH);
        this.snapshot.setFitHeight(SNAPSHOT_HEIGHT);
        this.snapshot.setLayoutX(SNAPSHOT_LAYOUT_X);
        this.snapshot.setLayoutY(SNAPSHOT_LAYOUT_Y);
        
        this.timestamp = new Label("Waiting...");
        this.timestamp.setLayoutX(TIMELABEL_LAYOUT_X);
        this.timestamp.setLayoutY(TIMELABEL_LAYOUT_Y);
        
        this.getChildren().addAll(this.checkbox, this.snapshot, this.timestamp);
    }
    
    
    public void update(Snapshot snapshot) throws IOException {
        BufferedImage bi = Utils.byteArray2BufferedImage(Base64.decode(snapshot.getSnapshot()));
        Image image = Utils.bufferedImage2FXImage(bi);
        
        Platform.runLater(() -> {
            this.snapshot.setImage(image);
            this.timestamp.setText(snapshot.getTimestamp());
        });
    }
    
    public Student getStudent() {
        return this.student;
    }
    
}