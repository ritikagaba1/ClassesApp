package com.example.classesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import example.javatpoint.com.introonetimefirsttime.R;

public class MainActivity extends AppCompatActivity {

    Button btn_login;
EditText et_email,et_password;
    SharedPreferences pref;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private InputStream is=null;
    private String result=null;
    private String line=null;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String UserId = "userId";
TextView btn_register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn_login=findViewById(R.id.btn_login);
        btn_register=findViewById(R.id.btn_register);
        pref = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        Intent intent = new Intent(MainActivity.this,Dashboard.class);
        if(pref.contains("email") && pref.contains("password")){
            startActivity(intent);
        }
        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,Register.class);
                startActivity(i);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mylist = new ArrayList<String>();
                mylist.add(String.valueOf(et_email.getText().toString()));
                mylist.add(String.valueOf(et_password.getText().toString()));
//                Toast.makeText(getApplicationContext(),"chhhh",Toast.LENGTH_LONG).show();
                new MainActivity.login().execute(mylist);

//                     startActivity(new Intent(MainActivity.this,Dashboard.class));
            }
        });
        PrefManager prefManager = new PrefManager(getApplicationContext());
        if(prefManager.isFirstTimeLaunch()){
            prefManager.setFirstTimeLaunch(false);
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
        }
    }
    public void btn_Click(View view){
        Toast.makeText(MainActivity.this, "clicked on button", Toast.LENGTH_LONG).show();
    }

    public class login extends AsyncTask< ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String email = passed.get(0);
            String password = passed.get(1);


            // current time

            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("email", email ));
                nameValuePairs.add(new BasicNameValuePair("password", password));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=login");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs ,"UTF-8")); // UTF-8  support multi language
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                     Log.d("pass 1", "connection success ");
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
                 Log.d("pass 2", "connection success " + result);
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

                JSONObject jObj = new JSONObject(result);
             String   myoutput = jObj.getString("result1");
             String user_id=jObj.getString("user_id");
                String email=jObj.getString("email");
                String password=jObj.getString("password");
              SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("user_id", user_id);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.commit();

                Toast.makeText(getApplicationContext(),user_id.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Log.d("ritikk",result.toString());
                if(myoutput.toString().equals("Login")){
                    Intent i=new Intent(MainActivity.this,Dashboard.class);
                    startActivity(i);
                }
                Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();
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