package com.example.classesapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classesapp.Adapter.BatchListAdapter;
import com.example.classesapp.Adapter.OwnerListAd;

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
import java.util.ArrayList;

import example.javatpoint.com.introonetimefirsttime.R;

public class EditBatchFields extends AppCompatActivity {

    String batch_id="";
    private InputStream is = null;
    private String result = null;
    private String line = null;
    ArrayList<String> mylist = new ArrayList<String>();
    EditText batch_name,batch_location,batchtime,batchteacher,maximumslot,batchstatus,txtTime;
    CheckBox chmonday,ch_tuesday,ch_wednesday,ch_thursady,ch_friday,ch_saturday,ch_sunday;
    Button update,cancel;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_batch_fields);
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

        ch_sunday=(CheckBox)findViewById(R.id.sunday);
cancel=(Button)findViewById(R.id.cancel);
cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        finish();
    }
});


        update=(Button)findViewById(R.id.save);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EditBatchFields.UpdateBatchData().execute(mylist);
            }
        });
        Intent intent=getIntent();
        batch_id = intent. getStringExtra("batch_id");
        new EditBatchFields.LoadBatchData().execute(mylist);
    }
    public class LoadBatchData extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(EditBatchFields.this);

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
                nameValuePairs.add(new BasicNameValuePair("batch_id", batch_id));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=display_batch_data");
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
                String batch_nameVa = "";
                String batch_statusVa = "";
                String batch_locationVa = "";
                String batch_teacherVa = "";
                String batch_timeVa = "";
                String batch_max_slotsVa = "";
                boolean batch_week_sun, batch_week_mon, batch_week_tue, batch_week_wed,batch_week_thu,batch_week_fri,batch_week_sat;
                Toast.makeText(getApplicationContext(), jArray.length()+"jjj", Toast.LENGTH_LONG).show();
                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jArray.getJSONObject(i);
                        batch_nameVa = jsonObject.getString("batch_name");
                        batch_statusVa = jsonObject.getString("batch_status");
                        batch_locationVa = jsonObject.getString("batch_location");
                        batch_teacherVa = jsonObject.getString("batch_teacher");
                        batch_timeVa = jsonObject.getString("batch_time");
                        batch_max_slotsVa = jsonObject.getString("batch_max_slots");


                        batch_week_sun = jsonObject.getBoolean("batch_week_sun");
                        batch_week_mon = jsonObject.getBoolean("batch_week_mon");
                        batch_week_tue = jsonObject.getBoolean("batch_week_tue");
                        batch_week_wed = jsonObject.getBoolean("batch_week_wed");
                        batch_week_thu = jsonObject.getBoolean("batch_week_thus");

                        batch_week_fri = jsonObject.getBoolean("batch_week_fri");
                        batch_week_sat = jsonObject.getBoolean("batch_week_sat");
                        batch_name.setText(batch_nameVa+"");
                        batchstatus.setText(batch_statusVa+"");
                        batch_location.setText(batch_locationVa+"");
                        batchtime.setText(batch_timeVa+"");
                        batchteacher.setText(batch_teacherVa+"");
                        maximumslot.setText(batch_max_slotsVa+"");


                        ch_sunday.setChecked(batch_week_sun);
                        chmonday.setChecked(batch_week_mon);
                        ch_tuesday.setChecked(batch_week_tue);
                        ch_wednesday.setChecked(batch_week_wed);
                        ch_thursady.setChecked(batch_week_thu);
                        ch_friday.setChecked(batch_week_fri);
                        ch_saturday.setChecked(batch_week_sat);





//                        batchIdArray.add(jsonObject.getString("batch_id"));
//                        batchData.add(new OwnerListAd(batch_name, batch_status));
//                        Toast.makeText(getApplicationContext(), "batch_name" + batch_name, Toast.LENGTH_LONG).show();
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
//    public void updateData(){

        public class UpdateBatchData extends AsyncTask<ArrayList<String>, Void, String> {

            private ProgressDialog dialog = new ProgressDialog(EditBatchFields.this);

            @Override
            protected void onPreExecute() {
                this.dialog.setMessage("Please wait");
                this.dialog.show();
            }

            @SuppressLint("WrongThread")
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

                nameValuePairs.add(new BasicNameValuePair("batch_name", batch_name.getText().toString()+"" ));
                nameValuePairs.add(new BasicNameValuePair("batch_location", batch_location.getText().toString()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_teacher",  batchteacher.getText().toString()+""));
                nameValuePairs.add(new BasicNameValuePair("batchtime", batchtime.getText().toString()+""));
                nameValuePairs.add(new BasicNameValuePair("batch_status", batchstatus.getText()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_mon", chmonday.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_tue", ch_tuesday.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_wed", ch_wednesday.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_thur", ch_thursady.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_fri", ch_friday.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_sat", ch_saturday.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_week_sun", ch_sunday.isChecked()+""));
                    nameValuePairs.add(new BasicNameValuePair("batch_id", batch_id));
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=update_batch_data");
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

                    JSONObject jObj = new JSONObject(result);
                    String   myoutput = jObj.getString("result1");
Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();

if(myoutput.equals("Update")){
Intent intent=new Intent(EditBatchFields.this,BatchList.class);
intent.putExtra("batch_id",batch_id);
startActivity(intent);
}

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }

    }
//}
