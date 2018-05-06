from hacklympics.exceptions import AlreadyLoggedIn, NotLoggedIn
from hacklympics.models.models import *

class OnlineUsers:
    users = []

    @staticmethod
    def add(username):
        if not OnlineUsers.has(username):
            OnlineUsers.users.append( User.objects.get(username=username) )
        else:
            raise AlreadyLoggedIn("This user has already logged in.")

    @staticmethod
    def remove(username):
        if OnlineUsers.has(username):
            OnlineUsers.users.remove( OnlineUsers.get(username) )
        else:
            raise NotLoggedIn("This user has not logged in yet.")

    @staticmethod
    def update(**kwargs):
        username = [value[1] for value in kwargs.items() if value[0] == "username"][0]
        
        user = OnlineUsers.get(username)
        
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
