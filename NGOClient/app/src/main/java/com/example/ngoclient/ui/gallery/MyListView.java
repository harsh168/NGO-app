package com.example.ngoclient.ui.gallery;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ngoclient.R;

import java.util.ArrayList;

public class MyListView extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ngoList;

    public MyListView(Activity context, ArrayList<String> ngoList) {
        super(context, R.layout.mylist, ngoList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ngoList=ngoList;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.txtName);

        titleText.setText(ngoList.get(position));


        return rowView;

    };
}