package hacklympics.utility;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

public abstract class Dialog {
    
    protected final StackPane pane;
    protected final JFXDialog dialog;
    protected final JFXDialogLayout content;
    protected final Map<String, Node> buttons;
    
    public Dialog(StackPane pane, String title) {
        this.pane = pane;
        this.buttons = new HashMap<>();
        this.content = new JFXDialogLayout();
        this.dialog = new JFXDialog(pane,
                                    content,
                                    JFXDialog.DialogTransition.CENTER);
        
        this.buttons.put("cancelBtn", new JFXButton("Dismiss"));
        this.buttons.put("confirmBtn", new JFXButton("OK"));
        
        this.content.setHeading(new Text(title));
        this.content.setActions(new ArrayList<>(buttons.values()));
        
        getCancelBtn().setOnAction((ActionEvent e) -> {
            close();
        });
    }
    
    
    public void show() {
        this.pane.setMouseTransparent(false);
        dialog.show();
    }
    
    public void close() {
        this.pane.setEffect(null);
        this.pane.setMouseTransparent(true);
        dialog.close();
    }
    
    
    public JFXButton getConfirmBtn() {
        return ((JFXButton) buttons.get("confirmBtn"));
    }
    
    public JFXButton getCancelBtn() {
        return ((JFXButton) buttons.get("cancelBtn"));
    }
    
}
