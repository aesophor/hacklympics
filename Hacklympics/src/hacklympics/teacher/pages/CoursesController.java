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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.materials.Problem;
import com.hacklympics.api.session.CurrentUser;
import com.hacklympics.api.users.Role;
import com.hacklympics.api.users.User;
import hacklympics.utility.FormDialog;
import hacklympics.utility.UserListView;

public class CoursesController implements Initializable {
    
    private ObservableList<Course> coursesRecords;
    private ObservableList<Course> examsRecords;
    private ObservableList<Course> problemsRecords;
    
    private String keyword;

    @FXML
    private JFXTabPane tabPane;
    @FXML
    private JFXTextField keywordField;
    @FXML
    private StackPane stackPane;
    
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private TableColumn<Course, String> courseSemesterCol;
    @FXML
    private TableColumn<Course, Integer> courseNameCol;
    @FXML
    private TableColumn<Course, String> courseTeacherCol;
    
    @FXML
    private TableView<Exam> examsTable;
    @FXML
    private TableColumn<Exam, String> examTitleCol;
    @FXML
    private TableColumn<Exam, Integer> examDurationCol;
    @FXML
    private TableColumn<Exam, String> examDescCol;
    
    @FXML
    private TableView<Problem> problemsTable;
    @FXML
    private TableColumn<Problem, String> probTitleCol;
    @FXML
    private TableColumn<Problem, Integer> probDescCol;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTables();
        refresh();
    }    
    
    
    private void initTables() {
        coursesRecords = FXCollections.observableArrayList();
        examsRecords = FXCollections.observableArrayList();
        problemsRecords = FXCollections.observableArrayList();
        
        // Initialize columns in coursesTable.
        courseSemesterCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        courseTeacherCol.setCellValueFactory(new PropertyValueFactory<>("teacher"));
        
        // Initialize columns in examsTable.
        examTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        examDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        examDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        
        // Initialize columns problemsTable.
        probTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        probDescCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        
    }
    
    private void buildTable() {
        List<Course> courses = Course.getCourses();
        keyword = (keyword == null) ? "" : keyword;
        
        for (Course c: courses) {
            if (c.getData().getName().contains(keyword) |
                c.getData().getTeacher().contains(keyword)) {
                coursesRecords.add(c);
            }
        }
    }
    
    private void showTable() {
        coursesTable.getItems().clear();
        coursesTable.getItems().addAll(coursesRecords);
    }
    
    private void refresh() {
        buildTable();
        showTable();
    }
    
    
    public void search(ActionEvent event) {
        coursesRecords.clear();
        keyword = keywordField.getText();
        
        refresh();
    }
    
    public void add(ActionEvent event) {
        coursesRecords.clear();
        stackPane.setMouseTransparent(false);
        
        FormDialog dialog = new FormDialog(stackPane, "Add new course");
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        dialog.addField("Name", "");
        dialog.addField("Semester", "");
        dialog.add("Students", studentsList.getListView());
        
        
        dialog.getConfirmBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFXTextField nameField = (JFXTextField) dialog.get("Name");
                JFXTextField semesterField = (JFXTextField) dialog.get("Semester");
                String teacher = CurrentUser.getInstance().getUser().getProfile().getUsername();
                List<User> s = studentsList.getSelected();
                
                List<String> students = new ArrayList<>();
                for (User user: s) {
                    students.add(user.getProfile().getUsername());
                }
                        
                Course.create(nameField.getText(),
                              Integer.parseInt(semesterField.getText()),
                              teacher,
                              students);
                
                stackPane.setMouseTransparent(true);
                dialog.close();
                refresh();
            }
        });
        
        dialog.getCancelBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stackPane.setMouseTransparent(true);
                dialog.close();
            }
        });
        
        dialog.show();
    }
    
    public void update(ActionEvent event) {
        coursesRecords.clear();
        stackPane.setMouseTransparent(false);
        
        Course course = coursesTable.getSelectionModel().getSelectedItem();
        
        FormDialog dialog = new FormDialog(stackPane, "Edit course");
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        dialog.addField("Name", course.getData().getName());
        dialog.addField("Semester", course.getData().getSemester().toString());
        dialog.add("Students", studentsList.getListView());
        
        
        dialog.getConfirmBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFXTextField nameField = (JFXTextField) dialog.get("Name");
                JFXTextField semesterField = (JFXTextField) dialog.get("Semester");
                String teacher = CurrentUser.getInstance().getUser().getProfile().getUsername();
                List<User> s = studentsList.getSelected();
                
                List<String> students = new ArrayList<>();
                for (User user: s) {
                    students.add(user.getProfile().getUsername());
                }
                        
                Course.create(nameField.getText(),
                              Integer.parseInt(semesterField.getText()),
                              teacher,
                              students);
                
                stackPane.setMouseTransparent(true);
                dialog.close();
                refresh();
            }
        });
        
        dialog.getCancelBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stackPane.setMouseTransparent(true);
                dialog.close();
            }
        });
        
        dialog.show();
    }
    
    
    private void addCourse() {
        
    }
    
    private void addExam() {
        
    }
    
    private void addProblem() {
        
    }
    
    
    private void updateCourse() {
        
    }
    
    private void updateExam() {
        
    }
    
    private void updateProblem() {
        
    }
    
}