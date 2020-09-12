package com.example.murarikrishnabts;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.murarikrishnabts.Interface.IFirebaseLoadDone;
import com.example.murarikrishnabts.Model.IDs;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone, DatePickerDialog.OnDateSetListener {
    SearchableSpinner searchableSpinnerdeparture;
    SearchableSpinner searchableSpinnerArrival;
    private DatabaseReference locationref;
    IFirebaseLoadDone iFirebaseLoadDone;
    List<IDs> iDs;


    private FirebaseUser currentuser;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference database,loadbus;
    private String UserId,name_set;
    private TextView Tx_Name,Tx_Location_Destination,Tx_Location_Arrival;
    private Button btn_SignOut,MN_Select_date,Confirm;
    private ProgressDialog progressDialog;
    private String STlocation_arrival_pin,STlocation_departure_pin,Finaldate,dbEveryday;
    private String  dbDate;
    int day,month,year,dayfinal,monthfinal,yearfinal;

    private RecyclerView MNblload;
    private GridLayoutManager gridLayoutManager;
    private String ArrivalAd,DepartureAd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        Initialize();
        TextView_Name();
        FirebaseDataRetrieve();
        SpinnerGetText();
        Buttons();

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!STlocation_arrival_pin.equals(STlocation_departure_pin)){

                    if(!STlocation_arrival_pin.isEmpty()&&!STlocation_departure_pin.isEmpty()){
                        Searching();
                    }else{
                        Toast.makeText(MainActivity.this,"Please enter date",Toast.LENGTH_SHORT).show();
                    }


                }else{
                    Toast.makeText(MainActivity.this,"Location is repeated",Toast.LENGTH_SHORT).show();


                }

            }
        });






    }

    private void Buttons(){
        btn_SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(MainActivity.this,ActivityStart_User.class);
                startActivity(in);
            }
        });
        MN_Select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                day = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                year = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        MainActivity.this,year,month,day);
                datePickerDialog.show();



            }
        });





    }


    private void Initialize(){
        Tx_Name = (TextView)findViewById(R.id.MN_Name);
        btn_SignOut = findViewById(R.id.MN_Signout);
        searchableSpinnerdeparture = (SearchableSpinner) findViewById(R.id.MN_Location_Place_Start);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Configuring Environment");
        progressDialog.show();

        //firebase
        currentuser = FirebaseAuth.getInstance().getCurrentUser();
        UserId = currentuser.getUid();
        database = FirebaseDatabase.getInstance().getReference().child("Users").child(UserId);
        searchableSpinnerArrival = findViewById(R.id.MN_Location_Place_Destination);

        Tx_Location_Destination = findViewById(R.id.MN_destination_id);
        Tx_Location_Arrival = findViewById(R.id.MN_Starting_location_id);
        MN_Select_date = findViewById(R.id.MN_Select_date);
        Confirm = findViewById(R.id.MN_Search);

        gridLayoutManager = new GridLayoutManager(this , 1,GridLayoutManager.VERTICAL,false);


        MNblload = findViewById(R.id.MNblload);
        MNblload.setHasFixedSize(false);
        MNblload.setLayoutManager(gridLayoutManager);

        Button Ticekets_booked = new Button(this);
        Ticekets_booked = findViewById(R.id.MN_Ticekets_booked);

        Ticekets_booked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this,Ticekets_booked.class);
                startActivity(in);

            }
        });







    }



    private void TextView_Name(){
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name_set = dataSnapshot.child("Name").getValue().toString();
                Tx_Name.setText(name_set);
                progressDialog.cancel();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    private void FirebaseDataRetrieve(){


        locationref = FirebaseDatabase.getInstance().getReference("Locations");
        iFirebaseLoadDone = this;
        locationref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<IDs> iDs = new ArrayList<>();

                for (DataSnapshot idSnapShot:dataSnapshot.getChildren()){
                    iDs.add(idSnapShot.getValue(IDs.class));
                }
                iFirebaseLoadDone.onFirebaseLoadSuccess(iDs);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }

        });



    }

    //this code is for setting the text in
    private void  SpinnerGetText(){
        searchableSpinnerArrival.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IDs iD = iDs.get(position);
                STlocation_arrival_pin  = iD.getLocation_pin();
                Tx_Location_Destination.setText(STlocation_arrival_pin);
                DepartureAd = iD.getPlace();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        searchableSpinnerdeparture.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                IDs iD = iDs.get(position);
                STlocation_departure_pin = iD.getLocation_pin();
                Tx_Location_Arrival.setText(STlocation_departure_pin);
                ArrivalAd =iD.getPlace();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }




    @Override
    protected void onStart() {
        super.onStart();
        Finaldate = "Empty";

    }

    @Override
    public void onFirebaseLoadSuccess(List<IDs> Locationlist) {
        iDs = Locationlist;
        List<String> id_list = new ArrayList<>();
        for (IDs id:Locationlist){
            id_list.add(id.getPlace());
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,id_list);
            searchableSpinnerdeparture.setAdapter(adapter);
            searchableSpinnerArrival.setAdapter(adapter);

        }
    }



    @Override
    public void onFirebaseLoadFailed(String Message) {

    }



    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        yearfinal =year;
        monthfinal = month+1;
        dayfinal = dayOfMonth;
        Finaldate = ("Date:" + dayfinal + "_" + monthfinal +"_" + yearfinal);
        MN_Select_date.setText(Finaldate);
