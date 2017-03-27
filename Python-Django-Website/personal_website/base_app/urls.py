# base_app/urls.py
from django.conf.urls import url
from base_app import views

urlpatterns = [
    url(r'^$', views.LandingPageView),
]
