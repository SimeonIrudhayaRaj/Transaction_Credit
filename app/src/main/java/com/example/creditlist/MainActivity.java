package com.example.creditlist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    String[] name, phone, balance;
    ListView ls;

    @Override
    protected void onStart() {
        super.onStart();
        if (db.isOpen()) {
            Cursor result = db.rawQuery("SELECT * FROM account", null);
            int i = 0;
            name = new String[result.getCount()];
            phone = new String[result.getCount()];
            balance = new String[result.getCount()];

            while (result.moveToNext()) {
                name[i] = result.getString(1);
                phone[i] = result.getString(0);
                balance[i] = String.valueOf(result.getInt(2));
                i++;
            }
            result.close();
        }
        MyAdapter adapter = new MyAdapter(this, name, phone, balance);
        ls.setAdapter(adapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ls = findViewById(R.id.listview);
        db = openOrCreateDatabase("Student", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS account(Phone TEXT PRIMARY KEY , name TEXT , balance int);");
        db.execSQL("CREATE TABLE IF NOT EXISTS tt( sender VARCHAR,reciever VARCHAR,amount int,date VARCHAR);");

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] phones=new String[phone.length-1],names=new String[name.length-1];
                int j=0;
                for(int k=0;k<phone.length;k++){
                    if(k!=position)
                    {
                        phones[j]=phone[j];
                        names[j]=name[j];
                        j++;
                    }
                }
                Intent i = new Intent(MainActivity.this, Transaction.class);
                i.putExtra("id", phone[position]);
                i.putExtra("name", name[position]);
                i.putExtra("bal", balance[position]);
                i.putExtra("phone", phones);
                i.putExtra("names", names);
                startActivity(i);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            start();
            // run your one time code
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }


    }


    public void start() {
        db.execSQL("INSERT INTO account  VALUES('abcd11','user_1',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd22','user_2',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd33','user_3',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd44','user_4',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd55','user_5',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd66','user_6',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd77','user_7',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd88','user_8',100000.00)");
        db.execSQL("INSERT INTO account  VALUES('abcd99','user_9',100000.00)");
    }


    public void rem(View view) {
        Cursor result = db.rawQuery("SELECT * FROM tt ", null);

        if(!result.moveToFirst())
        {
            Toast.makeText(this,"No transactions in history",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(getBaseContext(),transfer.class);
        i.putExtra("sender", "");
        startActivity(i);
    }


}