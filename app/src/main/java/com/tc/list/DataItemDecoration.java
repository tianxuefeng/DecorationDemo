package com.tc.list;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DataItemDecoration extends RecyclerView.ItemDecoration {
    private int mDividerWidth;

    public DataItemDecoration(int defaultDivider) {
        mDividerWidth = defaultDivider;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanSize = layoutParams.getSpanSize();
        int spanIndex = layoutParams.getSpanIndex();
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            outRect.left = spanIndex == 0 ? mDividerWidth * 2 : mDividerWidth;
            outRect.right = spanIndex == (spanCount - 1) ? mDividerWidth * 2 : mDividerWidth;
        }
        Log.d("Tag", "spanSize=" + spanSize + " " + spanIndex);
    }
}
