package com.example.footapp.DAO;

import static com.example.footapp.utils.ConstantsKt.API_DATABASE;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.footapp.model.Bill;
import com.example.footapp.model.Item;
import com.example.footapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DAO {
    private DatabaseReference database;

    public DAO() {
        database = FirebaseDatabase.getInstance(API_DATABASE).getReference();
    }

    public DatabaseReference getReference(){
        database = FirebaseDatabase.getInstance(API_DATABASE).getReference();
        return database;
    }
    public DatabaseReference getUserReference(){
        database = FirebaseDatabase.getInstance(API_DATABASE).getReference().child("users");
        return database;
    }

    public boolean addUser(User user) {
        try {
            database.child("users").child(user.getId().toString()).setValue(user);
            return true;
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return false;
        }

    }

    public boolean updateUser(Map<String,Object> map,User user) {
        try {
            database.child("users").child(user.getId().toString()).updateChildren(map);
            return true;
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return false;
        }

    }

    public ArrayList<User> getUser() {
        ArrayList<User> list = new ArrayList();
        database.child("users").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @SuppressLint("CheckResult")
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                list.addAll(new Gson().fromJson(dataSnapshot.getValue().toString(), new TypeToken<List<User>>() {
                }.getType()));
                Log.e("TAG", list.size()+"");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: ");
            }
        });
        return list;
    }


//    public boolean addItem(Item item)
//    {
//        try {
//            database.child("items").child(item.getId().toString()).setValue(item);
//            return true;
//        } catch (Exception e) {
//            Log.e("Exception", e.toString());
//            return false;
//        }
//
//    }
//    public ArrayList<Item> getItems() {
//        ArrayList<Item> list = new ArrayList();
//        database.child("items").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                list.addAll(new Gson().fromJson(dataSnapshot.getValue().toString(), new TypeToken<List<Item>>() {
//                }.getType()));
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//
//        return list;
//    }


    public boolean addBill(Bill bill)
    {
        try {
            database.child("bills").child(bill.getIdBill().toString()).setValue(bill);
            return true;
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return false;
        }
    }
    public ArrayList<Bill> getBills() {
        ArrayList<Bill> list = new ArrayList();
        database.child("bills").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                list.addAll(new Gson().fromJson(dataSnapshot.getValue().toString(), new TypeToken<List<Bill>>() {
                }.getType()));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        return list;
    }
    public void deleteUser(int pos)
    {
        database.child("users").child(pos+"").removeValue();
    }


}
