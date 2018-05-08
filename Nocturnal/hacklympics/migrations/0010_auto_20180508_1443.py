# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0009_auto_20180508_1213'),
    ]

    operations = [
        migrations.CreateModel(
            name='Message',
            fields=[
                ('id', models.AutoField(primary_key=True, auto_created=True, serialize=False, verbose_name='ID')),
                ('source_ip', models.CharField(max_length=16)),
                ('content', models.CharField(max_length=256)),
                ('create_time', models.DateTimeField(editable=False)),
                ('user', models.ForeignKey(to='hacklympics.User')),
            ],
        ),
        migrations.RemoveField(
            model_name='chatmsg',
            name='user',
        ),
        migrations.DeleteModel(
            name='ChatMsg',
        ),
    ]
