package hacklympics.utility;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;

public class DialogForm {
    
    private static final double VBOX_SPACING = 30.0;
    private static final int CANCEL_BTN = 0;
    private static final int CONFIRM_BTN = 1;
    
    private final JFXDialog dialog;
    private final JFXDialogLayout content;
    private final Map<String, Node> components;
    private final List<JFXButton> buttons;
    private final VBox vbox;
    
    public DialogForm(StackPane pane, String title) {
        this.content = new JFXDialogLayout();
        this.components = new HashMap<>();
        this.buttons = new ArrayList<>();
        this.vbox = new VBox();
        
        this.dialog = new JFXDialog(pane,
                                    content,
                                    JFXDialog.DialogTransition.CENTER);
        
        this.vbox.setSpacing(VBOX_SPACING);
        
        this.buttons.add(new JFXButton("Dismiss"));
        this.buttons.add(new JFXButton("OK"));
        
        this.content.setHeading(new Text(title));
        this.content.setBody(vbox);
        this.content.setActions(buttons);
    }
    
    
    public Node get(String identifier) {
        return components.get(identifier);
    }
    
    public void add(String identifier, Node n) {
        components.put(identifier, n);
        
        vbox.getChildren().add(components.get(identifier));
    }
    
    public void addField(String identifier) {
        JFXTextField textField = new JFXTextField();
        
        textField.setLabelFloat(true);
        textField.setPromptText(identifier);
        
        add(identifier, textField);
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
