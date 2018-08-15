package com.hacklympics.student.logging;

import java.util.List;
import java.util.ArrayList;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.proctor.Keystroke;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Student;
import com.hacklympics.student.StudentController;
import com.hacklympics.student.code.CodeController;
import com.hacklympics.student.code.PendingCodePatches;

public class KeystrokeLogger implements Runnable {

    public static final List<Integer> FREQUENCY_OPTIONS = new ArrayList<>();

    public static final int DEFAULT_FREQUENCY = 4;

    static {
    	// Available frequency options (frequency of sending keystroke patches).
        FREQUENCY_OPTIONS.add(2);
        FREQUENCY_OPTIONS.add(4);
        FREQUENCY_OPTIONS.add(6);
        FREQUENCY_OPTIONS.add(8);
        FREQUENCY_OPTIONS.add(10);
    }

    private static KeystrokeLogger keystrokeLogger;
    private volatile boolean running;
    private int frequency;

    private KeystrokeLogger() {
        frequency = DEFAULT_FREQUENCY;
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

        Exam currentExam = Session.getInstance().getCurrentExam();
        Student currentUser = (Student) Session.getInstance().getCurrentUser();

        StudentController sc = (StudentController) Session.getInstance().getMainController();
        CodeController cc = (CodeController) sc.getControllers().get("Code");

        running = true;

        while (running) {
            try {
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
            	
            	System.out.println("Patch report complete");
            	
                Thread.sleep(frequency * 1000);
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

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

}