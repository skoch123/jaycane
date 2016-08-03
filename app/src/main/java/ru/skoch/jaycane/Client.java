package ru.skoch.jaycane;


import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.util.LinkedList;

import static java.lang.Thread.*;

/**
 * Created by root on 27.07.16.
 */
public class Client  implements Runnable{ // extends AsyncTask<String,Void,String> {

    Socket s;
    String code;
    LinkedList<DatagramPacket> dpList;

    public Client(Socket s, String code){
        this.s=s;
        this.code=code;
        dpList = new LinkedList<DatagramPacket>();
        dpList.addFirst(new DatagramPacket(code.getBytes(),code.getBytes().length,s.ia,s.port));
    }

    public void setMessage(String msg,String addr,int port){

    }



    @Override
    public void run() {
        while(true) {
            for (DatagramPacket dp: dpList) {
                if (dp != null)
                    s.send(dp);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
