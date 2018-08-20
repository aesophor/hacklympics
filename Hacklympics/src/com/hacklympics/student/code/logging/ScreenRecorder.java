package com.hacklympics.student.code.logging;

import java.util.Base64;
import java.io.IOException;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import com.hacklympics.api.preference.Config;
import com.hacklympics.api.proctor.Snapshot;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.utility.ImageUtils;

public class ScreenRecorder implements Runnable {
    
    private static ScreenRecorder screenRecorder;
    private volatile boolean running;
    private double snapshotQuality;
    private int syncFrequency;
    
    private ScreenRecorder() {
        // All students will be placed in Generic Group initially.
        snapshotQuality = Config.getInstance().getGenGrpSnapshotQuality();
        syncFrequency = Config.getInstance().getGenGrpSyncFrequency();
    }
    
    public static ScreenRecorder getInstance() {
        if (screenRecorder == null) {
            screenRecorder = new ScreenRecorder();
        }
        
        return screenRecorder;
    }

    
    @Override
    public void run() {
        System.out.println("[*] Started snapshot thread.");
        
        running = true;
        
        while (running) {
            try {
                BufferedImage img = ImageUtils.takeSnapshot(snapshotQuality);
                byte[] imgByteArray = ImageUtils.bufferedImage2ByteArray(img);
                String base64img = Base64.getEncoder().encodeToString(imgByteArray);
                
                Snapshot.sync(
                        Session.getInstance().getCurrentExam().getCourseID(),
                        Session.getInstance().getCurrentExam().getExamID(),
                        Session.getInstance().getCurrentUser().getUsername(),
                        base64img
                );
                
                Thread.sleep(syncFrequency * 1000);
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
    
    
    public double getSnapshotQuality() {
        return snapshotQuality;
    }
    
    public int getSyncFrequency() {
        return syncFrequency;
    }
    
    public void setSnapshotQuality(double snapshotQuality) {
        this.snapshotQuality = snapshotQuality;
    }
    
    public void setSyncFrequency(int syncFrequency) {
        this.syncFrequency = syncFrequency;
    }
    
}