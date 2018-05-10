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
            System.out.printf("[*] Started Event Listener on port %d\n", port);
            serverSocket = new ServerSocket(port);
            
            while (true) {
                client = serverSocket.accept();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String e = br.readLine();
                eventManager.fireEvent(new Event(e).toCorrectForm());
                
                client.close();
            }
        } catch (IOException ex) {
            
        }
    }
    
    public void close() {
        try {
            System.out.println("[*] Shutting down Event Listener...");
            serverSocket.close();
        } catch (IOException ioe) {
            System.out.println("[-] Failed to shutdown Event Listener.");
        }
    }
    
}