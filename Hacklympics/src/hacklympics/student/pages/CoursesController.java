package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.materials.Problem;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.users.Student;
import hacklympics.student.StudentController;
import hacklympics.utility.TextDialog;

public class CoursesController implements Initializable {

    private ObservableList<Course> courseRecords;
    private ObservableList<Exam> examRecords;
    private ObservableList<Problem> problemRecords;

    private String keyword;

    @FXML
    private JFXTabPane tabPane;
    @FXML
    private Tab courseTab;
    @FXML
    private Tab examTab;
    @FXML
    private Tab problemTab;
    @FXML
    private JFXButton attendExamBtn;

    @FXML
    private JFXTextField keywordField;
    @FXML
    private StackPane dialogPane;

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> courseSemesterCol;
    @FXML
    private TableColumn<Course, Integer> courseNameCol;
    @FXML
    private TableColumn<Course, String> courseTeacherCol;

    @FXML
    private TableView<Exam> examTable;
    @FXML
    private TableColumn<Exam, String> examTitleCol;
    @FXML
    private TableColumn<Exam, Integer> examDurationCol;
    @FXML
    private TableColumn<Exam, String> examDescCol;

    @FXML
    private TableView<Problem> problemTable;
    @FXML
    private TableColumn<Problem, String> probTitleCol;
    @FXML
    private TableColumn<Problem, Integer> probDescCol;
    @FXML
    private TableColumn<Problem, String> probInputCol;
    @FXML
    private TableColumn<Problem, Integer> probOutputCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTables();
        update(courseTab);
    }

    private void initTables() {
        courseRecords = FXCollections.observableArrayList();
        examRecords = FXCollections.observableArrayList();
        problemRecords = FXCollections.observableArrayList();

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
        probInputCol.setCellValueFactory(new PropertyValueFactory<>("input"));
        probOutputCol.setCellValueFactory(new PropertyValueFactory<>("output"));

        courseTable.setOnMouseClicked((Event e) -> {
            update(examTab, problemTab);
            this.attendExamBtn.setDisable(true);
        });
        
        examTable.setOnMouseClicked((Event e) -> {
            update(problemTab);
            this.attendExamBtn.setDisable(false);
        });
    }

    private void update(Tab... tabs) {
        keyword = (keyword == null) ? "" : keyword;

        for (Tab tab : tabs) {
            
            if (tab == courseTab) {
                courseRecords.clear();
                
                List<Course> courses = ((Student) Session.getInstance().getCurrentUser()).getCourses();
                for (Course c : courses) {
                    if (c.getData().getName().contains(keyword) |
                        c.getData().getTeacher().contains(keyword)) {
                        courseRecords.add(c);
                    }
                }

                courseTable.getItems().clear();
                courseTable.getItems().addAll(courseRecords);
                update(examTab);

            } else if (tab == examTab) {
                examRecords.clear();
                Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
                
                if (selectedCourse != null) {
                    List<Exam> exams = selectedCourse.getExams();
                    
                    for (Exam e : exams) {
                        if (e.getData().getTitle().contains(keyword) |
                            e.getData().getDesc().contains(keyword)) {
                            examRecords.add(e);
                        }
                    }
                }

                examTable.getItems().clear();
                examTable.getItems().addAll(examRecords);
                update(problemTab);
                
            } else if (tab == problemTab) {
                problemRecords.clear();
                Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
                
                if (selectedExam != null) {
                    List<Problem> problems = selectedExam.getProblems();
                    for (Problem p : problems) {
                        if (p.getData().getTitle().contains(keyword) |
                            p.getData().getDesc().contains(keyword)) {
                            problemRecords.add(p);
                        }
                    }
                }
                
                problemTable.getItems().clear();
                problemTable.getItems().addAll(problemRecords);

            } else {

            }
            
        }
    }
    

    public void search(ActionEvent event) {
        keyword = keywordField.getText();
        update(tabPane.getSelectionModel().getSelectedItem());
    }
    
    public void attendExam(ActionEvent event) {
        Exam selected = examTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        Session.getInstance().setCurrentExam(selected);
        
        TextDialog alert = new TextDialog(dialogPane,
                                          "Attend Exam",
                                          "You have selected the exam: " + selected + "\n\n"
                                        + "Attend the exam now?");
        
        alert.getConfirmBtn().setOnAction((ActionEvent e) -> {
            StudentController sc = (StudentController) Session.getInstance().getMainController();
            ((CodeController) sc.getControllers().get("code")).setExamLabel(selected.toString());
            ((StudentController) Session.getInstance().getMainController()).showCode(event);
            
            alert.close();
        });
        
        alert.show();
    }
    
}