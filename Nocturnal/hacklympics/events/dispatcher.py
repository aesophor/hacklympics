from hacklympics.events.event_type import EventType
from hacklympics.events.events import *

import socket
import json


def dispatch(event: Event, users: list):
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    print("--> dispatching ev: ", event, " to ", users)
    for user in users:
        evmsg = str(event) + "\n"
        s.connect((user.last_login_ip, 8001))
        s.send(evmsg.encode("utf-8"))
        s.close()
