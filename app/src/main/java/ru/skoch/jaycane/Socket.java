package ru.skoch.jaycane;


import android.os.AsyncTask;

import java.io.IOException;
import java.net.*;

/**
 * Created by root on 27.07.16.
 */
public class Socket extends AsyncTask<Void,Integer,Void> {

    DatagramSocket sock;
    InetAddress ia;
    int port;
    int localport;
    byte[] msg;
    Packet response;
    String host;
    Handler h = new Handler();

    public Socket(int localport, String remoteHost){
        this.localport=localport;
        String[] host=remoteHost.split(":");
        this.port = Integer.parseInt(host[1]);
        this.host = host[0];
        System.out.println("host: " + this.host);



        try {
            ia=InetAddress.getByName(this.host);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void createSocket(){
        try {
            try {
                sock = new DatagramSocket(localport,InetAddress.getByName("0.0.0.0"));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println("port: " + sock.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void send(DatagramPacket dp){
        try {
            sock.send(dp);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            createSocket();
        }

    }

    public Packet receive(){
        DatagramPacket p = new DatagramPacket(new byte[256],256);
        try {
            sock.receive(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[p.getLength()];
        System.arraycopy(p.getData(), p.getOffset(), data, 0, p.getLength());
        return new Packet(new String(data),p.getAddress().getHostAddress(),p.getPort());
    }
    public void close(){
        sock.close();
    }
/*
    @Override
    public void run() {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //this.response=receive();
            //h.handler(this.response);

        }
    }
*/

    @Override
    protected Void doInBackground(Void... voids) {
        while(true) {
            /*try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            this.response = receive();
            publishProgress(0);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        h.handler(this.response);
    }
}
