package practice.zhy.com.rxjavapractice.netWork;

import android.text.format.Time;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Wode9 on 2017/6/13.
 */

public class RetrofitHelper {

    public static <T> T createService(Class<T> serviceClass){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl("https://api.douban.com/v2/book/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(serviceClass);
    }
}
