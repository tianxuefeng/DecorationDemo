package com.tc.list;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.tc.list.databinding.ActivityMainBinding;
import com.tc.list.entity.CategoryEntity;
import com.tc.list.entity.DataEmptyEntity;
import com.tc.list.entity.DataEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding mBinding;
    private CategoryAdapter mCategoryAdapter;
    private DataAdapter mDataAdapter;
    private String[] mCategory = new String[]{"热门", "境内", "中国港澳台", "日本", "东南亚", "欧洲", "美洲", "澳洲东非", "免签", "落地签"};
    private String[] mData = new String[]{"北京", "上海", "广州", "深圳", "杭州", "成都", "天津", "武汉", "长沙", "南京"};
    private String[] mCover = new String[]{
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067497_1382963052947.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067517_1382963076266.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067537_1382963111617.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067555_1382963164900.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067567_1382963191641.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067585_1382963218217.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067618_1382963264609.jpg",
            "http://img.pconline.com.cn/images/upload/upc/tx/itbbs/1310/28/c11/28067634_1382963290148.jpg"};

    private int tempCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.etSearch.clearFocus();

        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setItemClickListener(new OnItemClickListener<CategoryEntity>() {
            @Override
            public void onItemClick(int position, CategoryEntity data) {
                mCategoryAdapter.updateSelectChange(position);
                mDataAdapter.setData(genData(data));
                mBinding.rcvData.scrollTo(0, 0);
            }
        });

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setItemClickListener(new OnItemClickListener<Object>() {
            @Override
            public void onItemClick(int position, Object data) {
                if (data instanceof DataEntity) {
                    DataEntity itemData = (DataEntity) data;
                    String toastValue = "您点击了position=" + position + " categoryName="
                            + itemData.categoryName + " content=" + itemData.content;
                    Toast.makeText(MainActivity.this, toastValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBinding.rcvCategory.setAdapter(mCategoryAdapter);
        mBinding.rcvData.setAdapter(mDataAdapter);
        mBinding.rcvData.addItemDecoration(new DataItemDecoration(8));
        GridPinnedSectionDecoration pinnedSectionDecoration = new GridPinnedSectionDecoration(this, new GridPinnedSectionDecoration.Callback() {
            @Override
            public long getGroupId(int position) {
                Object data = mDataAdapter.getItem(position);
                if (data instanceof DataEntity) {
                    return ((DataEntity) mDataAdapter.getItem(position)).groupId;
                }
                return -1;
            }

            @Override
            public String getGroupName(int position) {
                Object data = mDataAdapter.getItem(position);
                if (data instanceof DataEntity) {
                    return ((DataEntity) mDataAdapter.getItem(position)).groupName;
                }
                return "";
            }
        });
        mBinding.rcvData.addItemDecoration(pinnedSectionDecoration);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
        mBinding.rcvData.setLayoutManager(layoutManager);

        List<CategoryEntity> categoryData = new ArrayList<>();
        for (String item : mCategory) {
            categoryData.add(new CategoryEntity(item));
        }
        categoryData.get(0).selected = true;
        mCategoryAdapter.setData(categoryData);
        mDataAdapter.setData(genData(categoryData.get(0)));
    }

    private List<Object> genData(CategoryEntity category) {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            data.addAll(genGroupData(category, i, "组名" + i, 7));
        }
        return data;
    }

    private List<Object> genGroupData(CategoryEntity category, long groupId, String groupName, int count) {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int index = (int) (Math.random() * 100) % mData.length;
            String itemStr = mData[index];
            DataEntity item = new DataEntity();
            item.groupId = groupId;
            item.groupName = groupName;
            item.categoryName = category.content;
            item.content = itemStr + "" + tempCount++;
            item.cover = getRandomCover();
            data.add(item);
        }
        if (count % 3 > 0) {
            int emptyCount = 3 - count % 3;
            for (int i = 0; i < emptyCount; i++) {
                data.add(new DataEmptyEntity());
                tempCount++;
            }
        }
        return data;
    }

    private String getRandomCover() {
        int index = (int) (Math.random() * 100) % mCover.length;
        return mCover[index];
    }
}
