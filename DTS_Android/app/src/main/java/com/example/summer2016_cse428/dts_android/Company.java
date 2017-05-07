package com.example.summer2016_cse428.dts_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Company extends AppCompatActivity {
    EditText CompanyName,Address,Phone;
    Button AddCompany;
    void setup(){
        CompanyName= (EditText)findViewById(R.id.CompanyNameeditText);
        Address= (EditText)findViewById(R.id.AddresseditText2);
        Phone= (EditText)findViewById(R.id.PhoneeditText3);
        AddCompany= (Button)findViewById(R.id.CreateCompanyBtn);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        setup();
        AddCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String companyname,address,phone;
                companyname=CompanyName.getText().toString().trim();
                address=Address.getText().toString().trim();
                phone=Phone.getText().toString().trim();
                if((!companyname.equals("")) && (!address.equals("")) && (!phone.equals(""))){
                    Toast.makeText(getApplicationContext(),companyname+"   "+address+"  "+phone,Toast.LENGTH_LONG).show();
                }

                else{
                    Toast.makeText(getApplicationContext(),"Please Fill All Field ",Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
