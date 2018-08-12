package com.hacklympics.api.communication;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public final class Config {
    
    private static final String CONFIG_PATH = "config.properties";
    
    private static final String SERVER_HOSTNAME;
    private static final int SERVER_PORT;
    private static final int EVENT_LISTENER_PORT;
    
    static {
        File propertiesFile = new File(CONFIG_PATH);
        Properties prop = new Properties();
        
        // If the properties file does not exist, create one.
        if (!propertiesFile.exists()) {
            prop.setProperty("Server.hostname", "127.0.0.1");
            prop.setProperty("Server.port", "8000");
            prop.setProperty("EventListener.port", "8001");

            try {
                prop.store(new FileWriter(propertiesFile), "Hacklympics client properties");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        
        // Read the properties file.
        try {
            prop.load(new FileReader(propertiesFile));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        // Load server hostname, server port and event listener port from properties.
        SERVER_HOSTNAME = prop.getProperty("Server.hostname");
        SERVER_PORT = Integer.parseInt(prop.getProperty("Server.port"));
        EVENT_LISTENER_PORT = Integer.parseInt(prop.getProperty("EventListener.port"));
    }
    
    private Config() {
    	
    }
    
    
    public static String getURL() {
        return String.format("http://%s:%s", SERVER_HOSTNAME, SERVER_PORT);
    }
    
    public static int getEventListenerPort() {
        return EVENT_LISTENER_PORT;
    }
    
}