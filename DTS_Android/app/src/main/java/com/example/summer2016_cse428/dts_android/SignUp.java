package com.example.summer2016_cse428.dts_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SignUp extends Activity {
    private ProgressDialog pDialog;
    EditText FirstName,LastName,UserName,Email,Password,ConfirmPassword;
    String f_name="";
    String l_name="";
    String u_name="";
    String email="";
    String pass="";
    String c_pass="";
    Button Signup;
    String objID=null;
    String msg;
    JSONObject obj;
    String response;
    int RESPONSE_CODE;
    String SignUp_URL="http://dtsservice.azurewebsites.net/api/Account/Register";

    void setup() {
        FirstName= (EditText)findViewById(R.id.editTextFirstName);
        LastName= (EditText)findViewById(R.id.editTextLastName);
        UserName= (EditText)findViewById(R.id.editTextUserName);
        Email= (EditText)findViewById(R.id.editTextEmail);
        Password= (EditText)findViewById(R.id.editTextPassword);
        ConfirmPassword= (EditText)findViewById(R.id.editTextPasswordConfirm);
        Signup=(Button)findViewById(R.id.buttonSignUp1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setup();

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                  f_name=FirstName.getText().toString().trim();
                  l_name=LastName.getText().toString().trim();
                  u_name=UserName.getText().toString().trim();
                  email=Email.getText().toString().trim();
                  pass=Password.getText().toString().trim();
                  c_pass=ConfirmPassword.getText().toString().trim();

                  if((!f_name.equals("")) && (!l_name.equals("")) && (!u_name.equals(""))
                          && (!email.equals("")) && (!pass.equals("")) &&
                          (!c_pass.equals(""))){
                      if(Validation.emailValidation(email)==true){
                          //Toast.makeText(getApplicationContext(),"User name "+u_name,Toast.LENGTH_LONG).show();
                          new HttpAsyncTask().execute(SignUp_URL);
                      }
                      else{
                          Toast.makeText(getApplicationContext(),"Invalid Email Address",Toast.LENGTH_LONG).show();
                      }
                  }

                  else{
                      Toast.makeText(getApplicationContext(),"Please Fill all field ",Toast.LENGTH_LONG).show();
                  }



            }
        });
    }



    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUp.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected String doInBackground(String... urls) {



            return POST(urls[0],f_name,l_name,u_name,email,pass,c_pass);
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();
             Toast.makeText(getBaseContext(), "Responese Code "+RESPONSE_CODE, Toast.LENGTH_LONG).show();


        }
    }




    private static String InputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    public  String POST(String url,String f_name,String l_name,String u_name,String email,String pass, String c_pass){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            String jsonstring = "";
            JSONObject jsonobject = new JSONObject();
            jsonobject.accumulate("FirstName",f_name);
            jsonobject.accumulate("LastName",l_name);
            jsonobject.accumulate("UserName",u_name);
            jsonobject.accumulate("Email",email);
            jsonobject.accumulate("Password",pass);
            jsonobject.accumulate("ConfirmPassword",c_pass);
            jsonobject.accumulate("UserAclType","1");
            jsonobject.accumulate("CompanyId","1");
            jsonstring= jsonobject.toString();
            StringEntity stringentity = new StringEntity(jsonstring);
            httppost.setEntity(stringentity);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse httpresponse = httpclient.execute(httppost);

            RESPONSE_CODE= httpresponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpresponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.d("Response: ",response);
            Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();


            Toast.makeText(getApplicationContext(),"Response Code: "+httpresponse.getStatusLine().getStatusCode()+"Response: "+response,Toast.LENGTH_LONG).show();
            String x= httpresponse.toString();
            Toast.makeText(getApplicationContext(),"Response Code: "+httpresponse.getStatusLine().getStatusCode()+"Response: "+x,Toast.LENGTH_LONG).show();

            inputStream = httpresponse.getEntity().getContent();
            if(inputStream != null) {
                result = InputStreamToString(inputStream);

            }
            else
                result = "Did not work!";

            Toast.makeText(getApplication(), "result: " + result, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }
}



