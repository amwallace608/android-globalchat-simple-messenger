package com.amwallace.globalchat;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import Model.Message;

public class ChatApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //enable local datastore
        Parse.enableLocalDatastore(this);
        //register message subclass
        ParseObject.registerSubclass(Message.class);
        //initialize parse
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("GlobalChat")
                .server("http://10.0.2.2:1337/parse")
                .build());
    }
}
