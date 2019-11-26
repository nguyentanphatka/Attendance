from django.shortcuts import render
from rest_framework import viewsets
from .models import Student
# from .models import FaceImages
from django.http import HttpResponse
from .serializers import StudentSerializer
# from .serializers import FaceImagesSerializer
from rest_framework.views import APIView
from rest_framework.parsers import FormParser
from django.http.multipartparser import MultiPartParser
from . import helpers
from .serializers import TaskSerializer
from .models import Task

# Create your views here.
"""
class StudentView(viewsets.ModelViewSet):
    queryset = Student.objects.all().order_by('student_id')
    serializer_class = StudentSerializer


def student_show(request):
    if request.method == 'GET':
        students = Student.objects.all().order_by('student_id')
        students_serializer = StudentSerializer(students, many=True)
        return HttpResponse(status=HttpResponse.status_code)
    elif request.method == 'POST':
        students_data = HttpResponse(request)
        students_serializer = StudentSerializer(data=students_data)
        if students_serializer.is_valid():
            students_serializer.save()
            return HttpResponse(status=HttpResponse.status_code)
        return HttpResponse(status=HttpResponse.status_code)


class FaceImagesView(viewsets.ModelViewSet):
    serializer_class = StudentSerializer
    queryset = Student.objects.all()
"""
class Upload(viewsets.ModelViewSet):
    serializer_class = TaskSerializer
    queryset = Task.objects.all()