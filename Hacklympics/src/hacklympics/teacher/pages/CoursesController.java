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
import javafx.scene.control.Tab;
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
import javafx.event.Event;

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

        courseTable.setOnMouseClicked((Event event) -> {
            update(examTab, problemTab);
        });
        
        examTable.setOnMouseClicked((Event event) -> {
            update(problemTab);
        });
    }

    private void update(Tab... tabs) {
        keyword = (keyword == null) ? "" : keyword;

        for (Tab tab : tabs) {
            
            if (tab == courseTab) {
                courseRecords.clear();
                
                List<Course> courses = Course.getCourses();

                for (Course c : courses) {
                    if (c.getData().getName().contains(keyword) |
                        c.getData().getTeacher().contains(keyword)) {
                        courseRecords.add(c);
                    }
                }

                courseTable.getItems().clear();
                courseTable.getItems().addAll(courseRecords);

            } else if (tab == examTab) {
                examRecords.clear();
                
                Course selected = courseTable.getSelectionModel().getSelectedItem();
                List<Exam> exams = Exam.getExams(selected.getData().getCourseID());
                
                for (Exam e : exams) {
                    if (e.getData().getTitle().contains(keyword) |
                        e.getData().getDesc().contains(keyword)) {
                        examRecords.add(e);
                    }
                }

                examTable.getItems().clear();
                examTable.getItems().addAll(examRecords);
                
            } else if (tab == problemTab) {
                problemRecords.clear();
                
                Exam selected = examTable.getSelectionModel().getSelectedItem();
                
                if (selected != null) {
                    List<Problem> problems = Problem.getProblems(selected.getData().getCourseID(),
                                                                selected.getData().getExamID());

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
        formPane.setMouseTransparent(false);
        
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
        formPane.setMouseTransparent(false);
        
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
        
        FormDialog dialog = new FormDialog(formPane, "Add new course");
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
                for (User user : s) {
                    students.add(user.getProfile().getUsername());
                }

                Course.create(nameField.getText(),
                              Integer.parseInt(semesterField.getText()),
                              teacher,
                              students);

                update(tabPane.getSelectionModel().getSelectedItem());

                formPane.setMouseTransparent(true);
                dialog.close();
            }
        });

        dialog.getCancelBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                formPane.setMouseTransparent(true);
                dialog.close();
            }
        });

        dialog.show();
    }

    private void addExam() {
        FormDialog dialog = new FormDialog(formPane, "Add new exam");
        dialog.addField("Title", "");
        dialog.addField("Duration", "");
        dialog.addField("Description", "");

        dialog.getConfirmBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFXTextField titleField = (JFXTextField) dialog.get("Title");
                JFXTextField durationField = (JFXTextField) dialog.get("Duration");
                JFXTextField descField = (JFXTextField) dialog.get("Description");
                Course selected = courseTable.getSelectionModel().getSelectedItem();

                Exam.create(titleField.getText(),
                            descField.getText(),
                            Integer.parseInt(durationField.getText()),
                            selected.getData().getCourseID());

                update(tabPane.getSelectionModel().getSelectedItem());

                formPane.setMouseTransparent(true);
                dialog.close();
            }
        });

        dialog.getCancelBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                formPane.setMouseTransparent(true);
                dialog.close();
            }
        });

        dialog.show();
    }

    private void addProblem() {
        FormDialog dialog = new FormDialog(formPane, "Add new problem");
        dialog.addField("Title", "");
        dialog.addField("Description", "");

        dialog.getConfirmBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JFXTextField titleField = (JFXTextField) dialog.get("Title");
                JFXTextField descField = (JFXTextField) dialog.get("Description");
                Exam selected = examTable.getSelectionModel().getSelectedItem();

                Problem.create(titleField.getText(),
                               descField.getText(),
                               selected.getCourseID(),
                               selected.getExamID());

                update(tabPane.getSelectionModel().getSelectedItem());

                formPane.setMouseTransparent(true);
                dialog.close();
            }
        });

        dialog.getCancelBtn().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                formPane.setMouseTransparent(true);
                dialog.close();
            }
        });

        dialog.show();
    }

    private void editCourse() {
        Course course = courseTable.getSelectionModel().getSelectedItem();
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        FormDialog dialog = new FormDialog(formPane, "Edit course");
        dialog.addField("Name", course.getData().getName());
        dialog.addField("Semester", course.getData().getSemester().toString());
        dialog.add("Students", studentsList.getListView());

        dialog.getConfirmBtn().setOnAction((ActionEvent event) -> {
            JFXTextField nameField = (JFXTextField) dialog.get("Name");
            JFXTextField semesterField = (JFXTextField) dialog.get("Semester");
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

            formPane.setMouseTransparent(true);
            dialog.close();
        });

        dialog.getCancelBtn().setOnAction((ActionEvent event) -> {
            formPane.setMouseTransparent(true);
            dialog.close();
        });

        dialog.show();
    }

    private void editExam() {
        Exam exam = examTable.getSelectionModel().getSelectedItem();
        
        FormDialog dialog = new FormDialog(formPane, "Edit exam");
        dialog.addField("Title", exam.getData().getTitle());
        dialog.addField("Duration", exam.getData().getDuration().toString());
        dialog.addField("Description", exam.getData().getDesc());

        dialog.getConfirmBtn().setOnAction((ActionEvent event) -> {
            JFXTextField titleField = (JFXTextField) dialog.get("Title");
            JFXTextField durationField = (JFXTextField) dialog.get("Duration");
            JFXTextField descField = (JFXTextField) dialog.get("Description");

            exam.update(titleField.getText(),
                        descField.getText(),
                        Integer.parseInt(durationField.getText()));

            update(tabPane.getSelectionModel().getSelectedItem());

            formPane.setMouseTransparent(true);
            dialog.close();
        });

        dialog.getCancelBtn().setOnAction((ActionEvent event) -> {
            formPane.setMouseTransparent(true);
            dialog.close();
        });

        dialog.show();
    }

    private void editProblem() {
        Problem problem = problemTable.getSelectionModel().getSelectedItem();

        FormDialog dialog = new FormDialog(formPane, "Edit problem");
        dialog.addField("Title", problem.getData().getTitle());
        dialog.addField("Description", problem.getData().getDesc());

        dialog.getConfirmBtn().setOnAction((ActionEvent event) -> {
            JFXTextField titleField = (JFXTextField) dialog.get("Title");
            JFXTextField descField = (JFXTextField) dialog.get("Description");

            problem.update(titleField.getText(),
                           descField.getText());

            update(tabPane.getSelectionModel().getSelectedItem());

            formPane.setMouseTransparent(true);
            dialog.close();
        });

        dialog.getCancelBtn().setOnAction((ActionEvent event) -> {
            formPane.setMouseTransparent(true);
            dialog.close();
        });

        dialog.show();
    }

}
