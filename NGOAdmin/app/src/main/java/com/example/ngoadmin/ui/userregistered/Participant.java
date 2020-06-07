package com.example.ngoadmin.ui.userregistered;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ngoadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Participant extends Fragment {
    ArrayList<Integer> partId = new ArrayList<Integer>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView list;
    int eventId;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.users_registered_fragment, container, false);

        LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
        task1.execute();
        list = (ListView) root.findViewById(R.id.list);
        eventId = getArguments().getInt("id");
       
        return root;
    }

    class LoadAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;

        ArrayList<String> eventName = new ArrayList<String>();
        ArrayList<String> partName = new ArrayList<String>();
        ArrayList<String> partGen = new ArrayList<String>();
        ArrayList<String> partDob = new ArrayList<String>();
        ArrayList<String> partPh = new ArrayList<String>();
        ArrayList<String> partEmail = new ArrayList<String>();
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
                           // Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            for (int i = Integer.parseInt(document.get(FieldPath.of("Event"+eventId,"participant")).toString()); i > 0; i--)
                            {
                                partId.add(i);
                                eventName.add(document.get(FieldPath.of("Event"+eventId,"participant"+i)).toString());
                             //   Log.e("jjjjjjjjjjjjjjjjjjjjjjj",""+eventName.size());
                            }
                            int j = eventName.size();

                            for(int i =j-1; i>=0; i --)
                            {
                                DocumentReference docRef = db.collection("user").document(eventName.get(i));
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.e("TAG", "DocumentSnapshot data: " + document.getData());
                                                partName.add(document.get("name").toString());
                                                partGen.add(document.get("gender").toString());
                                                partDob.add(document.get("dob").toString());
                                                partPh.add(document.get("mobile").toString());
                                                partEmail.add(document.get("email").toString());
                                            }
                                            else {
                                                Log.d("TAG", "No such document");
                                            }
                                            MyListView2 adapter = new MyListView2(getActivity(),partName,partGen,partDob,partPh,partEmail);
                                            list.setAdapter(adapter);
                                        } else {
                                            Log.d("TAG", "get failed with ", task.getException());
                                        }
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
            return null;
        }
    }


}
