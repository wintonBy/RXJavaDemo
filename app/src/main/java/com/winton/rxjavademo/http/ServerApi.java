package com.winton.rxjavademo.http;

import com.winton.rxjavademo.http.response.CategoryResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by winton on 2017/9/4.
 */

public interface ServerApi {

     String BASE_URL = "http://gank.io/api/";

    @GET("data/{category}/{count}/{page}")
    Observable<CategoryResponse> getCategory(@Path("category")String category, @Path("count")int count, @Path("page")int page);

}
