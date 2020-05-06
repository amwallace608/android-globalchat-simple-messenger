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
import com.parse.SignUpCallback;

import Util.ProgressGenerator;

public class CreateAccount extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {
    private EditText emailEdt;
    private EditText usernameEdt;
    private EditText passwordEdt;
    private ActionProcessButton createAccountBtn;
    private ProgressGenerator progressGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //setup edit texts and button
        emailEdt = (EditText) findViewById(R.id.createEmailEdt);
        usernameEdt = (EditText) findViewById(R.id.createUserNameEdt);
        passwordEdt = (EditText) findViewById(R.id.createPasswordEdt);

        createAccountBtn = (ActionProcessButton) findViewById(R.id.btnSignUp);
        //Set button mode to progress to be able to use progress functions
        createAccountBtn.setMode(ActionProcessButton.Mode.PROGRESS);
        //progress Generator instantiation - pass this activity's oncomplete listener
        progressGenerator = new ProgressGenerator(this);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

    }

    private void createAccount(){
        //final vars for user account info
        final String email = emailEdt.getText().toString();
        final String username = usernameEdt.getText().toString();
        final String password = passwordEdt.getText().toString();
        //check for user input
        if (email.equals("") || username.equals("") || password.equals("")){
            //one or more fields left empty
            Toast.makeText(this, "Enter your desired account information for each field", Toast.LENGTH_SHORT).show();
        } else {
            //all fields have input
            ParseUser newUser = new ParseUser();
            //set core properties for user (email, username, password)
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setPassword(password);
            //could put extra key/value pairs for user details here

            //sign up user with parse
            newUser.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e != null){
                        //error with signup
                        e.printStackTrace();
                    } else{
                        //e == null, signup success, start progress bar animation
                        progressGenerator.start(createAccountBtn);
                        //disable edit texts and button while progress bar animation runs
                        createAccountBtn.setEnabled(false);
                        emailEdt.setEnabled(false);
                        usernameEdt.setEnabled(false);
                        passwordEdt.setEnabled(false);
                        //log new user in method
                        logInUser(username, password);
                    }
                }
            });
        }


    }

    private void logInUser(String username, String password) {
        //check if username and password strings are empty
        if(username.equals("") || password.equals("")){
            //username or password is empty
        } else {
            //not empty
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(e == null){
                        //log in success
                        Log.d("USER LOGGED IN:", user.getUsername());
                    } else {
                        //log in error
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    //onComplete listener for ProgressGenerator
    @Override
    public void onComplete() {
        //go to chat activity
        startActivity(new Intent(CreateAccount.this,ChatActivity.class));
    }
}
