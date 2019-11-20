package com.tc.list;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter {
    protected List<T> mData;
    protected OnItemClickListener<T> mItemClickListener;

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    protected void bindItemClickListener(@NonNull RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(mInnerItemClickListener);
    }

    public T getItem(int position) {
        return (mData == null || position >= mData.size())  ? null : mData.get(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    private View.OnClickListener mInnerItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(position, mData.get(position));
            }
        }
    };

}
