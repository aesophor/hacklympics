package com.hacklympics.api.communication;

public final class Config {
    
    public static final String serverHost = "192.168.19.8";
    public static final int serverPort = 8000;
    public static final int evLstnPort = 8001;
    
    private Config() {
    
    }
    
    
    public static String getURL() {
        return String.format("http://%s:%s", serverHost, serverPort);
    }
    
    public static int getEventListenerPort() {
        return evLstnPort;
    }
    
}
