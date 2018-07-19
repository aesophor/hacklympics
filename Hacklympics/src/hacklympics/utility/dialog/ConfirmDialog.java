package hacklympics.utility.dialog;

public class ConfirmDialog extends Dialog {
    
    public ConfirmDialog(String title, String body) {
        super(title);
        
        this.content.setBody(new WrappingText(body, this.dialogPane.getWidth()));
    }
    
}