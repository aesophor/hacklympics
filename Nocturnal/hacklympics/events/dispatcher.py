from hacklympics.events.event_type import EventType

import socket
import json


def dispatch(json: str, users: list):    
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    for user in users:
        json += "\n"
        s.connect((user.last_login_ip, 8001))
        s.send(json.encode("utf-8"))
