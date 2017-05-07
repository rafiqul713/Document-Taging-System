from django.shortcuts import render, HttpResponse, HttpResponseRedirect
from django.shortcuts import render
import requests
from django.http import JsonResponse
from django.core import serializers
import json
from django import forms
from .forms import SignUpForm, LoginForm, FileUploadForm, CompanyForm
#import django.utils import simplejson as json
from django.contrib import messages

api_url=""
app_id = ""
api_response=""


def index(request):
    signup = SignUpForm()
    login=  LoginForm()
    # params={'signup':signup,'login':login}
    return render(request, 'landing/index.html', {'signup':signup,'login':login})

def dashboard(request):
    if request.session['CompanyId'] is not None and request.session['UserType'] == 0:
        print("Company id ase")
        pass
    else:
        print("Users company nai")
        return HttpResponseRedirect('/company')

    fileForm = FileUploadForm()
    if request.session['userName'] != 0:
        return render(request, 'admin/dashboard.html', {'fileForm': fileForm})
    else:
        return HttpResponseRedirect('/')

def company(request):
    companyForm = CompanyForm()
    if request.method == "POST" and request.session['CompanyId'] == 0:
        formData = CompanyForm(request.POST)

        Name = formData['Name'].value()
        Address = formData['Address'].value()
        Phone = formData['Phone'].value()
        UserId = request.session['Id']
        token = 'Bearer '+str(request.session['access_token'])

        params = {'Name':Name, 'Address':Address, 'Phone':Phone, 'UserId':UserId}
        response = requests.post("http://dtsservice.azurewebsites.net/api/Company", data = params, headers={'Authorization': token})
        response_data = response.json()
        request.session['CompanyId'] = response_data['Id']
        print(request.session['CompanyId'])
        request.session['CompanyName'] = response_data['Name']
        return HttpResponse(response.json())
    else:
        return render(request, 'admin/createCompany.html', {'companyForm': companyForm})

def companyInfo(request):
    token = 'Bearer '+str(request.session['access_token'])
    CompanyId = request.session['CompanyId']
    response = requests.get("http://dtsservice.azurewebsites.net/api/Company/"+str(CompanyId), headers={'Authorization': token})
    response_data = response.json()
    print(response_data)
    return render(request, 'admin/companyInfo.html', {'response_data': response_data})

def single(request):
    if request.session['userName'] != 0:
        return render(request, 'admin/single.html')
    else:
        return HttpResponseRedirect('/')

#only for ajax call
def uploadFile(request):
    print(request.body)
    # upload_url=api_url+""
    # try:
    #     params = request.body
    #     response = requests.post(upload_url,params,headers={'Content-Type': 'application/json'})
    #
    # except:
    #     print("Error")
    # if request.is_ajax():
    #     form = FileUploadForm(request.POST)
    # if form.is_valid():
    #     print('valid form')
    # else:
    #     print ('invalid form')
    #     print (form.errors)


    return HttpResponse(request.body)

def file(request,id):
    file_id=id
    get_file_url=api_url+""
    try:
        response = requests.get(get_file_url, params, headers={'Content-Type': 'application/json'})
        if response.status == 200:
            #display something
            pass
    except:
        print("Error")

def signUp(request):
    signup = SignUpForm()
    login =  LoginForm()

    if request.method == "POST":
        form = SignUpForm(request.POST)
        Email = form['Email'].value()
        Password = form['Password'].value()
        ConfirmPassword = form['ConfirmPassword'].value()
        UserName = form['UserName'].value()
        CompanyId = '0'
        UserAclType = '0'

        params = {'Email':Email, 'Password':Password, 'ConfirmPassword':ConfirmPassword, 'UserAclType':UserAclType, 'CompanyId':CompanyId, 'UserName':UserName}
        response = requests.post("http://dtsservice.azurewebsites.net/api/Account/Register", data = params)

        if response.status_code == 200:
            # request.flash['message'] = "Signup Success full. Please login now."
            messages.success(request, "Signup Successfull. Please login now.")
            return render(request, 'landing/index.html', {'signup':signup,'login':login})

        elif response.status_code != 200:
            response_data = response.json()
            Message = response_data['Message']
            # ModelState = response_data['ModelState']
            # request.flash['message'] = ModelState
            print(response_data)
            messages.success(request, Message)

            return render(request, 'landing/index.html', {'signup':signup,'login':login})


def login(request):
    if request.method == "POST":
        #forms
        signup = SignUpForm()
        login=  LoginForm()
        #get forms data
        form = LoginForm(request.POST)
        UserName = form['UserName'].value()
        Password = form['Password'].value()
        params = {'UserName':UserName, 'Password':Password, 'grant_type':'password'}
        response = requests.post("http://dtsservice.azurewebsites.net/token",data=params)

        if response.status_code == 200:
            response_data = response.json()

            print("Saving session data")
            request.session['userName'] = response_data['userName']
            # print(response_data['CompanyId'])
            # request.session['CompanyId'] = response_data['CompanyId']
            request.session['access_token'] = response_data['access_token']
            # print(request.session['access_token'])
            request.session['token_type'] = response_data['token_type']
            print("User succesfully loged in, take him to dashboard")

            # get company id
            token = 'Bearer '+str(request.session['access_token'])
            print("Access Token")
            print(token)
            response2 = requests.get("http://dtsservice.azurewebsites.net/api/User/Info", headers={'Authorization': token} )
            print("Response 2")
            print(response2.json())
            response_data2 = response2.json()
            print(response_data2)
            request.session['CompanyId'] = response_data2[0]['ConmapyId']
            request.session['Id'] = response_data2[0]['Id']
            request.session['UserType'] = response_data2[0]['UserType']

            return HttpResponseRedirect('/dashboard')
        elif response.status_code == 400 or response.status_code == 401:
            response_data = response.json()
            Message = response_data['error_description']
            messages.success(request, Message)
            return render(request, 'landing/index.html', {'signup':signup,'login':login})
        return HttpResponse(response)

def logout(request):
    try:
        print("logging out!")
        request.session['userName'] = 0
        request.session['access_token'] = 0
        print(request.session['userName'])
    except KeyError:
        pass
    return HttpResponseRedirect('/')


def profile(request):
    url = "http://dtsservice.azurewebsites.net/api/User/info"
    access_token = request.session['access_token']
    headers = {'Authorization' : 'Bearer '+access_token}
    response_data = requests.get(url, headers=headers)
    response_data = response_data.json()
    for i in response_data:
        response_data = i
    request.session['Email'] = response_data['Email']
    request.session['Id'] = response_data['Id']
    request.session['FirstName'] = response_data['FirstName']
    request.session['LastName'] = response_data['LastName']
    request.session['UserType'] = response_data['UserType']
    return render(request, 'admin/profile.html', {'response_data': response_data})


def forgotpassword(request):
    pass


def fileLists(request):
    pass
