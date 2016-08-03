package ru.skoch.jaycane;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;


import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by root on 27.07.16.
 */
public class Handler {

    JsonParser parser = new JsonParser();
    Gson g = new Gson();
    Type type = new TypeToken<Map<String,JsonObject>>(){}.getType();
    Map<String,JsonObject> read;
    ContactList cl=ContactList.getInstance();



    public void handler(String message){
        System.out.println(message);
        read = g.fromJson(message,type);
        if(read.containsKey("cont")){
            contacts(read.get("cont"));
        }
        System.out.println(read.keySet().toArray()[0]);

    }

    public void contacts(JsonObject json){
        Iterator<Map.Entry<String, JsonElement>> it = json.entrySet().iterator();
        cl.clearContacts();
        while(it.hasNext()){
            JsonObject obj =it.next().getValue().getAsJsonObject();
            cl.addContact(obj.get("name").getAsString(),obj.get("address").getAsString());

        }
        cl.complete();
    }

}

