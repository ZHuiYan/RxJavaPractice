package practice.zhy.com.rxjavapractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;

public class ZipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zip);
        Observable observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                Log.e("iii", "emit 1");
                e.onNext(1);
                Log.e("iii", "emit 2");
                e.onNext(2);
                Log.e("iii", "emit 3");
                e.onNext(3);
                Log.e("iii", "emit 4");
                e.onNext(4);
                Log.e("iii", "emit complete1");
                e.onComplete();
            }
        });
        Observable observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.e("iii", "emit A");
                e.onNext("A");
                Log.e("iii", "emit B");
                e.onNext("B");
                Log.e("iii", "emit C");
                e.onNext("C");
                Log.e("iii", "emit complete2");
                e.onComplete();
            }
        });
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
