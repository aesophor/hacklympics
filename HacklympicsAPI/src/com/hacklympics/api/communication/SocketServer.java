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
    private Socket client;
    
    private SocketServer(int port) {
        this.eventManager = EventManager.getInstance();
        this.port = port;
    }

    public static SocketServer getInstance() {
        if (server == null) {
            server = new SocketServer(Config.getEventListenerPort());
        }
        
        return server;
    }
    
    
    @Override
    public void run() {
        try {
            System.out.printf("[*] Started listening for events on port %d\n", port);
            serverSocket = new ServerSocket(port);
            
            while (true) {
                client = serverSocket.accept();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String e = br.readLine();
                System.out.println("Received: " + e);
                eventManager.fireEvent(new Event(e).toConcreteEvent());
                
                client.close();
            }
        } catch (IOException ex) {
            
        }
    }
    
    public void close() {
        try {
            System.out.println("[*] Stopped listening for events.");
            serverSocket.close();
        } catch (IOException ioe) {
            System.out.println("[-] Failed to stop listening for events.");
        }
    }
    
}