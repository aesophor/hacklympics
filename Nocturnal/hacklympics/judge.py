from subprocess import call, check_output, CalledProcessError
from hacklympics.models import Answer

import os

def check(answer: Answer):
    try:
        input = str.encode(answer.problem.input)
        output = str.encode(answer.problem.output)
        
        print("==> Preparing source code ...")
        prepare(answer)
        
        print("==> Compiling ..." + answer.filepath)
        call(['javac', answer.filepath])
        
        print("==> Executing ..." + answer.classname)
        student_output = check_output([
            'java', '-cp', answer.location, answer.classname
        ], input = input)
        
        output = output[:-1] if output.endswith(b'\n') else output
        student_output = student_output[:-1] if student_output.endswith(b'\n') else student_output
        
        print("--> correct output:\n" + output.decode("utf-8"))
        print("--> student output:\n" + student_output.decode("utf-8"))
    except CalledProcessError as e:
        print(e)

    return student_output == output


def prepare(answer: Answer):
    try:
        if not os.path.exists(answer.location):
            os.makedirs(answer.location)
        
        with open(answer.filepath, 'w+') as source:
            source.write(answer.source_code)
            source.flush()
    except Exception as e:
        print(e)
