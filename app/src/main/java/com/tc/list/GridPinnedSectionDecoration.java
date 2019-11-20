package com.tc.list;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridPinnedSectionDecoration extends RecyclerView.ItemDecoration {
    private final String TAG = GridPinnedSectionDecoration.class.getSimpleName();
    private Context mContext;
    private Callback mCallback;
    private Paint mBgPaint;//画背景
    private Paint mTextPaint;//画文字
    private int mTitleHeight;//Title的高度
    private int mTitlePaddingLeft;
    private final Paint mPaint;

    private int mLastSectionIndex;

    public GridPinnedSectionDecoration(@NonNull Context context, @NonNull Callback callback) {
        mContext = context;
        mCallback = callback;
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setColor(Color.WHITE);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, 15));
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        mTitleHeight = DisplayUtil.dip2px(mContext, 30);
        mTitlePaddingLeft = DisplayUtil.dip2px(mContext, 8);

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = (parent.getLayoutManager()).getPosition(view);
        if (position == 0) {//第一个条目肯定需要Title
            outRect.set(0, mTitleHeight, 0, 0);
            return;
        }
        long currentGroupId = mCallback.getGroupId(position);
        long preGroupId = mCallback.getGroupId(position - 1);
        boolean notSameGroup = currentGroupId != preGroupId && currentGroupId >= 0;
        if (notSameGroup) {
            mLastSectionIndex = position;
            Log.d(TAG, "=============== mLastSectionIndex=" + mLastSectionIndex);
        }
        boolean sameRow = false;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int spanCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
            int indexDiff = position - mLastSectionIndex;
            sameRow = indexDiff <= (spanCount - 1) && indexDiff > 0;

            if (!sameRow && position < mLastSectionIndex) {
                //第一行的非第一个item，从后往前滑分组非第一个都需要设置顶部距离
                sameRow = (position - spanCount) < 0 || currentGroupId != mCallback.getGroupId(position - spanCount);
            }
        }
        Log.d(TAG, "getItemOffsets position=" + position + " notSameGroup=" + notSameGroup + " sameRow=" + sameRow);
        if (notSameGroup || sameRow) {
            //当前条目和上一个条目的第一个拼音不同时需要Title
            //与显示section的第一条在同一行时需要title
            outRect.set(0, mTitleHeight, 0, 0);
        } else { //
            outRect.set(0, 0, 0, 0);
        }
    }


    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
//        Log.d(TAG, "onDraw childCount=" + childCount);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = (parent.getLayoutManager()).getPosition(child);
//            c.drawRect(0, child.getBottom(), parent.getRight(), child.getBottom() + 3, mPaint);

            boolean drawSection = position == 0;
            if (position > 0) {
                long currentGroupId = mCallback.getGroupId(position);
                long preGroupId = mCallback.getGroupId(position - 1);
                if (currentGroupId != preGroupId && currentGroupId >= 0) {
                    drawSection = true;
                }
            }
            if (drawSection) {
                //画背景
                c.drawRect(0, child.getTop() - mTitleHeight, parent.getRight(), child.getTop(), mBgPaint);
                String c1 = mCallback.getGroupName(position);
                Rect rect = new Rect();
                mTextPaint.getTextBounds(c1, 0, 1, rect);
                //画文字
                c.drawText(c1, mTitlePaddingLeft, child.getTop() - (mTitleHeight / 3 - rect.height() / 2), mTextPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        //第一个可见条目的位置
        int position = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        View firstChild = parent.getLayoutManager().findViewByPosition(position);
        int nextChild = position + 1;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            nextChild = position + ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
        }
        View secondChild = parent.getLayoutManager().findViewByPosition(nextChild);
        if (secondChild.getTop() - firstChild.getTop() > firstChild.getHeight() * 2) {
            //当第二个title和第一个title重合时移动画板,产生动画效果
            c.translate(0, firstChild.getTop());
            Log.d(TAG, "onDrawOver position=" + position);
        }

        //画背景
        c.drawRect(0, 0, parent.getRight(), mTitleHeight, mBgPaint);
        String c1 = mCallback.getGroupName(position);
        Rect rect = new Rect();
        mTextPaint.getTextBounds(c1, 0, 1, rect);
        //画文字
        c.drawText(c1, mTitlePaddingLeft, mTitleHeight / 2 + rect.height() / 2, mTextPaint);
    }

    public interface Callback {
        long getGroupId(int position);

        String getGroupName(int position);
    }
}
