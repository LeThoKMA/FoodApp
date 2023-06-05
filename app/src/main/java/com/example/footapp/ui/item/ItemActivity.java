package com.example.footapp.ui.item;

import android.app.ProgressDialog;
import android.content.Intent;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.footapp.R;
import com.example.footapp.databinding.ActivityItemBinding;
import com.example.footapp.interface1.ItemInterface;
import com.example.footapp.model.Item;
import com.example.footapp.presenter.ItemPresenter;
import com.example.footapp.ui.Order.OrderViewModel;
import com.example.footapp.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ItemActivity extends BaseActivity<ActivityItemBinding, OrderViewModel> {


    private final List<Item> mListItem = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private ItemPresenter itemPresenter;

    private ProgressDialog progressDialog;




    public void initView() {
        setColorForStatusBar(R.color.colorPrimary);
        setLightIconStatusBar(false);

        itemPresenter = new ItemPresenter();
        progressDialog = new ProgressDialog(this);
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

        getListItem();
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
        binding.imgPlus.setOnClickListener(v -> {
            Intent intent = new Intent(ItemActivity.this, AddItem.class);
            startActivity(intent);
        });

        binding.rcView.setAdapter(itemAdapter);
        binding.imvBack.setOnClickListener(v->{
            finish();
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_item;
    }

    @Override
    public void observerData() {

    }

    @Override
    public void initViewModel() {

    }
}