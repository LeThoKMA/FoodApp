package com.example.footapp.ui.item;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.footapp.databinding.ActivityItemBinding;
import com.example.footapp.model.Item;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemActivity extends AppCompatActivity {

    private ActivityItemBinding binding;
    private final List<Item> mListItem = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private ItemPresenter itemPresenter;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUI();
        initView();
        initListener();
        getListItem();
    }

    private void initUI() {
        itemPresenter = new ItemPresenter();
        progressDialog = new ProgressDialog(this);
    }


    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.rcView.addItemDecoration(decoration);
        itemAdapter = new ItemAdapter(mListItem, new ItemInterface() {
            @Override
            public void deleteItem(int pos, Item item) {
                itemAdapter.remove(pos);
                itemPresenter.deleteItem(pos, item);
                Toast.makeText(getApplicationContext(), "" + pos, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateItem(Item item) {

            }

            @Override
            public void addItem(Item item) {

            }
        });

        binding.imgPlus.setOnClickListener(v -> {
            Intent intent = new Intent(ItemActivity.this, AddItem.class);
            startActivity(intent);
        });

        binding.rcView.setAdapter(itemAdapter);
    }


    private void getListItem() {
        progressDialog.show();
        itemPresenter.getDataItem();

//        itemPresenter.addItem.observe(this, item -> {
//            mListItem.add(item);
//            itemAdapter.notifyDataSetChanged();
//            progressDialog.dismiss();
//        });

        itemPresenter.listItem.observe(this,items -> {
            mListItem.clear();
            mListItem.addAll(items);
            itemAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        });

        itemPresenter.updateItem.observe(this, item -> {
            for (int i = 0; i < mListItem.size(); i++) {
                if (Objects.equals(item.getId(), mListItem.get(i).getId())) {
                    mListItem.set(i, item);
                }
            }
            itemAdapter.notifyDataSetChanged();
        });


    }

    public void initListener() {
        binding.imvBack.setOnClickListener(v -> finish());
    }
}