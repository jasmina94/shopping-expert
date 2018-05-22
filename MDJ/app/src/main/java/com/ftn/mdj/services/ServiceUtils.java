package com.ftn.mdj.services;

import android.os.Message;

import com.ftn.mdj.utils.ConnectionConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jasmina on 17/05/2018.
 */

public class ServiceUtils {



    public static OkHttpClient test() {
        MDJInterceptor interceptor = new MDJInterceptor();
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ConnectionConfig.SERVICE_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();



    public static Message getHandlerMessageFromResponse(Response r) {
        Message m = Message.obtain();
        m.obj = r.body();
        return m;
    }

    public static IUserService userService = retrofit.create(IUserService.class);
    // TODO: Add all needed service interfaces with here
}
