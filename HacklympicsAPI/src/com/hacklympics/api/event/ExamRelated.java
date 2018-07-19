package com.hacklympics.api.event;

public interface ExamRelated {
    
    /**
     * Checks if this event is related to the exam that the user
     * is currently taking/proctoring.
     * @return if event is for the current exam.
     */
    public boolean isForCurrentExam();
    
}