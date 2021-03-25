package com.example.classesapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.classesapp.AttendanceList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import example.javatpoint.com.introonetimefirsttime.R;



public class AttendanceListAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<AttendanceListAd> reportsData;
    ArrayList<AttendanceListAd> reportsDataOriginal;

    public AttendanceListAdapter(Activity context, ArrayList<AttendanceListAd> reportsData) {
        super();
        this.context = context;
        this.reportsData = reportsData;

    }

    public AttendanceListAdapter(AttendanceList context, ArrayList<OwnerListAd> batchData) {
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

                reportsData = (ArrayList<AttendanceListAd>) results.values; // has

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
                List<AttendanceListAd> FilteredArrList = new ArrayList<>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<AttendanceListAd>(reportsData); // saves

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
                        AttendanceListAd model = reportsDataOriginal.get(i);

                        String data = model.getBatch_name();
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
        TextView batch_name;
        TextView batch_time;
        TextView present;
        TextView absent;
        TextView leaves;
        //        TextView station_id;
        TextView auto_number;

    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder;
        LayoutInflater inflater =  context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.custom_list_attendance, null);
            holder = new ViewHolder();
            holder.batch_name = (TextView) convertView.findViewById(R.id.batchname);
            holder.batch_time = (TextView) convertView.findViewById(R.id.student_gender);
            holder.present = (TextView) convertView.findViewById(R.id.present_total);
            holder.absent = (TextView) convertView.findViewById(R.id.absent_total);
            holder.leaves = (TextView) convertView.findViewById(R.id.leaves_total);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        AttendanceListAd medicineListModel=reportsData.get(position);
        holder.batch_name.setText(medicineListModel.getBatch_name());
        holder.batch_time.setText(medicineListModel.getBatch_time());
        holder.present.setText(medicineListModel.getPresent());

        holder.absent.setText(medicineListModel.getAbsent());
        holder.leaves.setText(medicineListModel.getTotal_leaves());
        return convertView;
    }
}