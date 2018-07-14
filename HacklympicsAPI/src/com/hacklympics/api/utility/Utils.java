package com.hacklympics.api.utility;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.NetworkInterface;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import com.google.gson.Gson;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.hacklympics.api.communication.Config;
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import net.coobird.thumbnailator.Thumbnails;

public class Utils {
    
    private static final MediaType JSON;
    private static final OkHttpClient CLIENT;
    private static final Gson GSON;
    
    static {
        JSON = MediaType.parse("application/json; charset=utf-8");
        CLIENT = new OkHttpClient();
        GSON = new Gson();
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
    
    public static BufferedImage takeSnapshot(double scale) throws AWTException, IOException {
        BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
        BufferedImage thumbnail = Thumbnails.of(image).scale(scale).asBufferedImage();
        
        return thumbnail;
    }
    
    public static byte[] bufferedImage2ByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
    
    public static BufferedImage byteArray2BufferedImage(byte[] bytes) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
    
    public static Image bufferedImage2FXImage(BufferedImage bufferedImage) {
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
    
    public static String formatTime(int seconds) {
        return String.format("%02d:%02d", (seconds / 60), (seconds % 60));
    }
    
    public static String hash(String s) {
        return s;
    }
    
    
    public static Gson getGson() {
        return GSON;
    }
    
}
