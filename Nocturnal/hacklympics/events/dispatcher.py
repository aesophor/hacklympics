from hacklympics.events.event_type import EventType
from hacklympics.events.events import *

import socket
import json
import time


def dispatch(event: Event, users: list, interval=0):
    print("[*] dispatching ev: ", event, " to ", users)
    
    for user in users:
        print("+ dispatching to ", user, "(", user.last_login_ip, ")")
        
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        evmsg = str(event) + "\n"
        s.connect((user.last_login_ip, 8001))
        s.send(evmsg.encode("utf-8"))
        s.close()
        
        time.sleep(interval)
