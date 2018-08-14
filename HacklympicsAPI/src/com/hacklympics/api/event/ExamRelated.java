package com.hacklympics.api.event;

public interface ExamRelated {
	
    /**
     * Checks if this event is related to the exam which the user
     * is currently taking/proctoring.
     * @return if this event is for user's current exam.
     */
    public boolean isForCurrentExam();
    
}