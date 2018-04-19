package hacklympics.teacher;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import com.hacklympics.api.communication.Response;
import com.hacklympics.api.users.User;
import com.hacklympics.api.users.Teacher;

public class TeacherController implements Initializable {
    
    private Teacher teacher;
    
    private static Map<String, String> fxmls;
    private AnchorPane dashboard;
    private AnchorPane students;
    private AnchorPane courses;
    private AnchorPane exams;
    private AnchorPane problems;
    
    @FXML
    private AnchorPane holderPane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Label bannerMsg;
    @FXML
    private JFXButton logoutBtn;
    @FXML
    private JFXListView onlineUsersList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initOnlineUsersList();
        initPages();
        showPage(dashboard);
    }
    
    
    private void initPages() {
        fxmls = new HashMap<>();
        fxmls.put("dashboard", "/hacklympics/teacher/pages/Dashboard.fxml");
        fxmls.put("students",  "/hacklympics/teacher/pages/Students.fxml");
        fxmls.put("courses",   "/hacklympics/teacher/pages/Courses.fxml");
        fxmls.put("exams",     "/hacklympics/teacher/pages/Exams.fxml");
        fxmls.put("problems",  "/hacklympics/teacher/pages/Problems.fxml");
        
        try {
            dashboard = FXMLLoader.load(getClass().getResource(fxmls.get("dashboard")));
            students = FXMLLoader.load(getClass().getResource(fxmls.get("students")));
            courses = FXMLLoader.load(getClass().getResource(fxmls.get("courses")));
            exams = FXMLLoader.load(getClass().getResource(fxmls.get("exams")));
            problems = FXMLLoader.load(getClass().getResource(fxmls.get("problems")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    private void showPage(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
    }
    
    private void initOnlineUsersList() {
        Label andrey = new Label("Andrey");
        andrey.getStyleClass().add("online-user-label");
        onlineUsersList.getItems().add(andrey);
    }
    
    public void logout(ActionEvent event) {
        stackPane.setMouseTransparent(false);
        
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Alert"));
        content.setBody(new Text("You are about to be logged out. Are you sure?"));
        
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        JFXButton confirmBtn = new JFXButton("Yes");
        JFXButton cancelBtn = new JFXButton("No");
        cancelBtn.setDefaultButton(true);
        
        List<JFXButton> buttons = new ArrayList<>();
        buttons.add(cancelBtn);
        buttons.add(confirmBtn);
        
        confirmBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Response logout = User.logout(teacher.getProfile().getUsername());
        
                if (logout.success()) {
                    logoutBtn.getScene().getWindow().hide();
            
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/hacklympics/login/Login.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
    
    
    public void showDashboard(ActionEvent event) {
        showPage(dashboard);
    }
    
    public void showStudents(ActionEvent event) {
        showPage(students);
    }
    
    public void showCourses(ActionEvent event) {
        showPage(courses);
    }
    
    public void showExams(ActionEvent event) {
        showPage(exams);
    }
    
    public void showProblems(ActionEvent event) {
        showPage(problems);
    }
    
    
    public Teacher getTeacher() {
        return teacher;
    }
    
    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
        
        String greetMsg = String.format("Welcome, %s", teacher.getProfile().getFullname());
        bannerMsg.setText(greetMsg);
    }
    
}
