package com.hacklympics.api.snapshot;

import com.hacklympics.api.communication.Response;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.utility.Utils;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SnapshotManager implements Runnable {
    
    public static final double GENERIC_GRP_DEFAULT_QUALITY = 0.25;
    public static final int GENERIC_GRP_DEFAULT_FREQUENCY = 5;
    
    public static final double SPECIAL_GRP_DEFAULT_QUALITY = 0.25;
    public static final int SPECIAL_GRP_DEFAULT_FREQUENCY = 5;
    
    
    private static SnapshotManager snapshotManager;
    
    private boolean running;
    private double quality;
    private int frequency;
    
    public SnapshotManager() {
        // All students will be placed in Generic Group initially.
        this.quality = GENERIC_GRP_DEFAULT_QUALITY;
        this.frequency = GENERIC_GRP_DEFAULT_FREQUENCY;
    }
    
    public static SnapshotManager getInstance() {
        if (snapshotManager == null) {
            snapshotManager = new SnapshotManager();
        }
        
        return snapshotManager;
    }

    
    @Override
    public void run() {
        System.out.println("[*] Started snapshot thread.");
        
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
        
        System.out.println("[*] Stopped snapshot thread.");
    }
    
    public void shutdown() {
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
