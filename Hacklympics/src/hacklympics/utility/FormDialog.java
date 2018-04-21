package hacklympics.utility;

import java.util.Map;
import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import com.jfoenix.controls.JFXTextField;

public class FormDialog extends Dialog {
    
    private static final double VBOX_SPACING = 30.0;
    
    private final VBox vbox;
    private final Map<String, Node> components;
    
    public FormDialog(StackPane pane, String title) {
        super(pane, title);
        
        this.components = new HashMap<>();
        this.vbox = new VBox();
        
        this.vbox.setSpacing(VBOX_SPACING);
        this.content.setBody(vbox);
    }
    
    
    public Node get(String identifier) {
        return components.get(identifier);
    }
    
    public void add(String identifier, Node n) {
        components.put(identifier, n);
        
        vbox.getChildren().add(components.get(identifier));
    }
    
    public void addField(String identifier, String content) {
        JFXTextField textField = new JFXTextField();
        
        textField.setLabelFloat(true);
        textField.setPromptText(identifier);
        textField.setText(content);
        
        add(identifier, textField);
    }
    
}