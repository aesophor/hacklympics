package hacklympics.utility.proctor;

import static com.hacklympics.api.snapshot.SnapshotManager.GENERIC_GRP_DEFAULT_QUALITY;
import static com.hacklympics.api.snapshot.SnapshotManager.GENERIC_GRP_DEFAULT_FREQUENCY;
import static com.hacklympics.api.snapshot.SnapshotManager.SPECIAL_GRP_DEFAULT_QUALITY;
import static com.hacklympics.api.snapshot.SnapshotManager.SPECIAL_GRP_DEFAULT_FREQUENCY;

public enum SnapshotGroup {
    
    GENERIC("Generic", GENERIC_GRP_DEFAULT_QUALITY, GENERIC_GRP_DEFAULT_FREQUENCY),
    SPECIAL("Special", SPECIAL_GRP_DEFAULT_QUALITY, SPECIAL_GRP_DEFAULT_FREQUENCY);
    
    
    private final SnapshotGrpVBox snapshotGrpVBox;
    private final String groupName;
    
    private SnapshotGroup(String groupName, double defaultQuality, int defaultFrequency) {
        this.groupName = groupName;
        this.snapshotGrpVBox = new SnapshotGrpVBox(defaultQuality, defaultFrequency);
    }
    
    
    public SnapshotGrpVBox getSnapshotGrpVBox() {
        return this.snapshotGrpVBox;
    }
    
    @Override
    public String toString() {
        return this.groupName;
    }
    
}