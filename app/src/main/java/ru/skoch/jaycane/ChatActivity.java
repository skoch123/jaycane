package ru.skoch.jaycane;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Iterator;

public class ChatActivity extends AppCompatActivity {

    String contactId;
    String myContactId;
    Button sendButton;
    Button seeContacts;
    EditText editMessage;
    ContactList cl;
    MessageSocket socket;
    Client client;
    Socket sock;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ll = (LinearLayout) findViewById(R.id.ll);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contactId = extras.getString("contactId");
            myContactId = extras.getString("myContactId");

        }

        cl=ContactList.getInstance();
        socket=MessageSocket.getInstance();
        client = Client.getInstance();
        client.setChatActivity(this);
        client.dpList.clear();

        client.initConnect(contactId);



        editMessage = (EditText) findViewById(R.id.editMessage);
        seeContacts = (Button) findViewById(R.id.seeContacts);
        seeContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast t = Toast.makeText(getChatActivity(), cl.getContact(contactId),Toast.LENGTH_LONG);
                t.show();

            }
        });
        sendButton =  (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMessage.getText().toString()!=""){
                    try {
                        String[] host = cl.getContact(contactId).split(":");
                        client.setMessage(editMessage.getText().toString(),contactId,host[0],Integer.parseInt(host[1]));
                    }catch (NullPointerException e){
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        String[] host = cl.getContact(contactId).split(":");
                        client.setMessage(editMessage.getText().toString(),contactId,host[0],Integer.parseInt(host[1]));
                    }
                    addOutboundMessage(editMessage.getText().toString());
                    editMessage.setText("");

                }
            }
        });


    }

    public ChatActivity getChatActivity(){
        return this;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Client.getInstance().dpList.clear();
        cl.contacts.remove(this.contactId);
    }

    public void addInboundMessage(String message){
        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setGravity(Gravity.LEFT);
        LinearLayout.LayoutParams leftGravityParams = new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        leftGravityParams.gravity = Gravity.LEFT|Gravity.BOTTOM;
        ll.addView(tv, leftGravityParams);
    }
    public void addOutboundMessage(String message){
        TextView tv = new TextView(this);
        tv.setText(message);
        tv.setGravity(Gravity.RIGHT);
        LinearLayout.LayoutParams rightGravityParams = new
                LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rightGravityParams.gravity = Gravity.RIGHT|Gravity.BOTTOM;

        ll.addView(tv,rightGravityParams);



    }
}
