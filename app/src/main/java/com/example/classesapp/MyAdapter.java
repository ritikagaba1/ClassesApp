package com.example.classesapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import example.javatpoint.com.introonetimefirsttime.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MyList> myListList;
    private Context ct;

    public MyAdapter(List<MyList> myListList, Context ct) {
        this.myListList = myListList;
        this.ct = ct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_dashboard_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        MyList myList = myListList.get(position);
        holder.imageView.setImageDrawable(ct.getResources().getDrawable(myList.getImage()));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ct, myList.getImage(position), Toast.LENGTH_SHORT).show();


                Toast.makeText(ct, "hhh" + position, Toast.LENGTH_LONG).show();
                if (position == 0) {
                    Intent intent = new Intent(ct, StudentList.class);

                    ct.startActivity(intent);
                }
                if(position==1){
                    Intent intent = new Intent(ct, BatchList.class);

                    ct.startActivity(intent);
                }
                if(position==3){
                    Intent intent = new Intent(ct, AttendanceList.class);

                    ct.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return myListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.myimage);
        }
    }
}

