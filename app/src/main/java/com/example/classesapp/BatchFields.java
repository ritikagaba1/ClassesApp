package com.example.classesapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.security.auth.Subject;

import example.javatpoint.com.introonetimefirsttime.R;

import static com.example.classesapp.MainActivity.MyPREFERENCES;

public class BatchFields extends AppCompatActivity {
    Button save,cancel;
    private int mYear, mMonth, mDay, mHour, mMinute;
    EditText batch_name,batch_location,batchtime,batchteacher,maximumslot,batchstatus,txtTime;
    private InputStream is=null;
    private String result=null;
    private String line=null,userId="";
    CheckBox chmonday,ch_tuesday,ch_wednesday,ch_thursady,ch_friday,ch_saturday,ch_sunday;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_fields);
        save=findViewById(R.id.save);
        SharedPreferences sh
                = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

// The value will be default as empty string
// because for the very first time
// when the app is opened,
// there is nothing to show
        userId = sh.getString("userId", "");

        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        getSupportActionBar().setTitle("Add Batch");
        batch_name=(EditText)findViewById(R.id.batch_name);
        batch_location=(EditText)findViewById(R.id.batch_location);
        batchteacher=(EditText)findViewById(R.id.batchteacher);
        batchtime=(EditText)findViewById(R.id.batchtime);
        batchstatus=(EditText)findViewById(R.id.batchstatus);
        maximumslot=(EditText)findViewById(R.id.maximumslot);
        chmonday=(CheckBox)findViewById(R.id.monday);
        ch_tuesday=(CheckBox)findViewById(R.id.tuesday);
        ch_wednesday=(CheckBox)findViewById(R.id.wednesday);
        ch_thursady=(CheckBox)findViewById(R.id.thursday);
        ch_friday=(CheckBox)findViewById(R.id.friday);
        ch_saturday=(CheckBox)findViewById(R.id.saturday);
cancel=(Button)findViewById(R.id.cancel);
        ch_sunday=(CheckBox)findViewById(R.id.sunday);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        batchtime.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {

                                             final Calendar c = Calendar.getInstance();
                                             mHour = c.get(Calendar.HOUR_OF_DAY);
                                             mMinute = c.get(Calendar.MINUTE);
                                             Calendar mcurrentTime = Calendar.getInstance();
                                             int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                             int minute = mcurrentTime.get(Calendar.MINUTE);
                                             TimePickerDialog mTimePicker;
                                             mTimePicker = new TimePickerDialog(BatchFields.this, new TimePickerDialog.OnTimeSetListener() {
                                                 @Override
                                                 public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                     batchtime.setText( selectedHour + ":" + selectedMinute);
                                                 }
                                             }, hour, minute, true);//Yes 24 hour time
                                             mTimePicker.setTitle("Select Time");
                                             mTimePicker.show();
                                         }

                                     });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> mylist = new ArrayList<String>();
                if (TextUtils.isEmpty(batch_name.getText().toString().trim())) {
                    batch_name.setError("Enter Batch Name");
                    batch_name.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(batch_location.getText().toString().trim())) {
                    batch_location.setError("Enter Batch Location");
                    batch_location.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(batchtime.getText().toString().trim())) {
                    batchtime.setError("Enter Batch Time");
                    batchtime.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(batchteacher.getText().toString().trim())) {
                    batchteacher.setError("Enter Batch Teacher");
                    batchteacher.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(batchstatus.getText().toString().trim())) {
                    batchstatus.setError("Select Batch Status");
                    batchstatus.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(maximumslot.getText().toString().trim())) {
                    maximumslot.setError("Enter Maximum Slot");
                    maximumslot.requestFocus();
                    return;
                }
                mylist.add(String.valueOf(batch_name.getText().toString()));
                mylist.add(String.valueOf(batch_location.getText().toString()));
                mylist.add(String.valueOf(batchteacher.getText().toString()));
                mylist.add(String.valueOf(batchtime.getText().toString()));
                mylist.add(String.valueOf(batchstatus.getText().toString()));
                mylist.add(String.valueOf(maximumslot.getText().toString()));
                new BatchFields.save().execute(mylist);
            }
        });
    }

    public class save extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(BatchFields.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @SuppressLint("WrongThread")
        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String batch_name = passed.get(0);
            String batch_location = passed.get(1);
            String batch_teacher = passed.get(2);
            String batchtime = passed.get(3);
            String batch_status = passed.get(4);
            String maximumslot = passed.get(5);
            // current time

            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("batch_user_id", userId ));
                nameValuePairs.add(new BasicNameValuePair("batch_name", batch_name ));
                nameValuePairs.add(new BasicNameValuePair("batch_location", batch_location));
                nameValuePairs.add(new BasicNameValuePair("batch_teacher",  batch_teacher));
                nameValuePairs.add(new BasicNameValuePair("batchtime", batchtime));
                nameValuePairs.add(new BasicNameValuePair("batch_status", batch_status));
                nameValuePairs.add(new BasicNameValuePair("maximumslot", maximumslot));
                nameValuePairs.add(new BasicNameValuePair("batch_week_mon", chmonday.isChecked()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_week_tue", ch_tuesday.isChecked()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_week_wed", ch_wednesday.isChecked()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_week_thur", ch_thursady.isChecked()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_week_fri", ch_friday.isChecked()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_week_sat", ch_saturday.isChecked()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_week_sun", ch_sunday.isChecked()+""));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=save_batch");
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

                JSONObject jObj = new JSONObject(result);
                String   myoutput = jObj.getString("result1");

finish();
//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Log.d("ritikk",result.toString());
//                if(myoutput.toString().equals("Save")){
//                    Intent i=new Intent(BatchFields.this,BatchList.class);
//                    startActivity(i);
//                }
                Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();
//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
//                Log.i("query",result.toString());
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
