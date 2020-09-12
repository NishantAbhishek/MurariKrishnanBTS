package com.example.murarikrishnabts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class names_user extends AppCompatActivity {

    ArrayList<String> Tickets_name = new ArrayList<>();
    private ListView myListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_names_user);



        myListview = findViewById(R.id.NM_listView);

        final ArrayAdapter<String> myArrayAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,Tickets_name);
        myListview.setAdapter(myArrayAdapter);


        FirebaseUser mauth = FirebaseAuth.getInstance().getCurrentUser();
        String User = mauth.getUid();

        String intent_ref = getIntent().getStringExtra("database_ref");
        Toast.makeText(names_user.this, intent_ref ,Toast.LENGTH_LONG).show();


        DatabaseReference Ticket_User_Search = FirebaseDatabase.getInstance().
                getReference().child("Tickets").child("Tickets_User_Search").child(User).child(intent_ref);
        //getIntent().getStringExtra("database_ref")

        Ticket_User_Search.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String myChildViews = dataSnapshot.getValue(String.class);
                Tickets_name.add(myChildViews);
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



    }
}
