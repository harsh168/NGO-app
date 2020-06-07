package com.example.ngoclient.ui.gallery;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    public static ArrayList<String> ngoId = new ArrayList<String>();
    ListView list;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
        task1.execute();
        list=(ListView)root.findViewById(R.id.list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventFragment ldf = new EventFragment();
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
    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void> {
        ArrayList<String> ngoList = new ArrayList<String>();
        private FirebaseFirestore db = FirebaseFirestore.getInstance();
        Context context;
        public LoadAddressAsyncTask(Context context) {
            this.context = context;

        }
        @Override
        protected Void doInBackground(Void... voids) {
            db.collection("admin")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    if (!document.get("totalEvents").toString().equals("0")) {
                                        ngoList.add(document.get("name").toString());
                                        ngoId.add(document.getId());
                                    }
                                }
                                MyListView adapter=new MyListView(getActivity(),ngoList);
                                list.setAdapter(adapter);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            return null;
        }
    }
}
