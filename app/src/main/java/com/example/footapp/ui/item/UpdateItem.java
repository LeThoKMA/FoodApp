package com.example.footapp.ui.item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.footapp.R;
import com.example.footapp.databinding.ActivityUpdateItemBinding;
import com.example.footapp.model.Item;
import com.example.footapp.ui.BaseActivity;

public class UpdateItem extends BaseActivity<ActivityUpdateItemBinding> {

    private ArrayAdapter<CharSequence> adapter;
    private Item item;
    private String type;
    private final ItemPresenter itemPresenter = new ItemPresenter();

    private void setItemInfo() {
        if (getIntent().getExtras() != null) {

            item = (Item) getIntent().getExtras().get("item");
        }
        binding.edtName.setText(item.getName());
        Glide.with(UpdateItem.this).load(item.getImgUrl()).error(R.mipmap.ic_launcher).into(binding.img);
        binding.edtImgUrl.setText(item.getImgUrl());
        binding.edtPrice.setText(String.valueOf(item.getPrice()));
        binding.edtAmount.setText(String.valueOf(item.getAmount()));
        switch (item.getType()) {
            case 0:
                type = "Coffee";
                break;
            case 1:
                type = "Tea";
                break;
            case 2:
                type = "Juice";
                break;
            case 3:
                type = "Smoothie";
                break;
        }
        binding.spinner.setSelection(adapter.getPosition(type));
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_update_item;
    }

    @Override
    public void initView() {
        adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);


        setItemInfo();


    }

    @Override
    public void initListener() {
        binding.edtImgUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Glide.with(UpdateItem.this).load(binding.edtImgUrl.getText().toString().trim()).into(binding.img);
            }
        });

        binding.tvRegister.setOnClickListener(v -> {
            item.setName(binding.edtName.getText().toString().trim());
            item.setImgUrl(binding.edtImgUrl.getText().toString().trim());
            item.setPrice(Integer.parseInt(binding.edtPrice.getText().toString().trim()));
            item.setAmount(Integer.parseInt(binding.edtAmount.getText().toString().trim()));
            item.setType(binding.spinner.getSelectedItemPosition());
            itemPresenter.updateItem(item);
            finish();
        });

        binding.imvBack.setOnClickListener(v -> {
            finish();
        });

    }
}
