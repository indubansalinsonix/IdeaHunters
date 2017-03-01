package com.ideahunters.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ideahunters.R;
import com.ideahunters.model.CompanyData;
import com.ideahunters.view.activity.SignUpActivity;

import java.util.ArrayList;


/**
 * Created by insonix on 15/2/17.
 */

public class CompanyListAdapter extends ArrayAdapter<CompanyData> {

   private ArrayList<CompanyData> list=new ArrayList<>();
    private  Context context;

    public CompanyListAdapter(Context context, ArrayList<CompanyData> list) {
        super(context, 0);
        this.context = context;
        this.list=list;
    }

    /*public CompanyListAdapter(Context context, ArrayList<CompanyData> list) {
        super(context,list);

        this.items = items;
        this.ids = ids;
    }
*/


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater
                .inflate(R.layout.spinner_item, parent, false);
        TextView t1 = (TextView) rowView.findViewById(R.id.text);

        t1.setText(list.get(position).getCompany());
       rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof SignUpActivity){
                    ((SignUpActivity)context).setEmpCodeId(list.get(position).getCodeName(),list.get(position).getCompany());
                }
            }
        });
        return rowView;
    }



}