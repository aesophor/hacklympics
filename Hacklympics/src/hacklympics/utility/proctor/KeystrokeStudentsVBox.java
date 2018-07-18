package hacklympics.utility.proctor;

import java.util.List;
import java.util.ArrayList;
import javafx.scene.control.ToggleGroup;
import com.hacklympics.api.user.Student;

public class KeystrokeStudentsVBox extends StudentsVBox {
    
    private final ToggleGroup toggleGroup;
    private int frequency;
    
    public KeystrokeStudentsVBox() {
        super();
        
        this.toggleGroup = new ToggleGroup();
    }
    
    /**
     * Clears all KeystrokeBoxes in this VBox. rearrange() should
     * be called right after this operation.
     */
    public void clear() {
        this.studentBoxes.clear();
    }

    /**
     * Adds the specified KeystrokeBox to this group.
     * @param keystrokeBox the KeystrokeBox to add.
     */
    public void add(KeystrokeBox keystrokeBox) {
        this.studentBoxes.add(keystrokeBox);
        
        keystrokeBox.getRadioBtn().setToggleGroup(this.toggleGroup);
    }
    
    /**
     * Adds all specified KeystrokeBoxes to this group. rearrange() should
     * be called right after this operation.
     * @param keystrokeBoxes the KeystrokeBoxes to add.
     */
    public void addAll(List<KeystrokeBox> keystrokeBoxes) {
        for (KeystrokeBox box : keystrokeBoxes) {
            this.studentBoxes.add(box);
            
            box.getRadioBtn().setToggleGroup(this.toggleGroup);
        }
    }

    /**
     * Removes the specified KeystrokeBox from this group. rearrange() should
     * be called right after this operation.
     * @param keystrokeBox the KeystrokeBox to remove.
     */
    public void remove(KeystrokeBox keystrokeBox) {
        this.studentBoxes.remove(keystrokeBox);
        
        keystrokeBox.getRadioBtn().setToggleGroup(null);
    }
    
    /**
     * Removes all specified KeystrokeBoxes from this group. rearrange() should
     * be called right after this operation.
     * @param keystrokeBoxes the KeystrokeBoxes to remove.
     */
    public void removeAll(List<KeystrokeBox> keystrokeBoxes) {
        for (KeystrokeBox box : keystrokeBoxes) {
            this.studentBoxes.remove(box);
            
            box.getRadioBtn().setToggleGroup(null);
        }
    }

    /**
     * Gets the KeystrokeBox of the specified student.
     * @param student the student to search for.
     * @return the student's KeystrokeBox.
     */
    public KeystrokeBox get(Student student) {
        KeystrokeBox target = null;

        for (StudentBox box : studentBoxes) {
            if (box.getStudent().equals(student)) {
                target = (KeystrokeBox) box;
            }
        }

        return target;
    }

    /**
     * Gets the KeystrokeBox of the specified student.
     * @param username the username of the student to search for.
     * @return the student's KeystrokeBox.
     */
    public KeystrokeBox get(String username) {
        KeystrokeBox target = null;

        for (StudentBox box : studentBoxes) {
            if (box.getStudent().getUsername().equals(username)) {
                target = (KeystrokeBox) box;
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
     * Gets the KeystrokeBox that is selected by the user.
     * @return selected KeystrokeBox.
     */
    public KeystrokeBox getSelectedItem() {
        KeystrokeBox selectedBox = null;
        
        for (StudentBox box : studentBoxes) {
            if (((KeystrokeBox) box).getRadioBtn().isSelected()) {
                selectedBox = (KeystrokeBox) box;
            }
        }
        
        return selectedBox;
    }
    
    
    public int getFrequency() {
        return this.frequency;
    }
    
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
    
}
