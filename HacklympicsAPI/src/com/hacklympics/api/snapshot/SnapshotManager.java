package com.hacklympics.api.snapshot;

import com.hacklympics.api.communication.Response;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.utility.Utils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SnapshotManager implements Runnable {
    
    private static SnapshotManager snapshotManager;
    private boolean running;
    private double scale;
    private int freq;
    
    public SnapshotManager(double scale, int freq) {
        this.running = false;
        this.scale = scale;
        this.freq = freq;
    }
    
    public static SnapshotManager getInstance() {
        if (snapshotManager == null) {
            snapshotManager = new SnapshotManager(0.25, 5);
        }
        
        return snapshotManager;
    }

    
    @Override
    public void run() {
        System.out.println("[*] Starting snapshot thread...");
        
        this.running = true;
        
        while (running) {
            try {
                BufferedImage img = Utils.takeSnapshot(scale);
                String base64img = Base64.encode(Utils.bufferedImage2ByteArray(img));
                
                Response snapshot = Snapshot.create(
                        Session.getInstance().getCurrentExam().getCourseID(),
                        Session.getInstance().getCurrentExam().getExamID(),
                        Session.getInstance().getCurrentUser().getUsername(),
                        base64img
                );
                
                Thread.sleep(freq * 1000);
            } catch (AWTException | IOException | InterruptedException ex) {
                ex.printStackTrace();
                this.running = false;
            }
        }
    }
    
    public void shutdown() {
        System.out.println("[*] Shutting down anpshot thread...");
        this.running = false;
    }
    
    
    public double getScale() {
        return this.scale;
    }
    
    public int getFreq() {
        return this.freq;
    }
    
    public void setScale(double scale) {
        this.scale = scale;
    }
    
    public void setFreq(int freq) {
        this.freq = freq;
    }
    
}
