package hacklympics.teacher.proctor;


import com.hacklympics.api.proctor.Snapshot;
import java.util.List;
import java.util.ArrayList;
import java.util.Base64;
import java.io.IOException;
import java.awt.AWTException;
import java.awt.image.BufferedImage;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.utility.ImageUtils;

public class SnapshotManager implements Runnable {
    
    public static final List<Double> QUALITY_OPTIONS = new ArrayList<>();
    public static final List<Integer> FREQUENCY_OPTIONS = new ArrayList<>();
    
    public static final double GENERIC_GRP_DEFAULT_QUALITY = 0.25;
    public static final int GENERIC_GRP_DEFAULT_FREQUENCY = 5;
    
    public static final double SPECIAL_GRP_DEFAULT_QUALITY = 0.50;
    public static final int SPECIAL_GRP_DEFAULT_FREQUENCY = 3;
    
    static {
        // Available quality options (scale of original snapshot).
        QUALITY_OPTIONS.add(0.15);
        QUALITY_OPTIONS.add(0.25);
        QUALITY_OPTIONS.add(0.50);
        QUALITY_OPTIONS.add(0.75);
        QUALITY_OPTIONS.add(1.00);
        
        // Available frequency options (snapshot per sec).
        FREQUENCY_OPTIONS.add(3);
        FREQUENCY_OPTIONS.add(5);
        FREQUENCY_OPTIONS.add(8);
        FREQUENCY_OPTIONS.add(10);
        FREQUENCY_OPTIONS.add(15);
    }
    
    
    private static SnapshotManager snapshotManager;
    
    private boolean running;
    private double quality;
    private int frequency;
    
    private SnapshotManager() {
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
                BufferedImage img = ImageUtils.takeSnapshot(quality);
                byte[] imgByteArray = ImageUtils.bufferedImage2ByteArray(img);
                String base64img = Base64.getEncoder().encodeToString(imgByteArray);
                
                Snapshot.create(
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
