package com.example.ngoadmin.ui.userregistered;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ngoadmin.R;


import java.util.ArrayList;

public class MyListView2 extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> ngoList;
    private final ArrayList<String> eventStart;
    private final ArrayList<String> eventEnd;
    private final ArrayList<String> arrph;
    private final ArrayList<String> arremail;
    public MyListView2(Activity context, ArrayList<String> ngoList, ArrayList<String> eventStart,ArrayList<String> eventEnd,ArrayList<String> txtPh,ArrayList<String> txtEmail ) {
        super(context, R.layout.mylist1, ngoList);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.ngoList=ngoList;
        this.eventStart=eventStart;
        this.eventEnd=eventEnd;
        this.arrph=txtPh;
        this.arremail=txtEmail;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist1, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.txtName);
        TextView txtStart = rowView.findViewById(R.id.txtGender);
        TextView txtEnd = rowView.findViewById(R.id.txtDob);
        TextView txtPh = rowView.findViewById(R.id.txtPh);
        TextView txtEmail = rowView.findViewById(R.id.txtEmail);
        titleText.setText(ngoList.get(position));
        txtStart.setText("Gender :"+eventStart.get(position));
        txtEnd.setText("Dob :"+eventEnd.get(position));
        txtPh.setText("Mobile :"+arrph.get(position));
        txtEnd.setText("Email :"+arremail.get(position));

        return rowView;

    };
}