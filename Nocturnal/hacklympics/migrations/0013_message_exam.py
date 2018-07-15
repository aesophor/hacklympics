# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0012_auto_20180711_0558'),
    ]

    operations = [
        migrations.AddField(
            model_name='message',
            name='exam',
            field=models.ForeignKey(null=True, blank=True, to='hacklympics.Exam'),
        ),
    ]
