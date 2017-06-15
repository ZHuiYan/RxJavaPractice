package practice.zhy.com.rxjavapractice.netWork;

import io.reactivex.Observable;
import practice.zhy.com.rxjavapractice.entity.LoginRequest;
import practice.zhy.com.rxjavapractice.entity.LoginResponse;
import practice.zhy.com.rxjavapractice.entity.RegisterRequest;
import practice.zhy.com.rxjavapractice.entity.RegisterResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;

/**
 * Created by Wode9 on 2017/6/12.
 */

public interface Api {
    @GET("1220562")
    Observable<LoginResponse> getLogin();

    @GET("1220562")
    Observable<RegisterResponse> register();
}
