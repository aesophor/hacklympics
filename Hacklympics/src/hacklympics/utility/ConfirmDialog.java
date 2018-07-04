package hacklympics.utility;

import javafx.scene.layout.StackPane;

public class ConfirmDialog extends Dialog {
    
    public ConfirmDialog(StackPane pane, String title, String body) {
        super(pane, title);
        
        this.content.setBody(new WrappingText(body, pane.getWidth()));
    }
    
}