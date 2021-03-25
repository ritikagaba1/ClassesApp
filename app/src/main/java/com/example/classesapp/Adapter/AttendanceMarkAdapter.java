package com.example.classesapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classesapp.AttendanceList;
import com.example.classesapp.BatchFields;

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
import java.util.List;
import java.util.Locale;

import example.javatpoint.com.introonetimefirsttime.R;



public class AttendanceMarkAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<AttendanceMarkAd> reportsData;
    ArrayList<AttendanceMarkAd> reportsDataOriginal;
    private InputStream is = null;
    private String result = null;
    private String line = null, userId = "";
    ArrayList<String> mylist = new ArrayList<String>();

    public AttendanceMarkAdapter(Activity context, ArrayList<AttendanceMarkAd> reportsData) {
        super();
        this.context = context;
        this.reportsData = reportsData;

    }

    public AttendanceMarkAdapter(AttendanceList context, ArrayList<OwnerListAd> batchData) {
    }


    public int getCount() {
        // TODO Auto-generated method stub
        return reportsData.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                reportsData = (ArrayList<AttendanceMarkAd>) results.values; // has

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults(); // Holds the
                // results of a
                // filtering
                // operation in
                // values
                // List<String> FilteredArrList = new ArrayList<String>();
                List<AttendanceMarkAd> FilteredArrList = new ArrayList<>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<AttendanceMarkAd>(reportsData); // saves

                }

                /********
                 *
                 * If constraint(CharSequence that is received) is null returns
                 * the mOriginalValues(Original) values else does the Filtering
                 * and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = reportsDataOriginal.size();
                    results.values = reportsDataOriginal;
                } else {

                    Locale locale = Locale.getDefault();
                    constraint = constraint.toString().toLowerCase(locale);
                    for (int i = 0; i < reportsDataOriginal.size(); i++) {
                        AttendanceMarkAd model = reportsDataOriginal.get(i);

                        String data = model.getStudent_id();
                        if (data.toLowerCase(locale).contains(constraint.toString())) {
                            FilteredArrList.add(model);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;

                }
                return results;
            }
        };
        return filter;
    }


    private class ViewHolder {
        TextView student_id;
        TextView student_name;

        //        TextView attendance;
        Switch sampleswitch;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_attendance_mark, null);
            holder = new ViewHolder();
            holder.student_id = (TextView) convertView.findViewById(R.id.studentid);
            holder.student_name = (TextView) convertView.findViewById(R.id.studentname);
//            holder.attendance=(TextView) convertView.findViewById(R.id.attendance);
            holder.sampleswitch = (Switch) convertView.findViewById(R.id.simpleSwitch);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AttendanceMarkAd medicineListModel = reportsData.get(position);
        holder.student_id.setText(medicineListModel.getStudent_id());
        holder.student_name.setText(medicineListModel.getStudent_name());
        holder.sampleswitch.setTag(R.id.section, medicineListModel.getStudent_id());
        holder.sampleswitch.setTag(R.id.hide_show, medicineListModel.getStudent_name());
        holder.sampleswitch.setTag(R.id.batchname, medicineListModel.getBatch_id());
        holder.sampleswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mylist.add(String.valueOf(holder.sampleswitch.getTag(R.id.section)));
                mylist.add(String.valueOf(holder.sampleswitch.getTag(R.id.hide_show)));
                mylist.add(String.valueOf(holder.sampleswitch.getTag(R.id.batchname)));


                if (isChecked) {
                    Toast.makeText(context, "Presnt", Toast.LENGTH_LONG).show();
                    mylist.add("Present");
                }
//                    holder.attendance.setText("Present");
                // The toggle is enabled
                else {
//                    holder.attendance.setText("Absent");
                    Toast.makeText(context, "Absent", Toast.LENGTH_LONG).show();
                    mylist.add("Absent");
                    // The toggle is disabled
                }
                Toast.makeText(context, "Prjjjjjesnt"+String.valueOf(medicineListModel.getDate()), Toast.LENGTH_LONG).show();
                mylist.add(String.valueOf(medicineListModel.getDate()));
//                new save().execute(mylist);
                new CheckAttendance().execute(mylist);
            }
        });
        return convertView;
    }

    public class save extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @SuppressLint("WrongThread")
        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String student_id = passed.get(0);
            String student_name = passed.get(1);
            String batch_name = passed.get(2);
            String attendance = passed.get(3);
            String attendate = passed.get(4);
            // current time

            try {
                 Log.d("setupActivity ", "user detail to server " + " "+ attendate);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
                nameValuePairs.add(new BasicNameValuePair("student_name", student_name));
                nameValuePairs.add(new BasicNameValuePair("batch_name", batch_name));
                nameValuePairs.add(new BasicNameValuePair("attendance", attendance));
                nameValuePairs.add(new BasicNameValuePair("attendate", attendate+""));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=insert_attendance");
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
                String myoutput = jObj.getString("result1");

//                finish();
//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Log.d("ritikk", result.toString());
//                if(myoutput.toString().equals("Save")){
//                    Intent i=new Intent(BatchFields.this,BatchList.class);
//                    startActivity(i);
//                }
//                Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();
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


            } catch (Exception e) {
//                LoginError(" Network Problem , Please try again");
                //    Log.e("Fail 3 main result ", e.toString());
                // Log.d(" setup Activity "," fail 3 error - "+ result );
            }
        }

    }

    public class CheckAttendance extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @SuppressLint("WrongThread")
        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String student_id = passed.get(0);
            String student_name = passed.get(1);
            String batch_name = passed.get(2);
            String attendance = passed.get(3);
            String attendate = passed.get(4);
            // current time

            try {
                // Log.e(" setup Activity ", "  user detail to server " + " "+ SendName+" "+Sendmobile+" "+Checkgender);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
                nameValuePairs.add(new BasicNameValuePair("student_name", student_name));
                nameValuePairs.add(new BasicNameValuePair("batch_name", batch_name));
                nameValuePairs.add(new BasicNameValuePair("attendate", attendate));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=check_attendance");
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
                String myoutput = jObj.getString("result1");
                Toast.makeText(context,myoutput,Toast.LENGTH_LONG).show();
                if (myoutput.equals("Yes")) {
                    new update().execute(mylist);
                } else {

                    new save().execute(mylist);
                }

//                finish();
//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Log.d("ritikk", result.toString());
//                if(myoutput.toString().equals("Save")){
//                    Intent i=new Intent(BatchFields.this,BatchList.class);
//                    startActivity(i);
//                }
//                Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();
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


            } catch (Exception e) {
//                LoginError(" Network Problem , Please try again");
                //    Log.e("Fail 3 main result ", e.toString());
                // Log.d(" setup Activity "," fail 3 error - "+ result );
            }
        }

    }

    public class update extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        @SuppressLint("WrongThread")
        protected String doInBackground(ArrayList<String>... alldata) {

            ArrayList<String> passed = alldata[0]; //get passed arraylist
            String student_id = passed.get(0);
            String student_name = passed.get(1);
            String batch_name = passed.get(2);
            String attendance = passed.get(3);
            String attendate = passed.get(4);
            // current time

            try {
                 Log.e(" setupActivityNew ", "  user detail to server " +attendate);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("student_id", student_id));
                nameValuePairs.add(new BasicNameValuePair("student_name", student_name));

                nameValuePairs.add(new BasicNameValuePair("batch_name", batch_name));
                nameValuePairs.add(new BasicNameValuePair("attendance", attendance));
                nameValuePairs.add(new BasicNameValuePair("attendate", attendate));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=update_attendance");
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
                String myoutput = jObj.getString("result1");

//                finish();
//                Toast.makeText(getApplicationContext(),result.toString(),Toast.LENGTH_LONG).show();
//                JSONObject json_data = new JSONObject(result);
//               String code1=(json_data.getString("message"));
                Log.d("ritikk", result.toString());
//                if(myoutput.toString().equals("Save")){
//                    Intent i=new Intent(BatchFields.this,BatchList.class);
//                    startActivity(i);
//                }
//                Toast.makeText(getApplicationContext(),myoutput,Toast.LENGTH_LONG).show();
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


            } catch (Exception e) {
//                LoginError(" Network Problem , Please try again");
                //    Log.e("Fail 3 main result ", e.toString());
                // Log.d(" setup Activity "," fail 3 error - "+ result );
            }
        }

    }
}

