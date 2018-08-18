from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.events.dispatcher import *
from hacklympics.events.events import *
from hacklympics.sessions import OngoingExams
from hacklympics.status_code import StatusCode
from hacklympics.models import *

from datetime import datetime
import base64
import json

def sync(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        student = req_body["student"]
        image = req_body["image"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        timestamp = datetime.now().strftime("%Y/%m/%d %H:%M")
        
        exam.snapshot_set.create(
            img = base64.b64decode(image),
            student_id = student
        )
        
        dispatch(NewSnapshotEvent(exam, student, image, timestamp), OngoingExams.get(exam).teachers)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def adjust_param(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        students = req_body["students"]
        quality = req_body["quality"]
        frequency = req_body["frequency"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        students = User.objects.filter(username__in=students)
        
        # Update the parameters in OngoingExams here.
        # But wait... I do not know the incoming parameters here
        # is for gengrp or spegrp? Enum?
        #OngoingExams.exams[exam].
        
        dispatch(AdjustSnapshotParamEvent(exam, quality, frequency), OngoingExams.get(exam).students)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    except Exception as e:
        print(e)

    return JsonResponse(response_data)

