package com.example.footapp.ui.item;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.footapp.R;
import com.example.footapp.model.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private final List<Item> mListItem;
    private ItemInterface mItemInterface;

    public ItemAdapter(List<Item> mListItem, ItemInterface mItemInterface) {
        this.mListItem = mListItem;
        this.mItemInterface = mItemInterface;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = mListItem.get(position);
        if (item == null) {
            return;
        }
        holder.tvItemName.setText(item.getName());
        holder.tvItemAmount.setText(String.valueOf(item.getAmount()));
        holder.tvItemPrice.setText(String.valueOf(item.getPrice()));

        Glide.with(holder.imgAvatar.getContext()).load(item.getImgUrl()).into(holder.imgAvatar);

        holder.imgUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(holder.imgUpdate.getContext(), UpdateItem.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("item", item);
            intent.putExtras(bundle);
            holder.imgUpdate.getContext().startActivity(intent);
        });
        holder.imgDelete.setOnClickListener(v -> {
            mItemInterface.deleteItem(position, item);
        });

    }

    @Override
    public int getItemCount() {
        if (mListItem != null) {
            return mListItem.size();
        }
        return 0;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgAvatar, imgUpdate, imgDelete;
        private TextView tvItemName, tvItemAmount, tvItemPrice;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.img_item);
            imgUpdate = itemView.findViewById(R.id.img_update);
            imgDelete = itemView.findViewById(R.id.img_delete);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemAmount = itemView.findViewById(R.id.tv_item_amount);
            tvItemPrice = itemView.findViewById(R.id.tv_item_price);
        }
    }

    public void remove(int pos) {
        mListItem.remove(pos);
        notifyItemRemoved(pos);
        notifyDataSetChanged();
    }
}
