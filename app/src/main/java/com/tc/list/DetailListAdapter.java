package com.tc.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tc.list.entity.DataEmptyEntity;
import com.tc.list.entity.DataEntity;
import com.tc.list.entity.SongEntity;

public class DetailListAdapter extends BaseRecyclerViewAdapter<Object> {
    private static final int TYPE_DATA = 0;
    private static final int TYPE_EMPTY = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DATA:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_data, parent, false);
                return new MViewHolder(view);
        }
        View empty = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_data_empty, parent, false);
        return new EmptyViewHolder(empty);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object itemData = mData.get(position);
        if (itemData instanceof SongEntity) {
            ((MViewHolder) holder).bind(position, (SongEntity) itemData);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object object = getItem(position);
        if (object instanceof DataEntity) {
            return TYPE_DATA;
        } else if (object instanceof DataEmptyEntity) {
            return TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }

    private static class MViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNumber;
        private TextView tvName;
        private TextView tvAuthorName;

        private MViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvNumber = itemView.findViewById(R.id.tv_num);
            this.tvName = itemView.findViewById(R.id.tv_name);
            this.tvAuthorName = itemView.findViewById(R.id.tv_author_name);
        }

        private void bind(int index, SongEntity data) {
            tvNumber.setText(String.valueOf(index + 1));
            tvName.setText(data.name);
            tvAuthorName.setText(data.authorName);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
