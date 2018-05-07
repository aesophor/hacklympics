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
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ioe) {
            System.out.println("[-] Unable to start EventListener: " + ioe);
        }
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
            while (true) {
                client = serverSocket.accept();
                
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String raw = br.readLine();
                
                EventHandler.handle(new Event(raw));
                System.out.printf("[event] %s\n", raw);
                
                client.close();
            }
        } catch (IOException ioe) {
            System.out.println("Shutting down...");
            ioe.printStackTrace();
        }
    }
    
    
    public ServerSocket getServerSocket() {
        return serverSocket;
    }
    
    public Socket getClient() {
        return client;
    }

}
