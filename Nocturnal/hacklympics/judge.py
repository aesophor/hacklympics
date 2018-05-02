from hacklympics.models import *
from subprocess import call, check_output

def check(answer: Answer):
    input = answer.problem.input
    output = answer.problem.output

    print('==> Compiling ...' + answer.filepath)
    call(['javac', answer.filepath])

    student_output = check_output([
        'java', '-cp', answer.location, answer.classname, input
    ]).decode("utf-8")

    output = output[:-1] if output.endswith('\n') else output
    student_output = student_output[:-1] if student_output.endswith('\n') else student_output
    
    return student_output == output
