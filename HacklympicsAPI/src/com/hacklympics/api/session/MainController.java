package com.hacklympics.api.session;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public interface MainController {

    /**
     * Gets the mainPane of either StudentController or TeacherController
     * depending on the current user's role. This method is for
     * setting/canceling the blurring effect of all dialog windows.
     *
     * @return the mainPane of current user's MainController.
     */
    public AnchorPane getMainPane();

    /**
     * Gets the dialogPane of either StudentController or TeacherController
     * depending on the current user's role. You can display a dialog on the
     * retrieved dialog pane.
     *
     * @return the dialogPane of current user's MainController.
     */
    public StackPane getDialogPane();
    
    /**
     * Push a notification to the user.
     */
    public void pushNotification(String message);

}