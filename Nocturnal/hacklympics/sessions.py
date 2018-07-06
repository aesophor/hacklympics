from hacklympics.exceptions import AlreadyLoggedIn, NotLoggedIn
from hacklympics.events.events import *
from hacklympics.events.dispatcher import *
from hacklympics.models import *

class OnlineUsers:
    users = []

    @staticmethod
    def add(username):
        if not OnlineUsers.has(username):
            user = User.objects.get(username=username)
            
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
    def remove(username):
        if OnlineUsers.has(username):
            user = User.objects.get(username=username)
            
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
        username = [value[1] for value in kwargs.items() if value[0] == "username"][0]
        
        # Now get the real user object from DB.
        user = OnlineUsers.get(username)
        
        # Update that user's data.
        for value in kwargs.items():
            if value[0] == "username":
                pass
            else:
                exec("user.{0} = \"{1}\"".format(value[0], value[1]))
        
        user.save()

    @staticmethod
    def has(username):
        return True if OnlineUsers.get(username) else False

    @staticmethod
    def show():
        print(OnlineUsers.users)

    @staticmethod
    def get(username):
        for user in OnlineUsers.users:
            if user.username == username:
                return user
    
    @staticmethod
    def get_all(role: str):
        return [user for user in OnlineUsers.users if user.role == role] 


class OngoingExams:
    exams = {}
