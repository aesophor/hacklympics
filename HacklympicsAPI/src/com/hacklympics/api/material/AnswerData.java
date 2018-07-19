package com.hacklympics.api.material;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class AnswerData {
    
    private final SimpleIntegerProperty courseID;
    private final SimpleIntegerProperty examID;
    private final SimpleIntegerProperty problemID;
    private final SimpleIntegerProperty answerID;
    
    private final SimpleStringProperty className;
    private final SimpleStringProperty sourceCode;
    private final SimpleStringProperty student;
    
    public AnswerData(int courseID, int examID, int problemID, int answerID, 
            String className, String sourceCode, String student) {
        
        this.courseID = new SimpleIntegerProperty(courseID);
        this.examID = new SimpleIntegerProperty(examID);
        this.problemID = new SimpleIntegerProperty(problemID);
        this.answerID = new SimpleIntegerProperty(answerID);
        
        this.className = new SimpleStringProperty(className);
        this.sourceCode = new SimpleStringProperty(sourceCode);
        this.student = new SimpleStringProperty(student);
    }
    
    
    public Integer getCourseID() {
        return courseID.get();
    }
    
    public Integer getExamID() {
        return examID.get();
    }
    
    public Integer getProblemID() {
        return problemID.get();
    }
    
    public Integer getAnswerID() {
        return answerID.get();
    }
    
    public String getClassName() {
        return className.get();
    }
    
    public String getSourceCode() {
        return sourceCode.get();
    }
    
    public String getStudent() {
        return student.get();
    }
    
    
    public void setClassName(String className) {
        this.className.set(className);
    }
    
    public void setSourceCode(String sourceCode) {
        this.sourceCode.set(sourceCode);
    }
    
    
    public SimpleStringProperty classNameProperty() {
        return className;
    }
    
    public SimpleStringProperty sourceCodeProperty() {
        return sourceCode;
    }
    
    public SimpleStringProperty studentProperty() {
        return student;
    }
    
    
    @Override
    public String toString() {
        return className.get();
    }
}
