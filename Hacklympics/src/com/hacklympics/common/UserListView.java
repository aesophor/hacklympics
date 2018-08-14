package com.hacklympics.common;

import java.util.List;
import javafx.scene.control.SelectionMode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.jfoenix.controls.JFXListView;
import com.hacklympics.api.user.Role;
import com.hacklympics.api.user.User;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.user.Teacher;

public class UserListView extends JFXListView<User> {
    
    private final ObservableList<User> users;
    
    private final double minHeight = 200;
    private final double maxHeight = 200;
    
    public UserListView(SelectionMode mode, Role role) {
        getSelectionModel().setSelectionMode(mode);
        setMinHeight(minHeight);
        setMaxHeight(maxHeight);
        
        users = FXCollections.observableArrayList();
        setItems(users);
        
        populate(role);
    }
    
    private void populate(Role role) {
        switch (role) {
            case STUDENT:
                users.addAll(Student.getStudents());
                break;
                
            case TEACHER:
                users.addAll(Teacher.getTeachers());
                break;
                
            default:
                break;
        }
    }
    
    
    public List<User> getSelected() {
        return getSelectionModel().getSelectedItems();
    }
    
    public List<User> getAllItems() {
        return users;
    }
    
}