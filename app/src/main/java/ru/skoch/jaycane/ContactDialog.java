package ru.skoch.jaycane;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by root on 19.08.16.
 */
public class ContactDialog extends Dialog {

    MainActivity mainActivity;

    public ContactDialog(MainActivity activity) {
        super(activity);
        this.mainActivity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add contact");
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_contact);
        Button okBtn = (Button) findViewById(R.id.contact_ok);
        Button cancelBtn = (Button) findViewById(R.id.contact_cancel);
        final EditText contactNameEditText = (EditText) findViewById(R.id.contact_name);
        final EditText contactIdEditText = (EditText) findViewById(R.id.contact_id);

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contactIdEditText.getText()!=null && contactNameEditText!=null){
                    mainActivity.createContact(contactIdEditText.getText().toString(),contactNameEditText.getText().toString());
                }
                dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
