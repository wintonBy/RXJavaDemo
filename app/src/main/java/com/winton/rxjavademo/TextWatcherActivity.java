package com.winton.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by winton on 2017/8/24.
 */

public class TextWatcherActivity extends Activity{
    @BindView(R.id.et)EditText mET;
    @BindView(R.id.tv_rs)TextView mTVResult;

    private Disposable mDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_text_watcher);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener(){

        ObservableOnSubscribe<String> source = new ObservableOnSubscribe<String>(){
            @Override
            public void subscribe(final @NonNull ObservableEmitter<String> e) throws Exception {
                mET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        e.onNext(s.toString());
                        e.onComplete();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        };

        mDisposable = Observable.create(source).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                mTVResult.setText(s+"");
            }
        });
    }

    @OnClick(R.id.bt_clear)
    public void clickDispose(View view){
        if(mDisposable != null){
            mDisposable.dispose();
        }
    }




}
