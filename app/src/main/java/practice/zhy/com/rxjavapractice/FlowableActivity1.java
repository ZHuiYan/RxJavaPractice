package practice.zhy.com.rxjavapractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class FlowableActivity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowable1);
        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
                Log.e("flowable","emit 1");
                e.onNext(1);
                Log.e("flowable","emit 2");
                e.onNext(2);
                Log.e("flowable","emit 3");
                e.onNext(3);
                Log.e("flowable","emit complete");
                e.onComplete();

            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        Subscriber<Integer> downstream = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.e("flowable","subscribe");
//                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                Log.e("flowable","onnext:" + integer);
            }

            @Override
            public void onError(Throwable t) {
                Log.e("flowable","onError:" + t);
            }

            @Override
            public void onComplete() {
                Log.e("flowable","onComplete");
            }
        };
        upstream.subscribe(downstream);

    }
}
