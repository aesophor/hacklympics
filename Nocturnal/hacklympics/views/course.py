from django.core.exceptions import ObjectDoesNotExist
from django.db import IntegrityError
from django.http import JsonResponse

from hacklympics.status_code import StatusCode
from hacklympics.session import OnlineUsers
from hacklympics.models import *

import json


def get(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    course = Course.objects.get(id=c_id)

    response_data["content"] = {
        "id": course.id,
        "name": course.name,
        "semester": course.semester,
        "teacher": course.teacher,
        "students": course.students.all()
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
            "students": [{
                "username": student.username,
                "fullname": student.fullname,
                "graduationYear": student.graduation_year,
            } for student in course.students.all()]
        } for course in courses]
    }

    return JsonResponse(response_data)
