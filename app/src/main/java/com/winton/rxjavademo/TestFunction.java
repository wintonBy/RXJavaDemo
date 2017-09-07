package com.winton.rxjavademo;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.common.collect.Lists;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by winton on 2017/8/29.
 */

public class TestFunction {

    private static final String TAG = "RXJava";


    private void sample(){
        Observable.fromArray(Lists.newArrayList())
                .map(new Function<ArrayList<Object>, Object>() {
                    @Override
                    public Object apply(@NonNull ArrayList<Object> objects) throws Exception {
                        //做数据流转换

                        return null;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        //应用数据流
                    }
                });
    }

    private void createOb(){
        Observable<String> observable = Observable.just("大家好","这是一个简单的创建RxJava的例子","介绍完毕");

        String source[] = new String[]{"大家好","这是一个简单的创建RxJava的例子","介绍完毕"};
        Observable<String> observable1 = Observable.fromArray(source);


        Observer<String> observer = new Observer<String>() {
            private Disposable mSwitch;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mSwitch = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                if(s.equals("大家好")){
                    mSwitch.dispose();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };

        Subscriber<String> subscriber = new Subscriber<String>() {
            private Subscription mSwitch;
            @Override
            public void onSubscribe(Subscription s) {
                mSwitch = s;
            }

            @Override
            public void onNext(String s) {
                if(s.equals("大家好")){
                    mSwitch.cancel();
                }
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribe(observer);

        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {

            }
        };

        observable.subscribe(consumer);
    }

    private void transOb(){
        String imageUrls[] = new String[]{};

        Observable.fromArray(imageUrls)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        return getImageFromNet(s);
                    }
                })
                .subscribe();

        String provinces[] = new String[]{};
        Observable.fromArray(provinces)
                .flatMap(new Function<String, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull String s) throws Exception {
                        return Observable.fromArray(getCitysByProvince(s));
                    }
                })
                .subscribe();

    }


    private Bitmap getImageFromNet(String url){
        return null;
    }

    private Object[] getCitysByProvince(String provinceName){
        return null;
    }

    private void filterOb(){

        Observable.just(1,1,3,4,5,6,5,3,4,5)
                .distinct()
                .subscribe();

        Observable.just(1,1,3,4,5,6,5,3,4,5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 3;
                    }
                }).subscribe();

    }

    private void threadOb(){
        Observable.just(1,1,3,4,5,6,5,3,4,5)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())
                .map(new Function<Integer, Object>() {
                    @Override
                    public Object apply(@NonNull Integer integer) throws Exception {
                        return null;
                    }
                }).observeOn(Schedulers.newThread())
                .distinct()
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });
    }

    private void testFol(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("hah");
                e.onComplete();
            }
        }, BackpressureStrategy.DROP);



    }

    /**
     * 实现三步曲
     */
    public void sampleOTTByOb(){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.e(TAG, "Observable emit 1" + "\n");
                e.onNext(1);
                Log.e(TAG, "Observable emit 2" + "\n");
                e.onNext(2);
                Log.e(TAG, "Observable emit 3" + "\n");
                e.onNext(3);
                e.onComplete();
                Log.e(TAG, "Observable emit 4" + "\n" );
                e.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {

            private Disposable mSwitch ;
            int count =0 ;
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mSwitch = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                count ++;
                if(count == 2){
                    mSwitch.dispose();
                }
                Log.e(TAG, "onNext" + integer+"\n" );
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError : value : " + e.getMessage() + "\n" );
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete" + "\n" );
            }
        });

   }

    /**
     * 实现三步曲
     */
    public void sampleOTTByFo(){
        Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<String> e) throws Exception {
                e.onNext("大家好");
                Log.e(TAG,"emit  大家好");
                e.onNext("这是一个简单的的例子");
                Log.e(TAG,"emit  这是一个简单的的例子");
                e.onNext("结束");
                Log.e(TAG,"emit  结束");
                e.onNext("大家散了吧");
                Log.e(TAG,"emit  大家散了吧");
                e.onComplete();

            }
        },BackpressureStrategy.BUFFER).subscribe(new Subscriber<String>() {
            private Subscription mSwitch;

            @Override
            public void onSubscribe(Subscription s) {
                mSwitch = s;
                mSwitch.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String s) {
                Log.e(TAG,"onNext()  "+ s);
               if(s.equals("结束")){
                   mSwitch.cancel();
               }
            }

            @Override
            public void onError(Throwable t) {
                Log.e(TAG,t.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG,"onComplete()");
            }
        });
    }

    public void sampleSchedual(){

        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.e(TAG,"subscribe currentThread : "+Thread.currentThread().getName());
                e.onNext(1);

            }
        })
        .subscribeOn(Schedulers.newThread())
        .observeOn(Schedulers.newThread())
        .map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                Log.e(TAG,"map currentThread : "+Thread.currentThread().getName());
                return integer;
            }
        }).subscribeOn(Schedulers.newThread())
        .doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG,"doOnNext currentThread : "+Thread.currentThread().getName());
            }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.e(TAG,"consumer currentThread : "+Thread.currentThread().getName());
            }
        });

    }

    /**
     * 创建Observersable
     */
    public void sampleCreate(){

        Observable<Object> obObject = Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> e) throws Exception {

            }
        });
        Observable<Integer> obInt = Observable.just(1,2,3,4,5);
        Observable<String> obString = Observable.fromArray(new String[]{"大家好","我是一个例子"});
        Observable<Long> obLong = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Integer> obInt2 = Observable.range(0,100);
        Observable<Long> obLong2 = Observable.timer(0,TimeUnit.SECONDS);

        Observable.zip(
                    Observable.just(1,2,3,4,5),
                    Observable.fromArray(new String[]{"大家好","我是一个例子"}),
                    new BiFunction<Integer, String, String>() {
                        @Override
                        public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                            return s+integer;
                        }
                    }
        ).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG,s);
            }
        });

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onError(new NullPointerException(""));
            }
        }).retry(2).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG,"onNext"+integer);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.d(TAG,"onError");
            }
        });
    }

    public void sampleOP(){
        Flowable.create(new FlowableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Object> e) throws Exception {

            }
        },BackpressureStrategy.BUFFER)
                .timeout(new Function<Object, Publisher<String>>() {
                    @Override
                    public Publisher<String > apply(@NonNull Object o) throws Exception {
                        return new Publisher<String>() {
                            @Override
                            public void subscribe(Subscriber<? super String> s) {

                            }
                        };
                    }
                }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {

            }
        });
    }

}
