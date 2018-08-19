from enum import IntEnum

class SnapshotGroup(IntEnum):
    GENERIC = 0
    SPECIAL = 1


class SnapshotGroupData:
    
    def __init__(self, quality, frequency):
        # Students in this group.
        self.students = []
        
        # Snapshot quality and sync frequency.
        self.quality = quality
        self.frequency = frequency
