package hacklympics.utility.proctor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javafx.application.Platform;;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.jfoenix.controls.JFXCheckBox;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.snapshot.Snapshot;
import com.hacklympics.api.utility.ImageUtils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class SnapshotBox extends StudentBox {
    
    private static final int SNAPSHOT_WIDTH = 155;
    private static final int SNAPSHOT_HEIGHT = 93;
    private static final int SNAPSHOT_LAYOUT_X = 0;
    private static final int SNAPSHOT_LAYOUT_Y = 20;
    
    private static final int TIMELABEL_LAYOUT_X = 15;
    private static final int TIMELABEL_LAYOUT_Y = 115;
    
    private final JFXCheckBox checkbox;
    private final ImageView snapshot;
    private final Label timestamp;

    public SnapshotBox(Student student) {
        super(student);
        
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
    
    
    @Override
    public void update(Snapshot snapshot) throws IOException {
        BufferedImage bi = ImageUtils.byteArray2BufferedImage(Base64.decode(snapshot.getSnapshot()));
        Image image = ImageUtils.bufferedImage2FXImage(bi);
        
        Platform.runLater(() -> {
            this.snapshot.setImage(image);
            this.timestamp.setText(snapshot.getTimestamp());
        });
    }
    
    public JFXCheckBox getCheckBox() {
        return this.checkbox;
    }
    
}