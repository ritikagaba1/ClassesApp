package com.example.classesapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import com.example.classesapp.Adapter.BatchListAdapter;
import com.example.classesapp.Adapter.StudentListAd;
import com.example.classesapp.Adapter.StudentListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

import static com.example.classesapp.MainActivity.MyPREFERENCES;

public class StudentList extends AppCompatActivity {
    StudentListAdapter adapter;
    Spinner batchListSpinner;

    ArrayList<StudentListAd> batchData = new ArrayList<StudentListAd>();
    public Context context;
    private InputStream is = null;
    private String result = null;
    private String line = null;
    ArrayList batchIdArray = new ArrayList();
    String batch_id="",student_id="";
    ArrayList<String> mylist = new ArrayList<String>();
    String HttpURL = "https://www.classestime.com/api/classesapi.php?action=display_batch";
    ListView BatchList;
    ProgressBar progressBar;
    String userId = "";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        SharedPreferences sh
                = getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);

// The value will be default as empty string
// because for the very first time
// when the app is opened,
// there is nothing to show
        userId = sh.getString("userId", "");
        Toast.makeText(getApplicationContext(), userId, Toast.LENGTH_LONG).show();
//        ActionBar actionBar = getSupportActionBar(); // or getActionBar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Student Manager");
        }
//        ActionBar actionBar = getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().show();
//        getSupportActionBar().setTitle("Student Manager");
//        try {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        } catch (NullPointerException e) {
//            Log.e("SearchActivity Toolbar", "You have got a NULL POINTER EXCEPTION");
//        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        BatchList = findViewById(R.id.batch_list);
        mylist.add(String.valueOf("1"));

        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();

        {
            if (nf != null && nf.isConnected() == true) {
                //   Toast.makeText(this, "Internet Connection Available", Toast.LENGTH_LONG).show();

                new loadBmrList1().execute(mylist);

            } else {
                Toast.makeText(this, getResources().getString(R.string.connection_error_message), Toast.LENGTH_LONG).show();

            }

        }
//        new BatchList.loadBmrList1().execute(ServerConstants.TEST);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StudentProfile.class);
                //	            intent.putExtras(dataBundle);
//                startActivity(intent);
                startActivityForResult(intent, 2);
//                Toast.makeText(getApplicationContext(),"jjj",Toast.LENGTH_LONG).show();
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    public class DeleteBatch extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(StudentList.this);

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
                nameValuePairs.add(new BasicNameValuePair("student_id",student_id));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=delete_student");
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
        }

    }
    public class loadBmrList1 extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(StudentList.this);

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
                nameValuePairs.add(new BasicNameValuePair("user_id", userId));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("https://www.classestime.com/api/classesapi.php?action=display_student");
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
                String student_name = "";
                String student_gender= "";
                String student_batch= "";
                String student_birth= "";
                Toast.makeText(getApplicationContext(), jArray.length()+"jjj", Toast.LENGTH_LONG).show();
                for (int i = 0; i < jArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jArray.getJSONObject(i);
                        student_name = jsonObject.getString("student_name");
                        student_gender = jsonObject.getString("student_gender");
                        student_batch = jsonObject.getString("student_batch_name");
                        student_birth = jsonObject.getString("student_dob");
                        batchIdArray.add(jsonObject.getString("auto_id"));
                        batchData.add(new StudentListAd(student_name, student_batch,student_gender,student_birth));
//                        Toast.makeText(getApplicationContext(), "batch_name" + student_name, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
//                    batchData.add(new OwnerListAd(batch_name, batch_status));
                adapter = new StudentListAdapter(StudentList.this, batchData);
                BatchList.setAdapter(adapter);

                BatchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                        AlertDialog.Builder builderSingle = new AlertDialog.Builder(StudentList.this);
//                     builderSingle.setIcon(R.drawable.ic_start_up);
                        builderSingle.setTitle("Select Option");

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(StudentList.this, android.R.layout.simple_list_item_1);
                        arrayAdapter.add("Edit");
                        arrayAdapter.add("Delete");

                        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                if(strName.equals("Edit")){
                                    student_id= (String) batchIdArray.get(position);
                                    Intent intent=new Intent(StudentList.this,EditStudentsFields.class);
                                    intent.putExtra("auto_id",student_id);
                                    startActivity(intent);

                                }
                                if(strName.equals("Delete")){
                                    AlertDialog.Builder builderInner = new AlertDialog.Builder(StudentList.this);
                                    builderInner.setMessage("You Want to delete??");
                                    builderInner.setTitle("Delete");
                                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,int which) {
                                            student_id= (String) batchIdArray.get(position);
                                            new DeleteBatch().execute(mylist);
                                            dialog.dismiss();
//                                         adapter.notifyDataSetChanged();
                                        }
                                    });
                                    builderInner.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builderInner.show();
                                }


                            }
                        });
                        builderSingle.show();

//                     adapter.notifyDataSetChanged();
//                     String batch_id= (String) batchIdArray.get(position);
//                     Intent intent=new Intent(BatchList.this,EditBatchFields.class);
//                     intent.putExtra("batch_id",batch_id);
//                     startActivity(intent);

                    }
                });




            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("hello", newText);
                adapter.getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }

}



