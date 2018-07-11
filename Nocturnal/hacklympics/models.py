from django.db import models
from django.utils import timezone
from os.path import dirname

class User(models.Model):
    username = models.CharField(max_length=20, primary_key=True)
    password = models.CharField(max_length=64)
    fullname = models.CharField(max_length=20)
    graduation_year = models.IntegerField()
    is_student = models.BooleanField()
    avatar = models.BinaryField()
    last_login_ip = models.CharField(max_length=16, null=True, blank=True)

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)

    @property
    def role(self):
        return "student" if self.is_student else "teacher"

    def __str__(self):
        return self.fullname


class Course(models.Model):
    name = models.CharField(max_length=50)
    semester = models.IntegerField()
    teacher = models.ForeignKey("User", related_name="teacher")
    students = models.ManyToManyField("User", related_name="students", blank=True)

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return "_".join((str(self.semester), self.name))


class Exam(models.Model):
    title = models.CharField(max_length=50)
    desc = models.CharField(max_length=256)
    duration = models.IntegerField()
    course = models.ForeignKey("Course")

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return self.title


class Problem(models.Model):
    title = models.CharField(max_length=50)
    desc = models.CharField(max_length=256)
    input = models.CharField(max_length=256)
    output = models.CharField(max_length=256)
    exam = models.ForeignKey("Exam")

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return self.title


class Answer(models.Model):
    filepath = models.CharField(max_length=200)
    source_code = models.CharField(max_length=5000)
    problem = models.ForeignKey("Problem")
    student = models.ForeignKey("User")
    
    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)
 
    @property
    def location(self):
        return dirname(self.filepath)

    @property
    def filename(self):
        return self.filepath.split("/")[-1:][0]

    @property
    def classname(self):
        return self.filename.split(".")[0]

    def __str__(self):
        return "_".join((self.problem.__str__(), self.student.__str__(), self.classname))


class Report(models.Model):
    score = models.IntegerField()
    comment = models.CharField(max_length=256)
    exam = models.ForeignKey("Exam")
    student = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return self.score


class Message(models.Model):
    source_ip = models.CharField(max_length=16)
    content = models.CharField(max_length=256)
    user = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return ": ".join((self.user.fullname, self.content))


class Snapshot(models.Model):
    img = models.BinaryField()
    exam = models.ForeignKey("Exam")
    student = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return "_".join((self.student.username, self.create_time.strftime("%Y-%m-%d_%H:%M")))
