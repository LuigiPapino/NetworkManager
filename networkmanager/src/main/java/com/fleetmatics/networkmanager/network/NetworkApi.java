package com.fleetmatics.networkmanager.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fleetmatics.networkmanager.BuildConfig;
import com.github.aurae.retrofit2.LoganSquareConverterFactory;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by luigi.papino on 06/04/16.
 */
public class NetworkApi<API> {
    private static final String TAG = NetworkApi.class.getSimpleName();
    private static NetworkApi instance;
    private final String BASE_PATH_API;
    public Retrofit retrofit;
    private API apiService;
    private Class<API> apiClass;
    private Context context;

    private NetworkApi(@NonNull Context context, @NonNull String basePath, @NonNull Class<API> apiClass) {
        this.context = context.getApplicationContext();
        this.BASE_PATH_API = basePath;
        this.apiClass = apiClass;
        construct();
    }

    @Nullable
    public static NetworkApi getInstance() {
        return instance;
    }

    public static void init(@NonNull Context context, @NonNull String basePath, @NonNull Class apiClass) {
        if (instance == null)
            instance = new NetworkApi(context, basePath, apiClass);

    }

    public API getApiService() {
        return apiService;
    }

    private void construct() {

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);


        Interceptor requestInterceptor = chain -> {
            Log.d(TAG, "NetworkApi() called with: " + "context = [" + context + "]");

            Request request = chain.request().newBuilder()
                    //.addHeader(WsseToken.HEADER_WSSE, wsseToken.getWsseHeader())

                    .build();
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            if (BuildConfig.DEBUG)
                urlBuilder.addQueryParameter("XDEBUG_SESSION_START", "phpstorm");

            request = request.newBuilder()
                    .url(urlBuilder.build())
                    .build();
            return chain.proceed(request);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(requestInterceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_PATH_API)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        apiService = retrofit.create(apiClass);
    }


/*    public MockWebServer constructForTest(){
        mainPrefs = new MainPrefs_(this.context);
        wsseToken = new WsseToken(mainPrefs);

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);


        Interceptor requestInterceptor = chain -> {
            Log.d(TAG, "NetworkApi() called with: " + "context = [" + context + "]");

            Request request = chain.request().newBuilder()
                    .addHeader(WsseToken.HEADER_WSSE, wsseToken.getWsseHeader())

                    .build();
            HttpUrl.Builder urlBuilder = request.url().newBuilder();
            if (BuildConfig.DEBUG)
                urlBuilder.addQueryParameter("XDEBUG_SESSION_START", "phpstorm");


            request = request.newBuilder()
                    .url(urlBuilder.build())
                    .build();
            return chain.proceed(request);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .addInterceptor(requestInterceptor)
                .build();

        MockWebServer server = new MockWebServer();
        retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(LoganSquareConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        apiService = retrofit.create(APIService.class);
        return server;
    }*/

}
