package com.amwallace.globalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import Util.ProgressGenerator;

public class LoginActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {
    private EditText usernameEdt, passwordEdt;
    private ActionProcessButton loginBtn;
    private ProgressGenerator progressGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //set up UI
        usernameEdt = (EditText) findViewById(R.id.loginUsernameEdt);
        passwordEdt = (EditText) findViewById(R.id.loginPasswordEdt);
        loginBtn = (ActionProcessButton) findViewById(R.id.loginScreenBtn);
        progressGenerator = new ProgressGenerator(this);

        //login button onclick listener
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

    }

    private void loginUser() {
        //get username and password from edit texts
        final String userName = usernameEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        //check if username and password fields had input
        if(userName.equals("") || password.equals("")){
            //no input from user in at least one field
            Toast.makeText(this,
                    "Enter your username and password", Toast.LENGTH_SHORT).show();
        } else {
            //user input, attempt to log user in
            ParseUser.logInInBackground(userName, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e == null){
                        // login success, animate button to move to next activity
                        Log.d("LOGGED IN USER: ", userName);
                        progressGenerator.start(loginBtn);
                        //disable edit texts and button while progress bar animation runs
                        loginBtn.setEnabled(false);
                        usernameEdt.setEnabled(false);
                        passwordEdt.setEnabled(false);
                    } else{
                        //login failed
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),
                                "Login Failed: " + e.getMessage().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

    @Override
    public void onComplete() {
        //go to chat activity
        startActivity(new Intent(
                LoginActivity.this, ChatActivity.class));
        finish();
    }
}
