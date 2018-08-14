package com.hacklympics.api.communication;

import java.net.*;
import java.io.*;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.event.EventManager;

public class SocketServer implements Runnable {
    
    private static SocketServer server;
    
    private final EventManager eventManager;
    private final int port;
    private ServerSocket serverSocket;
    private Socket socket;
    
    private SocketServer(int port) {
        this.eventManager = EventManager.getInstance();
        this.port = port;
    }

    public static SocketServer getInstance() {
        if (server == null) {
            server = new SocketServer(Config.EVENT_LISTENER_PORT);
        }
        
        return server;
    }
    
    
    @Override
    public void run() {
        try {
            System.out.printf("[*] Started listening for events on port %d\n", port);
            serverSocket = new ServerSocket(port);
            
            while (true) {
                socket = serverSocket.accept();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String e = br.readLine();
                
                System.out.println("Received: " + e);
                eventManager.fireEvent(new Event(e).toConcreteEvent());
                
                socket.close();
            }
        } catch (IOException ex) {
        	
        }
        
        System.out.println("[*] Stopped listening for events.");
    }
    
    public void shutdown() {
        try {
            serverSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    
}