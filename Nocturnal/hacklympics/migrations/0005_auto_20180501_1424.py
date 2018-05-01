# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('hacklympics', '0004_auto_20180501_1417'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='testdata',
            name='problem',
        ),
        migrations.AddField(
            model_name='problem',
            name='input',
            field=models.CharField(default='', max_length=256),
            preserve_default=False,
        ),
        migrations.AddField(
            model_name='problem',
            name='output',
            field=models.CharField(default='', max_length=256),
            preserve_default=False,
        ),
        migrations.AlterField(
            model_name='course',
            name='name',
            field=models.CharField(max_length=50),
        ),
        migrations.AlterField(
            model_name='exam',
            name='title',
            field=models.CharField(max_length=50),
        ),
        migrations.AlterField(
            model_name='problem',
            name='title',
            field=models.CharField(max_length=50),
        ),
        migrations.DeleteModel(
            name='TestData',
        ),
    ]
