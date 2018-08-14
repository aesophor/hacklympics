package com.hacklympics.teacher.proctor;

import java.io.IOException;
import javafx.scene.Group;
import com.hacklympics.api.user.Student;
import com.hacklympics.api.proctor.ProctorMedium;

public abstract class StudentBox<T extends ProctorMedium> extends Group {
    
    protected final Student student;

    public StudentBox(Student student) {
        this.student = student;
    }
    
    
    public abstract void update(T t) throws IOException;
    
    public abstract void markAsFinished();
    
    
    public Student getStudent() {
        return student;
    }
    
}