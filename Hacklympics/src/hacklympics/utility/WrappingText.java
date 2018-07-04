package hacklympics.utility;

import javafx.scene.text.Text;

public class WrappingText extends Text {
    
    public WrappingText(String text, double width) {
        super(text);
        this.setWrappingWidth(width);
    }
    
}
