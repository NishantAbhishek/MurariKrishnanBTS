package com.example.murarikrishnabts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookTicket extends AppCompatActivity {

    private TextView BT_Busnumber,BK_Date,BT_DepartureAddress,BT_DeparturePin,
            BT_ArrivalAddress,BT_ArrivalPin,BT_DepartureTime
            ,BT_ArrivalTime,BT_SeatNoavailable,BT_TypeSeatavailable;
    private FirebaseUser mauth;


    private Button BK_Confirm;
    private  String Change_no_of_Seat = "3";


    private String ST_Busnumber,ST_Date,ST_DepartureAddress,ST_DeparturePin,
            ST_ArrivalAddress,ST_ArrivalPin,ST_DepartureTime
            ,ST_ArrivalTime,ST_SeatNoavailable,ST_TypeSeatavailable,ST_TicketPrice;

    private DatabaseReference seatNo;
    private String seatNoIDReference;

    private DatabaseReference Confirming_ticket;

    int no_of_seat;




    EditText et_name;
    Button bt_add;
    ListView list_View_name;
    ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        Instantiate();
        GetStringfromIntent();
        ProgressDialog configure = new ProgressDialog(this);
        configure.setCancelable(false);
        configure.setTitle("Configuring Environment");
        configure.show();
        SetStringToTextView(configure);
        //GetNoOFPlace();


        //Here I have created arraylist to call the data
        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(BookTicket.this,android.R.layout.simple_list_item_1,arrayList);
        list_View_name.setAdapter(adapter);
        onBtnClick_add();

            BK_Confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!arrayList.isEmpty()){
                        GetNoOfPSeat();
                        Book_Ticket();
                }
                else {
                    Toast.makeText(BookTicket.this,"Please select some places",Toast.LENGTH_LONG).show();
                }



            }
            });






    }
    private void Instantiate(){
        BT_Busnumber =(TextView) findViewById(R.id.BT_Busnumber);
        BK_Date =(TextView) findViewById(R.id.BK_Date);
        BT_DepartureAddress =(TextView) findViewById(R.id.BT_DepartureAddress);
        BT_DeparturePin =(TextView) findViewById(R.id.BT_DeparturePin);
        BT_ArrivalAddress =(TextView) findViewById(R.id.BT_ArrivalAddress);
        BT_ArrivalPin =(TextView) findViewById(R.id.BT_ArrivalPin);
        BT_DepartureTime =(TextView) findViewById(R.id.BT_DepartureTime);
        BT_ArrivalTime =(TextView) findViewById(R.id.BT_ArrivalTime);
        BT_SeatNoavailable =(TextView) findViewById(R.id.BT_SeatNoavailable);
        BT_TypeSeatavailable =(TextView) findViewById(R.id.BT_TypeSeatavailable);
        BK_Confirm =(Button) findViewById(R.id.BK_Confirm);




        et_name = (EditText) findViewById(R.id.BT_travellername);
        bt_add = (Button) findViewById(R.id.BT_adTravellerbtn);

        list_View_name = (ListView) findViewById(R.id.MN_Nameslistv);




    }

    public void onBtnClick_add(){
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = et_name.getText().toString();
                arrayList.add(result);
                adapter.notifyDataSetChanged();
                et_name.setText("");
                calculate_price();
            }
        });

    }


    private void GetStringfromIntent(){
        ST_Busnumber =getIntent().getStringExtra("BusNo");
        ST_Date = getIntent().getStringExtra("Date");
        ST_DepartureAddress = getIntent().getStringExtra("DepartureAd");
        ST_DeparturePin = getIntent().getStringExtra("Departurepin");
        ST_ArrivalAddress = getIntent().getStringExtra("ArrivalAd");
        ST_ArrivalPin = getIntent().getStringExtra("ArrivalPin");
        ST_DepartureTime = getIntent().getStringExtra("DepartureTime");
        ST_ArrivalTime = getIntent().getStringExtra("ArrivalTime");
        ST_SeatNoavailable = getIntent().getStringExtra("NumberOfSeat");
        ST_TypeSeatavailable= getIntent().getStringExtra("TypeSit");
        ST_TicketPrice = getIntent().getStringExtra("Price");
        seatNoIDReference = "Runs Everyday" + ST_DeparturePin + ST_ArrivalPin;


        seatNo = FirebaseDatabase.getInstance().getReference().
                child("Buses").child(seatNoIDReference).child(ST_Busnumber);





    }


    private void SetStringToTextView(ProgressDialog configure){

        BT_Busnumber.setText("Bus No."+ST_Busnumber);
        BK_Date.setText(ST_Date);
        BT_DepartureAddress.setText("From: "+ST_DepartureAddress);
        BT_DeparturePin.setText("Pin: "+ST_DeparturePin);
        BT_ArrivalAddress.setText("To: "+ST_ArrivalAddress);
        BT_ArrivalPin.setText("Pin: "+ST_ArrivalPin);
        BT_DepartureTime.setText("Starting at: "+ST_DepartureTime);
        BT_ArrivalTime.setText("Reaching at: "+ST_ArrivalTime);
        BT_SeatNoavailable.setText("Seats available: "+ST_SeatNoavailable);
        BT_TypeSeatavailable.setText("Seat type: "+ST_TypeSeatavailable);
        BK_Confirm.setText("Confirm("+ST_TicketPrice+" Rs)");
        Toast.makeText(BookTicket.this,ST_TicketPrice,Toast.LENGTH_LONG).show();
        configure.dismiss();


    }


    private void GetNoOfPSeat(){





        no_of_seat = Integer.parseInt(ST_SeatNoavailable);
        if( no_of_seat<=0){
            Toast.makeText(BookTicket.this,"Sorry seats are full",Toast.LENGTH_LONG).show();
        }else {

            int new_val = Integer.parseInt(ST_SeatNoavailable)- arrayList.size();
            String st = Integer.toString(new_val);
            Toast.makeText(BookTicket.this,st,Toast.LENGTH_LONG).show();
            Map<String, Object> putd  = new HashMap<>();
            putd.put("NumberOfSeat",st);
            seatNo.updateChildren(putd).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent in = new Intent(BookTicket.this,Ticekets_booked.class);
                        startActivity(in);



                    }
                }
            });

        }



    }

    private void calculate_price(){
        int number_of_traveller = arrayList.size();
        int Price = Integer.parseInt(ST_TicketPrice);
        int total_price = Price * number_of_traveller;

        String str_total_price = Integer.toString(total_price);

        BK_Confirm.setText("Confirm("+str_total_price+" Rs)");



    }

    private void Book_Ticket(){

        if(no_of_seat<=0){


        }else{
            mauth = FirebaseAuth.getInstance().getCurrentUser();

            String User = mauth.getUid();
            Confirming_ticket = FirebaseDatabase.getInstance().getReference().
                    child("Tickets").child(ST_Date).child(ST_Busnumber).child(User);


            DatabaseReference TicketID = FirebaseDatabase.getInstance().getReference().
                    child("Tickets").child("TicketID").child(ST_Date);


            DatabaseReference Ticket_Check_User_Admin = FirebaseDatabase.getInstance().
                    getReference().child("Tickets").child("Ticket_Check_User_Admin").child("On "+ST_Date + " by bus no "+ ST_Busnumber);

            DatabaseReference Ticket_User_Search = FirebaseDatabase.getInstance().
                    getReference().child("Tickets").child("Tickets_User_Search").child(User).child("On "+ST_Date + " by bus no "+ ST_Busnumber);

            DatabaseReference Ticket_HashCode = FirebaseDatabase.getInstance().getReference().child("Tickets").child("Ticket_HashCode").child(User);


            //Here is the code to store the ticket in TicketID node
            HashMap<String, Object> H_TicketID = new HashMap<>();
            H_TicketID.put(ST_Date+ST_Busnumber,"On "+ST_Date + " by bus no "+ ST_Busnumber);
            TicketID.updateChildren(H_TicketID);
            //Complete  TicketID block

            //Here is the code to store Ticket_Check_User_Admin
            int Length = arrayList.size();//Data stored in list
            HashMap<String, Object> H_Ticket_Check_User_Admin = new HashMap<>();//Data stored in list are accessed one by one
            for(int a = 0; a<Length ;a++){
                H_Ticket_Check_User_Admin.put(User+"_Traveller_"+ a ,arrayList.get(a));
            }
            Ticket_Check_User_Admin.updateChildren(H_Ticket_Check_User_Admin);

            //Here is the code to store Ticket_User_Search node it will have same hashmap as  Ticket_Check_User_Admin
            Ticket_User_Search.updateChildren(H_Ticket_Check_User_Admin);

            //Ticket_HashCode is stored
            HashMap<String, Object> H_Ticket_HashCode = new HashMap<>();
            H_Ticket_HashCode.put(ST_Date+ST_Busnumber,"On "+ST_Date + " by bus no "+ ST_Busnumber);

            Ticket_HashCode.updateChildren(H_Ticket_HashCode);




        }






    }











}
