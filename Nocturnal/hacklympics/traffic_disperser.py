from hacklympics.models import User, Exam
from hacklympics.events.events import AdjustSnapshotParamEvent, AdjustKeystrokeParamEvent
from hacklympics.sessions import OngoingExams

'''
class TrafficDisperser:
   
    @staticmethod
    def disperse(exam: Exam):
        students = OngoingExams.exams[exam].students
        sleep_interval = 4 / len(students)
        
        dispatch(AdjustSnapshotParamEvent(exam, quality, frequency), students)
        dispatch(AdjustKeystrokeParamEvent(exam, frequency), students)
'''
