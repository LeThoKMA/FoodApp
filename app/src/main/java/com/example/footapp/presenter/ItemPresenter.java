package com.example.footapp.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.footapp.interface1.ItemInterface;
import com.example.footapp.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        list.remove(list.get(pos));
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

}
