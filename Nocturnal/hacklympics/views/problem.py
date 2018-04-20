from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.status_code import StatusCode
from hacklympics.models import *

import json


def get(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    problem = Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id)

    response_data["content"] = {
        "id": problem.id,
        "title": problem.title,
        "desc": problem.desc,
    }

    return JsonResponse(response_data)


def list(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    problems = Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.all()

    response_data["content"] = {
        "exams": [{
            "id": problem.id,
            "title": problem.title,
            "desc": problem.desc,
        } for problem in problems]
    }

    return JsonResponse(response_data)


def create(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        title = req_body["title"]
        desc = req_body["desc"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.create(
            title = title,
            desc = desc,
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def update(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        p_id = req_body["problemID"]
        title = req_body["title"]
        desc = req_body["desc"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.all().filter(id=p_id).update(
            title = title,
            desc = desc
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def remove(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        p_id = req_body["problemID"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).delete()
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)
