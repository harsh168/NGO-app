package com.example.ngoclient.ui.slideshow;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.ngoclient.R;
import com.example.ngoclient.ui.gallery.GalleryFragment;
import com.example.ngoclient.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DetailFragment extends Fragment {

    int pos, id, flag=0,flag1=0;
    TextView txtName, txtStDt, txtStTi, txtEnDt, txtEnTi, txtDetails, txtError;

    int partNo = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail2, container, false);
        pos = getArguments().getInt("pos");
        id = getArguments().getInt("id");
        txtName = root.findViewById(R.id.txtName);
        txtStDt = root.findViewById(R.id.txtStart);
        txtStTi = root.findViewById(R.id.txtStartTime);
        txtEnDt = root.findViewById(R.id.txtEnd);
        txtError = root.findViewById(R.id.txtError);
        txtEnTi = root.findViewById(R.id.txtEndTime);
        txtDetails = root.findViewById(R.id.txtDetails);

        LoadAddressAsyncTask task1 = new LoadAddressAsyncTask(getContext());
        task1.execute();

        return root;
    }

    class LoadAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;

        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("admin").document(SlideshowFragment.ngoId.get(pos));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            txtName.setText(document.get(FieldPath.of("Event" + id, "Event Name")).toString());
                            txtStDt.setText(document.get(FieldPath.of("Event" + id, "Event Start Date")).toString());
                            txtEnDt.setText(document.get(FieldPath.of("Event" + id, "Event End Date")).toString());
                            txtStTi.setText(document.get(FieldPath.of("Event" + id, "Event Start Time")).toString());
                            txtEnTi.setText(document.get(FieldPath.of("Event" + id, "Event End Time")).toString());
                            txtDetails.setText(document.get(FieldPath.of("Event" + id, "Event Details")).toString());
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                }
            });
            docRef = db.collection("admin").document(SlideshowFragment.ngoId.get(pos));
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            partNo=Integer.parseInt(document.get(FieldPath.of("Event"+id,"participant")).toString());
                            partNo++;
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
}
