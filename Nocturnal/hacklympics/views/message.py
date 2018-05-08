from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.status_code import StatusCode
from hacklympics.models import *

import json


def get(request, username, m_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    message = User.objects.get(username=username).message_set.get(id=m_id)

    response_data["content"] = {
        "source_ip": message.source_ip,
        "content": message.content,
        "user": message.user_id
    }

    return JsonResponse(response_data)


def list(request, username):
    response_data = {"statusCode": StatusCode.SUCCESS}

    messages = User.objects.get(username=username).message_set.all()

    response_data["content"] = {
        "messages": [{
            "source_ip": message.source_ip,
            "content": message.content,
            "user": message.user_id
        } for message in messages]
    }

    return JsonResponse(response_data)


def create(request, username):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        username = req_body["user"]
        content = req_body["content"]
        source_ip = User.objects.get(username=username).last_login_ip
        
        Message.objects.get(username=username).message_set.create(
            source_ip = source_ip,
            content = content,
            user_id = username
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)