//        String dbDate = STlocation_arrival_pin.gett,STlocation_departure_pin,Finaldate;'
         dbDate = Finaldate + STlocation_departure_pin +STlocation_arrival_pin;


    }

    public void Searching(){
        if(Finaldate.equals("Empty")){
            Toast.makeText(MainActivity.this,"Please select a specific date",Toast.LENGTH_LONG).show();
            //User should select a specific date.

        }else{

            dbEveryday = "Runs Everyday"+ STlocation_departure_pin + STlocation_arrival_pin;
            loadbus = FirebaseDatabase.getInstance().getReference().child("Buses").child(dbEveryday);
            //


            FirebaseRecyclerAdapter <BusModel,loadView> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<BusModel, loadView>(
                    BusModel.class,
                    R.layout.bus_loading,
                    loadView.class,
                    loadbus
            ) {
                @Override
                protected void populateViewHolder(final loadView viewHolder, BusModel model, int position) {

                    final String Blid  = getRef(position).getKey();


                    loadbus.child(Blid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.hasChild("ArrivalPin") && dataSnapshot.hasChild("ArrivalTime")
                                    &&dataSnapshot.hasChild("DeparturePin") && dataSnapshot.hasChild("BusNo")
                                    && dataSnapshot.hasChild("Date")&&dataSnapshot.hasChild("DepartureTime") && dataSnapshot.hasChild("TypeSit")){


                                final String ArrivalPin = dataSnapshot.child("ArrivalPin").getValue().toString();
                                final String ArrivalTime = dataSnapshot.child("ArrivalTime").getValue().toString();
                                final String Departurepin = dataSnapshot.child("DeparturePin").getValue().toString();
                                final String BusNo = dataSnapshot.child("BusNo").getValue().toString();
                                final String Date = dataSnapshot.child("Date").getValue().toString();
                                final String DepartureTime = dataSnapshot.child("DepartureTime").getValue().toString();
                                final String TypeSit = dataSnapshot.child("TypeSit").getValue().toString();
                                final String NoOfSit = dataSnapshot.child("NumberOfSeat").getValue().toString();
                                final String Ticket_price = dataSnapshot.child("TicketPrice").getValue().toString();

                                viewHolder.settextBusno(BusNo);
                                viewHolder.settextDate(Date);
                                viewHolder.settextLocationEnd(DepartureAd);
                                viewHolder.settextLocationStart(ArrivalAd);
                                viewHolder.settextSeatType(TypeSit);
                                viewHolder.settextTimeStart(DepartureTime);
                                viewHolder.setTextTimeEnd(ArrivalTime);
                                viewHolder.settextPrice(Ticket_price);



                                viewHolder.blCard.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent in = new Intent(MainActivity.this,BookTicket.class);
                                        in.putExtra("ArrivalTime",ArrivalTime);
                                        in.putExtra("DepartureTime",DepartureTime);
                                        in.putExtra("BusNo",BusNo);
                                        in.putExtra("NumberOfSeat",NoOfSit);
                                        in.putExtra("TypeSit",TypeSit);
                                        in.putExtra("DepartureAd",DepartureAd);
                                        in.putExtra("ArrivalAd",ArrivalAd);
                                        in.putExtra("ArrivalPin",ArrivalPin);
                                        in.putExtra("Departurepin",Departurepin);
                                        in.putExtra("Date",Finaldate);
                                        in.putExtra("Price",Ticket_price);
                                        startActivity(in);
                                    }
                                });
                            }else {


                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }

            };

            MNblload.setAdapter(firebaseRecyclerAdapter);

        }



    }




}
