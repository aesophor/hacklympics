package com.hacklympics.teacher.courses;

import com.hacklympics.api.communication.Response;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.hacklympics.api.material.Course;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.material.Problem;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Role;
import com.hacklympics.api.user.User;
import com.hacklympics.teacher.TeacherController;
import com.hacklympics.teacher.proctor.ProctorController;
import com.hacklympics.utility.dialog.AlertDialog;
import com.hacklympics.utility.dialog.ConfirmDialog;
import com.hacklympics.utility.dialog.FormDialog;
import com.hacklympics.api.user.Teacher;

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
    @FXML
    private TableColumn<Problem, String> probInputCol;
    @FXML
    private TableColumn<Problem, Integer> probOutputCol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTables();
        fetchAndUpdate(courseTab);
    }

    /**
     * Initializes the three primary tables: Course, Exam and Problem.
     * Whenever the user clicks on the courseTab/examTab, the relevant
     * subtables will be refreshed.
     * In addition, it sets up the mouse behavior which allows the user
     * to launch an exam upon which is double clicked.
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
        probInputCol.setCellValueFactory(new PropertyValueFactory<>("input"));
        probOutputCol.setCellValueFactory(new PropertyValueFactory<>("output"));
        
        
        // Override the default empty message of tables.
        courseTable.setPlaceholder(new Label("You have not created any course yet."));
        examTable.setPlaceholder(new Label("No exams under the selected course."));
        problemTable.setPlaceholder(new Label("No problems under the selected exam."));
        
        
        // Update examTab and problemTab if the user clicks on courseTab.
        // Since another course might has been selected, we have to update
        // the content in exam/problem table accordingly.
        courseTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    fetchAndUpdate(examTab, problemTab);
                }
        );
        
        // Update problemTab if the user clicks on examTab.
        // Since another exam might has been selected, we have to update
        // the content in problem table accordingly.
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
                
                courseRecordsCache = ((Teacher) Session.getInstance().getCurrentUser()).getCourses();
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
    @FXML
    public void search(KeyEvent event) {
        keyword = keywordField.getText();
        updateLocally(tabPane.getSelectionModel().getSelectedItem());
    }
    

    /**
     * Called when the add button is clicked.
     * @param event emitted by JavaFX.
     */
    @FXML
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

    /**
     * Called when the edit button is clicked.
     * @param event emitted by JavaFX.
     */
    public void edit(ActionEvent event) {
        Tab current = tabPane.getSelectionModel().getSelectedItem();

        if (current == courseTab) {
            Course c = courseTable.getSelectionModel().getSelectedItem();
            if (c != null) editCourse(c);
            
        } else if (current == examTab) {
            Exam e = examTable.getSelectionModel().getSelectedItem();
            if (e != null) editExam(e);
            
        } else if (current == problemTab) {
            Problem p = problemTable.getSelectionModel().getSelectedItem();
            if (p != null) editProblem(p);
            
        } else {
            
        }
    }
    
    /**
     * Launches the specified exam.
     * Asks the user for confirmation for launching the exam.
     * If the user answers yes, then he/she will be taken to the proctor page.
     */
    @FXML
    public void launchExam(ActionEvent event) {
        // If the user is trying to "launch a course/problem",
        // block this attempt and alert the user.
        if (tabPane.getSelectionModel().getSelectedItem() != examTab) {
            AlertDialog alert = new AlertDialog(
                    "Alert",
                    "Please note that only an exam can be launched."
            );
            
            alert.show();
            return;
        }
        
        // If the user is try to launch an exam, but no exam is selected,
        // block this attempt and alert the user.
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        
        if (selectedExam == null) {
            AlertDialog alert = new AlertDialog(
                    "Alert",
                    "You have not selected any exam to launch."
            );
            
            alert.show();
            return;
        }
        
        // If everything alright, then ask the user for confirmation.
        // If yes, then we will proceed.
        ConfirmDialog dialog = new ConfirmDialog(
                "Launch Exam",
                "You have selected the exam: " + selectedExam + "\n\n"
              + "Launch the exam now? (Other teachers will also be able to "
              + "proctor your exam.)"
        );
        
        dialog.getConfirmBtn().setOnAction((ActionEvent e) -> {
            Response launch = selectedExam.launch();
            
            switch (launch.getStatusCode()) {
                case SUCCESS:
                    Session.getInstance().setCurrentExam(selectedExam);
            
                    TeacherController tc = (TeacherController) Session.getInstance().getMainController();
                    ProctorController cc = (ProctorController) tc.getControllers().get("Proctor");
            
                    cc.reset();
                    cc.setExamLabel(selectedExam.getTitle(), selectedExam.getRemainingTime());
                    cc.enableHaltExamBtn();
                    tc.showProctor(e);
                    
                    dialog.close();
                    break;
                    
                case ALREADY_LAUNCHED:
                    AlertDialog alert = new AlertDialog(
                            "Exam Already Launched",
                            "The selected exam has already been launched."
                    );
                    
                    dialog.close();
                    alert.show();
                    break;
                    
                default:
                    break;
            }
        });
        
        dialog.show();
    }
    

    /**
     * Popups a FormDialog which allows the user to add a course.
     */
    private void addCourse() {
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        FormDialog form = new FormDialog("Add new course");
        form.addTextField("Name", "");
        form.addTextField("Semester", "");
        form.add("Students", studentsList);

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField nameField = (JFXTextField) form.get("Name");
            JFXTextField semesterField = (JFXTextField) form.get("Semester");
            String teacher = Session.getInstance().getCurrentUser().getUsername();
            List<User> selectedStudents = studentsList.getSelectionModel().getSelectedItems();
            
            List<String> students = new ArrayList<>();
            for (User user : selectedStudents) {
                students.add(user.getUsername());
            }
            
            Course.create(
                    nameField.getText(),
                    Integer.parseInt(semesterField.getText()),
                    teacher,
                    students
            );
            
            fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        form.show();
    }

    /**
     * Popups a FormDialog which allows the user to add an exam.
     */
    private void addExam() {
        FormDialog form = new FormDialog("Add new exam");
        form.addTextField("Title", "");
        form.addTextField("Duration", "");
        form.addTextField("Description", "");
        
        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextField durationField = (JFXTextField) form.get("Duration");
            JFXTextField descField = (JFXTextField) form.get("Description");
            Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
            
            Exam.create(
                    selectedCourse.getCourseID(),
                    titleField.getText(),
                    descField.getText(),
                    Integer.parseInt(durationField.getText())
            );
            
            fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });

        form.show();
    }

    /**
     * Popups a FormDialog which allows the user to add a problem.
     */
    private void addProblem() {
        FormDialog form = new FormDialog("Add new problem");
        form.addTextField("Title", "");
        form.addTextArea("Description", "");
        form.addTextArea("Input", "");
        form.addTextArea("Output", "");
        
        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextArea descField = (JFXTextArea) form.get("Description");
            JFXTextArea inputArea = (JFXTextArea) form.get("Input");
            JFXTextArea outputArea = (JFXTextArea) form.get("Output");
            Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
            
            Problem.create(
                    selectedExam.getCourseID(),
                    selectedExam.getExamID(),
                    titleField.getText(),
                    descField.getText(),
                    inputArea.getText(),
                    outputArea.getText()
            );
            
            fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });

        form.show();
    }

    /**
     * Popups a FormDialog which allows the user to edit an existing course.
     */
    private void editCourse(Course course) {
        UserListView studentsList = new UserListView(SelectionMode.MULTIPLE, Role.STUDENT);
        
        FormDialog form = new FormDialog("Edit course");
        form.addTextField("Name", course.getName());
        form.addTextField("Semester", course.getSemester().toString());
        form.add("Students", studentsList);
        form.addDeleteBtn("deleteBtn");
        
        for (String username : course.getStudents()) {
            for (User user : studentsList.getItems()) {
                if (username.equals(user.getUsername())) {
                    studentsList.getSelectionModel().select(user);
                }
            }
        }

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField nameField = (JFXTextField) form.get("Name");
            JFXTextField semesterField = (JFXTextField) form.get("Semester");
            String teacher = Session.getInstance().getCurrentUser().getUsername();
            List<User> s = studentsList.getSelectionModel().getSelectedItems();

            List<String> students = new ArrayList<>();
            for (User user : s) {
                students.add(user.getUsername());
            }

            course.update(
                    nameField.getText(),
                    Integer.parseInt(semesterField.getText()),
                    teacher,
                    students
            );

            fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        
        form.getAsButton("deleteBtn").setOnAction((ActionEvent e) -> {
            ConfirmDialog dialog = new ConfirmDialog(
                    "Delete course",
                    "Do you want to delete this course?"
            );
        
            dialog.getConfirmBtn().setOnAction((ActionEvent confirm) -> {
                course.remove();
                fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
                
                dialog.close();
            });
        
            form.close();
            dialog.show();
        });

        form.show();
    }

    /**
     * Popups a FormDialog which allows the user to edit an existing exam.
     */
    private void editExam(Exam exam) {    
        FormDialog form = new FormDialog("Edit exam");
        form.addTextField("Title", exam.getTitle());
        form.addTextField("Duration", exam.getDuration().toString());
        form.addTextField("Description", exam.getDesc());
        form.addDeleteBtn("deleteBtn");

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextField durationField = (JFXTextField) form.get("Duration");
            JFXTextField descField = (JFXTextField) form.get("Description");

            exam.update(
                    titleField.getText(),
                    descField.getText(),
                    Integer.parseInt(durationField.getText())
            );

            fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        
        ((Button) form.get("deleteBtn")).setOnAction((ActionEvent e) -> {
            ConfirmDialog dialog = new ConfirmDialog(
                    "Delete exam",
                    "Do you want to delete this exam?"
            );
        
            dialog.getConfirmBtn().setOnAction((ActionEvent confirm) -> {
                exam.remove();
                fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem(), problemTab);

                dialog.close();
            });
        
            form.close();
            dialog.show();
        });

        form.show();
    }

    /**
     * Popups a FormDialog which allows the user to edit an existing problem.
     */
    private void editProblem(Problem problem) {
        FormDialog form = new FormDialog("Edit problem");
        form.addTextField("Title", problem.getTitle());
        form.addTextArea("Description", problem.getDesc());
        form.addTextArea("Input", problem.getInput());
        form.addTextArea("Output", problem.getOutput());
        form.addDeleteBtn("deleteBtn");

        form.getConfirmBtn().setOnAction((ActionEvent e) -> {
            JFXTextField titleField = (JFXTextField) form.get("Title");
            JFXTextArea descField = (JFXTextArea) form.get("Description");
            JFXTextArea inputArea = (JFXTextArea) form.get("Input");
            JFXTextArea outputArea = (JFXTextArea) form.get("Output");

            problem.update(
                    titleField.getText(),
                    descField.getText(),
                    inputArea.getText(),
                    outputArea.getText()
            );

            fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
            form.close();
        });
        
        
        ((Button) form.get("deleteBtn")).setOnAction((ActionEvent e) -> {
            ConfirmDialog dialog = new ConfirmDialog(
                    "Delete problem",
                    "Do you want to delete this problem?"
            );
            
            dialog.getConfirmBtn().setOnAction((ActionEvent confirm) -> {
                problem.remove();
                fetchAndUpdate(tabPane.getSelectionModel().getSelectedItem());
                
                dialog.close();
            });
        
            form.close();
            dialog.show();
        });

        form.show();
    }

}