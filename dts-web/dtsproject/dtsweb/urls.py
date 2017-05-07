from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^dashboard$',views.dashboard,name='dashboard'),
    url(r'^uploadFile$',views.uploadFile,name='uploadFile'),
    url(r'^single$',views.single,name='single'),

    url(r'^fileLists$',views.fileLists,name='fileLists'),
    url(r'^file/(?P<id>\d+)$',views.file,name='file'),

    url(r'^signup$',views.signUp,name='signup'),
    url(r'^login$',views.login,name='login'),
    url(r'^logout$',views.logout,name='logout'),
    url(r'^profile$',views.profile,name='profile'),
    url(r'^forgotpassword$',views.forgotpassword,name='forgotpassword'),

    url(r'^company$',views.company,name='company'),
    url(r'^companyInfo$',views.companyInfo,name='companyInfo'),

]
