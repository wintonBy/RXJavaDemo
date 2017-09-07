package com.winton.rxjavademo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.common.collect.Lists;
import com.winton.rxjavademo.adapter.DemoAdapter;
import com.winton.rxjavademo.model.DemoItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by winton on 2017/8/23.
 */

public class DemoListActivity extends Activity {

    @BindView(R.id.rv)RecyclerView mRV;

    private DemoAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_demo_list);
        ButterKnife.bind(this);
        initData();
    }
    private void initData(){
        List<DemoItem> list = Lists.newArrayList();
        list.add(new DemoItem("RxJava实现文本输入监听",new Intent(this,TextWatcherActivity.class)));
        list.add(new DemoItem("RxJava实现搜索框联想",new Intent(this,SearchActivity.class)));
        list.add(new DemoItem("RxJava实现Android动画",new Intent(this,RxJavaAnimActivity.class)));
        list.add(new DemoItem("RxJava&Retrofit实现网络引擎",new Intent(this,RetrofitActivity.class)));

        mAdapter = new DemoAdapter(list,this);
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mRV.setAdapter(mAdapter);

    }

    public static void open(Context fromContext,Bundle params){
        if(fromContext == null){
            return;
        }
        Intent intent = new Intent(fromContext,DemoListActivity.class);
        if(params != null){
            intent.putExtras(params);
        }
        fromContext.startActivity(intent);
    }


}
