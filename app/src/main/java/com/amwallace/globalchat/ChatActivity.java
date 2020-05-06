package com.amwallace.globalchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;

import Data.ChatAdapter;
import Model.Message;
import Util.ProgressGenerator;

public class ChatActivity extends AppCompatActivity {

    private EditText message;
    private FlatButton sendBtn;
    private ProgressGenerator progressGenerator;
    public static final String USER_ID_KEY = "userId";
    private String currentUserId;
    private ListView listView;
    private ArrayList<Message> mMessages;
    private ChatAdapter adapter;
    private Handler handler = new Handler();
    //maximum messages to show
    private static final int MAX_CHAT_MSGS = 70;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //get current user
        getCurrentUser();
        //Post delayed runnable to check for messages
        handler.postDelayed(runnable, 100);

    }

    //new runnable to refresh messages every 100ms
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            receiveMessage();
            handler.postDelayed(this,100);
        }
    };

    //get current user data from parse server
    private void getCurrentUser() {
        //get userId of current user from Parse Server
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        messagePosting();
    }

    //post
    private void messagePosting() {
        //setup chat activity view widgets (edittext, button, listview, etc.)
        message = (EditText) findViewById(R.id.messageEdt);
        sendBtn = (FlatButton) findViewById(R.id.sendBtn);
        listView = (ListView) findViewById(R.id.chatListView);
        //create list for messages
        mMessages = new ArrayList<Message>();
        //create new ChatAdapter w/userId and list for messages
        adapter = new ChatAdapter(ChatActivity.this, currentUserId, mMessages);
        //set adapter for list view to new adapter
        listView.setAdapter(adapter);
        //send button on click listener (to send message)
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if message is empty
                if(!message.getText().toString().equals("")){
                    //not empty
                    //create new message
                    Message msg = new Message();
                    //set userId of message
                    msg.setUserId(currentUserId);
                    //set body of message
                    msg.setBody(message.getText().toString());
                    //save message to Parse server
                    msg.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            receiveMessage();
                            if(e != null){
                                //exception
                                e.printStackTrace();
                            }
                        }
                    });
                    //clear edit text input
                    message.setText("");
                } else {
                    //message is empty
                    Toast.makeText(getApplicationContext(), "Enter a Message to Send", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //receive message(s) method
    private void receiveMessage() {
        //create parse query for message class object
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        //set max number of messages to query for
        query.setLimit(MAX_CHAT_MSGS);

        //send query to Parse server to get chat messages
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if(e != null){
                    //exception
                    e.printStackTrace();
                } else {
                    //no exception
                    //clear message list
                    mMessages.clear();
                    //add messages from parse query result to list
                    mMessages.addAll(objects);
                    //notify data has changed for adapter
                    adapter.notifyDataSetChanged();
                    //invalidate listview so it can be redrawn
                    listView.invalidate();
                }
            }
        });
    }

    //Action Bar Menu methods
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            //sign out of account, return to main activity
            ParseUser.logOutInBackground(new LogOutCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null){
                        //error with logout
                        e.printStackTrace();
                    } else {
                        //log out successful
                        startActivity(new Intent(
                                ChatActivity.this, MainActivity.class));
                    }
                }
            });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
