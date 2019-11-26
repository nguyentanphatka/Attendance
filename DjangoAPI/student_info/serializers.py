from rest_framework import serializers
from .models import Student
# from .models import FaceImages
from . import models
from .models import TaskImage
from .models import Task

"""
class FaceImagesSerializer(serializers.ModelSerializer):
    class Meta:
        model = FaceImages
        # fields = '__all__'
        # fields = ['id', 'images_data', 'images_name', 'date_upload', 'student_id']
        fields = ['images_data']
"""

class StudentSerializer(serializers.HyperlinkedModelSerializer):
    # face_images = FaceImagesSerializer(many=True, allow_null=True)
    """
    Because 'Student' is a reverse relationship on the User model,
    it will not be included by default when using the ModelSerializer class,
    so we needed to add an explicit field for it.
    """

    class Meta:
        model = Student
        # fields = '__all__'
        fields = ['id', 'student_id', 'student_name', 'student_email']

class TaskImageSerializer(serializers.ModelSerializer):
    class Meta:
        model = TaskImage
        fields = ('image',)


class TaskSerializer(serializers.HyperlinkedModelSerializer):
    Student = serializers.ReadOnlyField()
    images = TaskImageSerializer(many=True, read_only=True)

    class Meta:
        model = Task
        fields = '__all__'

    def create(self, validated_data):
        images_data = self.context.get('view').request.FILES
        task = Task.objects.create(title=validated_data.get('title', 'no-title'),
                                   user_id=1)
        for image_data in images_data.values():
            TaskImage.objects.create(task=task, image=image_data)
        return task