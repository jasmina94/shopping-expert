package com.ftn.mdj.utils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Jasmina on 17/05/2018.
 */

public class MDJInterceptor implements Interceptor {

    public static String jwt = "";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request interceptedRequest;
        Request request = chain.request();

        System.out.println(request.method() + ": " + request.url());
        System.out.println("JWT: " + jwt);

        interceptedRequest = request.newBuilder()
                .addHeader("Authorization", jwt)
                .build();

        return chain.proceed(interceptedRequest);
    }
}
