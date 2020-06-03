package com.example.ngoadmin.ui.slideshow;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ngoadmin.R;
import com.example.ngoadmin.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
class TimePickerFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Activity has to implement this interface
        TimePickerDialog.OnTimeSetListener listener = (TimePickerDialog.OnTimeSetListener) getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
    }
}
public class SlideshowFragment extends Fragment implements View.OnClickListener{

    EditText eName, eStDate, eStTime, eEnDate, eEnTime, eDetails;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button btAdd;
    TimePickerDialog.OnTimeSetListener timePicker1;
    TimePicker timePicker2;
    DatePickerDialog picker1,picker2;
    int totalEvent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        eStDate = root.findViewById(R.id.etEventStart);
        eStTime = root.findViewById(R.id.etEventStartTime);
        eEnDate = root.findViewById(R.id.etEventEnd);
        eEnTime = root.findViewById(R.id.etEventEndTime);
        eName = root.findViewById(R.id.etEventName);
        eStTime.setOnClickListener(this);
        eEnTime.setOnClickListener(this);
        eStDate.setOnClickListener(this);
        eEnDate.setOnClickListener(this);
        eDetails=root.findViewById(R.id.etEventDetail);
        LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
        task1.execute();
        btAdd= root.findViewById(R.id.btRegEvent);
        btAdd.setOnClickListener(this);
        return root;
    }
    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {
        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private FirebaseUser currentUser = mAuth.getCurrentUser();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        Context context;
        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }
        @Override
        protected Void doInBackground(Void... voids) {
            DocumentReference docRef = db.collection("admin").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            totalEvent= Integer.parseInt(document.get("totalEvents").toString());
                            totalEvent++;

                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
            return null;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btRegEvent:

                DocumentReference docRef = db.collection("admin").document(currentUser.getUid());
                docRef
                        .update(
                                    "Event"+totalEvent+".Event Name",eName.getText().toString(),
                                "Event"+totalEvent+".Event Start Date",eStDate.getText().toString(),
                                "Event"+totalEvent+".Event End Date",eEnDate.getText().toString(),
                                "Event"+totalEvent+".Event Start Time",eStTime.getText().toString(),
                                "Event"+totalEvent+".Event End Time",eEnTime.getText().toString(),
                                "Event"+totalEvent+".Event Details",eDetails.getText().toString(),
                                "totalEvents",totalEvent+1+""



                        )
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully updated!");
                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.replace(R.id.nav_host_fragment, new HomeFragment());
                                ft.addToBackStack(null);
                                ft.commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error updating document", e);
                            }
                        });

            case R.id.etEventStart:
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker1 = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                eStDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker1.show();
                break;
            case R.id.etEventEnd:
                final Calendar cldr1 = Calendar.getInstance();
                int day1 = cldr1.get(Calendar.DAY_OF_MONTH);
                int month1 = cldr1.get(Calendar.MONTH);
                int year1 = cldr1.get(Calendar.YEAR);
                // date picker dialog
                picker2 = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year1, int monthOfYear1, int dayOfMonth1) {
                                eEnDate.setText(dayOfMonth1 + "/" + (monthOfYear1 + 1) + "/" + year1);
                            }
                        }, year1, month1, day1);
                picker2.show();
                break;
            case R.id.etEventStartTime:
                Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog dialog =
                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                eStTime.setText(""+hourOfDay+":"+minute);
                            }
                        }, mHour, mMinute, true);
                dialog.show();
                break;
            case R.id.etEventEndTime:
                Calendar c1 = Calendar.getInstance();
                int mHour1 = c1.get(Calendar.HOUR_OF_DAY);
                int mMinute1 = c1.get(Calendar.MINUTE);
                TimePickerDialog dialog1 =
                        new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override public void onTimeSet(TimePicker view, int hourOfDay1, int minute1) {
                                eEnTime.setText(""+hourOfDay1+":"+minute1);
                            }
                        }, mHour1, mMinute1, true);
                dialog1.show();
                break;
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        totalEvent=0;
    }
}
