from enum import IntEnum

class StatusCode(IntEnum):
    SUCCESS = 0
    NETWORK_ERR = 1
    JSON_PARSE_ERR = 2
    VALIDATION_ERR = 3
    INSUFFICIENT_ARGS = 4
    NOT_LOGGED_IN = 5
    NOT_REGISTERED = 6
    ALREADY_LOGGED_IN = 7
    ALREADY_REGISTERED = 8
