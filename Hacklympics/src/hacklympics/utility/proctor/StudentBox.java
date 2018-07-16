package hacklympics.utility.proctor;

import java.io.IOException;
import javafx.scene.Group;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.snapshot.Snapshot;

public abstract class StudentBox extends Group {
    
    protected final Student student;

    public StudentBox(Student student) {
        this.student = student;
    }
    
    
    public abstract void update(Snapshot snapshot) throws IOException;
    
    public Student getStudent() {
        return this.student;
    }
    
}