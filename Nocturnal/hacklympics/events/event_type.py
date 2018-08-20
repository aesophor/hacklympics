from enum import IntEnum

class EventType(IntEnum):
    LOGIN = 0
    LOGOUT = 1
    
    NEW_MESSAGE = 2

    LAUNCH_EXAM = 3
    HALT_EXAM = 4
    ATTEND_EXAM = 5
    LEAVE_EXAM = 6

    NEW_SNAPSHOT = 7
    NEW_KEYSTROKE = 8
    ADJUST_PROCTOR_PARAMS = 9
