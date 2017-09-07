package com.winton.rxjavademo.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * Created by winton on 2017/9/2.
 */

public class OkhttpHelper {

    private static OkHttpClient mOkHttpClient;

    public static Bitmap getImage(String url){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        Request request = new Request.Builder().url(url).build();
        try {
            ResponseBody body = getmOkHttpClient().newCall(request).execute().body();
            InputStream is = body.byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static OkHttpClient getmOkHttpClient() {
        if(mOkHttpClient == null){
            mOkHttpClient = new OkHttpClient.Builder().build();
        }
        return mOkHttpClient;
    }
}
