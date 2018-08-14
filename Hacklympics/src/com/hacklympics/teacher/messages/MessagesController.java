package com.hacklympics.teacher.messages;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import com.jfoenix.controls.JFXTextArea;
import com.hacklympics.api.event.EventType;
import com.hacklympics.api.event.EventManager;
import com.hacklympics.api.event.message.NewMessageEvent;
import com.hacklympics.api.message.Message;
import com.hacklympics.api.session.Session;
import com.hacklympics.utility.ui.dialog.AlertDialog;
import com.hacklympics.api.event.EventHandler;

import javafx.scene.layout.StackPane;

public class MessagesController implements Initializable {

    @FXML
    private TextArea messageBoard;
    @FXML
    private JFXTextArea inputArea;
    @FXML
    private StackPane dialogPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setOnNewMessage((NewMessageEvent event) -> {
            if (Session.getInstance().isInExam()) {
                messageBoard.appendText(event.getMessage() + "\n");
            }
        });
    }
    
    public void setOnNewMessage(EventHandler<NewMessageEvent> listener) {
        EventManager.getInstance().addEventHandler(EventType.NEW_MESSAGE, listener);
    }
    
    
    public void send(ActionEvent event) {
        // If current user tries to send a message but he/she is not in a exam, 
        // block this attempt and alert the user.
        if (!Session.getInstance().isInExam()) {
            AlertDialog alert = new AlertDialog(
                    "Alert",
                    "You can only send message while you are in an exam."
            );

            alert.show();
            return;
        }
        
        // Send the message only if the user has entered something in inputArea.
        String message = inputArea.getText();
        
        if (!message.isEmpty()) {
            Message.create(
                    Session.getInstance().getCurrentExam().getCourseID(),
                    Session.getInstance().getCurrentExam().getExamID(),
                    Session.getInstance().getCurrentUser().getUsername(),
                    message
            );
            
            inputArea.clear();
        }
    }
    
}