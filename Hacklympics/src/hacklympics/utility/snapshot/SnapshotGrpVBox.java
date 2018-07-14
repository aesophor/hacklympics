package hacklympics.utility.snapshot;

import java.util.List;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import com.hacklympics.api.user.Student;

public class SnapshotGrpVBox extends VBox {

    private static final int COL_SIZE = 2;
    
    private List<SnapshotBox> snapshotBoxes;
    private double quality;
    private int frequency;
    
    

    public SnapshotGrpVBox(double quality, int frequency) {
        this.snapshotBoxes = new ArrayList<>();
        
        this.quality = quality;
        this.frequency = frequency;
        
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
     * Gets the SnapshotBoxes that are selected by the user.
     * @return selected SnapshotBoxes.
     */
    public List<SnapshotBox> getSelectedItems() {
        List<SnapshotBox> selectedBoxes = new ArrayList<>();
        
        for (SnapshotBox box : snapshotBoxes) {
            if (box.getCheckBox().isSelected()) {
                selectedBoxes.add(box);
            }
        }
        
        return (selectedBoxes.isEmpty()) ? null : selectedBoxes;
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
     * Adds all specified SnapshotBoxes to this group. rearrange() should
     * be called right after this operation.
     * @param snapshotBoxes the SnapshotBoxes to add.
     */
    public void addAll(List<SnapshotBox> snapshotBoxes) {
        for (SnapshotBox box : snapshotBoxes) {
            this.snapshotBoxes.add(box);
        }
    }

    /**
     * Removes the specified SnapshotBox from this group. rearrange() should be
     * called right after this operation.
     * @param snapshotBox the SnapshotBox to remove.
     */
    public void remove(SnapshotBox snapshotBox) {
        this.snapshotBoxes.remove(snapshotBox);
    }
    
    /**
     * Removes all specified SnapshotBoxes from this group. rearrange() should
     * be called right after this operation.
     * @param snapshotBoxes the SnapshotBoxes to remove.
     */
    public void removeAll(List<SnapshotBox> snapshotBoxes) {
        for (SnapshotBox box : snapshotBoxes) {
            this.snapshotBoxes.remove(box);
        }
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
    
    /**
     * Gets all students currently in this group.
     * @return all students in this group.
     */
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        
        for (SnapshotBox box : snapshotBoxes) {
            students.add(box.getStudent());
        }
        
        return students;
    }

    
    public double getQuality() {
        return this.quality;
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
    public void setQuality(double quality) {
        this.quality = quality;
    }
    
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    
}
