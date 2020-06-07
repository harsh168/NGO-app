package com.example.ngoadmin.ui.userregistered;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

public class UsersRegistered extends Fragment {
    ArrayList<Integer> eventId = new ArrayList<Integer>();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView list;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.users_registered_fragment, container, false);

        LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
        task1.execute();
        list = (ListView) root.findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Participant ldf = new Participant();
                Bundle args = new Bundle();
                args.putInt("id", eventId.get(position));
                ldf.setArguments(args);
                getFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, ldf)
                        .commit();
            }
        });
        return root;
    }

    class LoadAddressAsyncTask extends AsyncTask<Void, Void, Void> {

        Context context;

        ArrayList<String> eventName = new ArrayList<String>();
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
                            for (int i = Integer.parseInt(document.get("totalEvents").toString()); i > 0; i--)
                            {
                                    eventId.add(i);
                                    eventName.add(document.get(FieldPath.of("Event"+i,"Event Name")).toString());
                            }
                            MyListView adapter = new MyListView(getActivity(), eventName);
                            list.setAdapter(adapter);
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

