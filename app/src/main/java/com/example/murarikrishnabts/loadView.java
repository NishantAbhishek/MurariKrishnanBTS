package com.example.murarikrishnabts;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class loadView extends RecyclerView.ViewHolder {
    View mView;
    TextView textDate,textTimeStart,textTimeEnd,textLocationPinStart,textLocationPinEnd,textBusno,textSeatType,ticketPrice;
    CardView blCard;

    public loadView(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        textDate= mView.findViewById(R.id.blDate);
        textTimeStart = mView.findViewById(R.id.blLocatioionStartTime);
        textTimeEnd = mView.findViewById(R.id.blLocationEndTime);
        textLocationPinStart = mView.findViewById(R.id.blLocationStart);
        textLocationPinEnd = mView.findViewById(R.id.blLocationArrival);
        textBusno = mView.findViewById(R.id.blBusId);
        textSeatType = mView.findViewById(R.id.blSeatType);
        blCard = mView.findViewById(R.id.loadbusview);
        ticketPrice = mView.findViewById(R.id.blticket_price);

    }


    public void settextDate(String Textdate){
        textDate.setText("Date: "+Textdate);
    }

    public void settextTimeStart(String TextTimeStart){
        textTimeStart.setText("Starting at : "+TextTimeStart);
    }


    public void setTextTimeEnd(String TextTimeEnd){
        textTimeEnd.setText("Arriving at : "+ TextTimeEnd);
    }


    public void settextLocationStart(String TextLocationPinStart){
        textLocationPinStart.setText("From : "+TextLocationPinStart);
    }


    public void settextLocationEnd(String TextLocationPinEnd){
        textLocationPinEnd.setText("To : "+TextLocationPinEnd);
    }


    public void settextBusno(String TextBusno){
        textBusno.setText("Bus no: "+TextBusno);
    }



    public void settextSeatType(String TextSeatType){
        textSeatType.setText("Seat Type: "+TextSeatType);
    }
    public void settextPrice(String TextPrice){
        ticketPrice.setText(TextPrice+" Rs");
    }



}
