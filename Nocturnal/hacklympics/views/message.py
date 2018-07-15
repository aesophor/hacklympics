from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.events.dispatcher import *
from hacklympics.events.events import *
from hacklympics.sessions import OnlineUsers
from hacklympics.status_code import StatusCode
from hacklympics.models import *

import json

def create(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        username = req_body["username"]
        content = req_body["content"]
        
        user = User.objects.get(username=username)
        source_ip = user.last_login_ip
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        
        user.message_set.create(
            source_ip = source_ip,
            content = content,
            exam = exam
        )
        
        dispatch(NewMessageEvent(user, exam, content), OnlineUsers.users)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXISTS

    return JsonResponse(response_data)
