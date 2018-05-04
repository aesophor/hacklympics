from django.core.exceptions import ObjectDoesNotExist
from django.http import JsonResponse

from hacklympics.exceptions import AlreadySubmitted
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
        "className": answer.classname,
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
            "className": answer.classname,
            "sourceCode": answer.source_code,
            "student": answer.student_id
        } for answer in answers]
    }

    return JsonResponse(response_data)


def create(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        filename = req_body["filename"]
        source_code = req_body["sourceCode"]
        student = req_body["student"]
        
        course = Course.objects.get(id=c_id)
        exam = course.exam_set.get(id=e_id)
        problem = exam.problem_set.get(id=p_id)
        answers = problem.answer_set
        
        if len(answers.all().filter(student_id=student)) > 0:
            raise AlreadySubmitted('Student' + student + 'has already submitted an answer!')
        
        filepath = "/".join([".", "data", course.teacher_id, course.name, exam.title, problem.title, student, filename])
        answer = answers.create(
            filepath = filepath,
            source_code = source_code,
            student_id = student
        )
        
        response_data["content"] = {
            "id": answer.id
        }
    except KeyError:
        response_data["statusCode"] = StatusCode.INSUFFICIENT_ARGS
    except ObjectDoesNotExist:
        response_data["statusCode"] = StatusCode.MATERIAL_DOES_NOT_EXIST
    except AlreadySubmitted:
        response_data["statusCode"] = StatusCode.ALREADY_SUBMITTED
    
    return JsonResponse(response_data)


def update(request, c_id, e_id, p_id):
    response_data = {"statusCode": StatusCode.SUCCESS}

    try:
        req_body = json.loads(request.body.decode("utf-8"))
        
        a_id = req_body["answerID"]
        filename = req_body["filename"]
        source_code = req_body["sourceCode"]
        
        course = Course.objects.get(id=c_id)
        exam = course.exam_set.get(id=e_id)
        problem = exam.problem_set.get(id=p_id)
        answers = problem.answer_set
        student = Answer.objects.get(id=a_id).student_id
        
        filepath = "/".join([".", "data", course.teacher_id, course.name, exam.title, problem.title, student, filename])
        
        answers.all().filter(id=a_id).update(
            filepath = filepath,
            source_code = source_code
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
