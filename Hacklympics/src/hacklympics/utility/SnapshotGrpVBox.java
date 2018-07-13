package hacklympics.utility;

import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.hacklympics.api.user.Student;

public class SnapshotGrpVBox extends VBox {

    private static final int COL_SIZE = 2;

    private List<SnapshotBox> snapshotBoxes;

    public SnapshotGrpVBox() {
        this.snapshotBoxes = new ArrayList<>();

        // Set the predHeight of VBox to USE_PREF_SIZE
        // to ensure the scroll bar of ScrollPane will appear.
        this.setPrefHeight(USE_PREF_SIZE);
    }

    /**
     * Rearranges the layout of this Snapshot Group VBox. Should be called when
     * any SnapshotBox are moved to the other group.
     */
    public void rearrange() {
        // Remove all rows (actually HBoxes).
        this.getChildren().clear();

        // Calculate the number of HBox needed.
        int hboxNeeded = (int) Math.ceil((float) this.snapshotBoxes.size() / COL_SIZE);

        // Then add every two SnapshotBoxes to one HBox,
        // until all SnapshotBoxes have been added.
        // e.g., if there is 5 SnapshotBoxes (numbered 0,1,2,3,4),
        //       then we will need 3 HBoxes for them (each HBox contains 2 at most).
        for (int i = 0; i < hboxNeeded; i++) {
            // Create an HBox and make sure it is added to the VBox.
            HBox row = new HBox();
            row.setPadding(new Insets(0, 0, 10, 0));
            this.getChildren().add(row);

            // Add SnapshotBoxes to this row according to COLUMN_SIZE.
            for (int j = i * COL_SIZE; j < (i * COL_SIZE + COL_SIZE) && j < this.snapshotBoxes.size(); j++) {
                row.getChildren().add(this.snapshotBoxes.get(j));
            }
        }
    }

    /**
     * Clears all SnapshotBoxes in this Snapshot Group VBox. rearrange() should
     * be called right after this operation.
     */
    public void clear() {
        this.snapshotBoxes.clear();
    }

    /**
     * Adds the specified SnapshotBox to this group.
     *
     * @param snapshotBox the SnapshotBox to add.
     */
    public void add(SnapshotBox snapshotBox) {
        this.snapshotBoxes.add(snapshotBox);
    }

    /**
     * Removes the specified SnapshotBox from this group. rearrange() should be
     * called right after this operation.
     *
     * @param snapshotBox the SnapshotBox to add.
     */
    public void remove(SnapshotBox snapshotBox) {
        this.snapshotBoxes.remove(snapshotBox);
    }

    /**
     * Gets the SnapshotBox of the specified student.
     *
     * @param student the student to search for.
     * @return the student's SnapshotBox.
     */
    public SnapshotBox get(Student student) {
        SnapshotBox target = null;

        for (SnapshotBox box : snapshotBoxes) {
            if (box.getStudent().equals(student)) {
                target = box;
            }
        }

        return target;
    }

    /**
     * Gets the SnapshotBox of the specified student.
     *
     * @param username the username of the student to search for.
     * @return the student's SnapshotBox.
     */
    public SnapshotBox get(String username) {
        SnapshotBox target = null;

        for (SnapshotBox box : snapshotBoxes) {
            if (box.getStudent().getUsername().equals(username)) {
                target = box;
            }
        }

        return target;
    }

}
