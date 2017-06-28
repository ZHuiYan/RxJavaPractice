package practice.zhy.com.rxjavapractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InterruptedIOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
/**
 * 需要两个异步方法完成之后组合获得最终的值
 */
public class ZipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
            RxJavaPlugins.setErrorHandler(new Consumer() {
                @Override
                public void accept(@NonNull Object o) throws Exception {
                    Log.e("iii", "on error：" + o.toString());
                    if ( o instanceof InterruptedIOException) {
                        Log.e("iii", "on error");
                    }
                }
            });
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.e("iii", "emit 1");
                e.onNext(1);
                Thread.sleep(1000);//测试其顺序
                Log.e("iii", "emit 2");
                e.onNext(2);
                Thread.sleep(1000);
                Log.e("iii", "emit 3");
                e.onNext(3);
                Thread.sleep(1000);
                Log.e("iii", "emit 4");
                e.onNext(4);
                Thread.sleep(1000);

                Log.e("iii", "emit complete1");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.e("iii", "emit A");
                e.onNext("A");
                Thread.sleep(1000);
                Log.e("iii", "emit B");
                e.onNext("B");
                Thread.sleep(1000);
                Log.e("iii", "emit C");
                e.onNext("C");
                Thread.sleep(1000);
                Log.e("iii", "emit complete2");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io());
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                return integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("iii", "onSubscribe");
            }

            @Override
            public void onNext(@NonNull String value) {
                Log.e("iii", "onNext:" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("iii", "onError");
            }

            @Override
            public void onComplete() {
                Log.e("iii", "onComplete");
            }
        });
    }
}
