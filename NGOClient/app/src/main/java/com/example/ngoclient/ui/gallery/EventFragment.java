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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class EventFragment extends Fragment {
    int pos = 0;
    ListView list;
    public static ArrayList<Integer> eventId = new ArrayList<Integer>();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_event, container, false);
        pos = getArguments().getInt("pos");
        list = (ListView) root.findViewById(R.id.list);
        LoadAddressAsyncTask task1 = new LoadAddressAsyncTask(getContext());
        task1.execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailFragment ldf = new DetailFragment();
                Bundle args = new Bundle();
                args.putInt("pos", pos);
                args.putInt("id",eventId.get(position));
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
        ArrayList<String> eventStart = new ArrayList<String>();
        ArrayList<String> eventEnd = new ArrayList<String>();
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
                            int i = Integer.parseInt(document.get("totalEvents").toString());
                            for (int j =1;j<=i;j++)
                            {
                                eventId.add(j);
                                eventName.add(document.get(FieldPath.of("Event"+j,"Event Name")).toString());
                                eventStart.add(document.get(FieldPath.of("Event"+j,"Event Start Date")).toString());
                                eventEnd.add(document.get(FieldPath.of("Event"+j,"Event End Date")).toString());
                            }
                            MyListView2 adapter = new MyListView2(getActivity(),eventName,eventStart,eventEnd);
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

