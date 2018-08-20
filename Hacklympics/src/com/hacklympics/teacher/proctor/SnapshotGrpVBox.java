package com.hacklympics.teacher.proctor;

import java.util.List;
import java.util.ArrayList;
import com.hacklympics.api.user.Student;

public class SnapshotGrpVBox extends StudentsVBox {

    private double snapshotQuality;
    private int syncFrequency;
    
    public SnapshotGrpVBox(double snapshotQuality, int syncFrequency) {
        super();
        
        this.snapshotQuality = snapshotQuality;
        this.syncFrequency = syncFrequency;
    }


    /**
     * Clears all SnapshotBoxes in this Snapshot Group VBox. rearrange() should
     * be called right after this operation.
     */
    public void clear() {
        studentBoxes.clear();
    }

    /**
     * Adds the specified SnapshotBox to this group.
     * @param snapshotBox the SnapshotBox to add.
     */
    public void add(SnapshotBox snapshotBox) {
        studentBoxes.add(snapshotBox);
    }
    
    /**
     * Adds all specified SnapshotBoxes to this group. rearrange() should
     * be called right after this operation.
     * @param snapshotBoxes the SnapshotBoxes to add.
     */
    public void addAll(List<SnapshotBox> snapshotBoxes) {
        for (SnapshotBox box : snapshotBoxes) {
            studentBoxes.add(box);
        }
    }

    /**
     * Removes the specified SnapshotBox from this group. rearrange() should 
     * be called right after this operation.
     * @param snapshotBox the SnapshotBox to remove.
     */
    public void remove(SnapshotBox snapshotBox) {
        studentBoxes.remove(snapshotBox);
    }
    
    /**
     * Removes all specified SnapshotBoxes from this group. rearrange() should
     * be called right after this operation.
     * @param snapshotBoxes the SnapshotBoxes to remove.
     */
    public void removeAll(List<SnapshotBox> snapshotBoxes) {
        for (SnapshotBox box : snapshotBoxes) {
            studentBoxes.remove(box);
        }
    }

    /**
     * Gets the SnapshotBox of the specified student.
     * @param student the student to search for.
     * @return the student's SnapshotBox.
     */
    public SnapshotBox get(Student student) {
        SnapshotBox target = null;

        for (StudentBox box : studentBoxes) {
            if (box.getStudent().equals(student)) {
                target = (SnapshotBox) box;
            }
        }

        return target;
    }

    /**
     * Gets the SnapshotBox of the specified student.
     * @param username the username of the student to search for.
     * @return the student's SnapshotBox.
     */
    public SnapshotBox get(String username) {
        SnapshotBox target = null;

        for (StudentBox box : studentBoxes) {
            if (box.getStudent().getUsername().equals(username)) {
                target = (SnapshotBox) box;
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
        
        for (StudentBox box : studentBoxes) {
            students.add(box.getStudent());
        }
        
        return students;
    }
    
    /**
     * Gets the SnapshotBoxes that are selected by the user.
     * @return selected SnapshotBoxes.
     */
    public List<SnapshotBox> getSelectedItems() {
        List<SnapshotBox> selectedBoxes = new ArrayList<>();
        
        for (StudentBox box : studentBoxes) {
            if (((SnapshotBox) box).getCheckBox().isSelected()) {
                selectedBoxes.add((SnapshotBox) box);
            }
        }
        
        return (selectedBoxes.isEmpty()) ? null : selectedBoxes;
    }

    
    public double getSnapshotQuality() {
        return snapshotQuality;
    }
    
    public int getSyncFrequency() {
        return syncFrequency;
    }
    
    public void setSnapshotQuality(double snapshotQuality) {
        this.snapshotQuality = snapshotQuality;
    }
    
    public void setSyncFrequency(int syncFrequency) {
        this.syncFrequency = syncFrequency;
    }
    
}