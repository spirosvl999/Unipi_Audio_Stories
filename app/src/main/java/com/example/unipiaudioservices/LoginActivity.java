package com.example.unipiaudioservices;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    TextView txtSignUp;
    private FirebaseAuth mAuth;
    EditText edtEmail, edtPassword;
    Button btnSignIn;
    String txtEmail, txtPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);

        txtSignUp = findViewById(R.id.txtSignUp);

        edtEmail = findViewById(R.id.edtSignInEmail);
        edtPassword = findViewById(R.id.edtSignInPassword);
        progressBar = findViewById(R.id.signInProgressBar);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        txtSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent regIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                txtEmail = edtEmail.getText().toString().trim();
                txtPassword = edtPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(txtEmail))
                {
                    if (txtEmail.matches(emailPattern))
                    {
                        if (!TextUtils.isEmpty(txtPassword))
                        {
                            SignInUser();
                        }
                        else
                        {
                            edtPassword.setError("Password Field can't be empty.");
                        }
                    }
                    else
                    {
                        edtEmail.setError("Enter a valid Email Address.");
                    }
                }
                else
                {
                    edtEmail.setError("Email Field can't be empty.");
                }
            }
        });
    }

    private void SignInUser()
    {
        progressBar.setVisibility(View.VISIBLE);
        btnSignIn.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(txtEmail, txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>()
        {
            @Override
            public void onSuccess(AuthResult authResult)
            {
                Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, Menu_Stories.class);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener()
        {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                Toast.makeText(LoginActivity.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                btnSignIn.setVisibility(View.VISIBLE);
            }
        });
    }

}