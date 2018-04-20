from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.status_code import StatusCode
from hacklympics.models import *

import json


def get(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    course = Course.objects.get(id=c_id)

    response_data["content"] = {
        "id": course.id,
        "name": course.name,
        "semester": course.semester,
        "teacher": course.teacher.username,
        "students": [student.username for student in course.students.all()]
    }

    return JsonResponse(response_data)


def list(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    courses = Course.objects.all()

    response_data["content"] = {
        "courses": [{
            "id": course.id,
            "name": course.name,
            "semester": course.semester,
            "teacher": course.teacher.username,
            "students": [student.username for student in course.students.all()]
        } for course in courses]
    }

    return JsonResponse(response_data)


def create(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        name = req_body["name"]
        semester = req_body["semester"]
        teacher = req_body["teacher"]
        students = req_body["students"]
        
        course = Course.objects.create(
            name = name,
            semester = semester,
            teacher_id = teacher,
            students = students
        )
        
        course.students = students
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS

    return JsonResponse(response_data)


def update(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        id = req_body["id"]
        name = req_body["name"]
        semester = req_body["semester"]
        teacher = req_body["teacher"]
        students = req_body["students"]
        
        Course.objects.filter(id=id).update(
            name = name,
            semester = semester,
            teacher_id = teacher,
        )
        
        Course.objects.get(id=id).students = students
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def remove(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        c_id = req_body["id"]
        
        Course.objects.get(id=c_id).delete()
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)
