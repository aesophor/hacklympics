from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.events.dispatcher import *
from hacklympics.events.events import *
from hacklympics.sessions import OngoingExams
from hacklympics.status_code import StatusCode
from hacklympics.models import *

import json

def create(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        student = req_body["student"]
        image = req_body["image"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        
        exam.snapshot_set.create(
            img = base64.decode(image),
            student_id = student
        )
        
        dispatch(NewSnapshotEvent(user, content), OngoingExams.get(exam).teachers)
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.NOT_REGISTERED
    except Exception as e:
        print(e)

    return JsonResponse(response_data)
