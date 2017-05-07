package com.example.summer2016_cse428.dts_android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    static SharedPreferences prefer;
    Button dashboard,company,userProfile,logout;
    private void setup(){
        dashboard=(Button)findViewById(R.id.dashoardBtn);
        company=(Button)findViewById(R.id.companybtn);
        userProfile=(Button)findViewById(R.id.userprofileBtn);
        logout=(Button)findViewById(R.id.Logoutbutton);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setup();
        prefer= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(Home.this,Profile.class);
                startActivity(i);
            }
        });

        company.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Home.this,Company.class);
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor= prefer.edit();
                editor.clear();
                editor.commit();
                Intent i= new Intent(Home.this,MainActivity.class);
                startActivity(i);

            }
        });
    }
}
