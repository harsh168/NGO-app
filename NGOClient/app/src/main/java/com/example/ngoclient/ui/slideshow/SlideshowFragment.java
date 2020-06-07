package com.example.ngoclient.ui.slideshow;

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

import com.example.ngoclient.R;
import com.example.ngoclient.ui.gallery.MyListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlideshowFragment extends Fragment {
    List<String> l;
    public static ArrayList<String> ngoId = new ArrayList<String>();
    ArrayList<String> ngoName = new ArrayList<String>();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        LoadAddressAsyncTask task1 = new LoadAddressAsyncTask(getContext());
        task1.execute();
        list=(ListView)root.findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                com.example.ngoclient.ui.slideshow.EventFragment ldf = new EventFragment();
                Bundle args = new Bundle();
                args.putInt("pos", position);
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


        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            DocumentReference docRef = db.collection("user").document(currentUser.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                            Log.e("Event", document.get("Event").toString());
                            String data = document.get("Event").toString();
                            data=data.replace("{","").replace("}","");
                            Log.e("Data",data);
                            l = Arrays.asList(data.split(","));


                        }
                        for(int i= 0; i < l.size();i++)
                        {
                            String data = l.get(i);
                            data=data.substring(0,data.indexOf("="));
                            Log.e("Data",data);
                            l.set(i,data);
                        }
                        Log.e("list",l.toString());
                        db.collection("admin")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("TAG", document.getId() + " => " + document.getData());
                                                if (l.contains(document.getId())) {
                                                        ngoName.add(document.get("name").toString());
                                                        ngoId.add(document.getId());
                                                        Log.e("d",document.get("name").toString());
                                                }
                                            }
                                            MyListView adapter=new MyListView(getActivity(),ngoName);
                                            list.setAdapter(adapter);
                                        } else {
                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                    } else {
                        Log.d("TAG", "No such document");
                    }
                }

        });


            return null;
    }
}
}
