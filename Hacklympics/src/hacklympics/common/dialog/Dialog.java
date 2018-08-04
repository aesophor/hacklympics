package hacklympics.common.dialog;

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
    
    private static boolean showingDialog;
    
    protected final AnchorPane mainPane;
    protected final StackPane container;
    protected final BoxBlur blur;
    
    protected final JFXDialog dialog;
    protected final JFXDialogLayout content;
    protected final Map<String, Node> buttons;
    
    public Dialog(String title) {
        this.mainPane = Session.getInstance().getMainController().getMainPane();
        this.container = Session.getInstance().getMainController().getDialogPane();
        this.blur = new BoxBlur(3, 3, 3);
        
        this.buttons = new HashMap<>();
        this.content = new JFXDialogLayout();
        this.dialog = new JFXDialog(
        		this.container,
                this.content,
                JFXDialog.DialogTransition.TOP
        );
        
        this.buttons.put("cancelBtn", new JFXButton("Dismiss"));
        this.buttons.put("confirmBtn", new JFXButton("OK"));
        
        this.content.setHeading(new WrappingText(title, container.getWidth()));
        this.content.setActions(new ArrayList<>(buttons.values()));
        
        
        // The default behaviors for confirm and cancel buttons are closing
        // the dialog window.
        this.getCancelBtn().setOnAction((ActionEvent e) -> {
            this.close();
        });
        
        this.getConfirmBtn().setOnAction((ActionEvent e) -> {
            this.close();
        });
        
        this.dialog.setOnDialogOpened((JFXDialogEvent event) -> {
            // This is a workaround to an unsolvable bug :(
            // which can be reproduced by going to courses->edit->delete,
            // the dialog window will not be recentered correctly,
            // unless recenter() is called outside of CourseController::edit.
            this.recenter();
        });
        
        this.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            // When user clicks on a border of the dialog window,
            // the dialog will close but it will not invoke this.close();
            // This EventHandler deals with this problem.
            if (!showingDialog) {
                this.container.setMouseTransparent(true);
                this.mainPane.setMouseTransparent(false);
                this.mainPane.setEffect(null);
            }
        });
    }
    
    
    public void show() {
        this.container.setMouseTransparent(false);
        this.mainPane.setMouseTransparent(true);
        this.mainPane.setEffect(blur);
        this.recenter();
        
        this.dialog.show();
        showingDialog = true;
    }
    
    public void close() {
        this.container.setMouseTransparent(true);
        this.mainPane.setMouseTransparent(false);
        this.mainPane.setEffect(null);
        
        this.dialog.close();
        showingDialog = false;
    }
    
    public void recenter() {
        // Recalculate the ideal layout Y of dialogPane (StackPane),
        // Relocating the dialogPane to the center of the main window.
        this.dialog.getParent().applyCss();
        this.dialog.getParent().layout();
        
        double idealLayoutY = this.mainPane.getHeight() / 2 - this.dialog.getHeight() / 2;
        this.container.setLayoutY(idealLayoutY);
    }
    
    
    public JFXButton getConfirmBtn() {
        return ((JFXButton) buttons.get("confirmBtn"));
    }
    
    public JFXButton getCancelBtn() {
        return ((JFXButton) buttons.get("cancelBtn"));
    }
    
    public StackPane getContainer() {
        return this.container;
    }
    
}