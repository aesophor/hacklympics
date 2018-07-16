package com.hacklympics.api.event.snapshot;

import java.util.Map;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.event.ExamDependent;
import com.hacklympics.api.session.Session;


public class AdjustSnapshotParamEvent extends Event implements ExamDependent {
    
    private final int examID;
    private final double quality;
    private final int frequency;
    
    public AdjustSnapshotParamEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        this.examID = (int) Double.parseDouble(content.get("examID").toString());
        this.quality = Double.parseDouble(content.get("quality").toString());
        this.frequency = (int) Double.parseDouble(content.get("frequency").toString());
    }
    
    
    public int getExamID() {
        return this.examID;
    }
    
    public double getQuality() {
        return this.quality;
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