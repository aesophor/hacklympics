import com.hacklympics.api.event.Event;
import com.hacklympics.api.event.EventHandler;

public class EventMessageTest {
    
    public static void main(String[] args) {
        /*
        Event msg = new Event("{\"eventType\": 1, \"content\": {\"user\": \"JimRat\"}}");
        System.out.println(msg.toString());
        */
        
        Event login = new Event("{\"content\": {\"isStudent\": true, \"graduationYear\": 108, \"fullname\": \"Jimmy Xie\", \"username\": \"1080630212\"}, \"eventType\": 0}");
        EventHandler.handle(login);
    }
    
}
