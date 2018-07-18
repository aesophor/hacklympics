package com.hacklympics.api.event.proctor;

import java.util.Map;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.event.ExamRelated;


public class AdjustKeystrokeParamEvent extends Event implements ExamRelated {
    
    private final int examID;
    private final int frequency;
    
    public AdjustKeystrokeParamEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        this.examID = (int) Double.parseDouble(content.get("examID").toString());
        this.frequency = (int) Double.parseDouble(content.get("frequency").toString());
    }
    
    
    public int getExamID() {
        return this.examID;
    }
    
    public int getFrequency() {
        return this.frequency;
    }
    
    @Override
    public boolean isForCurrentExam() {
        int currentExamID = Session.getInstance().getCurrentExam().getExamID();
        
        return this.examID == currentExamID;
    }
    
}