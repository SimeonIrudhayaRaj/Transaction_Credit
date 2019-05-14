package com.example.creditlist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction extends AppCompatActivity {

    SQLiteDatabase db;
    String uid, uname, ubalance;
    String[] name, phone;
    ListView lsq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent i = getIntent();
        uid = i.getStringExtra("id");
        uname = i.getStringExtra("name");
        ubalance = i.getStringExtra("bal");

        name = i.getStringArrayExtra("names");
        phone = i.getStringArrayExtra("phone");


        lsq = findViewById(R.id.list1);
        TextView Uname = findViewById(R.id.sname);
        TextView Uemail = findViewById(R.id.sPhone);
        TextView bal = findViewById(R.id.balance);

        Uname.setText("Name  :" + uname);
        Uemail.setText("Phone:" + uid);
        bal.setText("Bal   :" + ubalance);


        lsq.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if (!uname.equals(name[position])) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Transaction.this);
                    alertDialog.setTitle("Transaction").setMessage(uname + "-->" + name[position] + "\n\nEnter Credits to transfer").setCancelable(false);

                    final EditText input = new EditText(Transaction.this);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    alertDialog.setView(input);
                    alertDialog.setIcon(R.drawable.ic_launcher_foreground);

                    alertDialog.setPositiveButton("YES",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!(input.getText().toString().isEmpty())) {
                                        double amount = Double.valueOf(input.getText().toString());
                                        transf(phone[position], amount);
                                    }

                                }
                            });

                    alertDialog.setNegativeButton("NO",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    alertDialog.show();
                }
            }
        });

    }

    public void transaction(View view) {
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, name);

        lsq.setAdapter(adapter);
    }

    public void transf(String sid, double amount) {
        if (amount < Double.valueOf(ubalance)) {
            db = openOrCreateDatabase("Student", Context.MODE_PRIVATE, null);
            if (db.isOpen()) {
                Cursor result = db.rawQuery("SELECT balance FROM account where Phone='" + sid + "'", null);
                result.moveToFirst();
                double sbalance = Double.valueOf(result.getString(0)) + amount;
                result.close();

                db.execSQL("UPDATE account SET balance = '" + sbalance + "' WHERE Phone = '" + sid + "'");
                double uubalance = Double.valueOf(ubalance) - amount;
                db.execSQL("UPDATE account SET balance = '" + uubalance + "' WHERE Phone = '" + uid + "'");
                String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                db.execSQL("insert into tt values('"+uid+"','"+sid+"',"+amount+",'"+currentDateandTime+"')");
                Intent i = new Intent(getBaseContext(),transfer.class);
                i.putExtra("sender", "");
                startActivity(i);
                finish();
            }
        } else Toast.makeText(Transaction.this, "Insufficient balance", Toast.LENGTH_SHORT).show();
    }

    public void transact(View view) {
        Intent i = new Intent(getBaseContext(),transfer.class);
        i.putExtra("sender",uid);
        startActivity(i);
    }
}

