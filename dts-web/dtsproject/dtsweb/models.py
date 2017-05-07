from django.db import models

# Create your models here.

class UserModel(models.Model):
	"""create users forms & save it to service"""
	Email = models.CharField(max_length=100)
	Password = models.CharField(max_length=100)
	ConfirmPassword = models.CharField(max_length=100)
	CompanyId = models.IntegerField(default=100)
	UserAclType = models.IntegerField(default=100)
	UserName = models.CharField(max_length=100)
	FirstName = models.CharField(max_length=100)
	LastName = models.CharField(max_length=100)

class FileModel(models.Model):
	"""To Save files in service"""
	FileContent = models.FileField()
	Name = models.CharField(max_length=100)
	DocType = models.CharField(max_length=10)


class CompanyModel(models.Model):
	"""Company info for form & service"""
	Name = models.CharField(max_length=100)
	Address = models.CharField(max_length=100)
	Phone = models.CharField(max_length=100)
