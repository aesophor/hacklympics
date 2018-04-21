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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.session.CurrentUser;
import com.hacklympics.api.users.Role;
import com.hacklympics.api.users.Student;
import com.hacklympics.api.users.Teacher;
import com.hacklympics.api.users.User;
import hacklympics.utility.DialogForm;
import hacklympics.utility.UserListView;

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
    private StackPane addStackPane;
    @FXML
    private JFXButton addBtn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTable();
        refresh();
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
    
    private void refresh() {
        buildTable();
        showTable();
    }
    
    
    public void search(ActionEvent event) {
        records.clear();
        keyword = keywordField.getText();
        
        refresh();
    }
    
    public void add(ActionEvent event) {
        records.clear();
        addStackPane.setMouseTransparent(false);
        
        DialogForm dialog = new DialogForm(addStackPane, "Add new course");
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        dialog.addField("Name");
        dialog.addField("Semester");
        dialog.add("Students", studentsList.getListView());
        
        studentsList.getListView().setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                ObservableList<Student> selectedItems =  studentsList.getListView().getSelectionModel().getSelectedItems();
                
                for(Student s: selectedItems) {
                    System.out.println("selected item " + s);
                }
            }
        });
        
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
                
                addStackPane.setMouseTransparent(true);
                dialog.close();
                refresh();
            }
        });
        
        dialog.getCancelBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addStackPane.setMouseTransparent(true);
                dialog.close();
            }
        });
        
        dialog.show();
    }
    
}