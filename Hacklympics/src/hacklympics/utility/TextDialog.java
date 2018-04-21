package hacklympics.utility;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class TextDialog extends Dialog {
    
    public TextDialog(StackPane pane, String title, String body) {
        super(pane, title);
        
        this.content.setBody(new Text(body));
    }
    
}