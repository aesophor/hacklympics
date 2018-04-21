package hacklympics.utility;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public abstract class Dialog {
    
    private static final int CANCEL_BTN = 0;
    private static final int CONFIRM_BTN = 1;
    
    protected final JFXDialog dialog;
    protected final JFXDialogLayout content;
    
    protected final List<JFXButton> buttons;
    
    public Dialog(StackPane pane, String title) {
        this.buttons = new ArrayList<>();
        this.content = new JFXDialogLayout();
        this.dialog = new JFXDialog(pane,
                                    content,
                                    JFXDialog.DialogTransition.CENTER);
        
        this.buttons.add(new JFXButton("Dismiss"));
        this.buttons.add(new JFXButton("OK"));
        
        this.content.setHeading(new Text(title));
        this.content.setActions(buttons);
    }
    
    
    public void show() {
        dialog.show();
    }
    
    public void close() {
        dialog.close();
    }
    
    
    public JFXButton getConfirmBtn() {
        return buttons.get(CONFIRM_BTN);
    }
    
    public JFXButton getCancelBtn() {
        return buttons.get(CANCEL_BTN);
    }
    
}
