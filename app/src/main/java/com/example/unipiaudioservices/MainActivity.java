package com.example.unipiaudioservices;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;


import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) ->
        {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnLogin = findViewById(R.id.button);                                                        // We go on the Login Activity.
    btnLogin.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v) {
            Intent logIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(logIntent);
        }
    });

        Button btnRegister = findViewById(R.id.button2);                                                    // This is for when we click on button to go on the register form
    btnRegister.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent regIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(regIntent);
            }
    });


    }
}