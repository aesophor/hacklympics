# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models
import datetime
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0006_auto_20180501_1425'),
    ]

    operations = [
        migrations.AddField(
            model_name='answer',
            name='create_time',
            field=models.DateTimeField(default=datetime.datetime(2018, 5, 2, 8, 23, 42, 424853, tzinfo=utc), editable=False),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='answer',
            name='update_time',
            field=models.DateTimeField(default=datetime.datetime(2018, 5, 2, 8, 23, 51, 712169, tzinfo=utc)),
            preserve_default=False,
        ),
    ]
