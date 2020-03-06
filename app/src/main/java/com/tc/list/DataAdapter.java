package com.tc.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.tc.list.entity.DataEmptyEntity;
import com.tc.list.entity.DataEntity;

public class DataAdapter extends BaseRecyclerViewAdapter<Object> {
    private static final int TYPE_DATA = 0;
    private static final int TYPE_EMPTY = 1;
    private Context mContext;

    public DataAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_DATA:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_data, parent, false);
                return new MViewHolder(view);
        }
        View empty = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_data_empty, parent, false);
        return new EmptyViewHolder(empty);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object itemData = mData.get(position);
        if (itemData instanceof DataEntity) {
            ((MViewHolder) holder).bind((DataEntity) itemData);
            bindItemClickListener(holder, position);
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
        private ImageView ivCover;
        private TextView tvContent;
        private int mImageSize;

        private MViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvContent = itemView.findViewById(R.id.tv_content);
            this.ivCover = itemView.findViewById(R.id.iv_cover);
            mImageSize = mImageSize = DisplayUtil.dip2px(itemView.getContext(), 100);
        }

        private void bind(DataEntity data) {
            tvContent.setText(data.content);
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new FitCenter(), new GlideRoundTransform(ivCover.getContext(), 10));
            Glide.with(ivCover)
                    .load(data.cover)
                    .override(mImageSize, mImageSize)
                    .apply(requestOptions)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(ivCover);

//            RequestOptions requestOptions = new RequestOptions()
//                    .transform(new CenterCrop(), new RoundedCorners(15));
//            Glide.with(ivCover)
//                    .load(data.cover)
//                    .apply(requestOptions)
//                    .into(ivCover);
//            Picasso.get().load(data.cover).into(ivCover);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
