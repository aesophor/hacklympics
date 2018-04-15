from django.core.exceptions import ObjectDoesNotExist
from django.db import IntegrityError
from django.http import JsonResponse

from hacklympics.exceptions import AlreadyLoggedIn, NotLoggedIn
from hacklympics.status_code import StatusCode
from hacklympics.session import OnlineUsers
from hacklympics.models import *

import json


def get(request, username):
    response_data = {"statusCode": StatusCode.SUCCESS}

    user = User.objects.get(username=username)

    response_data["content"] = {
        "username": user.username,
        "fullname": user.fullname,
        "graduationYear": user.graduation_year,
        "isStudent": user.is_student
    }

    return JsonResponse(response_data)


def list(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    users = User.objects.all()

    response_data["content"] = {
        "users": [{
            "username": user.username,
            "fullname": user.fullname,
            "graduationYear": user.graduation_year,
            "isStudent": user.is_student
        } for user in users]
    }

    return JsonResponse(response_data)


def register(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        username = req_body["username"]
        password = req_body["password"]
        fullname = req_body["fullname"]
        graduation_year = req_body["graduationYear"]
        is_student = req_body["isStudent"]
        
        User.objects.create(
            username = username,
            password = password,
            fullname = fullname,
            graduation_year = graduation_year,
            is_student = is_student
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except IntegrityError:
        response_data["statusCode"] = StatusCode.ALREADY_REGISTERED

    return JsonResponse(response_data)


def login(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        username = req_body["username"]
        password = req_body["password"]
        login_ip = req_body["loginIP"]
        
        user = User.objects.get(username=username, password=password)
        
        response_data["content"] = {
            "role": user.role
        }
        
        OnlineUsers.add(user.username)
        OnlineUsers.update(username=username, last_login_ip=login_ip)
        OnlineUsers.show()
    except AlreadyLoggedIn:
        response_data["statusCode"] = StatusCode.ALREADY_LOGGED_IN
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.VALIDATION_ERR

    return JsonResponse(response_data)


def logout(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        username = req_body["username"]
        
        OnlineUsers.remove(username)
    except NotLoggedIn:
        response_data["statusCode"] = StatusCode.NOT_LOGGED_IN
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.VALIDATION_ERR

    return JsonResponse(response_data)


# This method probably needs better naming...
def reset(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        # What the hell did I wrote lol...
        username = req_body["username"]
        password = req_body["password"]
        new_password = req_body["newPassword"]
        
        # Test needed.
        OnlineUsers.update(username=username, password=new_password)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.VALIDATION_ERR

    return JsonResponse(response_data)
