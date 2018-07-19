package hacklympics.student.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.material.Course;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.material.Problem;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Student;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class CoursesController implements Initializable {

    private ObservableList<Course> courseRecords;
    private ObservableList<Exam> examRecords;
    private ObservableList<Problem> problemRecords;
    
    private List<Course> courseRecordsCache;
    private List<Exam> examRecordsCache;
    private List<Problem> problemRecordsCache;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTables();
        fetchAndUpdate(courseTab);
    }

    /**
     * Initializes the three primary tables: Course, Exam and Problem.
     * Whenever the user clicks on the courseTab/examTab, the relevant
     * subtables will be refreshed.
     * In addition, the button to attend exam will be made available
     * once an exam has been selected. However, if the user selects
     * another course, the button will be disabled again
     * (i.e., the user will have to re-select an exam).
     */
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
        
        
        // Override the default empty message of tables.
        courseTable.setPlaceholder(new Label("You have not taken any course yet."));
        examTable.setPlaceholder(new Label("No exams under the selected course."));
        problemTable.setPlaceholder(new Label("No problems under the selected exam."));

        
        // Update examTab and problemTab if the user clicks on courseTab.
        // Since another course might has been selected, we have to update
        // the content in exam/problem table accordingly.
        // Disables the attendExamBtn since a different course has been selected.
        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    fetchAndUpdate(examTab, problemTab);
                }
        );
        
        // Update problemTab if the user clicks on examTab.
        // Since another exam might has been selected, we have to update
        // the content in problem table accordingly.
        // Enables the attendExamBtn since an exam has been selected.
        examTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    fetchAndUpdate(problemTab);
                }
        );
        
        // Double clicking on a course will take the user to exam tab.
        courseTable.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                tabPane.getSelectionModel().select(examTab);
            }
        });
        
        // Double clicking on a exam will take the user to exam tab.
        examTable.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                tabPane.getSelectionModel().select(problemTab);
            }
        });
    }

    /**
     * Fetches new course/exam/problem data from the server and updates
     * the contents of the tables in course/exam/problem tabs. It will only
     * display the records that contain the keyword provided by the user.
     * e.g., updating the course table also updates exam and problem table.
     *       updating the exam table also updates problem table.
     *       updating the problem table only updates problem table itself.
     * @param tabs the tables that you wish to update.
     */
    private void fetchAndUpdate(Tab... tabs) {
        keyword = (keyword == null) ? "" : keyword;

        for (Tab tab : tabs) {
            
            if (tab == courseTab) {
                courseRecords.clear();
                
                courseRecordsCache = ((Student) Session.getInstance().getCurrentUser()).getCourses();
                for (Course c : courseRecordsCache) {
                    if (c.getName().contains(keyword) | c.getTeacher().contains(keyword)) {
                        courseRecords.add(c);
                    }
                }

                courseTable.getItems().clear();
                courseTable.getItems().addAll(courseRecords);
                fetchAndUpdate(examTab);

            } else if (tab == examTab) {
                examRecords.clear();
                Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
                
                if (selectedCourse != null) {
                    examRecordsCache = selectedCourse.getExams();
                    for (Exam e : examRecordsCache) {
                        if (e.getTitle().contains(keyword) | e.getDesc().contains(keyword)) {
                            examRecords.add(e);
                        }
                    }
                }

                examTable.getItems().clear();
                examTable.getItems().addAll(examRecords);
                fetchAndUpdate(problemTab);
                
            } else if (tab == problemTab) {
                problemRecords.clear();
                Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
                
                if (selectedExam != null) {
                    problemRecordsCache = selectedExam.getProblems();
                    for (Problem p : problemRecordsCache) {
                        if (p.getTitle().contains(keyword) | p.getDesc().contains(keyword)) {
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
    
    /**
     * Similar to the fetchAndUpdate(Tab...). But this method does not fetch
     * the source of table contents from the server, it uses local cache instead.
     * This method is only useful when user wants to search for courses/exams/problems.
     * @param tabs 
     */
    private void updateLocally(Tab... tabs) {
        keyword = (keyword == null) ? "" : keyword;

        for (Tab tab : tabs) {
            
            if (tab == courseTab) {
                courseRecords.clear();
                
                for (Course c : courseRecordsCache) {
                    if (c.getName().contains(keyword) | c.getTeacher().contains(keyword)) {
                        courseRecords.add(c);
                    }
                }

                courseTable.getItems().clear();
                courseTable.getItems().addAll(courseRecords);

            } else if (tab == examTab) {
                examRecords.clear();
                Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
                
                if (selectedCourse != null) {
                    for (Exam e : examRecordsCache) {
                        if (e.getTitle().contains(keyword) | e.getDesc().contains(keyword)) {
                            examRecords.add(e);
                        }
                    }
                }

                examTable.getItems().clear();
                examTable.getItems().addAll(examRecords);
                
            } else if (tab == problemTab) {
                problemRecords.clear();
                Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
                
                if (selectedExam != null) {
                    for (Problem p : problemRecordsCache) {
                        if (p.getTitle().contains(keyword) | p.getDesc().contains(keyword)) {
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
    
    /**
     * Invoked when something is typed into the filter textbox.
     * @param event emitted by JavaFX.
     */
    public void search(KeyEvent event) {
        keyword = keywordField.getText();
        updateLocally(tabPane.getSelectionModel().getSelectedItem());
    }
    
}