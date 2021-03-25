package com.example.classesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import example.javatpoint.com.introonetimefirsttime.R;

public class Register extends AppCompatActivity {
    Button btn_save;
    EditText full_name,academy_name,email1,mobileno,pwd,retype;
    TextView btn_already;
    JSONArray jsonaray = new JSONArray();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private InputStream is=null;
    private String result=null;
    private String line=null;
    private int code;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        btn_save=findViewById(R.id.button_save);
        btn_already=findViewById(R.id.button_already);
        full_name=findViewById(R.id.full_name);
        academy_name=findViewById(R.id.academy_name);
        email1=findViewById(R.id.email1);
        mobileno=findViewById(R.id.mobileno);
        pwd=findViewById(R.id.pwd);
        retype=findViewById(R.id.retype);
        btn_already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Register.this,MainActivity.class);
                startActivity(i);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mylist = new ArrayList<String>();
                if (TextUtils.isEmpty(full_name.getText().toString().trim())) {
                    full_name.setError("Enter Full Name");
                    full_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(academy_name.getText().toString().trim())) {
                    academy_name.setError("Enter Academy Name");
                    academy_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email1.getText().toString().trim())) {
                    email1.setError("Enter Email");
                    email1.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(mobileno.getText().toString().trim())) {
                    mobileno.setError("Enter Mobile");
                    mobileno.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(pwd.getText().toString().trim())) {
                    pwd.setError("Enter Password");
                    pwd.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(retype.getText().toString().trim())) {
                    retype.setError("Enter Retype");
                    retype.requestFocus();
                    return;
                }
                Log.i("pwd",pwd.getText().toString());
                Log.i("retypepwd",retype.getText().toString());
                String password=pwd.getText().toString();
                String retypepw=retype.getText().toString();
                if(!password.equals(retypepw)){
                 Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_LONG).show();
                    retype.requestFocus();
                 return;
                }
                mylist.add(String.valueOf(full_name.getText().toString()));
                mylist.add(String.valueOf(academy_name.getText().toString()));
                mylist.add(String.valueOf(email1.getText().toString()));
                mylist.add(String.valueOf(mobileno.getText().toString()));
                mylist.add(String.valueOf(pwd.getText().toString()));
                mylist.add(String.valueOf(retype.getText().toString()));
                new Register.register().execute(mylist);
            }
        });
//        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
//        getSupportActionBar().setTitle("Register");

    }

    public class register extends AsyncTask< ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(Register.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String full_name = passed.get(0);
            String academy_name = passed.get(1);
            String email1 = passed.get(2);
            String mobileno = passed.get(3);
            String pwd = passed.get(4);

            // current time

            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("full_name", full_name ));
                nameValuePairs.add(new BasicNameValuePair("academy_name", academy_name));
                nameValuePairs.add(new BasicNameValuePair("email1",  email1));
                nameValuePairs.add(new BasicNameValuePair("mobileno", mobileno));
                nameValuePairs.add(new BasicNameValuePair("pwd", pwd));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=register");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs ,"UTF-8")); // UTF-8  support multi language
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                //     Log.e("pass 1", "connection success ");
            }
            catch(Exception e)
            {
                //  Log.e("Fail 1", e.toString());
                //  Log.d("setup Activity ","  fail 1  "+e.toString());
            }

            try
            {
                BufferedReader reader = new BufferedReader (new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                // Log.d("pass 2", "connection success " + result);
            }
            catch(Exception e)
            {
                //  Log.e("Fail 2", e.toString());
                //  Log.e("setup Activity  "," fail  2 "+ e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            //  Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            try
            {


//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Log.i("query",result.toString());
                Intent intent = new Intent(Register.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                ActivityCompat.finishAffinity(Register.this);
//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                //   Log.d("pass 3", "connection success " + result);
//                if(code==1)
//                {
//                    try {
////                        String Res_username =json_data.getString("Res_username");
////                        String Res_password =json_data.getString("Res_password");
////
////                        Intent nextScreen = new Intent(getApplicationContext(), HomeScreen.class);
////                        nextScreen.putExtra("username", Res_username );
////                        nextScreen.putExtra("password", Res_password);
////                        //Sending data to another Activity
////                        startActivity(nextScreen);
//
//                    }catch (Exception e){
//
//                    }
//                }


            }
            catch(Exception e)
            {
//                LoginError(" Network Problem , Please try again");
                //    Log.e("Fail 3 main result ", e.toString());
                // Log.d(" setup Activity "," fail 3 error - "+ result );
            }
        }

    }
}
