package com.winton.rxjavademo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.google.common.collect.Lists;
import com.winton.rxjavademo.adapter.SearchAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by winton on 2017/8/24.
 */

public class SearchActivity extends Activity {

    @BindView(R.id.query_edit_text)
    EditText mET;
    @BindView(R.id.list)
    RecyclerView mRV;
    private SearchAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_search);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mAdapter = new SearchAdapter();
        mRV.setLayoutManager(new LinearLayoutManager(this));
        mRV.setAdapter(mAdapter);

        ObservableOnSubscribe<String> searchSource = new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(final @NonNull ObservableEmitter<String> e) throws Exception {
                //创建数据源
                mET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        e.onNext(s.toString());
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        };

        Observable.create(searchSource).map(new Function<String, List<String>>() {
            @Override
            public List<String> apply(@NonNull String s) throws Exception {
                List<String> allItems = Lists.newArrayList(SearchActivity.this.getResources().getStringArray(R.array.search_items));
                return filterList(s, allItems);
            }
        }).throttleWithTimeout(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        mAdapter.setItems(strings);
                    }
        });
    }

    /**
     * 过滤
     *
     * @param filter
     * @param all
     * @return
     */
    private List<String> filterList(String filter, List<String> all) {
        List<String> res = Lists.newArrayList();
        if (all == null || (TextUtils.isEmpty(filter))) {
            return res;
        }
        for (String tmp : all) {
            if (tmp.toLowerCase().contains(filter)) {
                res.add(tmp);
            }
        }
        return res;
    }
}
