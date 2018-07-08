from hacklympics.events.event_type import EventType
from hacklympics.models import *

import json


class Event:
    def __init__(self, event_type: EventType):
        self.event_type = event_type


class LoginEvent(Event):
    def __init__(self, user: User):
        super(LoginEvent, self).__init__(EventType.LOGIN)        
        self.user = user

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "username": self.user.username,
            "fullname": self.user.fullname,
            "graduationYear": self.user.graduation_year,
            "isStudent": self.user.is_student
        }
        
        return json.dumps(event)


class LogoutEvent(Event):
    def __init__(self, user: User):
        super(LogoutEvent, self).__init__(EventType.LOGOUT)
        self.user = user

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "username": self.user.username,
            "fullname": self.user.fullname,
            "graduationYear": self.user.graduation_year,
            "isStudent": self.user.is_student
        }
        
        return json.dumps(event)


class NewMessageEvent(Event):
    def __init__(self, user: User, content: str):
        super(NewMessageEvent, self).__init__(EventType.NEW_MESSAGE)
        self.user = user
        self.content = content

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "content": self.content,
            "user": {
                "username": self.user.username,
                "fullname": self.user.fullname,
                "graduationYear": self.user.graduation_year,
                "isStudent": self.user.is_student
            }
        }
        
        return json.dumps(event)


class LaunchExamEvent(Event):
    def __init__(self, exam: Exam):
        super(LaunchExamEvent, self).__init__(EventType.LAUNCH_EXAM)
        self.exam = exam
        self.teacher = exam.course.teacher

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "exam": {
                "courseID": self.exam.course.id,
                "examID": self.exam.id,
                "title": self.exam.title,
                "desc": self.exam.desc,
                "duration": self.exam.duration,
            },
            "teacher": {
                "username": self.teacher.username,
                "fullname": self.teacher.fullname,
                "graduationYear": self.teacher.graduation_year
            }
        }
        
        return json.dumps(event)


class HaltExamEvent(Event):
    def __init__(self, exam: Exam):
        super(HaltExamEvent, self).__init__(EventType.HALT_EXAM)
        self.exam = exam
        self.teacher = exam.course.teacher

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "exam": {
                "courseID": self.exam.course.id,
                "examID": self.exam.id,
                "title": self.exam.title,
                "desc": self.exam.desc,
                "duration": self.exam.duration,
            },
            "teacher": {
                "username": self.teacher.username,
                "fullname": self.teacher.fullname,
                "graduationYear": self.teacher.graduation_year
            }
        }
        
        return json.dumps(event)


# Both student and teacher can attend an Exam.
# Student: take the exam.
# Teacher: proctor the exam.
class AttendExamEvent(Event):
    def __init__(self, exam: Exam, user: User):
        super(AttendExamEvent, self).__init__(EventType.ATTEND_EXAM)
        self.exam = exam
        self.user = user

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "exam": {
                "courseID": self.exam.course.id,
                "examID": self.exam.id,
                "title": self.exam.title,
                "desc": self.exam.desc,
                "duration": self.exam.duration
            },
            "user": {
                "username": self.user.username,
                "fullname": self.user.fullname,
                "graduationYear": self.user.graduation_year,
                "isStudent": self.user.is_student
            }
        }
        
        return json.dumps(event)


class LeaveExamEvent(Event):
    def __init__(self, exam: Exam, user: User):
        super(LeaveExamEvent, self).__init__(EventType.LEAVE_EXAM)
        self.exam = exam
        self.user = user

    def __str__(self):
        event = {"eventType": self.event_type}
        
        event["content"] = {
            "exam": {
                "courseID": self.exam.course.id,
                "examID": self.exam.id,
                "title": self.exam.title,
                "desc": self.exam.desc,
                "duration": self.exam.duration
            }, 
            "user": {
                "username": self.user.username,
                "fullname": self.user.fullname,
                "graduationYear": self.user.graduation_year,
                "isStudent": self.user.is_student
            }
        }
        
        return json.dumps(event)
