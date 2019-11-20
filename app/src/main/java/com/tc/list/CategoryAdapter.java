package com.tc.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tc.list.entity.CategoryEntity;

public class CategoryAdapter extends BaseRecyclerViewAdapter<CategoryEntity> {

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_category, parent, false);
        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CategoryEntity itemData = mData.get(position);
        MViewHolder mHolder = (MViewHolder) holder;
        mHolder.bind(itemData);
        bindItemClickListener(holder, position);
    }

    public void updateSelectChange(int selectedPosition) {
        int size = getItemCount();
        for (int i = 0; i < size; i++) {
            mData.get(i).selected = selectedPosition == i;
        }
        notifyDataSetChanged();
    }

    private static class MViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        private MViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvContent = itemView.findViewById(R.id.tv_content);
        }

        private void bind(CategoryEntity data) {
            tvContent.setText(data.content);
            tvContent.setSelected(data.selected);
        }
    }

}
