package hacklympics.utility;

import java.util.List;
import com.hacklympics.api.users.Role;
import com.hacklympics.api.users.User;
import com.hacklympics.api.users.Student;
import com.hacklympics.api.users.Teacher;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;

public class UserListView {
    
    private final JFXListView listView;
    private final ObservableList<User> users;
    
    private double minHeight = 200;
    private double maxHeight = 200;
    
    public UserListView(SelectionMode mode, Role role) {
        this.listView = new JFXListView();
        
        this.listView.getSelectionModel().setSelectionMode(mode);
        this.listView.setMinHeight(minHeight);
        this.listView.setMaxHeight(maxHeight);
        
        this.users = FXCollections.observableArrayList();
        this.listView.setItems(users);
        
        populate(role);
    }
    
    private void populate(Role role) {
        switch (role) {
            case STUDENT:
                this.users.addAll(Student.getStudents());
                break;
                
            case TEACHER:
                this.users.addAll(Teacher.getTeachers());
                break;
                
            default:
                break;
        }
    }
    
    
    public List<User> getSelected() {
        return listView.getSelectionModel().getSelectedItems();
    }
    
    public JFXListView getListView() {
        return listView;
    }
    
}
