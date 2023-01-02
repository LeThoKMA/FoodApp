package com.example.footapp.ui.item;

import android.content.Context;

import com.example.footapp.model.Item;

public interface ItemInterface {
    void deleteItem(int pos, Item item);
    void updateItem(int pos, Item item, Context context);
    void addItem(Context context);
}
