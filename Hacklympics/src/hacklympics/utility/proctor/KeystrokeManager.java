package hacklympics.utility.proctor;

import java.util.List;
import java.util.ArrayList;
import com.hacklympics.api.material.Exam;
import com.hacklympics.api.proctor.Keystroke;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.user.Student;
import hacklympics.student.StudentController;
import hacklympics.student.pages.CodeController;

public class KeystrokeManager implements Runnable {

    public static final List<Integer> FREQUENCY_OPTIONS = new ArrayList<>();

    public static final int DEFAULT_FREQUENCY = 4;

    static {
        FREQUENCY_OPTIONS.add(2);
        FREQUENCY_OPTIONS.add(4);
        FREQUENCY_OPTIONS.add(6);
        FREQUENCY_OPTIONS.add(8);
        FREQUENCY_OPTIONS.add(10);
    }

    private static KeystrokeManager keystrokeManager;

    private boolean running;
    private int frequency;

    private KeystrokeManager() {
        this.frequency = DEFAULT_FREQUENCY;
    }

    public static KeystrokeManager getInstance() {
        if (keystrokeManager == null) {
            keystrokeManager = new KeystrokeManager();
        }

        return keystrokeManager;
    }

    @Override
    public void run() {
        System.out.println("[*] Started keystroke synchronization thread.");

        Exam currentExam = Session.getInstance().getCurrentExam();
        Student currentUser = (Student) Session.getInstance().getCurrentUser();

        StudentController sc = (StudentController) Session.getInstance().getMainController();
        CodeController cc = (CodeController) sc.getControllers().get("Code");

        running = true;

        while (running) {
            try {
                if (Session.getInstance().isInExam()) {
                    Keystroke.sync(
                            currentExam.getCourseID(),
                            currentExam.getExamID(),
                            currentUser.getUsername(),
                            cc.getSelectedFileTab().getKeystrokeHistory()
                    );

                    Thread.sleep(frequency * 1000);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                running = false;
            }
        }

        System.out.println("[*] Stopped keystroke synchronization thread.");
    }

    public void shutdown() {
        running = false;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

}
