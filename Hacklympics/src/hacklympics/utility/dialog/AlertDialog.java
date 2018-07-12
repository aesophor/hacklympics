package hacklympics.utility.dialog;

import hacklympics.utility.WrappingText;
import java.util.ArrayList;
import javafx.scene.layout.StackPane;

public class AlertDialog extends Dialog {
    
    public AlertDialog(StackPane pane, String title, String body) {
        super(pane, title);
        
        this.content.setBody(new WrappingText(body, pane.getWidth()));
        this.buttons.remove("confirmBtn");
        
        this.content.setActions(new ArrayList<>(buttons.values()));
    }
    
}
