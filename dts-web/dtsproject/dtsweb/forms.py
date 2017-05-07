from django import forms
from django.contrib.auth.models import User
from .models import UserModel, FileModel, CompanyModel

class SignUpForm(forms.ModelForm):
    Password = forms.CharField(label=("Password"), widget = forms.PasswordInput)
    ConfirmPassword = forms.CharField(label=("Password confirmation"), widget = forms.PasswordInput, help_text = ("Enter the same password as above, for verification."))
    class Meta:
        model = UserModel
        fields = ('Email','Password','ConfirmPassword','CompanyId','UserAclType','UserName','FirstName','LastName',)

class LoginForm(forms.ModelForm):
    Password = forms.CharField(label=("Password"), widget = forms.PasswordInput)
    class Meta:
    	model = UserModel
    	fields = ('UserName','Password',)

class ForgotPassForm(forms.ModelForm):
    Password = forms.CharField(label=("Password"), widget = forms.PasswordInput)
    class Meta:
    	model = UserModel
    	fields = ('UserName','Password',)

class FileUploadForm(forms.ModelForm):
    class Meta:
        """docstring for Meta."""
        model = FileModel
        fields = ('FileContent', 'Name',)

class CompanyForm(forms.ModelForm):
    """docstring for CompanyModel."""
    class Meta:
        """docstring for Meta."""
        model = CompanyModel
        fields = ('Name', 'Address', 'Phone',)
