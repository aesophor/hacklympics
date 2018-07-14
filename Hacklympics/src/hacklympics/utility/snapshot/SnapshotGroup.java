package hacklympics.utility.snapshot;

public enum SnapshotGroup {
    
    GENERIC("Generic", 0.25, 5),
    SPECIAL("Special", 0.5, 3);
    
    
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