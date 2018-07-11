# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0010_auto_20180508_1443'),
    ]

    operations = [
        migrations.AddField(
            model_name='snapshot',
            name='exam',
            field=models.ForeignKey(to='hacklympics.Exam', blank=True, null=True),
        ),
    ]
