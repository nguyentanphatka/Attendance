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
    
    def save(self, *args, **kwargs):
        super(Student, self).save(*args, **kwargs)


#class FaceImages(models.Model):

    """ def path_and_rename(self, id):
        filename = self.images_data.name
        ext = filename.split('.')[-1]
        # get filename
        if ext == '':
            ext = '.png'
        if self.student_id:
            filename = 'students/student_{0}.{1}'.format(self.images_name, ext)
        else:
            filename = 'students/student_{0}{1}.{2}'.format(self.images_name, self.__str__(), ext)
        # return the whole path to the file
        return filename
    """
    # images_data = models.ImageField(upload_to=path_and_rename, null=True)
    # images_name = models.CharField(max_length=20, null=True)
    # date_upload = models.DateTimeField(auto_now_add=True, null=True)
    # student_id = models.ForeignKey(Student, on_delete=models.CASCADE, null=True)
    # images_data = models.FileField(upload_to='\media\students')
    # get url and return url of image here

    # def __str__(self):
    #    return self.images_data

class Task(models.Model):
    title = models.CharField(max_length=100, blank=False)
    student = models.ForeignKey(Student, on_delete=models.CASCADE)

    def save(self, *args, **kwargs):
        super(Task, self).save(*args, **kwargs)

class TaskImage(models.Model):
    task = models.ForeignKey(Task, on_delete=models.CASCADE)
    image = models.FileField(blank=True)