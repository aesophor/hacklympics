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
            "content": content,
            "user": {
                "username": self.user.username,
                "fullname": self.user.fullname,
                "graduationYear": self.user.graduation_year,
                "isStudent": self.user.is_student
            }
        }
        
        return json.dumps(event)
