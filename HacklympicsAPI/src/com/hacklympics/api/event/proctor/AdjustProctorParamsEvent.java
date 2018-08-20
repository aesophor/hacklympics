package com.hacklympics.api.event.proctor;

import java.util.Map;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.event.ExamRelated;


public class AdjustProctorParamsEvent extends Event implements ExamRelated {
    
    private final int examID;
    private final double snapshotQuality;
    private final int syncFrequency;
    
    public AdjustProctorParamsEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        // Extract examID, snapshot quality and frequency from json content.
        this.examID = (int) Double.parseDouble(content.get("examID").toString());
        this.snapshotQuality = Double.parseDouble(content.get("snapshotQuality").toString());
        this.syncFrequency = (int) Double.parseDouble(content.get("syncFrequency").toString());
    }
    
    
    public int getExamID() {
        return this.examID;
    }
    
    public double getSnapshotQuality() {
        return this.snapshotQuality;
    }
    
    public int getSyncFrequency() {
        return this.syncFrequency;
    }
    
    @Override
    public boolean isForCurrentExam() {
        int currentExamID = Session.getInstance().getCurrentExam().getExamID();
        
        return this.examID == currentExamID;
    }
    
}