from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.exceptions import AlreadyLaunched, NotLaunched, AlreadyAttended, NotAttended 
from hacklympics.status_code import StatusCode
from hacklympics.sessions import OngoingExams
from hacklympics.models import *

from threading import Timer
import time
import json


def get(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)

    response_data["content"] = {
        "id": exam.id,
        "title": exam.title,
        "desc": exam.desc,
        "duration": exam.duration
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
            "duration": exam.duration
        } for exam in exams]
    }

    return JsonResponse(response_data)


def list_ongoing(request):
    response_data = {"statusCode": StatusCode.SUCCESS}

    response_data["content"] = {
        "exams": [{
            "courseID": exam.course.id,
            "examID": exam.id,
            "title": exam.title,
            "desc": exam.desc,
            "duration": exam.duration,
            "teacher": {
                "username": exam.course.teacher.username,
                "fullname": exam.course.teacher.fullname,
                "graduationYear": exam.course.teacher.graduation_year
            }
        } for exam in OngoingExams.exams]
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
            duration = duration
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


def get_remaining_time(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        
        # Remaining time of an exam (in seconds).
        # Format it to MM:SS in clients.
        remaining_time = int(exam.duration * 60  - (time.time() - int(OngoingExams.get(exam).start_time)))
        
        response_data["content"] = {
            "remainingTime": remaining_time
        }
    except NotLaunched:
        # Tell the clients the remaining_time is 0 if exam already ends.
        response_data["remaining_time"] = 0
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST

    return JsonResponse(response_data)


def get_owner(request, c_id, e_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        teacher = exam.course.teacher
        
        response_data["content"] = {
            "username": teacher.username,
            "fullname": teacher.fullname,
            "graduationYear": teacher.graduation_year
        }
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
       
        # Groupings should better off be independent of the server...
        gen_grp_snapshot_quality = req_body["genGrpSnapshotQuality"]
        gen_grp_snapshot_frequency = req_body["genGrpSnapshotFrequency"]
        
        spe_grp_snapshot_quality = req_body["speGrpSnapshotQuality"]
        spe_grp_snapshot_frequency = req_body["speGrpSnapshotFrequency"]
        
        keystroke_frequency = req_body["keystrokeFrequency"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
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
    except Exception as e:
        print(e)
    
    return JsonResponse(response_data)


def halt(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
       
        OngoingExams.remove(exam)
        OngoingExams.show()
    except NotLaunched:
        response_data["statusCode"] = StatusCode.NOT_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    except Exception as e:
        print(e)
    
    return JsonResponse(response_data)


def attend(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        username = req_body["username"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        user = User.objects.get(username=username)
        
        OngoingExams.get(exam).add(user)
        OngoingExams.show()
    except AlreadyAttended:
        response_data["statusCode"] = StatusCode.ALREADY_ATTENDED
    except NotLaunched:
        response_data["statusCode"] = StatusCode.NOT_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    except Exception as e:
        print(e)

    return JsonResponse(response_data)


def leave(request, c_id):
    response_data = {"statusCode": StatusCode.SUCCESS}
    
    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        e_id = req_body["examID"]
        username = req_body["username"]
        
        exam = Course.objects.get(id=c_id).exam_set.get(id=e_id)
        user = User.objects.get(username=username)
        
        OngoingExams.get(exam).remove(user)
        OngoingExams.show()
    except NotAttended:
        response_data["statusCode"] = StatusCode.NOT_ATTENDED
    except NotLaunched:
        response_data["statusCode"] = StatusCode.NOT_LAUNCHED
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    except Exception as e:
        print(e)

    return JsonResponse(response_data)
