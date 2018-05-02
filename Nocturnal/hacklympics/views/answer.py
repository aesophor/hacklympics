from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.status_code import StatusCode
from hacklympics.models import *
from hacklympics.judge import *

import json


def get(request, c_id, e_id, p_id, a_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    answer = Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).answer_set.get(id=a_id)

    response_data["content"] = {
        "id": answer.id,
        "filepath": answer.filepath,
        "className": answer.class_name,
        "sourceCode": answer.source_code,
        "student": answer.student_id
    }

    return JsonResponse(response_data)


def list(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    answers = Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).answer_set.all()

    response_data["content"] = {
        "answers": [{
            "id": answer.id,
            "filepath": answer.filepath,
            "className": answer.class_name,
            "sourceCode": answer.source_code,
            "student": answer.student_id
        } for answer in answers]
    }

    return JsonResponse(response_data)


def create(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        filepath = req_body["filepath"]
        source_code = req_body["sourceCode"]
        student = req_body["student"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).answer_set.create(
            filepath = filepath,
            source_code = source_code,
            student_id = student
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def update(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        a_id = req_body["answerID"]
        filepath = req_body["filepath"]
        source_code = req_body["sourceCode"]
        student = req_body["student"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).answer_set.all().filter(id=a_id).update(
            filepath = filepath,
            source_code = source_code,
            student_id = student
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def remove(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        a_id = req_body["answerID"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).answer_set.get(id=a_id).delete()
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def validate(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        a_id = req_body["answerID"]
        
        answer = Course.objects.get(id=c_id).exam_set.get(id=e_id).problem_set.get(id=p_id).answer_set.get(id=a_id)
        
        if check(answer) is not True:
            response_data["statusCode"] = StatusCode.INCORRECT_ANSWER
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)
