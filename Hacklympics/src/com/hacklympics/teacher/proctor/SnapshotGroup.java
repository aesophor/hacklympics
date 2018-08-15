package com.hacklympics.teacher.proctor;

import static com.hacklympics.student.logging.ScreenRecorder.GENERIC_GRP_DEFAULT_FREQUENCY;
import static com.hacklympics.student.logging.ScreenRecorder.GENERIC_GRP_DEFAULT_QUALITY;
import static com.hacklympics.student.logging.ScreenRecorder.SPECIAL_GRP_DEFAULT_FREQUENCY;
import static com.hacklympics.student.logging.ScreenRecorder.SPECIAL_GRP_DEFAULT_QUALITY;

public enum SnapshotGroup {
    
    GENERIC("Generic", GENERIC_GRP_DEFAULT_QUALITY, GENERIC_GRP_DEFAULT_FREQUENCY),
    SPECIAL("Special", SPECIAL_GRP_DEFAULT_QUALITY, SPECIAL_GRP_DEFAULT_FREQUENCY);
    
    
    private final SnapshotGrpVBox snapshotGrpVBox;
    private final String groupName;
    
    private SnapshotGroup(String groupName, double defaultQuality, int defaultFrequency) {
        this.groupName = groupName;
        snapshotGrpVBox = new SnapshotGrpVBox(defaultQuality, defaultFrequency);
    }
    
    
    /**
     * Gets the SnapshotBox of the specified student by searching all groups
     * in which the student could possibly belong to.
     * @param studentUsername username of the student.
     * @return SnapshotGroup where students is in.
     */
    public static SnapshotBox getSnapshotBox(String studentUsername) {
        SnapshotBox target = null;
        
        for (SnapshotGroup group : SnapshotGroup.values()) {
            if ((target = group.getSnapshotGrpVBox().get(studentUsername)) != null) {
                break;
            }
        }
        
        return target;
    }
    
    
    /**
     * Gets the SnapshotGrpVBox of this group.
     * @return SnapshotGrpVBox of this group.
     */
    public SnapshotGrpVBox getSnapshotGrpVBox() {
        return snapshotGrpVBox;
    }
    
    @Override
    public String toString() {
        return groupName;
    }
    
}