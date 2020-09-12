package com.example.murarikrishnabts.Interface;

import com.example.murarikrishnabts.Model.IDs;

import java.util.List;

public interface IFirebaseLoadDone {

    void onFirebaseLoadSuccess(List<IDs> Locationlist);
    void onFirebaseLoadFailed(String Message);

}
