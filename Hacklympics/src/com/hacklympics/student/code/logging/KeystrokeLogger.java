package com.hacklympics.student.code.logging;

import com.hacklympics.api.material.Exam;
import com.hacklympics.api.preference.Config;
import com.hacklympics.api.proctor.Keystroke;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Student;
import com.hacklympics.student.code.PendingCodePatches;
import com.hacklympics.utility.Utils;

public class KeystrokeLogger implements Runnable {

    private static KeystrokeLogger keystrokeLogger;
    private volatile boolean running;
    private int syncFrequency;

    private KeystrokeLogger() {
        // All students are placed in general group by default.
        syncFrequency = Config.getInstance().getGenGrpSyncFrequency();
    }

    public static KeystrokeLogger getInstance() {
        if (keystrokeLogger == null) {
            keystrokeLogger = new KeystrokeLogger();
        }

        return keystrokeLogger;
    }

    
    @Override
    public void run() {
        System.out.println("[*] Started keystroke logging thread.");
        
        running = true;
        
        Exam currentExam = Session.getInstance().getCurrentExam();
        Student currentUser = (Student) Session.getInstance().getCurrentUser();

        while (running) {
            try {
                // We must make sure no new patches can be added while
                // "flushing" (i.e., sync() & clear()) pending patches to the server. 
                synchronized (PendingCodePatches.getInstance()) {
                    // If there are changes unsynchronized, 
                    // send all keystroke patches to the server.
                    if (!PendingCodePatches.getInstance().isEmpty()) {
                        Keystroke.sync(
                                currentExam.getCourseID(),
                                currentExam.getExamID(),
                                currentUser.getUsername(),
                                PendingCodePatches.getInstance().getPatches()
                        );
                        
                        // Clears the CodeArea patches of current selected tab.
                        PendingCodePatches.getInstance().clear();
                    }
                }
                
                // Extra sleep time is to disperse students request,
                // which prevents the server from being overloaded.
                Thread.sleep(syncFrequency * 1000 + Utils.randomInt(0, 2000));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                running = false;
            }
        }

        System.out.println("[*] Stopped keystroke logging thread.");
    }

    public void shutdown() {
        running = false;
    }

    public int getSyncFrequency() {
        return syncFrequency;
    }

    public void setSyncFrequency(int syncFrequency) {
        this.syncFrequency = syncFrequency;
    }

}