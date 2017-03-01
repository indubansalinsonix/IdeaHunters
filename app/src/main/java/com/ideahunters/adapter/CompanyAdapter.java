package com.ideahunters.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ideahunters.R;
import com.ideahunters.view.activity.SignUpActivity;

import java.util.List;

/**
 * Created by insonix on 15/2/17.
 */

public class CompanyAdapter extends ArrayAdapter<String> {

    private final String[] items;
    private final String[] ids;
    private final Context context;

    public CompanyAdapter(Context context, String[] items,
                          String[] ids) {
        super(context, R.layout.spinner_item, items);
        this.context = context;
        this.items = items;
        this.ids = ids;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater
                .inflate(R.layout.spinner_item, parent, false);
        TextView t1 = (TextView) rowView.findViewById(R.id.text);
        final String s1 = items[position];
        t1.setText(s1);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, ids[position], Toast.LENGTH_SHORT).show();
                if(context instanceof SignUpActivity){
                    ((SignUpActivity)context).setEmpCodeId(ids[position],items[position]);
                }
            }
        });
        return rowView;
    }

}