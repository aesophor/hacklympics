package hacklympics.utility;

import javafx.scene.Group;
import com.jfoenix.controls.JFXCheckBox;
import com.hacklympics.api.user.Student;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private final Label timeLabel;

    public SnapshotBox(Student student) {
        this.student = student;
        
        this.checkbox = new JFXCheckBox(student.getFullname());
        
        this.snapshot = new ImageView();
        this.snapshot.setFitWidth(SNAPSHOT_WIDTH);
        this.snapshot.setFitHeight(SNAPSHOT_HEIGHT);
        this.snapshot.setLayoutX(SNAPSHOT_LAYOUT_X);
        this.snapshot.setLayoutY(SNAPSHOT_LAYOUT_Y);
        this.snapshot.setImage(new Image("file:/home/aesophor/snap.jpg"));
        
        this.timeLabel = new Label();
        this.timeLabel.setLayoutX(TIMELABEL_LAYOUT_X);
        this.timeLabel.setLayoutY(TIMELABEL_LAYOUT_Y);
        this.timeLabel.setText("Waiting...");
        
        this.getChildren().addAll(this.checkbox, this.snapshot, this.timeLabel);
    }
    
    
    public void update(Image snapshot, String time) {
        this.snapshot.setImage(snapshot);
        this.timeLabel.setText(time);
    }
    
}
