package com.example.creditlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class transfer extends AppCompatActivity {

    SQLiteDatabase db;
    List<String> transaction;
    ListView list;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);


        Intent i = getIntent();
        name= i.getStringExtra("sender");
        transaction=new ArrayList<String>();
        db = openOrCreateDatabase("Student", Context.MODE_PRIVATE, null);

        if (db.isOpen()) {
            if(!name.equals("")) {
                transaction.add("Debit");
                Cursor result = db.rawQuery("SELECT * FROM tt where sender = '" + name + "'", null);
                while (result.moveToNext())
                    transaction.add("To\t"+result.getString(1) + "\tAmount\t" + result.getString(2) + "\tDate\t" + result.getString(3));

                transaction.add("Credit");
                result = db.rawQuery("SELECT * FROM tt where reciever = '" + name + "'", null);
                while (result.moveToNext())
                    transaction.add("From\t"+result.getString(0) + "\tAmount\t" + result.getString(2) + "\tDate\t" + result.getString(3));
            }
            else{
                Cursor result = db.rawQuery("SELECT * FROM tt ", null);
                while (result.moveToNext())
                    transaction.add("From\t"+result.getString(0) + "\tTo\t" + result.getString(1) + "\tAmount\t" + result.getString(2) + "\tDate\t" + result.getString(3));
            }
        }

        list=findViewById(R.id.list2);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,transaction);

        list.setAdapter(arrayAdapter);
    }
}
