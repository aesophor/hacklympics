package com.hacklympics.common;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;

public class ProfileController implements Initializable {

    @FXML
    private JFXTextField keywordField;
    @FXML
    private Circle avatarCircle;
    @FXML
    private JFXButton launchExamBtn;
    @FXML
    private TableView<?> courseTable;
    @FXML
    private TableColumn<?, ?> courseSemesterCol;
    @FXML
    private TableColumn<?, ?> courseNameCol;
    @FXML
    private TableColumn<?, ?> courseTeacherCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void search(KeyEvent event) {
    }

    @FXML
    private void launchExam(ActionEvent event) {
    }
    
}
