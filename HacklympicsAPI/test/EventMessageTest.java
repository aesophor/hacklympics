import com.hacklympics.api.event.Event;

public class EventMessageTest {
    
    public static void main(String[] args) {
        Event msg = new Event("{\"eventType\": 1, \"content\": {\"user\": \"JimRat\"}}");
        System.out.println(msg.toString());
    }
    
}
