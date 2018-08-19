from hacklympics.exceptions import *
from hacklympics.events.events import *
from hacklympics.events.dispatcher import dispatch
from hacklympics.models import User, Exam
from hacklympics.snapshot_group import SnapshotGroup, SnapshotGroupData

from threading import Timer
import time

class OnlineUsers:
    users = []

    @staticmethod
    def add(user: User):
        if not OnlineUsers.has(user):
            # The SocketServer which listens for events will start
            # only after the user has SUCCESSFULLY logged in!
            # Hence, the order of the following two lines of code
            # DOES matter. If we reverse their order, the dispatcher
            # will get a connection refused error
            # (since the SocketServer has not yet started) :)
            dispatch(LoginEvent(user), OnlineUsers.users)
            OnlineUsers.users.append(user)
        else:
            raise AlreadyLoggedIn("This user has already logged in.")

    @staticmethod
    def remove(user: User):
        if OnlineUsers.has(user):
            # Remove the user from OnlineUsers list first,
            # so that the server will not dispatch this event to 
            # the logging out user whose SocketServer
            # has already shutdown.
            OnlineUsers.users.remove(user)
            dispatch(LogoutEvent(user), OnlineUsers.users)
        else:
            raise NotLoggedIn("This user has already logged out.")

    @staticmethod
    def update(**kwargs):
        # Fetch the username from the varargs.
        user = [value[1] for value in kwargs.items() if value[0] == "user"][0]
        
        # Update that user's data.
        for value in kwargs.items():
            if value[0] == "user":
                pass
            else:
                exec("user.{0} = \"{1}\"".format(value[0], value[1]))
        
        user.save()

    @staticmethod
    def has(user):
        return user in OnlineUsers.users
 
    @staticmethod
    def get(user: User):
        for u in OnlingUsers.users:
            if u == user:
                return u
    
    @staticmethod
    def get_all(role: str):
        return [user for user in OnlineUsers.users if user.role == role] 

    @staticmethod
    def show():
        print(OnlineUsers.users)



class OngoingExams:
    # Key: "teacher username"+"exam id"
    # Value: a list of students currently in the exam.
    exams = {}

    @staticmethod
    def add(exam: Exam, gengrp_snapshot_quality: int, gengrp_snapshot_frequency: int, 
                        spegrp_snapshot_quality: int, spegrp_snapshot_frequency: int, 
                        keystroke_frequency: int):
        if not OngoingExams.has(exam):                        
            # Create an empty student list for this exam.
            OngoingExams.exams[exam] = ExamData(exam, gengrp_snapshot_quality, gengrp_snapshot_frequency, 
                                                      spegrp_snapshot_quality, spegrp_snapshot_frequency,
                                                      keystroke_frequency)
            # Start the timer of this exam.
            # Also record the start_time of timer for evaluating remaining time later.
            OngoingExams.exams[exam].timer = Timer(exam.duration * 60, OngoingExams.remove, args=[exam])
            OngoingExams.exams[exam].timer.start()
            OngoingExams.exams[exam].start_time = time.time()
            
            # Notify all users that the exam has been launched.
            dispatch(LaunchExamEvent(exam), OnlineUsers.users)
        else:
            raise AlreadyLaunched("This exam has already been launched.")

    @staticmethod
    def remove(exam: Exam):
        if OngoingExams.has(exam):
            # Stop the timer of this exam.
            OngoingExams.exams[exam].timer.cancel()
            
            # Remove the element from the dict.
            del OngoingExams.exams[exam]
            
            # Notify all users that the exam has been halt.
            dispatch(HaltExamEvent(exam), OnlineUsers.users)
        else:
            raise NotLaunched("This exam has not been launched or already ended.")

    @staticmethod
    def has(exam: Exam):
        return exam in OngoingExams.exams

    @staticmethod
    def get(exam: Exam):
        if OngoingExams.has(exam):
            return OngoingExams.exams[exam]
        else:
            raise NotLaunched("This exam has not been launched or already ended.")

    @staticmethod
    def show():
        print(OngoingExams.exams)


class ExamData:
    # ExamData = two lists of participants (Students and Teachers) + timer.
    
    # A participant of an exam can either be a student or a teacher.
    # Students take the exam while teacher proctor the exam.
    def __init__(self, exam: Exam, snapshot_gengrp_quality: int, snapshot_gengrp_frequency: int, 
                                   snapshot_spegrp_quality: int, snapshot_spegrp_frequency: int, 
                                   keystroke_frequency: int):
        self.exam = exam
        self.students = []
        self.teachers = []
        
        self.timer = None
        self.start_time = None
       
        self.snapshot_grp = {SnapshotGroup.GENERIC: SnapshotGroupData(snapshot_gengrp_quality, snapshot_gengrp_frequency), 
                             SnapshotGroup.SPECIAL: SnapshotGroupData(snapshot_spegrp_quality, snapshot_spegrp_frequency)}
        self.keystroke_frequency = keystroke_frequency

    def add(self, user: User):
        if not self.has(user):
            if user.is_student:
                self.students.append(user)
                
                # Notify all teachers that a student has attend to the exam,
                # and that they should prepare for receiving snapshot from that student.
                dispatch(AttendExamEvent(self.exam, user), self.teachers)
            else:
                self.teachers.append(user)
        else:
            raise AlreadyAttended("This user has already attended to this exam.")

    def remove(self, user: User):
        # Once a student left the exam, the student will not be able
        # to attend the exam again, unless the exam is re-launched.
        # A teacher can enter and leave as he/she wishes.
        if self.has(user):
            if not user.is_student:
                self.teachers.remove(user)
            else:
                # Notify all teachers that a student has left the exam,
                # and that they will not receive snapshot from that student any longer.
                dispatch(LeaveExamEvent(self.exam, user), self.teachers)
        else:
            raise NotAttended("This user has not attended to this exam.")

    def has(self, user: User):
        if user.is_student:
            return user in self.students
        else:
            return user in self.teachers

    def __str__(self):
        return str(self.students) + ", " + str(self.teachers)
