package com.hacklympics.teacher.proctor;

import static com.hacklympics.api.preference.Config.DEFAULT_GENGRP_SNAPSHOT_QUALITY;
import static com.hacklympics.api.preference.Config.DEFAULT_SPEGRP_SNAPSHOT_QUALITY;
import static com.hacklympics.api.preference.Config.DEFAULT_GENGRP_SYNC_FREQUENCY;
import static com.hacklympics.api.preference.Config.DEFAULT_SPEGRP_SYNC_FREQUENCY;

public enum SnapshotGroup {
    
    GENERIC("Generic", DEFAULT_GENGRP_SNAPSHOT_QUALITY, DEFAULT_GENGRP_SYNC_FREQUENCY),
    SPECIAL("Special", DEFAULT_SPEGRP_SNAPSHOT_QUALITY, DEFAULT_SPEGRP_SYNC_FREQUENCY);
    
    
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