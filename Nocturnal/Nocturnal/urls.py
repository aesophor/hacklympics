"""Nocturnal URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/1.8/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  url(r'^$', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  url(r'^$', Home.as_view(), name='home')
Including another URLconf
    1. Add a URL to urlpatterns:  url(r'^blog/', include('blog.urls'))
"""
from django.conf.urls import include, url
from django.contrib import admin

from hacklympics.views import user, course, exam, problem

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),

    url(r'^user$', user.list),
    url(r'^user/online$', user.list_online),

    url(r'^user/login$', user.login),
    url(r'^user/logout$', user.logout),
    url(r'^user/register$', user.register),
    url(r'^user/reset$', user.reset),
    url(r'^user/(?P<username>[\w.@+-]+)$', user.get),
    
    url(r'^course$', course.list),
    url(r'^course/create$', course.create),
    url(r'^course/remove$', course.remove),
    url(r'^course/(?P<c_id>[\w.@+-]+)$', course.get),

    url(r'^course/(?P<c_id>\d+)/exam$', exam.list),
    url(r'^course/(?P<c_id>\d+)/exam/create$', exam.create),
    url(r'^course/(?P<c_id>\d+)/exam/remove$', exam.remove),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)$', exam.get),

    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem$', problem.list),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/create$', problem.create),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/remove$', problem.remove),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)$', problem.get)
]
