package com.example.classesapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.EventLogTags;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import example.javatpoint.com.introonetimefirsttime.R;

public class Dashboard extends AppCompatActivity {
    List<MyList>myLists;
    GridLayout rv;
    MyAdapter adapter;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_new);
        GridLayout grid = (GridLayout) findViewById(R.id.gridlayout);
        int childCount = grid.getChildCount();
        if(childCount==0){
            Intent intent = new Intent(getApplicationContext(), BatchList.class);

            startActivity(intent);
        }

//        setSingleEvent(gridLayout);
//        rv=(GridLayout) findViewById(R.id.rec);
//        rv.setHasFixedSize(true);
//        rv.setLayoutManager(new GridLayoutManager(this,3));
        myLists=new ArrayList<>();
        ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()==true )
        {
//            Toast.makeText(Dashboard.this, "Network Available", Toast.LENGTH_LONG).show();

        }
        else
        {
//            Toast.makeText(Dashboard.this, "Network Not Available", Toast.LENGTH_LONG).show();

            new AlertDialog.Builder(Dashboard.this)
                .setTitle(getResources().getString(R.string.app_name))
                .setMessage(getResources().getString(R.string.internet_error))
                .setPositiveButton("OK", null).show();
        }
//        getdata();
    }
//    private void setSingleEvent(GridLayout gridLayout) {
//        for(int i = 0; i<gridLayout.getChildCount();i++){
//            CardView cardView=(CardView)gridLayout.getChildAt(i);
//            final int finalI= i;
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(MainActivity.this,"Clicked at index "+ finalI,
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
//    }

//    private void getdata() {
//        myLists.add(new MyList(R.drawable.student));
//        myLists.add(new MyList(R.drawable.ic_discount));
//        myLists.add(new MyList(R.drawable.ic_discount));
//        myLists.add(new MyList(R.drawable.ic_discount));
//        myLists.add(new MyList(R.drawable.ic_discount));
//        myLists.add(new MyList(R.drawable.ic_discount));
//        myLists.add(new MyList(R.drawable.ic_discount));
//        myLists.add(new MyList(R.drawable.ic_discount));
//
//        adapter=new MyAdapter(myLists,this);
//        rv.setAdapter(adapter);
//      rv.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//          @Override
//          public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//              return false;
//          }
//
//          @Override
//          public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//          }
//
//          @Override
//          public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//          }
//      });
//    }
}
