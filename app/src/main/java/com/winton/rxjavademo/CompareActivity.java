package com.winton.rxjavademo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by winton on 2017/9/6.
 */

public class CompareActivity extends Activity {

    private String directoryPath = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void oldSample(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File directory = new File(directoryPath);
                File files[] = directory.listFiles();
                for(File file: files){
                    if(file.getName().endsWith(".png")){
                        final Bitmap bitmap = getBitmapFromFile(file);
                        try {
                            Thread.sleep(2000);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setImage(bitmap);
                            }
                        });
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CompareActivity.this,"完成",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }).start();
    }

    private void rxSample(){
        Observable.create(new ObservableOnSubscribe<File>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<File> e) throws Exception {
                File directory = new File(directoryPath);
                File files[] = directory.listFiles();
                for(File file : files){
                    e.onNext(file);
                }
                e.onComplete();
            }
        }).filter(new Predicate<File>() {
            @Override
            public boolean test(@NonNull File file) throws Exception {
                return file.getName().endsWith(".png");
            }
        }).map(new Function<File, Bitmap>() {
            @Override
            public Bitmap apply(@NonNull File file) throws Exception {
                Thread.sleep(2000);
                return getBitmapFromFile(file);
            }
        })
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                setImage(bitmap);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Toast.makeText(CompareActivity.this,"完成",Toast.LENGTH_LONG).show();
            }
        });
    }























    private Bitmap getBitmapFromFile(File file){
        return null;
    }
    private void setImage(Bitmap bitmap){

    }

}
