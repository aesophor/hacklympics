from hacklympics.models import User, Exam
from hacklympics.events.events import AdjustSnapshotParamEvent, AdjustKeystrokeParamEvent
from hacklympics.sessions import ExamData

class TrafficDisperser:
    # Disperse the timepoint in which students sync their snapshot and keystroke patches to server.
    # Without this, students may try to sync their snapshot and keystroke patches to server
    # all at the same time, which causes the server to be overloaded.
    @staticmethod
    def disperse(exam_data: ExamData):
        students = exam_data.students
        snapshot_gengrp_quality = exam_data.snapshot_grp[SnapshotGroup.GENERIC].quality
        sleep_interval = 4 / len(students)
        # Calculate the sleep interval while dispersing snapshot
        
        # Each snapshot group has different sync frequency
        for group in exam_data.snapshot_grp:
            quality = exam_data.snapshot_grp[group].quality
            frequency = exam_data.snapshot_grp[group].frequency
            sleep_interval = frequency / len(students)
            dispatch(AdjustSnapshotParamEvent(exam, quality, frequency), students, sleep_interval)
        
        # Disperse all students 
        dispatch(AdjustKeystrokeParamEvent(exam, frequency), students, sleep_interval)
