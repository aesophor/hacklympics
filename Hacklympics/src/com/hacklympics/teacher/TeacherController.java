package com.hacklympics.teacher;

import java.io.IOException;
import java.util.HashMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import com.hacklympics.api.session.UserController;
import com.hacklympics.common.MainController;
import com.hacklympics.utility.FXMLTable;

public class TeacherController extends MainController implements Initializable, UserController {
    
    @Override
    protected void initPages() {
        pages = new HashMap<>();
        controllers = new HashMap<>();
        
        String dashboardFXML = FXMLTable.getInstance().get("Teacher/Dashboard");
        String coursesFXML = FXMLTable.getInstance().get("Teacher/Courses");
        String ongoingExamsFXML = FXMLTable.getInstance().get("Teacher/OngoingExams");
        String messagesFXML = FXMLTable.getInstance().get("Teacher/Messages");
        String proctorFXML = FXMLTable.getInstance().get("Teacher/Proctor");
        
        try {
            FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource(dashboardFXML));
            FXMLLoader coursesLoader = new FXMLLoader(getClass().getResource(coursesFXML));
            FXMLLoader ongoingExamsLoader = new FXMLLoader(getClass().getResource(ongoingExamsFXML));
            FXMLLoader messagesLoader = new FXMLLoader(getClass().getResource(messagesFXML));
            FXMLLoader proctorLoader = new FXMLLoader(getClass().getResource(proctorFXML));
            
            pages.put("Dashboard", dashboardLoader.load());
            pages.put("Courses", coursesLoader.load());
            pages.put("OngoingExams", ongoingExamsLoader.load());
            pages.put("Messages", messagesLoader.load());
            pages.put("Proctor", proctorLoader.load());
            
            controllers.put("Dashboard", dashboardLoader.getController());
            controllers.put("Courses", coursesLoader.getController());
            controllers.put("OngoingExams", ongoingExamsLoader.getController());
            controllers.put("Messages", messagesLoader.getController());
            controllers.put("Proctor", proctorLoader.getController());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
    
    @FXML
    public void showCourses(ActionEvent event) {
        showPage(pages.get("Courses"));
    }
    
    @FXML
    public void showOngoingExams(ActionEvent event) {
        showPage(pages.get("OngoingExams"));
    }
    
    @FXML
    public void showMessages(ActionEvent event) {
        showPage(pages.get("Messages"));
    }
    
    @FXML
    public void showProctor(ActionEvent event) {
        showPage(pages.get("Proctor"));
    }
    
}