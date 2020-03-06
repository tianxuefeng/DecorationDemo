package com.tc.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.tc.list.databinding.ActivityDetailBinding;
import com.tc.list.entity.SongEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private ActivityDetailBinding mBinding;
    private DetailListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        EventBus.getDefault().register(this);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
//        setImmerseLayout(mBinding.toolBar);
        mBinding.toolBar.setNavigationIcon(R.drawable.ic_white_back);
        mBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                Intent intent = new Intent();
                intent.setClass(DetailActivity.this, DetailActivity.class);
                startActivity(intent);
            }
        });
        mBinding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                mBinding.toolBar.setBackgroundColor(changeAlpha(getResources().getColor(R.color.colorPrimary), Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));
            }
        });
        mBinding.toolBar.setTitle("这是我的标题");
        mAdapter = new DetailListAdapter();
        mBinding.rcvContent.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rcvContent.setAdapter(mAdapter);
        mBinding.rcvContent.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new SongEntity("非常好听的歌", "张三"));
        }
        mAdapter.setData(data);
    }

    protected void setImmerseLayout(View view) {// view为标题栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = getStatusBarHeight(this.getBaseContext());
            CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams) view.getLayoutParams();
            params.topMargin = statusBarHeight;
            view.setLayoutParams(params);
//            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 根据百分比改变颜色透明度
     */
    public int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    public void onTest(View view) {
        EventBus.getDefault().post(new TestEvent("test test"));
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    //登录成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleTestEvent(TestEvent event) {
        Log.d("DetailActivity", "handleTestEvent =" + hashCode());
    }
}
