package ru.skoch.jaycane;

/**
 * Created by root on 26.08.16.
 */
public class Packet {
    private String data;
    private String address;
    private int port;

    public Packet(String data, String address, int port) {
        this.data = data;
        this.address = address;
        this.port = port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getData() {

        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
