package com.example.classesapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classesapp.Server.MultipartUtility;

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

import example.javatpoint.com.introonetimefirsttime.R;

public class EditStudentsFields extends AppCompatActivity {
    Button btn_login, btn_cancel;
    EditText studentid, studentname, parentsname, mobileno, alternativemobileno, dateofbirth, startdate, emailid, classes, school;
    private DateFormat df = new SimpleDateFormat("h:mm a");
    private Calendar c = Calendar.getInstance();
    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
    private String time, date;
    JSONArray jsonaray = new JSONArray();
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private InputStream is = null;
    private String result = null;
    private String line = null;
    private int code;
    String student_id = "";
    ArrayList<String> mylist = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_edit);
        btn_login = findViewById(R.id.btn_login);
        btn_cancel = findViewById(R.id.cancel);
        studentid = findViewById(R.id.studentid);
        studentname = findViewById(R.id.student_name);
        parentsname = findViewById(R.id.parentname);
        mobileno = findViewById(R.id.mobileno);
        alternativemobileno = findViewById(R.id.alternativemobileno);
        dateofbirth = findViewById(R.id.dateofbirth);
        startdate = findViewById(R.id.startdate);
        emailid = findViewById(R.id.emailid);
        classes = findViewById(R.id.classes);
        school = findViewById(R.id.school);
        Intent intent = getIntent();
        student_id = intent.getStringExtra("auto_id");
        new EditStudentsFields.LoadBatchData().execute(mylist);
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
                new EditStudentsFields.sendUserDetailTOServer().execute(mylist);
                Intent intent=new Intent(EditStudentsFields.this,StudentList.class);
                startActivity(intent);

            }
        });
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Manager");
        }
//        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
//        getSupportActionBar().setTitle("Add Student");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public class sendUserDetailTOServer extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(EditStudentsFields.this);

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
            String school = passed.get(9);

            // current time
            time = df.format(Calendar.getInstance().getTime());
            date = dt.format(c.getTime());
            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("studentid", studentid));
                nameValuePairs.add(new BasicNameValuePair("student_name", student_name));
                nameValuePairs.add(new BasicNameValuePair("parentsname", parentsname));
                nameValuePairs.add(new BasicNameValuePair("mobileno", mobileno));
                nameValuePairs.add(new BasicNameValuePair("alternativemobileno", alternativemobileno));
                nameValuePairs.add(new BasicNameValuePair("dateofbirth", dateofbirth));
                nameValuePairs.add(new BasicNameValuePair("startdate", startdate));
                nameValuePairs.add(new BasicNameValuePair("emailid", emailid));
                nameValuePairs.add(new BasicNameValuePair("classes", classes));
                nameValuePairs.add(new BasicNameValuePair("school", school));
                nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=update_student_data");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")); // UTF-8  support multi language
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                //     Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                //  Log.e("Fail 1", e.toString());
                //  Log.d("setup Activity ","  fail 1  "+e.toString());
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                // Log.d("pass 2", "connection success " + result);
            } catch (Exception e) {
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

            try {
                JSONObject jObj = new JSONObject(result);
                String   myoutput = jObj.getString("result1");
                Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();

                if(myoutput.equals("Update")){
                    Intent intent=new Intent(EditStudentsFields.this,StudentList.class);
//                    intent.putExtra("batch_id",batch_id);
                    startActivity(intent);
                }


//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Toast.makeText(getApplicationContext(), result.toString(), Toast.LENGTH_LONG).show();
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


            } catch (Exception e) {
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
            if (json != null) {
                try {
//                    boolean error = json.getBoolean("error");
                    String query = json.getString("query");
                    Log.e("query", query);
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


    public class LoadBatchData extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(EditStudentsFields.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {

//            ArrayList<String> passed = alldata[0]; //get passed arraylist
//            String batch_name = passed.get(0);
//            String batch_location = passed.get(1);
//            String batch_teacher = passed.get(2);
//            String batchtime = passed.get(3);
//            String batch_status = passed.get(4);
//            String maximumslot = passed.get(5);
//            // current time

            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

//                nameValuePairs.add(new BasicNameValuePair("batch_name", batch_name ));
//                nameValuePairs.add(new BasicNameValuePair("batch_location", batch_location));
//                nameValuePairs.add(new BasicNameValuePair("batch_teacher",  batch_teacher));
//                nameValuePairs.add(new BasicNameValuePair("batchtime", batchtime));
//                nameValuePairs.add(new BasicNameValuePair("batch_status", batch_status));
                nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=display_student_data");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8")); // UTF-8  support multi language
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("pass 1", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                //  Log.d("setup Activity ","  fail 1  "+e.toString());
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.d("pass 2", "connection success " + result);
            } catch (Exception e) {
                Log.e("Fail 2", e.toString());
                //  Log.e("setup Activity  "," fail  2 "+ e.toString());
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

//                JSONObject jObj = new JSONObject(result);
                String studentid1 = "";
                String student_name = "";
                String parentnameS = "";
                String mobilenoS = "";
                String alternativemobilenoS = "";
                String dateofbirthS = "";
                String emailidS = "";
                String classesS = "";
                String schoolS = "";
                String start_dates = "";
                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jArray.getJSONObject(i);
                        studentid1 = jsonObject.getString("student_id");
                        student_name = jsonObject.getString("student_name");
                        parentnameS = jsonObject.getString("student_guardian_name");
                        mobilenoS = jsonObject.getString("student_mobile");
                        alternativemobilenoS = jsonObject.getString("student_mobile_2");
                        dateofbirthS = jsonObject.getString("student_dob");

                        start_dates = jsonObject.getString("student_start_date");
                        emailidS = jsonObject.getString("email_address");
                        classesS = jsonObject.getString("student_class_subject");
                        schoolS = jsonObject.getString("student_school_college");
                        studentname.setText(student_name + "");
                        studentid.setText(studentid1+"");
                        parentsname.setText(parentnameS +"");
                        mobileno.setText(mobilenoS + "");
                        alternativemobileno.setText(alternativemobilenoS + "");
                        dateofbirth.setText(dateofbirthS + "");
                        emailid.setText(emailidS + "");
                        classes.setText(classesS + "");
                        school.setText(schoolS + "");
startdate.setText(start_dates);


//                        batchIdArray.add(jsonObject.getString("batch_id"));
//                        batchData.add(new OwnerListAd(batch_name, batch_status));
                        Toast.makeText(getApplicationContext(), "batch_name11111" + parentnameS, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
//                    batchData.add(new OwnerListAd(batch_name, batch_status));


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}