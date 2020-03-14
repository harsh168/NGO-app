package com.example.fireauthwithfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String gender,typeUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final TextView textView = findViewById(R.id.txtSign);
        final Button button = findViewById(R.id.btLogin);
        Spinner spinner = (Spinner) findViewById(R.id.etGender);
        Spinner spinner1 = (Spinner) findViewById(R.id.etType);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.type_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner1.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(this);
        spinner1.setOnItemSelectedListener(this);
        textView.setOnClickListener(this);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSign:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                break;
            case R.id.btLogin:
                final EditText etUser = findViewById(R.id.etUser);
                final EditText etPass = findViewById(R.id.etPass);
                final EditText etFname = findViewById(R.id.etFName);
                final EditText etLName = findViewById(R.id.etLName);
                final EditText etMob = findViewById(R.id.etMobile);
                final EditText etBirth = findViewById(R.id.etBirth);
                final EditText etAdd = findViewById(R.id.etAdd);
                final EditText etCity = findViewById(R.id.etCity);
                final EditText etState = findViewById(R.id.etState);
                final EditText etPin = findViewById(R.id.etPin);

                mAuth.createUserWithEmailAndPassword(etUser.getText().toString(), etPass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Success", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    MainActivity.uid=user.getUid();
                                    Map<String, Object> data = new HashMap<>();
                                    data.put("first",MainActivity.uFName=etFname.getText().toString());
                                    data.put("last", MainActivity.uLName=etLName.getText().toString());
                                    data.put("email",MainActivity.uEmail=etUser.getText().toString());
                                    data.put("mob", MainActivity.uMob=etMob.getText().toString());
                                    data.put("dob", MainActivity.uBirth=etBirth.getText().toString());
                                    data.put("Add", MainActivity.uAdd=etAdd.getText().toString());
                                    data.put("city", MainActivity.uCity=etCity.getText().toString());
                                    data.put("state", MainActivity.uState=etState.getText().toString());
                                    data.put("pin", MainActivity.uPin=etPin.getText().toString());
                                    data.put("gender", MainActivity.uGender=gender);
                                    db.collection(typeUser).document(MainActivity.uid)
                                            .set(data)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("Success", "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("Error", "Error writing document", e);
                                                }
                                            });
                                    if(typeUser.equals("NGO")){
                                    Intent i = new Intent(getApplicationContext(),AdminNav.class);
                                    i.putExtra("uid", MainActivity.uid);
                                    startActivity(i);}
                                    if(typeUser.equals("User")){
                                        Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                                        i.putExtra("uid", MainActivity.uid);
                                        startActivity(i);}
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Error", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                                // ...
                            }
                        });
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.etGender)
        {
            gender=parent.getItemAtPosition(position).toString();
        }
        else if(parent.getId() == R.id.etType)
        {
            typeUser=parent.getItemAtPosition(position).toString();
            MainActivity.uType=typeUser;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
