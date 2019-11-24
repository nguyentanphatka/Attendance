from django.db import models
from django.contrib.auth.models import AbstractUser
import os
from uuid import uuid4


# Create your models here.
class Teacher(AbstractUser):
    def path_and_rename(self, name):
        filename = self.teacher_image.name
        ext = filename.split('.')[-1]
        # get filename
        if ext == '':
            ext = '.png'
        if self.pk:
            filename = 'teacher/teacher_{0}{1}{2}.{3}'.format(self.id,  self.teacher_code, self.username, ext)
        else:
            # set filename as random string
            # filename = '{}.{}'.format(uuid4().hex, ext)
            filename = 'teacher/teacher_{0}{1}{2}.{3}'.format(self.id, self.teacher_code, self.username, ext)
        # return the whole path to the file
        return filename

    teacher_code = models.CharField(max_length=20, null=False)
    email = models.EmailField(default='teacher@email.com', max_length=70, null=True, blank=True, unique=True)
    teacher_image = models.ImageField(upload_to=path_and_rename)
