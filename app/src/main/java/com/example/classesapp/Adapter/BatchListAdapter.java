package com.example.classesapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.classesapp.Subject1;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import example.javatpoint.com.introonetimefirsttime.R;

public class BatchListAdapter extends BaseAdapter implements Filterable {
    Activity context;
    ArrayList<OwnerListAd> reportsData;
    ArrayList<OwnerListAd> reportsDataOriginal;

    public BatchListAdapter(Activity context, ArrayList<OwnerListAd> reportsData) {
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

                reportsData = (ArrayList<OwnerListAd>) results.values; // has

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
                List<OwnerListAd> FilteredArrList = new ArrayList<OwnerListAd>();

                if (reportsDataOriginal == null) {
                    reportsDataOriginal = new ArrayList<OwnerListAd>(reportsData); // saves

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
                        OwnerListAd model = reportsDataOriginal.get(i);

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
        TextView batch_status;
        TextView bmr_id;
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
            convertView = inflater.inflate(R.layout.custom_list_owner, null);
            holder = new ViewHolder();
            holder.batch_name = (TextView) convertView.findViewById(R.id.batch_name);
            holder.batch_status = (TextView) convertView.findViewById(R.id.batch_status);
//            holder.bmr_id = (TextView) convertView.findViewById(R.id.bmr_id);

//            holder.auto_number = (TextView) convertView.findViewById(R.id.auto_number);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        OwnerListAd medicineListModel=reportsData.get(position);
        holder.batch_name.setText(medicineListModel.getBatch_name());
        holder.batch_status.setText(medicineListModel.getBatch_status());
//        holder.bmr_id.setText(medicineListModel.getBmr_id());

//        holder.auto_number.setText(medicineListModel.getAuto_number());
        return convertView;
    }
}