class AlreadyLoggedIn(Exception):
    def __init__(self, message):
        super().__init__(message)


class NotLoggedIn(Exception):
    def __init__(self, message):
        super().__init__(message)


class AlreadySubmitted(Exception):
    def __init__(self, message):
        super().__init__(message)
