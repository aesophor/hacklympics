package hacklympics.utility.snapshot;

import com.hacklympics.api.snapshot.SnapshotManager;

public enum SnapshotGroup {
    
    GENERIC("Generic", SnapshotManager.GENERIC_GRP_DEFAULT_QUALITY, SnapshotManager.GENERIC_GRP_DEFAULT_FREQUENCY),
    SPECIAL("Special", SnapshotManager.SPECIAL_GRP_DEFAULT_QUALITY, SnapshotManager.SPECIAL_GRP_DEFAULT_FREQUENCY);
    
    
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