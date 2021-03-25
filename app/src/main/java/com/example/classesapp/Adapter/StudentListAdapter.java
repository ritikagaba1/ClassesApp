package com.example.classesapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import example.javatpoint.com.introonetimefirsttime.R;

public class StudentListAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<StudentListAd> reportsData;
    ArrayList<StudentListAd> reportsDataOriginal;

    public StudentListAdapter(Activity context, ArrayList<StudentListAd> reportsData) {
        super();
        this.context = context;
        this.reportsData = reportsData;

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

                reportsData = (ArrayList<StudentListAd>) results.values; // has

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
                List<StudentListAd> FilteredArrList = new ArrayList<>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<StudentListAd>(reportsData); // saves

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
                        StudentListAd model = reportsDataOriginal.get(i);

                        String data = model.getStudent_name();
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
        TextView student_name;
        TextView student_gender;
        TextView student_batch;
        //        TextView station_id;
        TextView student_birth;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.customlist_student, null);
            holder = new ViewHolder();
            holder.student_name = (TextView) convertView.findViewById(R.id.student_name);
            holder.student_gender = (TextView) convertView.findViewById(R.id.student_gender);
            holder.student_batch = (TextView) convertView.findViewById(R.id.student_batch);
            holder.student_birth = (TextView) convertView.findViewById(R.id.student_birth);

//            holder.bmr_id = (TextView) convertView.findViewById(R.id.bmr_id);

//            holder.auto_number = (TextView) convertView.findViewById(R.id.auto_number);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        StudentListAd medicineListModel=reportsData.get(position);
        holder.student_name.setText(medicineListModel.getStudent_name());
        holder.student_gender
                .setText(medicineListModel.getStudent_gender());
        holder.student_batch.setText(medicineListModel.getStudent_batch());

        holder.student_birth.setText(medicineListModel.getStudent_birth());
        return convertView;
    }
}