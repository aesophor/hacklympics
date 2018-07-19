package hacklympics.common.dialog;

import java.util.ArrayList;

public class AlertDialog extends Dialog {
    
    public AlertDialog(String title, String body) {
        super(title);
        
        this.content.setBody(new WrappingText(body, this.container.getWidth()));
        this.buttons.remove("confirmBtn");
        
        this.content.setActions(new ArrayList<>(buttons.values()));
    }
    
}
