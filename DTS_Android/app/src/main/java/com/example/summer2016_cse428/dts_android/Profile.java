package com.example.summer2016_cse428.dts_android;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Profile extends Activity {
    TextView userName, Email, companyName, userTypee,ID,passHash;
    static SharedPreferences preferences;
    String PROFILE_URL = "http://dtsservice.azurewebsites.net/api/User/Info";

    private void setup() {

        userName = (TextView) findViewById(R.id.userNameTxt1);
        Email = (TextView) findViewById(R.id.emailTxt1);
        companyName = (TextView) findViewById(R.id.companyNameTxt1);
        userTypee = (TextView) findViewById(R.id.userTypeTxt1);
        ID = (TextView) findViewById(R.id.IDTxt1);
        passHash = (TextView) findViewById(R.id.PasswordHashTxt1);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rofile);
        setup();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        new HttpAsyncTask().execute(PROFILE_URL);

    }

    public static String GET(String url) {
        InputStream inputStream = null;
        String result="";
        String access = preferences.getString("access_token", null);
        String token = "Bearer " + access;
        HttpClient httpclient = new DefaultHttpClient();
        //String url = "https://server.com/stuff";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        //nameValuePairs.add(new BasicNameValuePair("Authorization",token));
        HttpClient httpClient = new DefaultHttpClient();
        String paramsString = URLEncodedUtils.format(nameValuePairs, "UTF-8");
        HttpGet httpGet = new HttpGet(url + "?" + paramsString);
        httpGet.setHeader("Accept", "application/json");
        httpGet.setHeader("Authorization",token);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (IOException e) {
            e.printStackTrace();
        }


         return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Received! " + result, Toast.LENGTH_LONG).show();
            try {
                JSONArray arr = new JSONArray(result);

                for(int i=0;i<arr.length();i++){
                    JSONObject obj=arr.getJSONObject(i);
                    String username=obj.getString("UserName");
                    String email= obj.getString("Email");
                    String userType=obj.getString("UserType");
                    String companyname= obj.getString("CompanyName");
                    String id= obj.getString("Id");
                    String passwordhash=obj.getString("PasswordHash");
                    username="User Name: "+username;
                    email="Email: "+email;
                    companyname="Company Name: "+companyname;
                    userType= "User Type: "+userType;
                    id="ID: "+id;
                    passwordhash="Password Hash: "+passwordhash;
                    userName.setText(username);
                    Email.setText(email);
                    companyName.setText(companyname);
                    userTypee.setText(userType);
                    ID.setText(id);
                    passHash.setText(passwordhash);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}



