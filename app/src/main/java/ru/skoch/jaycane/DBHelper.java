package ru.skoch.jaycane;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 25.07.16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "jaycane.db"; // название бд
    private static final int VERSION = 3; // версия базы данных

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE account (id " +
                " INTEGER PRIMARY KEY AUTOINCREMENT, code " +
                " TEXT);");
        // добавление начальных данных
        db.execSQL("CREATE TABLE contacts (id INTEGER PRIMARY KEY AUTOINCREMENT, code TEXT, name TEXT);");
        db.execSQL("CREATE TABLE config (name TEXT, value TEXT);");
        db.execSQL("INSERT INTO config(name,value) VALUES('server','ec2-52-27-177-83.us-west-2.compute.amazonaws.com:8888'),('localport','33388'); ");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        if(newVersion>oldVersion){
            db.execSQL("DROP TABLE IF EXISTS 'config' ");
            db.execSQL("DROP TABLE IF EXISTS 'contacts' ");
            db.execSQL("DROP TABLE IF EXISTS 'account' ");
            onCreate(db);
        }
    }

    public void createAccount(SQLiteDatabase db,String code){
        db.execSQL("DELETE FROM account;");
        db.execSQL("INSERT INTO account (code) VALUES ('" + code + "');");
    }

    public String getAccount(SQLiteDatabase db){
        String s = null;
        System.out.println(db.isOpen());
        Cursor c = db.rawQuery("SELECT code FROM account",null);
        if(c.moveToFirst()){
            s=c.getString(0);
        }
        return s;
    }
    public String getProperty(SQLiteDatabase db, String property){
        String s = null;
        System.out.println(db.isOpen());
        Cursor c = db.rawQuery("SELECT value FROM config WHERE name='"+ property +"'",null);
        if(c.moveToFirst()){
            s=c.getString(0);
        }
        return s;
    }
}
