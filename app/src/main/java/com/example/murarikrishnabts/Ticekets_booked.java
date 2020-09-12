package com.example.murarikrishnabts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Ticekets_booked extends AppCompatActivity {

    ArrayList<String> Tickets = new ArrayList<>();
    private ListView myListview;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticekets_booked);
        myListview = findViewById(R.id.TC_listView);

        final ArrayAdapter<String> myArrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Tickets);
        myListview.setAdapter(myArrayAdapter);


         FirebaseUser mauth = FirebaseAuth.getInstance().getCurrentUser();

        String User = mauth.getUid();

        DatabaseReference Ticket_HashCode = FirebaseDatabase.getInstance().getReference().child("Tickets").child("Ticket_HashCode").child(User);
        Ticket_HashCode.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String myChildViews = dataSnapshot.getValue(String.class);
                Tickets.add(myChildViews);
                myArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String database_ref = Tickets.get(i);
                Toast.makeText(Ticekets_booked.this,database_ref ,Toast.LENGTH_LONG).show();
                Intent in = new Intent(Ticekets_booked.this,names_user.class);
                in.putExtra("database_ref",database_ref);
                startActivity(in);



            }
        });

















    }
}
