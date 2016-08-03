package ru.skoch.jaycane;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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


        HashMap<String, String> contacts = new HashMap<String, String>();
        System.out.println("localport: " + dbHelper.getProperty(db,"localport"));
        int localPort=Integer.parseInt(dbHelper.getProperty(db,"localport"));
        String server=dbHelper.getProperty(db,"server");
        Socket s = new Socket(localPort,server);

        Client c = new Client(s,"{\"code\":\"" + dbHelper.getAccount(db) +"\"}");
        cl = ContactList.getInstance();

        s.createSocket();
        System.out.println("sock created");
        new Thread(c).start();
        //new Thread(s).start();
        //c.execute();
        s.execute();

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,cl.getNameList());
        adapter.add("first");
        contactsLv.setAdapter(adapter);
        adapter.setNotifyOnChange(true);

        cl.setContactsUpdateListener(new ContactsUpdateListener() {
            @Override
            public void onContactsUpdate() {
                System.out.println("sizeof cl: " + cl.size());
                if(cl.size()>0)
                    System.out.println(cl.getNameList().get(0));
                adapter.clear();
                adapter.addAll(cl.getNameList());
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
        return super.onOptionsItemSelected(item);
    }
}
