package com.example.ngoclient.ui.gallery;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.ngoclient.R;
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
    Button bt;
    int partNo = 0;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        pos = getArguments().getInt("pos");
        id = getArguments().getInt("id");
        txtName = root.findViewById(R.id.txtName);
        txtStDt = root.findViewById(R.id.txtStart);
        txtStTi = root.findViewById(R.id.txtStartTime);
        txtEnDt = root.findViewById(R.id.txtEnd);
        txtError = root.findViewById(R.id.txtError);
        txtEnTi = root.findViewById(R.id.txtEndTime);
        txtDetails = root.findViewById(R.id.txtDetails);
        bt = root.findViewById(R.id.btPart);
        LoadAddressAsyncTask task1 = new LoadAddressAsyncTask(getContext());
        task1.execute();
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = df.format(c);
                Log.e("Date", formattedDate);
                String date = txtStDt.getText().toString();
                try {
                    Date date1 = df.parse(date);
                    Date date2 = df.parse(formattedDate);
                    if (date1.compareTo(date2) < 0) {
                        Log.e("Inside", "if");
                        txtError.setText("Too Late for Participation");
                    } else {


                        DocumentReference docRef = db.collection("admin").document(GalleryFragment.ngoId.get(pos));
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                                        String data = document.get("Event"+id).toString();
                                        String[] data1= data.replaceAll(", "," ").replace("{","").replace("}","").split(" ");



                                        for (int i = 0; i < data1.length; i++) {
                                            //data1[i] = data1[i].replaceAll("=.+", "");
                                            Log.e("data1",data1[i]);
                                            if(data1[i].contains(currentUser.getUid()))
                                            {
                                                flag =1;
                                                break;
                                            }
                                        }
                                        if(flag==1)
                                        {
                                            txtError.setText("Already Registered");
                                        }
                                        else
                                        {
                                            DocumentReference docRef = db.collection("admin").document(GalleryFragment.ngoId.get(pos));
                                            docRef
                                                    .update(

                                                            "Event" + id + "." + "participant" + partNo, currentUser.getUid(),
                                                            "Event" + id + "." + "participant",""+partNo


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
                                            docRef = db.collection("user").document(currentUser.getUid());
                                            docRef
                                                    .update(

                                                            "Event."+GalleryFragment.ngoId.get(pos),""+id


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
                                        }
                                    } else {
                                        Log.d("TAG", "No such document");
                                    }
                                } else {
                                    Log.d("TAG", "get failed with ", task.getException());
                                }
                            }
                        });


                        Log.e("Inside", "Else");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
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
            DocumentReference docRef = db.collection("admin").document(GalleryFragment.ngoId.get(pos));
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
            docRef = db.collection("admin").document(GalleryFragment.ngoId.get(pos));
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
