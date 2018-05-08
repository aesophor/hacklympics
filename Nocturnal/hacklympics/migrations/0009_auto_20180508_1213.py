# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0008_auto_20180503_0354'),
    ]

    operations = [
        migrations.RenameField(
            model_name='chatmsg',
            old_name='username',
            new_name='user',
        ),
    ]
