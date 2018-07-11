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

from hacklympics.views import user, course, exam, problem, answer, message, snapshot

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),

    url(r'^user/online$', user.list_online),
    url(r'^exam/ongoing$', exam.list_ongoing),

    url(r'^user$', user.list),
    url(r'^user/login$', user.login),
    url(r'^user/logout$', user.logout),
    url(r'^user/register$', user.register),
    url(r'^user/reset$', user.reset),
    url(r'^user/(?P<username>[\w.@+-]+)$', user.get),
    
    url(r'^user/(?P<username>[\w.@+-]+)/message$', message.list),
    url(r'^user/(?P<username>[\w.@+-]+)/message/create$', message.create),
    url(r'^user/(?P<username>[\w.@+-]+)/message/(?P<m_id>\d+)$', message.get),
   
    url(r'^course$', course.list),
    url(r'^course/create$', course.create),
    url(r'^course/update$', course.update),
    url(r'^course/remove$', course.remove),
    url(r'^course/(?P<c_id>[\w.@+-]+)$', course.get),

    url(r'^course/(?P<c_id>\d+)/exam$', exam.list),
    url(r'^course/(?P<c_id>\d+)/exam/create$', exam.create),
    url(r'^course/(?P<c_id>\d+)/exam/update$', exam.update),
    url(r'^course/(?P<c_id>\d+)/exam/remove$', exam.remove),
    url(r'^course/(?P<c_id>\d+)/exam/launch$', exam.launch),
    url(r'^course/(?P<c_id>\d+)/exam/halt$', exam.halt),
    url(r'^course/(?P<c_id>\d+)/exam/attend$', exam.attend),
    url(r'^course/(?P<c_id>\d+)/exam/leave$', exam.leave),

    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)$', exam.get),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/owner$', exam.get_owner),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/remaining_time$', exam.get_remaining_time),
    
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/snapshot/create$', snapshot.create),

    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem$', problem.list),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/create$', problem.create),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/update$', problem.update),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/remove$', problem.remove),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)$', problem.get),
    
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/answer$', answer.list),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/answer/create$', answer.create),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/answer/update$', answer.update),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/answer/remove$', answer.remove),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/answer/validate$', answer.validate),
    url(r'^course/(?P<c_id>\d+)/exam/(?P<e_id>\d+)/problem/(?P<p_id>\d+)/answer/(?P<a_id>\d+)$', answer.get)
]
