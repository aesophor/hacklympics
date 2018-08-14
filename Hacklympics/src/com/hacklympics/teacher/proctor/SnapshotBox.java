package com.hacklympics.teacher.proctor;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import com.jfoenix.controls.JFXCheckBox;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.proctor.Snapshot;
import com.hacklympics.api.utility.ImageUtils;

public class SnapshotBox extends StudentBox<Snapshot> {

    private static final int SNAPSHOT_WIDTH = 155;
    private static final int SNAPSHOT_HEIGHT = 93;
    private static final int SNAPSHOT_LAYOUT_X = 0;
    private static final int SNAPSHOT_LAYOUT_Y = 20;

    private static final int TIMELABEL_LAYOUT_X = 15;
    private static final int TIMELABEL_LAYOUT_Y = 115;
    private static final int FINISH_LABEL_LAYOUT_X = 50;

    private final JFXCheckBox checkbox;
    private final ImageView snapshot;
    private final Label timestamp;

    public SnapshotBox(Student student) {
        super(student);

        checkbox = new JFXCheckBox(student.getFullname());

        snapshot = new ImageView(new Image("/resources/images/BlankSnapshot.jpg"));
        snapshot.setFitWidth(SNAPSHOT_WIDTH);
        snapshot.setFitHeight(SNAPSHOT_HEIGHT);
        snapshot.setLayoutX(SNAPSHOT_LAYOUT_X);
        snapshot.setLayoutY(SNAPSHOT_LAYOUT_Y);

        timestamp = new Label("Waiting...");
        timestamp.setLayoutX(TIMELABEL_LAYOUT_X);
        timestamp.setLayoutY(TIMELABEL_LAYOUT_Y);

        getChildren().addAll(checkbox, snapshot, timestamp);
    }

    @Override
    public void update(Snapshot snapshot) throws IOException {
        byte[] imgByteArray = Base64.getDecoder().decode(snapshot.getSnapshot());
        
        BufferedImage bi = ImageUtils.byteArray2BufferedImage(imgByteArray);
        Image image = ImageUtils.bufferedImage2FXImage(bi);

        Platform.runLater(() -> {
            this.snapshot.setImage(image);
            timestamp.setText(snapshot.getTimestamp());
        });
    }
    
    @Override
    public void markAsFinished() {
        Platform.runLater(() -> {
            timestamp.setText("Finished");
            timestamp.setLayoutX(FINISH_LABEL_LAYOUT_X);
            timestamp.getStyleClass().add("finish-label");
        });
    }

    public JFXCheckBox getCheckBox() {
        return checkbox;
    }

}
