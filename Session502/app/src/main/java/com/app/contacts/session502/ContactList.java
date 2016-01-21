package com.app.contacts.session502;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;

public class ContactList extends AppCompatActivity {
        Button read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        read=(Button)findViewById(R.id.read);
        read.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
// TODO Auto-generated method stub
                Intent read1=new Intent();
                read1.setAction(android.content.Intent.ACTION_VIEW);
                read1.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivity(read1);
            }

            });
        }
    }

