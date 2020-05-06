package Model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

    public Message() {
    }

    //get User ID
    public String getUserId(){
        return getString("userId");
    }
    //get message body
    public String getBody(){
        return getString("body");
    }
    //set user ID
    public void setUserId(String userId){
        put("userId",userId);
    }
    //set message body
    public void setBody(String body){
        put("body",body);
    }
}
