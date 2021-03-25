package com.example.classesapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classesapp.Server.MultipartUtility;
import com.example.classesapp.Server.ServerConstants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import example.javatpoint.com.introonetimefirsttime.R;

public class StudentProfile extends AppCompatActivity {
    Button btn_login,btn_cancel;
    EditText studentid, studentname,parentsname,mobileno,alternativemobileno,dateofbirth,startdate,emailid,classes,school;
    private DateFormat df = new SimpleDateFormat("h:mm a");
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
    private String time ,date;
    JSONArray jsonaray = new JSONArray();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private InputStream is=null;
    private String result=null;
    private String line=null;
    DatePicker picker;
    private int code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        dateofbirth = findViewById(R.id.dateofbirth);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, monthOfYear);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateofbirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(StudentProfile.this, date, c
                        .get(Calendar.YEAR), c.get(Calendar.MONTH),
                        c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btn_login = findViewById(R.id.btn_login);
        btn_cancel=findViewById(R.id.cancel);
        studentid = findViewById(R.id.studentid);
        studentname = findViewById(R.id.student_name);
        parentsname = findViewById(R.id.parentname);
        mobileno = findViewById(R.id.mobileno);
        alternativemobileno = findViewById(R.id.alternativemobileno);

        startdate = findViewById(R.id.startdate);
        emailid = findViewById(R.id.emailid);
        classes = findViewById(R.id.classes);
        school = findViewById(R.id.school);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SaveData();
                JSONObject jsnobj = new JSONObject();
                try {
                    jsnobj.put("student_id", studentid.getText().toString());
                    jsnobj.put("student_name", studentname.getText().toString());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                new StudentProfile.SaveData().execute(ServerConstants.TEST);
//                new AsyncRetrieve().execute();
                ArrayList<String> mylist = new ArrayList<String>();

                mylist.add(String.valueOf(studentid.getText().toString()));
                mylist.add(String.valueOf(studentname.getText().toString()));
                mylist.add(String.valueOf(parentsname.getText().toString()));
                mylist.add(String.valueOf(mobileno.getText().toString()));
                mylist.add(String.valueOf(alternativemobileno.getText().toString()));
                mylist.add(String.valueOf(dateofbirth.getText().toString()));
                mylist.add(String.valueOf(startdate.getText().toString()));
                mylist.add(String.valueOf(emailid.getText().toString()));
                mylist.add(String.valueOf(classes.getText().toString()));
                mylist.add(String.valueOf(school.getText().toString()));
                new sendUserDetailTOServer().execute(mylist);
                Intent intent=new Intent(StudentProfile.this,StudentList.class);
                startActivity(intent);

            }
        });
        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Add Student");
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateofbirth.setText(sdf.format(c.getTime()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public String getCurrentDate(){
        StringBuilder builder=new StringBuilder();;
        builder.append((picker.getMonth() + 1)+"/");//month is 0 based
        builder.append(picker.getDayOfMonth()+"/");
        builder.append(picker.getYear());
        return builder.toString();
    }
    public class sendUserDetailTOServer extends AsyncTask< ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(StudentProfile.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String studentid = passed.get(0);
            String student_name = passed.get(1);
            String parentsname = passed.get(2);
            String mobileno = passed.get(3);
            String alternativemobileno = passed.get(4);
            String dateofbirth = passed.get(5);
            String startdate = passed.get(6);
            String emailid = passed.get(7);
            String classes = passed.get(8);
            String school= passed.get(9);

            // current time
            time = df.format(Calendar.getInstance().getTime());
            date = dt.format(c.getTime());
            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("studentid", studentid ));
                nameValuePairs.add(new BasicNameValuePair("student_name", student_name));
                nameValuePairs.add(new BasicNameValuePair("parentsname",  parentsname));
                nameValuePairs.add(new BasicNameValuePair("mobileno", mobileno));
                nameValuePairs.add(new BasicNameValuePair("alternativemobileno", alternativemobileno));
                nameValuePairs.add(new BasicNameValuePair("dateofbirth", dateofbirth));
                nameValuePairs.add(new BasicNameValuePair("startdate", startdate));
                nameValuePairs.add(new BasicNameValuePair("emailid", emailid));
                nameValuePairs.add(new BasicNameValuePair("classes", classes));
                nameValuePairs.add(new BasicNameValuePair("school", school));

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=save_student_data");
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
               Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
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
    private class SaveData extends AsyncTask<String, String, JSONObject> {
//	        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            String responseSTR = "";
            JSONObject json = null;
            try {

                String charset = "UTF-8";
                MultipartUtility multipart = new MultipartUtility(args[0], charset);

                multipart.addFormField("action", "save_student_data");
//                multipart.addFormField("studentid", "123");
                multipart.addFormField("data", jsonaray.toString());
                List<String> response = multipart.finish();
                for (String line : response) {
                    responseSTR = line;
                }
                Log.i("Server DATA", responseSTR);
                json = new JSONObject(responseSTR);
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//	                        pDialog.cancel();
                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            if (json != null)  {
                try {
//                    boolean error = json.getBoolean("error");
                    String query=json.getString("query");
                    Log.e("query",query);
//                    if (!error) {
                        Toast.makeText(getApplicationContext(), query, Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
//                        //	            intent.putExtras(dataBundle);
//                        startActivity(intent);
//                        // new category created successfully
////                        Log.e("Record added successfully ", "> " + json.getString("message"));
//                    } else {
////                        Log.e("Add Record Error: ",
////                                "> " + json.getString("message"));
//                    }

                } catch (Exception e) {
                }

            }
        }
    }
    private class AsyncRetrieve extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(StudentProfile.this);
        HttpURLConnection conn;
        URL url = null;

        //this method will interact with UI, here display loading message
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        // This method does not interact with UI, You need to pass result to onPostExecute to display
        @Override
        protected String doInBackground(String... params) {
            try {
                // Enter URL address where your php file resides
                url = new URL("https://www.classestime.com/api/testnew.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                // Setup HttpURLConnection class to send and receive data from php
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                // setDoOutput to true as we recieve data from json file
                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }


        }

        // this method will interact with UI, display result sent from doInBackground method
        @Override
        protected void onPostExecute(String result) {

            pdLoading.dismiss();
            if(result.equals("Success! This message is from PHP")) {
              Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
            }else{
                // you to understand error returned from doInBackground method
//                Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();
            }

        }

    }
}

