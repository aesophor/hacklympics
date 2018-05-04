package hacklympics.utility;

import java.util.ArrayList;
import javafx.scene.text.Text;
import javafx.scene.layout.StackPane;

public class AlertDialog extends Dialog {
    
    public AlertDialog(StackPane pane, String title, String body) {
        super(pane, title);
        
        this.content.setBody(new Text(body));
        this.buttons.remove("confirmBtn");
        
        this.content.setActions(new ArrayList<>(buttons.values()));
    }
    
}
