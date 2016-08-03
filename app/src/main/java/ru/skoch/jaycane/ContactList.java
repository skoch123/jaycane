package ru.skoch.jaycane;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 27.07.16.
 */
public class ContactList {
    private static ContactList ourInstance = new ContactList();
    private ContactsUpdateListener listener;

    public void setContactsUpdateListener(ContactsUpdateListener listener){
        this.listener=listener;
    }
    public static ContactList getInstance() {
        return ourInstance;
    }

    HashMap<String,String> contacts;


    private ContactList() {
        contacts = new HashMap<String, String>();
    }

    public String getContact(String code) {
        return contacts.get(code);
    }

    public void addContact(String code, String addr){
        contacts.put(code,addr);
    }
    public void clearContacts(){
        contacts.clear();
    }
    public void complete(){
        listener.onContactsUpdate();
    }
    public List<String> getNameList(){
        if(contacts!=null)
            return new ArrayList<String>(contacts.keySet());
        return new ArrayList<String>();
    }
    public int size(){
        return this.contacts.keySet().size();
    }
}

