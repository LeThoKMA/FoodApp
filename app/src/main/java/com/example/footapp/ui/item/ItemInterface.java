package com.example.footapp.ui.item;

import com.example.footapp.model.Item;

public interface ItemInterface {
    void deleteItem(int pos, Item item);
    void updateItem(Item item);
    void addItem(Item item);
}
