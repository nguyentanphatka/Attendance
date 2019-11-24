from django.shortcuts import render
from rest_framework import viewsets
from .models import Student
from .models import FaceImages
from django.http import HttpResponse
from .serializers import StudentSerializer
from .serializers import FaceImagesSerializer
from rest_framework.views import APIView
from rest_framework.parsers import FormParser
from django.http.multipartparser import MultiPartParser
from . import helpers


# Create your views here.

class StudentView(viewsets.ModelViewSet):
    queryset = Student.objects.all().order_by('student_id')
    serializer_class = StudentSerializer


def student_show(request):
    if request.method == 'GET':
        students = Student.objects.all().order_by('student_id')
        students_serializer = StudentSerializerSerializer(students, many=True)
        return HttpResponse(status=HttpResponse.status_code)
    elif request.method == 'POST':
        students_data = HttpResponse(request)
        students_serializer = StudentSerializer(data=students_data)
        if students_serializer.is_valid():
            students_serializer.save()
            return HttpResponse(status=HttpResponse.status_code)
        return HttpResponse(status=HttpResponse.status_code)


class FaceImagesView(APIView):
    parser_classes = (MultiPartParser, FormParser)
    @classmethod
    def get_extra_actions(cls):
        return []


def get(self, request):
    all_images = FaceImages.objects.all()
    serializer = FaceImagesSerializer(all_images, many=True)
    return JsonResponse(serializer.data, safe=False)


def post(self, request, *args, **kwargs):
    student_id = request.data['student_id']

    images = dict(request.data.lists())['images_data']
    flag = 1
    arr = []
    for img_name in images:
        modified_data = modify_input_for_multiple_files(student_id,
                                                        img_name)
        file_serializer = ImageSerializer(data=modified_data)
        if file_serializer.is_valid():
            file_serializer.save()
            arr.append(file_serializer.data)
        else:
            flag = 0

    if flag == 1:
        return Response(arr, status=status.HTTP_201_CREATED)
    else:
        return Response(arr, status=status.HTTP_400_BAD_REQUEST)
