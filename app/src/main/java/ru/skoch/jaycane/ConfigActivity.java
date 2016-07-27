package ru.skoch.jaycane;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    DBHelper dbHelper;
    SQLiteDatabase db;
    Button genCodeButton;
    TextView codeText;
    EditText serverEditText;
    EditText localPortEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        dbHelper = new DBHelper( getApplicationContext() );
        db=dbHelper.getWritableDatabase();

        genCodeButton = (Button) findViewById(R.id.genCodeButton);

        View.OnClickListener oclGenCodeButton = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("account: "  + dbHelper.getAccount(db));
                if(dbHelper.getAccount(db)==null){
                    dbHelper.createAccount(db,CodeGenerator.generate());
                    finish();
                    startActivity(getIntent());
                }
                else{
                    dbHelper.createAccount(db,CodeGenerator.generate());
                    finish();
                    startActivity(getIntent());
                }
            }
        };
        genCodeButton.setOnClickListener(oclGenCodeButton);

        codeText = (TextView) findViewById(R.id.codeText);

        if(dbHelper.getAccount(db)!=null){
            codeText = (TextView) findViewById(R.id.codeText);
            codeText.setText("Current code: " + dbHelper.getAccount(db));
        }
        else{
            codeText.setText("Необходимо сгенерировать код аккаунта");
        }
        serverEditText = (EditText) findViewById(R.id.serverEditText);
        localPortEditText = (EditText) findViewById(R.id.localPortEditText);
        serverEditText.setText(dbHelper.getProperty(db,"server"));
        localPortEditText.setText(dbHelper.getProperty(db,"localport"));



    }

}
