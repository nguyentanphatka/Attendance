from django.urls import path, include
from . import views
from rest_framework import routers

router = routers.DefaultRouter()
router.register('Student', views.Upload)
# router.register('FaceImages', views.FaceImagesView)
urlpatterns = [
    path('', include(router.urls)),
]