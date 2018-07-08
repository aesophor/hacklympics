from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.status_code import StatusCode
from hacklympics.sessions import OngoingExams
from hacklympics.models import *

import json


def get(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)

    response_data["content"] = {
        "id": exam.id,
        "title": exam.title,
        "desc": exam.desc,
        "duration": exam.duration,
    }

    return JsonResponse(response_data)


def list(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    exams = Course.objects.get(id=c_id).exam_set.all()

    response_data["content"] = {
        "exams": [{
            "id": exam.id,
            "title": exam.title,
            "desc": exam.desc,
            "duration": exam.duration,
        } for exam in exams]
    }

    return JsonResponse(response_data)


def create(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        title = req_body["title"]
        desc = req_body["desc"]
        duration = req_body["duration"]
        
        Course.objects.get(id=c_id).exam_set.create(
            title = title,
            desc = desc,
            duration = duration,
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def update(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        title = req_body["title"]
        desc = req_body["desc"]
        duration = req_body["duration"]
        
        Course.objects.get(id=c_id).exam_set.all().filter(id=e_id).update(
            title = title,
            desc = desc,
            duration = duration
        )
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def remove(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        
        Course.objects.get(id=c_id).exam_set.get(id=e_id).delete()
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def launch(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        
        exam = Exam.objects.get(id=e_id)
        teacher = exam.course.teacher
        
        # Add this exam to OngoingExams.
        # The teacher launching the exam will be the first proctor.
        OngoingExams.add(exam)
        OngoingExams.get(exam).add(teacher)
        OngoingExams.show()
    except AlreadyLaunched:
        response_data["statusCode"] = StatusCode.ALREADY_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    
    return JsonResponse(response_data)


def halt(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        
        exam = Exam.objects.get(id=e_id)
        
        OngoingExams.remove(exam)
        OngoingExams.show()
    except NotLaunched:
        response_data["statusCode"] = StatusCode.NOT_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    
    return JsonResponse(response_data)


def attend(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        username = req_body["username"]
        
        exam = Exam.objects.get(id=e_id)
        user = User.objects.get(username=username)
        
        OngoingExams.get(exam).add(user)
        OngoingExams.show()
        # Dispatch a event here!
    except NotLaunched:
        response_data["statusCode"] = StatusCode.NOT_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def leave(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}
    
    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        username = req_body["username"]
        
        exam = Exam.objects.get(id=e_id)
        user = User.objects.get(username=username)
        
        OngoingExams.get(exam).remove(user)
        OngoingExams.show()
        # Dispatch a event here!
    except NotLaunched:
        response_data["statusCode"] = StatusCode.NOT_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)
