package com.hacklympics.api.utility;

import java.util.Enumeration;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.hacklympics.api.communication.Config;

public class Utils {
    
    private static final MediaType JSON;
    private static final OkHttpClient CLIENT;
    
    static {
        JSON = MediaType.parse("application/json; charset=utf-8");
        CLIENT = new OkHttpClient();
    }
    
    private Utils() {}
    
    
    public static String get(String uri) {
        String url = String.format("%s/%s", Config.getURL(), uri);
        Request request = new Request.Builder().url(url).build();
        
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String post(String uri, String json) {
        String url = String.format("%s/%s", Config.getURL(), uri);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        
        try (Response response = CLIENT.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String getLocalAddress() {
        try {
            Enumeration ifaces = NetworkInterface.getNetworkInterfaces();
            
            while (ifaces.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) ifaces.nextElement();
                Enumeration addresses = n.getInetAddresses();
            
                while (addresses.hasMoreElements()) {
                    InetAddress address = (InetAddress) addresses.nextElement();
                    String addr = address.getHostAddress();
                
                    if (addr.startsWith("192.168") | addr.startsWith("172.16")) {
                        return addr;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        
        return null;
    }
    
    public static String hash(String s) {
        return s;
    }
    
}
