package ru.skoch.jaycane;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    ArrayAdapter<String> adapter;
    TextView codeText;
    ContactList cl;
    Client c;
    HashMap contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView contactsLv= (ListView) findViewById(R.id.lvContacts);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        App.setContext(this);
        dbHelper = new DBHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();


        contacts = dbHelper.getContacts(db);
        int localPort=Integer.parseInt(dbHelper.getProperty(db,"localport"));
        String server=dbHelper.getProperty(db,"server");
        Socket s = new Socket(localPort,server);
        c=Client.getInstance();
        c.createDpCode(s, dbHelper.getAccount(db) );
        //c.setChatActivity(this);
        cl = ContactList.getInstance();

        s.createSocket();
        System.out.println("sock created");
        new Thread(c).start();
        //new Thread(s).start();
        //c.execute();
        s.execute();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cl.getNameList());
        adapter.add("Connecting...");
        contactsLv.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        contactsLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startChat(dbHelper.getCodeByName(db,(String) adapterView.getAdapter().getItem(i)));
            }
        });



                cl.setContactsUpdateListener(new ContactsUpdateListener() {
                    @Override
                    public void onContactsUpdate() {

                        adapter.clear();

                        adapter.addAll(contacts.values());
                        adapter.notifyDataSetChanged();
                        contactsLv.invalidateViews();
                    }
                });



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_config){
            Intent intent = new Intent(this, ConfigActivity.class);
            this.startActivity(intent);
            return true;
        }
        if(item.getItemId()==R.id.menu_add_contact){
            ContactDialog cd = new ContactDialog(this);
            cd.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startChat(String contactId){
        System.out.println("Contact is " + contactId);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("contactId",contactId);
        intent.putExtra("myContactId",dbHelper.getAccount(db));
        this.startActivity(intent);
    }

    public void exit(View view)
    {
        System.exit(0);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    public String createContact(String id, String name){
        dbHelper.createContact(db,id,name);
        contacts=dbHelper.getContacts(db);
        return "Success";
    }
}
