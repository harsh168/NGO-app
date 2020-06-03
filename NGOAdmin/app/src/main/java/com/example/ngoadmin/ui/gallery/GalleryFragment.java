package com.example.ngoadmin.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.ngoadmin.MainActivity;
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

import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static EditText name,email,ph,lic,password,cpassword;
    public static Spinner spinner;
    public static String nType;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        spinner = (Spinner) root.findViewById(R.id.spNgoType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.nType_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        LoadAddressAsyncTask task1 =new LoadAddressAsyncTask(getContext());
        task1.execute();
        name = root.findViewById(R.id.etName);
        email = root.findViewById(R.id.etEmail);
        ph = root.findViewById(R.id.etPh);
        lic = root.findViewById(R.id.etLic);
        password = root.findViewById(R.id.etPass);
        cpassword = root.findViewById(R.id.etCPass);
        Button bt = root.findViewById(R.id.btReg);
        bt.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if(password.getText().toString().equals(cpassword.getText().toString())){
            DocumentReference docRef = db.collection("admin").document(currentUser.getUid());
            docRef
                    .update(

                            "name", name.getText().toString(),
                    "email", email.getText().toString(),
                            "mobile", ph.getText().toString(),
                            "password", password.getText().toString(),
                            "licNo", lic.getText().toString(),
                            "nType", nType


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

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        nType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        nType = "Animal Welfare";
    }


    class LoadAddressAsyncTask extends AsyncTask<Void,Void,Void>{
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
                            name.setText(document.get("name").toString());
                            email.setText(document.get("email").toString());
                            ph.setText(document.get("mobile").toString());
                            nType=document.get("nType").toString();
                            lic.setText(document.get("licNo").toString());
                            int x=0;
                            if(nType.equals("Animal Welfare")){
                                x=0;
                            }
                            else if(nType.equals("Children Welfare")){
                                x=1;
                            } else if(nType.equals("Disable Welfare")){
                                x=2;
                            }
                            spinner.setSelection(x);
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
