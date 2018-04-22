package hacklympics.teacher.pages;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.materials.Course;
import com.hacklympics.api.materials.Exam;
import com.hacklympics.api.materials.Problem;
import com.hacklympics.api.session.CurrentUser;
import com.hacklympics.api.users.Role;
import com.hacklympics.api.users.User;
import com.hacklympics.api.users.Teacher;
import hacklympics.utility.FormDialog;
import hacklympics.utility.TextDialog;
import hacklympics.utility.UserListView;

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
    private JFXTextField keywordField;
    @FXML
    private StackPane formPane;
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

        courseTable.setOnMouseClicked((Event e) -> {
            update(examTab, problemTab);
        });
        
        examTable.setOnMouseClicked((Event e) -> {
            update(problemTab);
        });
    }

    private void update(Tab... tabs) {
        keyword = (keyword == null) ? "" : keyword;

        for (Tab tab : tabs) {
            
            if (tab == courseTab) {
                courseRecords.clear();
                
                List<Course> courses = Course.getCourses((Teacher) CurrentUser.getInstance().getUser());
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
                    List<Exam> exams = Exam.getExams(selectedCourse);
                    
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
                    List<Problem> problems = Problem.getProblems(selectedExam);
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

    public void add(ActionEvent event) {
        Tab current = tabPane.getSelectionModel().getSelectedItem();

        if (current == courseTab) {
            addCourse();
        } else if (current == examTab) {
            addExam();
        } else if (current == problemTab) {
            addProblem();
        } else {

        }
    }

    public void edit(ActionEvent event) {
        Tab current = tabPane.getSelectionModel().getSelectedItem();

        if (current == courseTab) {
            editCourse();
        } else if (current == examTab) {
            editExam();
        } else if (current == problemTab) {
            editProblem();
        } else {

        }
    }
    

    private void addCourse() {
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        FormDialog form = new FormDialog(formPane, "Add new course");
        form.addField("Name", "");
        form.addField("Semester", "");
        form.add("Students", studentsList.getListView());

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField nameField = (JFXTextField) form.get("Name");
            JFXTextField semesterField = (JFXTextField) form.get("Semester");
            String teacher = CurrentUser.getInstance().getUser().getProfile().getUsername();
            List<User> selectedStudents = studentsList.getSelected();
            
            List<String> students = new ArrayList<>();
            for (User user : selectedStudents) {
                students.add(user.getProfile().getUsername());
            }
            
            Course.create(nameField.getText(),
                    Integer.parseInt(semesterField.getText()),
                    teacher,
                    students);
            
            update(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        form.show();
    }

    private void addExam() {
        FormDialog form = new FormDialog(formPane, "Add new exam");
        form.addField("Title", "");
        form.addField("Duration", "");
        form.addField("Description", "");

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextField durationField = (JFXTextField) form.get("Duration");
            JFXTextField descField = (JFXTextField) form.get("Description");
            Course selected = courseTable.getSelectionModel().getSelectedItem();
            
            Exam.create(titleField.getText(),
                    descField.getText(),
                    Integer.parseInt(durationField.getText()),
                    selected.getData().getCourseID());
            
            update(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });

        form.show();
    }

    private void addProblem() {
        FormDialog form = new FormDialog(formPane, "Add new problem");
        form.addField("Title", "");
        form.addField("Description", "");
        
        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextField descField = (JFXTextField) form.get("Description");
            Exam selected = examTable.getSelectionModel().getSelectedItem();
            
            Problem.create(titleField.getText(),
                    descField.getText(),
                    selected.getCourseID(),
                    selected.getExamID());
            
            update(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });

        form.show();
    }

    private void editCourse() {
        Course course = courseTable.getSelectionModel().getSelectedItem();
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        FormDialog form = new FormDialog(formPane, "Edit course");
        form.addField("Name", course.getData().getName());
        form.addField("Semester", course.getData().getSemester().toString());
        form.add("Students", studentsList.getListView());
        form.addDeleteBtn("deleteBtn");

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField nameField = (JFXTextField) form.get("Name");
            JFXTextField semesterField = (JFXTextField) form.get("Semester");
            String teacher = CurrentUser.getInstance().getUser().getProfile().getUsername();
            List<User> s = studentsList.getSelected();

            List<String> students = new ArrayList<>();
            for (User user : s) {
                students.add(user.getProfile().getUsername());
            }

            course.update(nameField.getText(),
                          Integer.parseInt(semesterField.getText()),
                          teacher,
                          students);

            update(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        
        form.getAsButton("deleteBtn").setOnAction((ActionEvent e) -> {
            TextDialog dialog = new TextDialog(dialogPane,
                                               "Delete course",
                                               "Do you want to delete this course?");
        
            dialog.getConfirmBtn().setOnAction((ActionEvent confirm) -> {
                course.remove();
                update(tabPane.getSelectionModel().getSelectedItem());
                
                dialog.close();
                form.close();
            });
        
            dialog.show();
        });

        form.show();
    }

    private void editExam() {
        Exam exam = examTable.getSelectionModel().getSelectedItem();
        
        FormDialog form = new FormDialog(formPane, "Edit exam");
        form.addField("Title", exam.getData().getTitle());
        form.addField("Duration", exam.getData().getDuration().toString());
        form.addField("Description", exam.getData().getDesc());
        form.addDeleteBtn("deleteBtn");

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextField durationField = (JFXTextField) form.get("Duration");
            JFXTextField descField = (JFXTextField) form.get("Description");

            exam.update(titleField.getText(),
                        descField.getText(),
                        Integer.parseInt(durationField.getText()));

            update(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        
        ((Button) form.get("deleteBtn")).setOnAction((ActionEvent e) -> {
            TextDialog dialog = new TextDialog(dialogPane,
                                               "Delete exam",
                                               "Do you want to delete this exam?");
        
            dialog.getConfirmBtn().setOnAction((ActionEvent confirm) -> {
                exam.remove();
                update(tabPane.getSelectionModel().getSelectedItem(), problemTab);

                dialog.close();
                form.close();
            });
        
            dialog.show();
        });

        form.show();
    }

    private void editProblem() {
        Problem problem = problemTable.getSelectionModel().getSelectedItem();

        FormDialog form = new FormDialog(formPane, "Edit problem");
        form.addField("Title", problem.getData().getTitle());
        form.addField("Description", problem.getData().getDesc());
        form.addDeleteBtn("deleteBtn");

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextField descField = (JFXTextField) form.get("Description");

            problem.update(titleField.getText(),
                           descField.getText());

            update(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        
        ((Button) form.get("deleteBtn")).setOnAction((ActionEvent e) -> {
            TextDialog dialog = new TextDialog(dialogPane,
                                               "Delete problem",
                                               "Do you want to delete this problem?");
        
            dialog.getConfirmBtn().setOnAction((ActionEvent confirm) -> {
                problem.remove();
                update(tabPane.getSelectionModel().getSelectedItem());
                
                dialog.close();
                form.close();
            });
        
            dialog.show();
        });

        form.show();
    }

}