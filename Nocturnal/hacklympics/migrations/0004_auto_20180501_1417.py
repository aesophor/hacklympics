# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0003_auto_20180419_0708'),
    ]

    operations = [
        migrations.RenameField(
            model_name='testdata',
            old_name='stdin',
            new_name='input',
        ),
        migrations.RenameField(
            model_name='testdata',
            old_name='stdout',
            new_name='output',
        ),
    ]
