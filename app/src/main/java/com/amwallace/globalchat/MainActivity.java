package com.amwallace.globalchat;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button signInBtn;
    private Button createAccountBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set up buttons
        signInBtn = (Button) findViewById(R.id.loginBtn);
        createAccountBtn = (Button) findViewById(R.id.signUpBtn);

        //button listeners
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to create account activity
                startActivity(new Intent(MainActivity.this,CreateAccount.class));

            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to sign in activity
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });


    }
}
