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
    //Gson g = new Gson();
    Type type = new TypeToken<Map<String,JsonObject>>(){}.getType();
    //Map<String,JsonObject> read;
    ContactList cl=ContactList.getInstance();




    public void handler(Packet packet){
        Gson g = new Gson();
        Map<String,JsonObject> read;
        read = g.fromJson(packet.getData(), type);

        if(read!=null) {

            if (read.containsKey("cont"))
                contacts(read.get("cont"));
            if (read.containsKey("applyConnect"))
                contacts(read.get("applyConnect"), packet.getAddress()+":"+packet.getPort());
            if(read.containsKey("connect")) {
                System.out.println("recived connect");
                sendConnect(read.get("connect"));
            }
            if(read.containsKey("message")){
                System.out.println("recived message");
                Client.getInstance().showMessage(read.get("message").get("string").getAsString());
            }


        }


    }

    public void contacts(JsonObject json){
        Iterator<Map.Entry<String, JsonElement>> it = json.entrySet().iterator();
        //cl.clearContacts();
        while(it.hasNext()){
            JsonObject obj =it.next().getValue().getAsJsonObject();
            if(!cl.contacts.containsKey(obj.get("name").getAsString())) {
                cl.addContact(obj.get("name").getAsString(), null);
            }

        }
        cl.complete();
    }
    public void contacts(JsonObject json,String address){
        //cl.clearContacts();
        System.out.println("reviced applyConnect from " + json.get("myCode").getAsString() + " with address " + address);
        Client.getInstance().dpList.clear();
        cl.addContact(json.get("myCode").getAsString(),address);
    }


    public void sendConnect(JsonObject json){
        System.out.print(json.get("name"));
        String name = json.get("name").getAsString();
        String[] address = json.get("address").getAsString().split(":");
        Client.getInstance().initConnect(name,address[0],Integer.parseInt(address[1]));

    }

}

