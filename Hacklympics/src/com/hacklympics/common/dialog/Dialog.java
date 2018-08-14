package com.hacklympics.common.dialog;

import com.hacklympics.api.session.Session;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.events.JFXDialogEvent;

public abstract class Dialog {
    
    private static boolean showing;
    
    protected final AnchorPane mainPane;
    protected final StackPane container;
    protected final BoxBlur blur;
    
    protected final JFXDialog dialog;
    protected final JFXDialogLayout content;
    protected final Map<String, Node> buttons;
    
    public Dialog(String title) {
        mainPane = Session.getInstance().getMainController().getMainPane();
        container = Session.getInstance().getMainController().getDialogPane();
        
        blur = new BoxBlur(3, 3, 3);
        
        buttons = new HashMap<>();
        content = new JFXDialogLayout();
        dialog = new JFXDialog(
        		container,
                content,
                JFXDialog.DialogTransition.TOP
        );
        
        buttons.put("cancelBtn", new JFXButton("Dismiss"));
        buttons.put("confirmBtn", new JFXButton("OK"));
        
        content.setHeading(new WrappingText(title, container.getWidth()));
        content.setActions(new ArrayList<>(buttons.values()));
        
        
        // The default behaviors for confirm and cancel buttons are closing
        // the dialog window.
        getCancelBtn().setOnAction((ActionEvent e) -> {
            close();
        });
        
        getConfirmBtn().setOnAction((ActionEvent e) -> {
            close();
        });
        
        dialog.setOnDialogOpened((JFXDialogEvent event) -> {
            // This is a workaround to an unsolvable bug :(
            // which can be reproduced by going to courses->edit->delete,
            // the dialog window will not be recentered correctly,
            // unless recenter() is called outside of CourseController::edit.
            recenter();
        });
        
        dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            // When user clicks on a border of the dialog window,
            // the dialog will close but it will not invoke this.close();
            // This EventHandler deals with this problem.
            if (!showing) {
                container.setMouseTransparent(true);
                mainPane.setMouseTransparent(false);
                mainPane.setEffect(null);
            }
        });
    }
    
    
    public void show() {
        container.setMouseTransparent(false);
        mainPane.setMouseTransparent(true);
        mainPane.setEffect(blur);
        recenter();
        
        dialog.show();
        showing = true;
    }
    
    public void close() {
        container.setMouseTransparent(true);
        mainPane.setMouseTransparent(false);
        mainPane.setEffect(null);
        
        dialog.close();
        showing = false;
    }
    
    public void recenter() {
        // Recalculate the ideal layout Y of dialogPane (StackPane),
        // Relocating the dialogPane to the center of the main window.
        dialog.getParent().applyCss();
        dialog.getParent().layout();
        
        double idealLayoutY = mainPane.getHeight() / 2 - dialog.getHeight() / 2;
        container.setLayoutY(idealLayoutY);
    }
    
    
    public JFXButton getConfirmBtn() {
        return ((JFXButton) buttons.get("confirmBtn"));
    }
    
    public JFXButton getCancelBtn() {
        return ((JFXButton) buttons.get("cancelBtn"));
    }
    
    public StackPane getContainer() {
        return container;
    }
    
}