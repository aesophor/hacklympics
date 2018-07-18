package com.hacklympics.api.event.proctor;

import java.util.Map;
import java.util.List;
import com.hacklympics.api.event.Event;
import com.hacklympics.api.event.ExamRelated;
import com.hacklympics.api.proctor.Keystroke;
import com.hacklympics.api.session.Session;
import com.hacklympics.api.utility.NetworkUtils;

public class NewKeystrokeEvent extends Event implements ExamRelated {
    
    private final Keystroke keystroke;
    
    public NewKeystrokeEvent(String rawJson) {
        super(rawJson);
        
        Map<String, Object> content = this.getContent();
        
        int examID = (int) Double.parseDouble(content.get("examID").toString());
        String student = content.get("student").toString();
        List<String> history = NetworkUtils.getGson().fromJson(content.get("students").toString(), List.class);
        
        this.keystroke = new Keystroke(
                examID,
                student,
                history
        );
    }
    
    
    /**
     * Returns the new snapshot that has just been sent.
     * @return the snapshot.
     */
    public Keystroke getKeystroke() {
        return this.keystroke;
    }
    
    @Override
    public boolean isForCurrentExam() {
        int eventExamID = this.getKeystroke().getExamID();
        int currentExamID = Session.getInstance().getCurrentExam().getExamID();
        
        return eventExamID == currentExamID;
    }
    
}