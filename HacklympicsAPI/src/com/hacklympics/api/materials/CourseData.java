package com.hacklympics.api.materials;

import java.util.List;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CourseData {
    
    private final SimpleIntegerProperty courseID;
    private SimpleStringProperty name;
    private SimpleIntegerProperty semester;
    private SimpleStringProperty teacher;
    private List<String> students;
    
    public CourseData(int courseID, String name, int semester, String teacher,
                      List<String> students) {
        this.courseID = new SimpleIntegerProperty(courseID);
        this.name = new SimpleStringProperty(name);
        this.semester = new SimpleIntegerProperty(semester);
        this.teacher = new SimpleStringProperty(teacher);
        this.students = students;
    }
    
    
    public int getCourseID() {
        return courseID.get();
    }
    
    public String getName() {
        return name.get();
    }
    
    public Integer getSemester() {
        return semester.get();
    }
    
    public String getTeacher() {
        return teacher.get();
    }
    
    public List<String> getStudents() {
        return students;
    }
    
    
    public void setName(String name) {
        this.name.set(name);
    }
    
    public void setSemester(int semester) {
        this.semester.set(semester);
    }
    
    public void setTeacher(String teacher) {
        this.teacher.set(teacher);
    }
    
    
    public SimpleIntegerProperty courseIDProperty() {
        return courseID;
    }
    
    public SimpleStringProperty nameProperty() {
        return name;
    }
    
    public SimpleIntegerProperty semesterProperty() {
        return semester;
    }
    
    public SimpleStringProperty teacherProperty() {
        return teacher;
    }
    
    
    @Override
    public String toString() {
        return String.format("[%d] %d_%s - %s ", courseID.get(), semester.get(), name.get(), teacher.get()) + students;
    }
}
