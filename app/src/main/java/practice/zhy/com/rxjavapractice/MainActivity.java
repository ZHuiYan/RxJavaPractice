package practice.zhy.com.rxjavapractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import practice.zhy.com.rxjavapractice.entity.LoginRequest;
import practice.zhy.com.rxjavapractice.entity.LoginResponse;
import practice.zhy.com.rxjavapractice.entity.RegisterRequest;
import practice.zhy.com.rxjavapractice.entity.RegisterResponse;
import practice.zhy.com.rxjavapractice.netWork.Api;
import practice.zhy.com.rxjavapractice.netWork.RetrofitHelper;


public class MainActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.tvZip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ZipActivity.class);
                startActivity(intent);
            }
        });
        compositeDisposable = new CompositeDisposable();
        Observable<String> observable = Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                        Log.e("sss","ObservabeThread is:" + Thread.currentThread().getName());
                        Log.e("sss","emit1");
                        e.onNext("1");
                    }
                }

        );
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Log.e("sss","Obser Thread is:" + Thread.currentThread().getName());
                Log.e("sss","s=" + s);
            }
        };
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e("sss","onSubscribe");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e("sss","onNext:" + "s=" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e("sss","onError");
            }

            @Override
            public void onComplete() {
                Log.e("sss","onComplete");
            }
        };
        observable.subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e("sss", "After observeOn(mainThread), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.e("sss", "After observeOn(io), current thread is: " + Thread.currentThread().getName());
                    }
                })
                .subscribe(consumer);
        final Api api = RetrofitHelper.createService(Api.class);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setId(22);
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("nn");
        registerRequest.setPhone("1111");
        api.register().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(@NonNull RegisterResponse registerResponse) throws Exception {
                        Log.e("ssss","registeraccept");
                    }
                }).observeOn(Schedulers.io())//回到Io线程发送登陆请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(@NonNull RegisterResponse registerResponse) throws Exception {
                        return api.getLogin();
                    }
                }).observeOn(AndroidSchedulers.mainThread()) //回到主线程处理登陆请求
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(@NonNull LoginResponse loginResponse) throws Exception {
                        Log.e("ssss","loginaccept");
                        Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e("ssss","loginaccept失败");
                        Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                    }
                });

      /*  api.getLogin().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResponse>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull LoginResponse loginResponse) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(MainActivity.this,"登陆失败",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(MainActivity.this,"登陆成功",Toast.LENGTH_SHORT).show();
                    }
                });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
