package com.hacklympics.api.event;

import java.net.*;
import java.io.*;
import com.hacklympics.api.communication.Config;

public class EventListener implements Runnable {

    private static EventListener eventListener;
    
    private ServerSocket serverSocket;
    private Socket client;
    private int port;
    
    private EventListener(int port) {
        this.port = port;
    }

    public static EventListener getInstance() {
        if (eventListener == null) {
            eventListener = new EventListener(Config.getEventListenerPort());
        }
        
        return eventListener;
    }

    
    @Override
    public void run() {
        System.out.printf("[*] Started Event Listener on port %d\n", port);
        
        try {
            serverSocket = new ServerSocket(port);
            
            while (true) {
                client = serverSocket.accept();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String raw = br.readLine();
                
                EventHandler.handle(new Event(raw));
                System.out.printf("[event] %s\n", raw);
                
                client.close();
            }
        } catch (IOException ioe) {
            
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