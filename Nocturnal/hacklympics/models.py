from django.db import models
from django.utils import timezone

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
    name = models.CharField(max_length=20)
    semester = models.IntegerField()
    teacher = models.ForeignKey("User", related_name="teacher")
    students = models.ManyToManyField("User", related_name="students")

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
    title = models.CharField(max_length=20)
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
    title = models.CharField(max_length=20)
    desc = models.CharField(max_length=256)
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


class TestData(models.Model):
    stdin = models.CharField(max_length=256)
    stdout = models.CharField(max_length=256)
    problem = models.ForeignKey("Problem")

    create_time = models.DateTimeField(editable=False)
    update_time = models.DateTimeField()

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        self.update_time = timezone.now()
        super().save(*args, **kwargs)


class Answer(models.Model):
    source_code = models.CharField(max_length=5000)
    filepath = models.CharField(max_length=32)
    problem = models.ForeignKey("Problem")
    student = models.ForeignKey("User")


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


class ChatMsg(models.Model):
    source_ip = models.CharField(max_length=16)
    content = models.CharField(max_length=256)
    username = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return self.content


class Snapshot(models.Model):
    img = models.BinaryField()
    student = models.ForeignKey("User")

    create_time = models.DateTimeField(editable=False)

    def save(self, *args, **kwargs):
        if not self.create_time:
            self.create_time = timezone.now()
        super().save(*args, **kwargs)

    def __str__(self):
        return "_".join((self.student.username, self.create_time))
