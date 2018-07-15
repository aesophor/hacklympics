# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0013_message_exam'),
    ]

    operations = [
        migrations.AlterField(
            model_name='message',
            name='exam',
            field=models.ForeignKey(to='hacklympics.Exam'),
        ),
    ]
