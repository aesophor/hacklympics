package com.hacklympics.api.communication;

public final class Config {
    
    public static final String serverHost = "127.0.0.1";
    public static final int serverPort = 8000;
    
    private Config() {}
    
    public static String getURL() {
        return String.format("http://%s:%s", serverHost, serverPort);
    }
}
