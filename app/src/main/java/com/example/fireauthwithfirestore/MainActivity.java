package com.example.fireauthwithfirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static String uid,uFName,uLName,uEmail,uMob,uBirth,uAdd,uCity,uState,uPin,uGender,uType="";
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(getApplicationContext(),AdminNav.class);
            i.putExtra("uid", uid=user.getUid());
            startActivity(i);
        }
        final TextView textView = findViewById(R.id.txtSign);
        final Button button = findViewById(R.id.btLogin);
        textView.setOnClickListener(this);
        button.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Intent i = new Intent(getApplicationContext(), AdminNav.class);
            i.putExtra("uid", uid = user.getUid());
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtSign:
                Intent i = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(i);
                break;
            case R.id.btLogin:
                final EditText etUser= findViewById(R.id.etUser);
                final EditText etPass= findViewById(R.id.etPass);
                mAuth.signInWithEmailAndPassword(etUser.getText().toString(), etPass.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Success", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    uid=user.getUid();
                                    db.collection("users")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Log.d("Success", document.getId() + " => " + document.getData());
                                                        }
                                                    } else {
                                                        Log.d("Error", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });
                                    Intent i = new Intent(getApplicationContext(),AdminNav.class);
                                    i.putExtra("uid", uid);
                                    startActivity(i);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Error", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
                break;

        }

    }
}
