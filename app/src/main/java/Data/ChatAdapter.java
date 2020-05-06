package Data;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amwallace.globalchat.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import Model.Message;

public class ChatAdapter extends ArrayAdapter<Message> {
    private String mUserId;

    //constructor w/ context, userId, list of messages
    public ChatAdapter(@NonNull Context context, String userId, List<Message> messages) {
        super(context, 0, messages);

        mUserId = userId;
    }

    //override getView method from ArrayAdapter class
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //check if convertView is null
        if(convertView == null){
            //inflate chat view
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.chat_row, parent , false);
            //set up new viewholder for chat row w/ imageViews and textView
            final ViewHolder holder = new ViewHolder();
            holder.imageLeft = (ImageView) convertView.findViewById(R.id.profileImgLeft);
            holder.imageRight = (ImageView) convertView.findViewById(R.id.profileImgRight);
            holder.body = (TextView) convertView.findViewById(R.id.bodyTxt);
            convertView.setTag(holder);
        }
        //get message info & viewholder
        final Message message = (Message) getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();
        //determine if message was sent by current user
        final boolean isMe = message.getUserId().equals(mUserId);

        if(isMe){
            //message sent by current user, set right side image visible, left gone
            holder.imageRight.setVisibility(View.VISIBLE);
            holder.imageLeft.setVisibility(View.GONE);
            //set body text gravity to right
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            //message sent by other user, set left side image visible, left gone
            holder.imageRight.setVisibility(View.GONE);
            holder.imageLeft.setVisibility(View.VISIBLE);
            //set body text gravity to left
            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }
        //set which imageView to use according to if current user sent the message or not
        final ImageView profileView = isMe ? holder.imageRight : holder.imageLeft;
        //get gravatar identicon profile image using Picasso and load it into imageview
        Picasso.with(getContext()).load(getProfileGravatar(message.getUserId())).into(profileView);
        //set body text of message
        holder.body.setText(message.getBody());

        return convertView;
    }

    //custom inner viewHolder class for chat row
    class ViewHolder {
        public ImageView imageLeft;
        public ImageView imageRight;
        public TextView body;
    }

    //create gravatar (identicon) image for user based on hash value from userId
    private static String getProfileGravatar(final String userId){
        //create String to hold hex format md5 of userId
        String hex = "";
        try {//create MD5 hex string using MD5 algorithm from MessageDigest
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            //get big integer representation of hash byte array
            final BigInteger bigInteger = new BigInteger(hash);
            //get hex (base 16) representation of big integer
            hex = bigInteger.abs().toString(16);
        } catch (Exception e){
            e.printStackTrace();
        }
        //Log.d("GRAVATAR LINK: ","http://www.gravatar.com/avatar" + hex + "?d=identicon" );
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }
}
