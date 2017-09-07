package com.winton.rxjavademo.http;

import android.text.TextUtils;

import com.winton.rxjavademo.http.response.CategoryResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by winton on 2017/9/4.
 */

public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 5;
    private ServerApi mServer;

    public static String baseUrl = ServerApi.BASE_URL;


    private OkHttpClient mOkHttpClient;

    private static class InstanceHolder{
        private static RetrofitClient instance = new RetrofitClient();
    }

    public static RetrofitClient getInstance(){
        return InstanceHolder.instance;
    }

    private RetrofitClient(){
        this(null);
    }

    private RetrofitClient(String url){
        if(TextUtils.isEmpty(url)){
            url = baseUrl;
        }
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
        mServer = retrofit.create(ServerApi.class);
    }

    public void getCategory(String category, int count, int page, Observer<CategoryResponse> observer){
        mServer.getCategory(category,count,page)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
