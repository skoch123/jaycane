package ru.skoch.jaycane;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by root on 26.07.16.
 */
public class Socket implements Runnable {

    DatagramSocket socket;
    int localport;
    InetAddress server;
    int serverPort;
    String code="sdfadfadf";


    public Socket(int localport,String server){
        this.localport=localport;
        String[] str=server.split(":");
        try {
            this.server = InetAddress.getByName(str[0]);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        serverPort=Integer.parseInt(str[1]);

    }

    public void createSocket(){

        try {
            DatagramSocket socket = new DatagramSocket(localport);

        } catch (SocketException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
            createSocket();
            while(true){
                try {
                    socket.send(new DatagramPacket(code.getBytes(),code.getBytes().length,server,serverPort));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    }
}
