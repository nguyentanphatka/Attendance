from django.contrib.auth.models import AbstractUser
from django.db import models
from django.contrib.auth.models import AbstractUser
from django import forms


# Create your models here.

class Student(models.Model):
    student_id = models.CharField(max_length=20, null=False)
    student_name = models.CharField(max_length=20, null=True)
    student_email = models.EmailField(max_length=70, null=True, blank=True, unique=True)

    def __str__(self):
        return self.student_id


class FaceImages(models.Model):

    def path_and_rename(self, id):
        filename = self.images_data.name
        ext = filename.split('.')[-1]
        # get filename
        if ext == '':
            ext = '.png'
        if self.student_id:
            filename = 'students/student_{0}{1}.{2}'.format(self.images_name, self.id, ext)
        else:
            filename = 'students/student_{0}{1}.{2}'.format(self.images_name, self.__str__(), ext)
        # return the whole path to the file
        return filename

    images_data = models.ImageField(upload_to=path_and_rename, blank=False, null=True)
    # images_name = models.CharField(max_length=20, null=True)
    # date_upload = models.DateTimeField(auto_now_add=True, null=True)
    student_id = models.ForeignKey(Student, on_delete=models.CASCADE, null=True, default=1)

    # get url and return url of image here

    def __str__(self):
        return self.images_data.url

