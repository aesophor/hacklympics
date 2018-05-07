package com.hacklympics.api.material;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProblemData {
    
    private final SimpleIntegerProperty courseID;
    private final SimpleIntegerProperty examID;
    private final SimpleIntegerProperty problemID;
    
    private SimpleStringProperty title;
    private SimpleStringProperty desc;
    private SimpleStringProperty input;
    private SimpleStringProperty output;
    
    public ProblemData(int courseID, int examID, int problemID, 
            String title, String desc, String input, String output) {
        
        this.courseID = new SimpleIntegerProperty(courseID);
        this.examID = new SimpleIntegerProperty(examID);
        this.problemID = new SimpleIntegerProperty(problemID);
        
        this.title = new SimpleStringProperty(title);
        this.desc = new SimpleStringProperty(desc);
        this.input = new SimpleStringProperty(input);
        this.output = new SimpleStringProperty(output);
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
    
    public String getTitle() {
        return title.get();
    }
    
    public String getDesc() {
        return desc.get();
    }
    
    public String getInput() {
        return input.get();
    }
    
    public String getOutput() {
        return output.get();
    }
    
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public void setDesc(String desc) {
        this.desc.set(desc);
    }
    
    public void setInput(String input) {
        this.input.set(input);
    }
    
    public void setOutput(String output) {
        this.output.set(output);
    }
    
    
    public SimpleStringProperty titleProperty() {
        return title;
    }
    
    public SimpleStringProperty descProperty() {
        return desc;
    }
    
    public SimpleStringProperty inputProperty() {
        return input;
    }
    
    public SimpleStringProperty outputProperty() {
        return output;
    }
    
    
    @Override
    public String toString() {
        return title.get();
    }
}
