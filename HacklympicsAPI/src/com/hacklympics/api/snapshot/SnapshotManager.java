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
    private static volatile boolean running;
    
    private double quality;
    private int frequency;
    
    public SnapshotManager(double quality, int frequency) {
        this.quality = quality;
        this.frequency = frequency;
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
        
        running = true;
        
        while (running) {
            try {
                BufferedImage img = Utils.takeSnapshot(quality);
                String base64img = Base64.encode(Utils.bufferedImage2ByteArray(img));
                
                Response snapshot = Snapshot.create(
                        Session.getInstance().getCurrentExam().getCourseID(),
                        Session.getInstance().getCurrentExam().getExamID(),
                        Session.getInstance().getCurrentUser().getUsername(),
                        base64img
                );
                
                Thread.sleep(frequency * 1000);
            } catch (AWTException | IOException | InterruptedException ex) {
                ex.printStackTrace();
                running = false;
            }
        }
    }
    
    public void shutdown() {
        System.out.println("[*] Shutting down snapshot thread...");
        running = false;
    }
    
    
    public double getQuality() {
        return this.quality;
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
    public void setQuality(double quality) {
        this.quality = quality;
    }
    
    public void setFrequency(int freq) {
        this.frequency = freq;
    }
    
}
