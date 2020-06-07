package com.example.ngoclient.ui.login;

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

import com.example.ngoclient.MainActivity;
import com.example.ngoclient.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    String genderType;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Button regBt = findViewById(R.id.btReg);
        TextView logBt = findViewById(R.id.txtLogin);
        logBt.setOnClickListener(this);
        regBt.setOnClickListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.spGender);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.nType_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogin:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.btReg:
                mAuth = FirebaseAuth.getInstance();
                final EditText email = findViewById(R.id.etEmail);
                final EditText password = findViewById(R.id.etPass);
                EditText cpassworf = findViewById(R.id.etCPass);
                final EditText mobile = findViewById(R.id.etPh);
                final EditText dob = findViewById(R.id.etDob);
                final EditText etname = findViewById(R.id.etName);
                CollectionReference mobileCheck = db.collection("user");

                Query query = mobileCheck.whereEqualTo("mobile", mobile.getText().toString());
                if (cpassworf.getText().toString().equals(password.getText().toString()) && !query.equals(mobile.getText().toString())) {
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("SignUpActivity", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Map<String, Object> data = new HashMap<>();
                                        data.put("name", etname.getText().toString());
                                        data.put("email", email.getText().toString());
                                        data.put("mobile", mobile.getText().toString());
                                        data.put("password", password.getText().toString());
                                        data.put("dob", dob.getText().toString());
                                        data.put("gender", genderType);
                                        db.collection("user").document(user.getUid())
                                                .set(data)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.d("Success", "DocumentSnapshot successfully written!");
                                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                        startActivity(i);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.w("Error", "Error writing document", e);
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("SignUpActivity", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }


                                }
                            });
                }
                break;


        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genderType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        genderType = "Male";
    }
}
