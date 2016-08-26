package ru.skoch.jaycane;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by root on 03.08.16.
 */
public class MessageSocket {

    private static MessageSocket instance = new MessageSocket();
    private DatagramSocket sock;
    private int localport=8888;


    public static MessageSocket getInstance(){
        return instance;
    }

    private MessageSocket(){
        createSocket();
    }
    public void createSocket(){
        try {
            try {
                sock = new DatagramSocket(localport, InetAddress.getByName("0.0.0.0"));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            System.out.println("port: " + sock.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }
    public void setMessage(String msg,String contactId,String addr,int port){
        String message="{\"message\":{\"clientId\":\"" + contactId +"\",\"string\":\"" + msg + "\"}}";
        System.out.println("message:" + message + " address: " + addr + " port: " + port);
        try {
            sock.send(new DatagramPacket(message.getBytes(),message.getBytes().length, InetAddress.getByName(addr),port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
