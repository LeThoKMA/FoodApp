package com.example.footapp.ui.item;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.footapp.R;
import com.example.footapp.model.Item;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ItemPresenter implements ItemInterface {
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
}
