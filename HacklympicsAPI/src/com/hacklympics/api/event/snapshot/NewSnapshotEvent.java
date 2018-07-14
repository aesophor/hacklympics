package com.hacklympics.api.event.snapshot;

import java.util.Map;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.snapshot.Snapshot;


public class NewSnapshotEvent extends Event {
    
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
    
}