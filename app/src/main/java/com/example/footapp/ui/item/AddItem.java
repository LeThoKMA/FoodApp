package com.example.footapp.ui.item;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.footapp.R;
import com.example.footapp.databinding.ActivityAddItemBinding;
import com.example.footapp.model.Item;
import com.example.footapp.presenter.ItemPresenter;

public class AddItem extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    private final ItemPresenter itemPresenter = new ItemPresenter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        binding.edtImgUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Glide.with(AddItem.this).load(binding.edtImgUrl.getText().toString().trim()).error(R.mipmap.ic_launcher).into(binding.img);
                binding.img.setVisibility(View.VISIBLE);
            }
        });

        binding.tvRegister.setOnClickListener(v -> {
            Item item = new Item();
            item.setId(Integer.parseInt(binding.edtId.getText().toString().trim()));
            item.setPrice(Integer.parseInt(binding.edtPrice.getText().toString().trim()));
            item.setAmount(Integer.parseInt(binding.edtAmount.getText().toString().trim()));
            item.setImgUrl(binding.edtImgUrl.getText().toString().trim());
            item.setName(binding.edtName.getText().toString().trim());
            item.setType(binding.spinner.getSelectedItemPosition());

            itemPresenter.addItem(item);
            finish();
        });

        binding.imvBack.setOnClickListener(v -> finish());

    }
}