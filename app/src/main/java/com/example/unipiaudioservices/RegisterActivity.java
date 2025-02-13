package com.example.unipiaudioservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity
{

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    TextView txtSignIn;
    EditText edtUserName, edtEmail,  edtPassword, edtConfirmPassword;
    ProgressBar progressBar;
    Button btnSignUp;
    String txtUserName, txtEmail,  txtPassword, txtConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtSignIn = findViewById(R.id.txtSignIn);
        edtUserName = findViewById(R.id.edtSignUpUsername);
        edtEmail = findViewById(R.id.edtSignUpEmail);
        edtPassword = findViewById(R.id.edtSignUpPassword);
        edtConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);
        progressBar = findViewById(R.id.signUpProgressBar);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Firebase Firestore
        db = FirebaseFirestore.getInstance();

        txtSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent logIntent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logIntent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                txtUserName = edtUserName.getText().toString();
                txtEmail = edtEmail.getText().toString().trim();
                txtPassword = edtPassword.getText().toString().trim();
                txtConfirmPassword = edtConfirmPassword.getText().toString().trim();

                if(!TextUtils.isEmpty(txtUserName))
                {
                    if(!TextUtils.isEmpty(txtEmail))
                    {
                        if (txtEmail.matches(emailPattern))
                        {
                            if(!TextUtils.isEmpty(txtPassword))
                            {
                                if(!TextUtils.isEmpty(txtConfirmPassword))
                                {
                                    if (txtConfirmPassword.equals(txtPassword))
                                    {
                                        SingUpUser();
                                    }
                                    else
                                    {
                                        edtConfirmPassword.setError("Confirm Password Don't Match Password.");
                                    }
                                }
                                else
                                {
                                    edtConfirmPassword.setError("Confirm Password Can't be empty.");
                                }
                            }
                            else
                            {
                                edtPassword.setError("Password Can't be empty.");
                            }
                        }
                        else
                        {
                            edtEmail.setError("Please enter a valid email.");
                        }
                    }
                    else
                    {
                        edtEmail.setError("Email Can't be empty.");
                    }
                }
                else
                {
                    edtUserName.setError("Username Can't be empty.");
                }
            }
        });
    }

    private void SingUpUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        btnSignUp.setVisibility(View.INVISIBLE);
        mAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                String userId = mAuth.getCurrentUser().getUid();

                Map<String, Object> user = new HashMap<>();
                user.put("Username", txtUserName);
                user.put("Email", txtEmail);
                user.put("Password", txtPassword);
                user.put("Created", System.currentTimeMillis());

                // Add a new document with a generated ID
                db.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener(unused ->
                        {
                            Toast.makeText(RegisterActivity.this, "Sign Up Successful !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        })
                        .addOnFailureListener(new OnFailureListener()
                        {
                            @Override
                            public void onFailure(@NonNull Exception e)
                            {
                                Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(RegisterActivity.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                btnSignUp.setVisibility(View.VISIBLE);
            }
        });
    }
}