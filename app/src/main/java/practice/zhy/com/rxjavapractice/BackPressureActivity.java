package practice.zhy.com.rxjavapractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class BackPressureActivity extends AppCompatActivity {

    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pressure);

        Observable observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                for (int i = 0; ; i++) {
                    e.onNext(i);
                    Thread.sleep(2000);
                }
            }
        }).subscribeOn(Schedulers.io())
                // .sample(2, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread());
        disposable = observable.subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer o) throws Exception {
                Log.e("backpressure", "o=" + o);
            }
        });
    }

    static {
        RxJavaPlugins.setErrorHandler(new Consumer() {
            @Override
            public void accept(@NonNull Object o) throws Exception {
                Log.e("backpressure", "on error：" + o.toString());
                if (o instanceof InterruptedIOException) {
                    Log.e("backpressure", "on error");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();

    }
}
