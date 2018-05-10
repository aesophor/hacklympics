import java.util.*;


interface MessageListener {
    void handle(String message);
}


class EvSocketServer {
    private List<MessageListener> listeners = new ArrayList<>();

    public void addListener(MessageListener listener) {
        listeners.add(listener);
    }

    public void produceEvent(String message) {
        System.out.println(message);

        // Notify everybody that may be interested.
        for (MessageListener listener : listeners)
            listener.handle(message);
    }
}

/*
class User implements NewMessageListener {
    @Override
    public void handle(String message) {
        System.out.println("received: " + message);
    }
}
*/

public class ObserverTest {
    public static void main(String[] args) {
        EvSocketServer server = new EvSocketServer();
        
        server.addListener(new MessageListener() {
            @Override
            public void handle(String message) {
                System.out.println("handling: " + message);
            }
        });

        server.produceEvent("[event] New message");
    }
}