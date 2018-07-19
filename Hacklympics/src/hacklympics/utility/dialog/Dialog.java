package hacklympics.utility.dialog;

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
import static javafx.scene.layout.Region.USE_PREF_SIZE;
import javafx.scene.layout.VBox;

public abstract class Dialog {
    
    protected final AnchorPane mainPane;
    protected final StackPane dialogPane;
    protected final BoxBlur blur;
    protected final JFXDialog dialog;
    protected final JFXDialogLayout content;
    protected final Map<String, Node> buttons;
    
    public Dialog(String title) {
        this.mainPane = Session.getInstance().getMainController().getMainPane();
        this.dialogPane = Session.getInstance().getMainController().getDialogPane();
        this.blur = new BoxBlur(3, 3, 3);
        
        this.buttons = new HashMap<>();
        this.content = new JFXDialogLayout();
        this.dialog = new JFXDialog(
                this.dialogPane,
                this.content,
                JFXDialog.DialogTransition.CENTER
        );
        
        this.buttons.put("cancelBtn", new JFXButton("Dismiss"));
        this.buttons.put("confirmBtn", new JFXButton("OK"));
        
        this.content.setHeading(new WrappingText(title, dialogPane.getWidth()));
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
            this.dialogPane.setMouseTransparent(false);
        });
        
        this.dialog.setOnDialogClosed((JFXDialogEvent event) -> {
            this.mainPane.setEffect(null);
            this.dialogPane.setMouseTransparent(true);
        });
    }
    
    
    public void show() {
        // Recalculate the ideal layout Y of dialogPane (StackPane),
        // Relocating the dialogPane to the center of the main window.
        this.dialog.getParent().applyCss();
        this.dialog.getParent().layout();
        this.dialogPane.setLayoutY(this.mainPane.getHeight() / 2 - this.dialog.getHeight() / 2);
        
        this.mainPane.setEffect(blur);
        dialog.show();
    }
    
    public void close() {
        this.mainPane.setEffect(null);
        dialog.close();
    }
    
    
    public JFXButton getConfirmBtn() {
        return ((JFXButton) buttons.get("confirmBtn"));
    }
    
    public JFXButton getCancelBtn() {
        return ((JFXButton) buttons.get("cancelBtn"));
    }
    
}