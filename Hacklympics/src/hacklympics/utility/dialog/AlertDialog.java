package hacklympics.utility.dialog;

import java.util.ArrayList;

public class AlertDialog extends Dialog {
    
    public AlertDialog(String title, String body) {
        super(title);
        
        this.content.setBody(new WrappingText(body, this.dialogPane.getWidth()));
        this.buttons.remove("confirmBtn");
        
        this.content.setActions(new ArrayList<>(buttons.values()));
    }
    
}
