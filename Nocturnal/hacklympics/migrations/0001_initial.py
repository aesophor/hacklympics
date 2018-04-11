# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Answer',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('source_code', models.CharField(max_length=5000)),
                ('filepath', models.CharField(max_length=32)),
            ],
        ),
        migrations.CreateModel(
            name='ChatMsg',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('source_ip', models.CharField(max_length=16)),
                ('content', models.CharField(max_length=256)),
                ('create_time', models.DateTimeField(editable=False)),
            ],
        ),
        migrations.CreateModel(
            name='Course',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('name', models.CharField(max_length=20)),
                ('semester', models.IntegerField()),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
            ],
        ),
        migrations.CreateModel(
            name='Exam',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('title', models.CharField(max_length=20)),
                ('desc', models.CharField(max_length=256)),
                ('duration', models.IntegerField()),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
                ('course', models.ForeignKey(to='hacklympics.Course')),
            ],
        ),
        migrations.CreateModel(
            name='Problem',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('title', models.CharField(max_length=20)),
                ('desc', models.CharField(max_length=256)),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
                ('exam', models.ForeignKey(to='hacklympics.Exam')),
            ],
        ),
        migrations.CreateModel(
            name='Report',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('score', models.IntegerField()),
                ('comment', models.CharField(max_length=256)),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
                ('exam', models.ForeignKey(to='hacklympics.Exam')),
            ],
        ),
        migrations.CreateModel(
            name='Snapshot',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('img', models.BinaryField()),
                ('create_time', models.DateTimeField(editable=False)),
            ],
        ),
        migrations.CreateModel(
            name='TestData',
            fields=[
                ('id', models.AutoField(auto_created=True, verbose_name='ID', serialize=False, primary_key=True)),
                ('stdin', models.CharField(max_length=256)),
                ('stdout', models.CharField(max_length=256)),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
                ('problem', models.ForeignKey(to='hacklympics.Problem')),
            ],
        ),
        migrations.CreateModel(
            name='User',
            fields=[
                ('username', models.CharField(max_length=20, serialize=False, primary_key=True)),
                ('password', models.CharField(max_length=64)),
                ('fullname', models.CharField(max_length=20)),
                ('graduation_year', models.IntegerField()),
                ('is_student', models.BooleanField()),
                ('avatar', models.BinaryField()),
                ('create_time', models.DateTimeField(editable=False)),
                ('update_time', models.DateTimeField()),
            ],
        ),
        migrations.AddField(
            model_name='snapshot',
            name='student',
            field=models.ForeignKey(to='hacklympics.User'),
        ),
        migrations.AddField(
            model_name='report',
            name='student',
            field=models.ForeignKey(to='hacklympics.User'),
        ),
        migrations.AddField(
            model_name='course',
            name='students',
            field=models.ManyToManyField(to='hacklympics.User', related_name='students'),
        ),
        migrations.AddField(
            model_name='course',
            name='teacher',
            field=models.ForeignKey(to='hacklympics.User', related_name='teacher'),
        ),
        migrations.AddField(
            model_name='chatmsg',
            name='username',
            field=models.ForeignKey(to='hacklympics.User'),
        ),
        migrations.AddField(
            model_name='answer',
            name='problem',
            field=models.ForeignKey(to='hacklympics.Problem'),
        ),
        migrations.AddField(
            model_name='answer',
            name='student',
            field=models.ForeignKey(to='hacklympics.User'),
        ),
    ]
