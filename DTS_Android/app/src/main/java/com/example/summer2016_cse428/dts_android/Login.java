package com.example.summer2016_cse428.dts_android;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class Login extends Activity {
    String LOGIN_URL="http://dtsservice.azurewebsites.net/token";
    private ProgressDialog pDialog;
    TextView testing;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    public static String preferences="login";
    String username="";
    String password="";
    EditText u_name,pass;
    Button btnLogin;
    int RESPONSE_CODE;
    String response="";

    void init(){
        u_name=(EditText)findViewById(R.id.LoginUsername);
        pass=(EditText)findViewById(R.id.LoginPassword);
        btnLogin=(Button)findViewById(R.id.buttonLogin);
        testing= (TextView)findViewById(R.id.testing);


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        shared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        editor= shared.edit();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username= u_name.getText().toString().trim();
                password=pass.getText().toString().trim();
                if((!username.equals("")) && (!password.equals(""))){
                    Toast.makeText(getApplicationContext(),"username "+username+" pass "+password,Toast.LENGTH_LONG).show();
                    new HttpAsyncTask().execute(LOGIN_URL);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please Fill All Field ",Toast.LENGTH_LONG).show();
                }


            }
        });
    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected String doInBackground(String... urls) {



            return POST(urls[0],username,password);
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing())
                pDialog.dismiss();
            Toast.makeText(getBaseContext(), "Responese Code "+RESPONSE_CODE, Toast.LENGTH_LONG).show();
            if(RESPONSE_CODE==200) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String access_token = obj.getString("access_token");
                    String username=obj.getString("userName");
                    String token_type=obj.getString("token_type");
                    editor.putString("username",username);
                    editor.putString("access_token",access_token);
                    editor.putString("password",password);
                    editor.putString("token_type",token_type);
                    editor.commit();
                    //testing.setText(access_token);
                    Toast.makeText(getApplicationContext(),"Access Token "+access_token,Toast.LENGTH_LONG).show();
                    Intent i=new Intent(Login.this,Home.class);
                    startActivity(i);
                } catch (Exception e) {

                }
            }
            else{
               try{
                   JSONObject obj = new JSONObject(response);
                   String error=obj.getString("error");
                   String error_description=obj.getString("error_description");
                   testing.setText(error_description);
                   Toast.makeText(getApplicationContext(), "Error "+error_description, Toast.LENGTH_LONG).show();
               }
               catch(Exception e){

               }
            }

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


    public  String POST(String url,String username,String password){
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httppost.setHeader("grant_type","password");
            httppost.setEntity(new StringEntity("grant_type=password&UserName="+username+"&Password="+password,"UTF-8"));
            HttpResponse httpresponse = httpclient.execute(httppost);
            RESPONSE_CODE= httpresponse.getStatusLine().getStatusCode();
            HttpEntity httpEntity = httpresponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.d("Response body: ",response);


            Toast.makeText(getApplicationContext(),"Response Code: "+httpresponse.getStatusLine().getStatusCode()+"Response: "+response,Toast.LENGTH_LONG).show();
            String x= httpresponse.toString();

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
