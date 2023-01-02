package com.example.footapp.ui.item;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.footapp.R;
import com.example.footapp.databinding.ActivityUpdateItemBinding;
import com.example.footapp.model.Item;

import java.io.IOException;

public class UpdateItem extends AppCompatActivity {

    private ActivityUpdateItemBinding binding;
    private ArrayAdapter<CharSequence> adapter;
    private Item item;
    private String type;
    private final ItemPresenter itemPresenter = new ItemPresenter();
    private final int REQUEST = 10;
    private int CAMERA_PIC_REQUEST=100;
    private Uri imageUri;

    private final ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data == null) {
                        return;
                    }
                    imageUri = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        binding.img.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = ArrayAdapter.createFromResource(this,
                R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        setItemInfo();


        binding.tvChangeImg.setOnClickListener(v -> {
            requestPermission();
        });

        binding.tvRegister.setOnClickListener(v -> {
            item.setName(binding.edtName.getText().toString().trim());
            item.setImgUrl(imageUri.toString());
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

    private void setItemInfo() {
        if (getIntent().getExtras() != null) {
            item = (Item) getIntent().getExtras().get("item");
        }
        binding.edtName.setText(item.getName());
//        imageUri = Uri.parse(item.getImgUrl());
//        try {
//            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
//            binding.img.setImageBitmap(bitmap);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

    private void requestPermission() {
        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showMessageDialog();
        } else {
            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST);
        }
    }

    private void showMessageDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_choose_img);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        TextView close = dialog.findViewById(R.id.tv_close);
        TextView camera = dialog.findViewById(R.id.tv_camera);
        TextView gallery = dialog.findViewById(R.id.tv_gallery);
        close.setOnClickListener(v1 -> dialog.dismiss());

        camera.setOnClickListener(v2 -> {
            openCamera();
            dialog.dismiss();
        });
        gallery.setOnClickListener(v3 -> {
            openGallery();
            dialog.dismiss();
        });
        dialog.show();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        mActivityResultLauncher.launch(Intent.createChooser(galleryIntent, "Select from gallery"));
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== CAMERA_PIC_REQUEST &&resultCode==RESULT_OK){
            if (data != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                binding.img.setImageBitmap(bitmap);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            showMessageDialog();
        }
    }
}
