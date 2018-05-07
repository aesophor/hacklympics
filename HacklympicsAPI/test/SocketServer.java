import java.net.*;
import java.io.*;

public class SocketServer {
    
    private static final int PORT = 9527;
    
    public static void main(String[] args) {
        try {
            ServerSocket sock = new ServerSocket(PORT);
            
            System.out.printf("[*] Starting socket server on port %d\n", PORT);
            
            /* Now listen for connections. */
            Socket client = sock.accept();
            
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            
            String raw;
            
            while ((raw = br.readLine()) != null) {
                System.out.printf("Received: %s\n", raw);
                bw.write( String.format("I received: %s\n", raw) );
            }
            
            client.close();
            sock.close();
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
}