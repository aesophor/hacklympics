package hacklympics.utility;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ConfirmDialog extends Dialog {
    
    public ConfirmDialog(StackPane pane, String title, String body) {
        super(pane, title);
        
        this.content.setBody(new Text(body));
    }
    
}