package com.example.ngoclient.ui.gallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ngoclient.R;

import java.util.ArrayList;

public class MyListView2 extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ngoList;
    private final ArrayList<String> eventStart;
    private final ArrayList<String> eventEnd;
    public MyListView2(Activity context, ArrayList<String> ngoList, ArrayList<String> eventStart,ArrayList<String> eventEnd ) {
        super(context, R.layout.mylist1, ngoList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ngoList=ngoList;
        this.eventStart=eventStart;
        this.eventEnd=eventEnd;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist1, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.txtName);
        TextView txtStart = rowView.findViewById(R.id.txtStart);
        TextView txtEnd = rowView.findViewById(R.id.txtEnd);
        titleText.setText(ngoList.get(position));
        txtStart.setText("Start Date :"+eventStart.get(position));
        txtEnd.setText("End Date :"+eventEnd.get(position));


        return rowView;

    };
}