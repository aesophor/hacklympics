# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0002_user_last_login_ip'),
    ]

    operations = [
        migrations.AlterField(
            model_name='course',
            name='students',
            field=models.ManyToManyField(to='hacklympics.User', null=True, related_name='students', blank=True),
        ),
        migrations.AlterField(
            model_name='user',
            name='last_login_ip',
            field=models.CharField(max_length=16, null=True, blank=True),
        ),
    ]
