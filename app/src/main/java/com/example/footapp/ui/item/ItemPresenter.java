package com.example.footapp.ui.item;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.footapp.R;
import com.example.footapp.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ItemPresenter implements ItemInterface{
    @Override
    public void deleteItem(int pos, Item item) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("items").child(String.valueOf(item.getId()));
        reference.removeValue();
    }

    @Override
    public void updateItem(int pos, Item item, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_update_item);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText id = dialog.findViewById(R.id.edt_id);
        EditText name = dialog.findViewById(R.id.edt_name);
        EditText imgUrl = dialog.findViewById(R.id.edt_imgUrl);
        EditText price = dialog.findViewById(R.id.edt_price);
        EditText amount = dialog.findViewById(R.id.edt_amount);
        EditText type = dialog.findViewById(R.id.edt_type);

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button update = dialog.findViewById(R.id.btn_update);

        id.setText(String.valueOf(item.getId()));
        name.setText(item.getName());
        imgUrl.setText(item.getImgUrl());
        price.setText(String.valueOf(item.getPrice()));
        amount.setText(String.valueOf(item.getAmount()));
        type.setText(String.valueOf(item.getType()));

        cancel.setOnClickListener(v -> dialog.dismiss());

        update.setOnClickListener(v -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference("items");

            item.setId(Integer.parseInt(id.getText().toString().trim()));
            item.setName(name.getText().toString().trim());
            item.setImgUrl(imgUrl.getText().toString().trim());
            item.setPrice(Integer.parseInt(price.getText().toString().trim()));
            item.setAmount(Integer.parseInt(amount.getText().toString().trim()));
            item.setType(Integer.parseInt(type.getText().toString().trim()));

            HashMap<String,Object> result = new HashMap<>();
            result.put("id",item.getId());
            result.put("name",item.getName());
            result.put("imgUrl",item.getImgUrl());
            result.put("price",item.getPrice());
            result.put("amount",item.getAmount());
            result.put("type",item.getType());

            reference.child(String.valueOf(item.getId())).updateChildren(result, (error, ref) -> {
                Toast.makeText(context, "update success", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    @Override
    public void addItem( Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_add_item);
        Window window = dialog.getWindow();
        if (window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText id = dialog.findViewById(R.id.edt_id);
        EditText name = dialog.findViewById(R.id.edt_name);
        EditText imgUrl = dialog.findViewById(R.id.edt_imgUrl);
        EditText price = dialog.findViewById(R.id.edt_price);
        EditText amount = dialog.findViewById(R.id.edt_amount);
        EditText type = dialog.findViewById(R.id.edt_type);

        Button cancel = dialog.findViewById(R.id.btn_cancel);
        Button add = dialog.findViewById(R.id.btn_add);


        cancel.setOnClickListener(v -> dialog.dismiss());

        add.setOnClickListener(v -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference("items");

            Item item = new Item();

            item.setId(Integer.parseInt(id.getText().toString().trim()));
            item.setName(name.getText().toString().trim());
            item.setImgUrl(imgUrl.getText().toString().trim());
            item.setPrice(Integer.parseInt(price.getText().toString().trim()));
            item.setAmount(Integer.parseInt(amount.getText().toString().trim()));
            item.setType(Integer.parseInt(type.getText().toString().trim()));

            reference.push().child(String.valueOf(item.getId()));

            reference.child(String.valueOf(item.getId())).setValue(item, (error, ref) -> {
                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });
        dialog.show();
    }
}
