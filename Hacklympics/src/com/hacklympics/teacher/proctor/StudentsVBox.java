package com.hacklympics.teacher.proctor;

import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.hacklympics.api.proctor.ProctorMedium;

public abstract class StudentsVBox extends VBox {
    
    private static final int COL_SIZE = 2;
    
    protected List<StudentBox<? extends ProctorMedium>> studentBoxes;
    
    public StudentsVBox() {
        studentBoxes = new ArrayList<>();
        
        // Set the predHeight of VBox to USE_PREF_SIZE
        // to ensure the scroll bar of ScrollPane will appear.
        setPrefHeight(USE_PREF_SIZE);
    }

    /**
     * Rearranges the layout of this Snapshot Group VBox. Should be called when
     * any SnapshotBox are moved to the other group.
     */
    public void rearrange() {
        // Remove all rows (actually HBoxes).
        getChildren().clear();

        // Calculate the number of HBox needed.
        int hboxNeeded = (int) Math.ceil((float) studentBoxes.size() / COL_SIZE);

        // Then add every two SnapshotBoxes to one HBox,
        // until all SnapshotBoxes have been added.
        // e.g., if there is 5 SnapshotBoxes (numbered 0,1,2,3,4),
        //       then we will need 3 HBoxes for them (each HBox contains 2 at most).
        for (int i = 0; i < hboxNeeded; i++) {
            // Create an HBox and make sure it is added to the VBox.
            HBox row = new HBox();
            row.setPadding(new Insets(0, 0, 10, 0));
            getChildren().add(row);

            // Add SnapshotBoxes to this row according to COLUMN_SIZE.
            for (int j = i * COL_SIZE; j < (i * COL_SIZE + COL_SIZE) && j < studentBoxes.size(); j++) {
                row.getChildren().add(studentBoxes.get(j));
            }
        }
    }
    
}