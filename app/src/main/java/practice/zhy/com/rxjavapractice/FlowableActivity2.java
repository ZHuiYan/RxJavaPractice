package practice.zhy.com.rxjavapractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class FlowableActivity2 extends AppCompatActivity {

    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable2);
        ArrayList<String> strs;
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request(128);
            }
        });

    }

    private void start() {
//        demo1();
//        demo2();
        demo3();
    }

    private void request(int n) {
        subscription.request(n);
    }

    private void demo1() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; i < 1000; i++) {
                    Log.e("flowable", "emit" + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("flowable", "onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("flowable", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("flowable", "onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("flowable", "onComplete");
                    }
                });
    }

    private void demo2() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    Log.e("flowable", "emit" + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("flowable", "onSubscribe");
                        subscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("flowable", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("flowable", "onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("flowable", "onComplete");
                    }

                });
    }

    private void demo3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                for (int i = 0;; i++) {
//                    Log.e("flowable", "emit:" + i);
                    e.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP).observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.e("flowable", "onSubscribe");
                        subscription = s;
                        s.request(128);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.e("flowable", "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e("flowable", "onError:" + t);
                    }

                    @Override
                    public void onComplete() {
                        Log.e("flowable", "onComplete");
                    }
                });
    }


}
