package com.hacklympics.api.event.proctor;

import java.util.Map;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.proctor.Snapshot;
import com.hacklympics.api.event.ExamRelated;


public class NewSnapshotEvent extends Event implements ExamRelated {
    
    private final Snapshot snapshot;
    
    public NewSnapshotEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        int examID = (int) Double.parseDouble(content.get("examID").toString());
        String student = content.get("student").toString();
        String snapshot = content.get("snapshot").toString();
        String timestamp = content.get("timestamp").toString();
        
        this.snapshot = new Snapshot(examID, student, snapshot, timestamp);
    }
    
    
    /**
     * Returns the new snapshot that has just been sent.
     * @return the snapshot.
     */
    public Snapshot getSnapshot() {
        return this.snapshot;
    }
    
    @Override
    public boolean isForCurrentExam() {
        int eventExamID = this.getSnapshot().getExamID();
        int currentExamID = Session.getInstance().getCurrentExam().getExamID();
        
        return eventExamID == currentExamID;
    }
    
}