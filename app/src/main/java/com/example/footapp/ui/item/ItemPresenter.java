package com.example.footapp.ui.item;

import android.content.ClipData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.footapp.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ItemPresenter implements ItemInterface {

    public MutableLiveData<Item> addItem= new MutableLiveData<>();
    public MutableLiveData<Item> updateItem= new MutableLiveData<>();
    public MutableLiveData<List<Item>> listItem= new MutableLiveData<>();
    private ArrayList<Item> list= new ArrayList<>();

    @Override
    public void deleteItem(int pos, Item item) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("items").child(String.valueOf(item.getId()));
        reference.removeValue();
    }

    @Override
    public void updateItem(Item item) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("items");

        HashMap<String, Object> result = new HashMap<>();
        result.put("id", item.getId());
        result.put("name", item.getName());
        result.put("imgUrl", item.getImgUrl());
        result.put("price", item.getPrice());
        result.put("amount", item.getAmount());
        result.put("type", item.getType());

        reference.child(String.valueOf(item.getId())).updateChildren(result);
    }

    @Override
    public void addItem(Item item) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("items");
        reference.push().child(String.valueOf(item.getId()));
        reference.child(String.valueOf(item.getId())).setValue(item);
    }

    public void getDataItem()
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("items");
        Query query = reference.orderByChild("type");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Item item = snapshot.getValue(Item.class);
                if (item != null) {
                    list.add(item);
                }
                listItem.postValue(list);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Item item = snapshot.getValue(Item.class);
                if (item != null){
                   updateItem.postValue(item);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }
    public void getListItem()
    {
        ArrayList<Item> list= new ArrayList<>();
        ValueEventListener listener= new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(Item item:  (List<Item>)snapshot.getValue(List.class))
                    {
                        if(item!=null)
                        {
                            list.add(item); }

                    }
                }
               // dataItems.postValue(list)


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
//            val list: ArrayList<Item?> = arrayListOf()
//            val itemListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//
//
//                dataSnapshot.getValue<List<Item>>()?.let {
//                    for(item in it)
//                    {
//                        if(item!=null)
//                        {
//                            list.add(item) }
//
//                    }
//                }
//                dataItems.postValue(list)
//
//
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("TAG", "loadPost:onCancelled", databaseError.toException())
//                dataItems.postValue(null)
//            }
//        }
//            dao.itemReference.addValueEventListener(itemListener)
//
//        }
    }
}
