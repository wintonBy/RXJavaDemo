package com.winton.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.winton.rxjavademo.http.RetrofitClient;
import com.winton.rxjavademo.http.response.CategoryResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by winton on 2017/9/4.
 */

public class RetrofitActivity extends Activity {

    @BindView(R.id.tv_result)TextView mTVResult;

    private List<Disposable> mDisposables;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_retrofit);
        ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        mDisposables = Lists.newArrayList();
    }

    @OnClick(R.id.bt_start)
    public void clickStart(View v){
        RetrofitClient.getInstance().getCategory("Android", 60, 1, new Observer<CategoryResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposables.add(d);
            }

            @Override
            public void onNext(@NonNull CategoryResponse categoryResponse) {
                if(categoryResponse.getResults() == null){
                    mTVResult.setText("内容为空");
                    return;
                }
                for(CategoryResponse.ResultsBean bean:categoryResponse.getResults()){
                     mTVResult.append(bean.getDesc()+bean.getWho()+"\n");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                showMsg(e.getMessage());
            }

            @Override
            public void onComplete() {
                showMsg("onComplete");
            }

        });
    }

    private void showMsg(String msg){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        for(Disposable d:mDisposables){
            d.dispose();
        }
    }
}
