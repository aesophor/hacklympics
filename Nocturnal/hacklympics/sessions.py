from hacklympics.exceptions import *
from hacklympics.events.events import *
from hacklympics.events.dispatcher import *
from hacklympics.models import *
from threading import Timer

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
    def add(exam: Exam):
        if not OngoingExams.has(exam):                        
            # Create an empty student list for this exam.
            OngoingExams.exams[exam] = ExamData()
            
            # Start the timer of this exam.
            OngoingExams.exams[exam].timer = Timer(exam.duration * 60, OngoingExams.remove, args=[exam])
            OngoingExams.exams[exam].timer.start()
            
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
    def __init__(self):
        self.students = []
        self.teachers = []
        
        self.timer = None

    def add(self, user: User):
        if user.is_student:
            self.students.append(user)
        else:
            self.teachers.append(user)

    def remove(self, user: User):
        if user.is_student:
            self.students.remove(user)
        else:
            self.teachers.remove(user)

    def __str__(self):
        return str(self.students) + ", " + str(self.teachers)
