package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.users.Teacher;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import hacklympics.utility.Utils;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CoursesController implements Initializable {
    
    private ObservableList<Course> records;
    private String keyword;

    @FXML
    private TableView<Course> table;
    @FXML
    private TableColumn<Course, Integer> courseIDColumn;
    @FXML
    private TableColumn<Course, String> nameColumn;
    @FXML
    private TableColumn<Course, Integer> semesterColumn;
    @FXML
    private TableColumn<Course, String> teacherColumn;
    
    @FXML
    private JFXTextField keywordField;
    @FXML
    private JFXButton searchBtn;
    
    @FXML
    private StackPane stackPane;
    @FXML
    private JFXButton addBtn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        buildTable();
        showTable();
    }    
    
    
    private void initTable() {
        records = FXCollections.observableArrayList();
        
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        teacherColumn.setCellValueFactory(new PropertyValueFactory<>("teacher"));
    }
    
    private void buildTable() {
        List<Course> courses = Course.getCourses();
        keyword = (keyword == null) ? "" : keyword;
        
        for (Course c: courses) {
            if (c.getData().getName().contains(keyword) |
                c.getData().getTeacher().contains(keyword)) {
                records.add(c);
            }
        }
    }
    
    private void showTable() {
        table.getItems().clear();
        table.getItems().addAll(records);
    }
    
    
    public void search(ActionEvent event) {
        records.clear();
        keyword = keywordField.getText();
        
        buildTable();
        showTable();
    }
    
    public void add(ActionEvent event) {
        records.clear();
        
        System.out.println(table.getSelectionModel().getSelectedItem());
        
        stackPane.setMouseTransparent(false);
        VBox vbox = new VBox();
        vbox.setSpacing(30.0);
        
        JFXTextField nameField = new JFXTextField();
        JFXTextField semesterField = new JFXTextField();
        JFXComboBox teacherBox = new JFXComboBox();
        
        nameField.setLabelFloat(true);
        nameField.setPromptText("Name");
        
        semesterField.setLabelFloat(true);
        semesterField.setPromptText("Semester");
        
        teacherBox.setLabelFloat(true);
        teacherBox.setPromptText("Teacher");
        
        List<Teacher> teachers = Teacher.getTeachers();
        teacherBox.getItems().addAll(teachers);
        
        vbox.getChildren().addAll(nameField, semesterField, teacherBox);
        
        
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Add new course"));
        content.setBody(vbox);
        
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton confirmBtn = new JFXButton("Add");
        JFXButton cancelBtn = new JFXButton("Dismiss");
        confirmBtn.setDefaultButton(true);
        
        List<JFXButton> buttons = new ArrayList<>();
        buttons.add(cancelBtn);
        buttons.add(confirmBtn);
        
        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String name = nameField.getText();
                int semester = Integer.parseInt(semesterField.getText());
                String teacher = ((Teacher) teacherBox.getSelectionModel().getSelectedItem()).getProfile().getUsername();
                List<String> students = new ArrayList<>();
                Course.create(name, semester, teacher, students);
                
                buildTable();
                showTable();
                
                stackPane.setMouseTransparent(true);
                dialog.close();
            }
        });
        
        cancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stackPane.setMouseTransparent(true);
                dialog.close();
            }
        });
        
        content.setActions(buttons);
        dialog.show();
    }
    
}