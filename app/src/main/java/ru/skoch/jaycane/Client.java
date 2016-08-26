package ru.skoch.jaycane;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;

import static java.lang.Thread.*;

/**
 * Created by root on 27.07.16.
 */
public class Client  implements Runnable{ // extends AsyncTask<String,Void,String> {

    Socket s;
    String code;
    LinkedList<DatagramPacket> dpList;
    DatagramPacket dpCode;
    ChatActivity chatActivity;

    public static Client ourInstance = new Client();

    public static Client getInstance(){
        return  ourInstance;
    }

    public void setChatActivity(ChatActivity chatActivity){
        this.chatActivity = chatActivity;
    }

    private Client(){
        dpList = new LinkedList<DatagramPacket>();
    }
    public void createDpCode(Socket s, String code){
        this.s=s;
        this.code=code;
        String msg = "{\"code\":\"" + code + "\"}";
        dpCode=new DatagramPacket(msg.getBytes(),msg.getBytes().length,s.ia,s.port);
    }

    public void setMessage(String msg,String contactId,String addr,int port){
        String message="{\"message\":{\"clientId\":\"" + contactId +"\",\"string\":\"" + msg + "\"}}";
        System.out.println("message:" + message + " address: " + addr + " port: " + port);
        try {
            s.send(new DatagramPacket(message.getBytes(),message.getBytes().length, InetAddress.getByName(addr),port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void initConnect(String contactId){
        String message="{\"connect\":{\"code\":\"" + contactId +"\",\"myCode\":\"" + code + "\"}}";
        System.out.println(message);
        dpList.add(new DatagramPacket(message.getBytes(),message.getBytes().length, s.ia,s.port));
        //s.send(new DatagramPacket(message.getBytes(),message.getBytes().length, s.ia,s.port));

    }

    public void initConnect(String contactId,String addr,int port){
        String message="{\"applyConnect\":{\"code\":\"" + contactId +"\",\"myCode\":\"" + code + "\"}}";
        System.out.println(message);
        try {
            dpList.add(new DatagramPacket(message.getBytes(),message.getBytes().length, InetAddress.getByName(addr),port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void showMessage(String message){
        System.out.println(message);
        if(this.chatActivity!=null) {
            chatActivity.addInboundMessage(message);
            //t.show();
        }
    }

    @Override
    public void run() {
        while(true) {
            for(int i=0; i<dpList.size();i++){
                DatagramPacket dp = dpList.get(i);
                if (dp != null)
                    s.send(dp);

            }

            if(!dpList.contains((dpCode)))
               dpList.addFirst(dpCode);
            try {
                 Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
