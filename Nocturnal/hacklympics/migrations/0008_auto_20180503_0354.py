# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0007_auto_20180502_0823'),
    ]

    operations = [
        migrations.AlterField(
            model_name='answer',
            name='filepath',
            field=models.CharField(max_length=200),
        ),
    ]
