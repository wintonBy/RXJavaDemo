package com.winton.rxjavademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.winton.rxjavademo.http.OkhttpHelper;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by winton on 2017/8/31.
 */

public class RxJavaAnimActivity extends Activity {

    @BindView(R.id.tv_desc)
    TextView mTV;
    @BindView(R.id.iv_image)
    ImageView mIVAnim;

    @BindString(R.string.anim_desc)String animDesc;

    List<String> iconList;
    private Subscription mIVAnimSub;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_rx_anim);
        ButterKnife.bind(this);
        initData();
    }

    private void initData(){
        iconList = Lists.newArrayList();
        iconList.add("http://img.hb.aicdn.com/3e5701fd4a92b1a7bd78460dcdebe908cdd12d1815f8d-kCsqVB_fw658");
        iconList.add("http://img.hb.aicdn.com/292d7370f7877d4bea8bb657ca09a6320c83c45d293d4-pwdRZU_fw658");
        iconList.add("http://img.hb.aicdn.com/b2c481afd86545ba5fab9a2f6561ba3e4bd740411a8b4-2gbZKv_fw658");
        iconList.add("http://img.hb.aicdn.com/dbcdc16aeb64ccabe6648690dd95b0222186a3c73f9df-Y67fD5_fw658");
    }

    @Override
    protected void onResume() {
        super.onResume();
        doTextAnim();
    }


    private void doTextAnim(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                String text[] = animDesc.split("");
                for(String tmp:text){
                    e.onNext(tmp);
                }
                e.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
        .subscribeOn(Schedulers.io())
        .observeOn(Schedulers.newThread())
        .map(new Function<String, String>() {
            @Override
            public String apply(@NonNull String s) throws Exception {
                Thread.sleep(200);
                return s;
            }
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            private Subscription mSub;


            @Override
            public void onSubscribe(Subscription s) {
                mSub = s;
                mSub.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                mTV.append(s);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(RxJavaAnimActivity.this,"获取完成",Toast.LENGTH_SHORT).show();
                doImageAnim();
            }
        });
    }

    private void doImageAnim(){
        final Flowable<Bitmap> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {

                for(String tmp:iconList){
                    e.onNext(tmp);
                }
                e.onComplete();
            }
        },BackpressureStrategy.BUFFER).map(new Function<String, Bitmap>() {
            @Override
            public Bitmap apply(@NonNull String url) throws Exception {
                Thread.sleep(1000);
                return OkhttpHelper.getImage(url);
            }
        });
           flowable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Bitmap>() {
                @Override
                public void onSubscribe(Subscription s) {
                    mIVAnimSub = s;
                    mIVAnimSub.request(Long.MAX_VALUE);
                }

                @Override
                public void onNext(Bitmap bitmap) {
                    if(bitmap != null){
                        mIVAnim.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onError(Throwable t) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mIVAnimSub != null){
            mIVAnimSub.cancel();
        }
    }
}
