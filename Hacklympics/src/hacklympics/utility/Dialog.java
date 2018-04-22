package hacklympics.utility;

import java.util.List;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

public abstract class Dialog {
    
    private static final int CANCEL_BTN = 0;
    private static final int CONFIRM_BTN = 1;
    
    protected final StackPane pane;
    protected final JFXDialog dialog;
    protected final JFXDialogLayout content;
    
    protected final List<JFXButton> buttons;
    
    public Dialog(StackPane pane, String title) {
        this.pane = pane;
        this.buttons = new ArrayList<>();
        this.content = new JFXDialogLayout();
        this.dialog = new JFXDialog(pane,
                                    content,
                                    JFXDialog.DialogTransition.CENTER);
        
        this.buttons.add(new JFXButton("Dismiss"));
        this.buttons.add(new JFXButton("OK"));
        
        this.content.setHeading(new Text(title));
        this.content.setActions(buttons);
        
        this.buttons.get(CANCEL_BTN).setOnAction((ActionEvent e) -> {
            close();
        });
    }
    
    
    public void show() {
        this.pane.setMouseTransparent(false);
        dialog.show();
    }
    
    public void close() {
        this.pane.setMouseTransparent(true);
        dialog.close();
    }
    
    
    public JFXButton getConfirmBtn() {
        return buttons.get(CONFIRM_BTN);
    }
    
    public JFXButton getCancelBtn() {
        return buttons.get(CANCEL_BTN);
    }
    
}
