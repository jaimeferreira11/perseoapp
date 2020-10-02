package py.com.ideasweb.perseo.restApi.adapter;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.Endpoints;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by root on 19/12/16.
 */

public class RestApiAdapter {

    public Endpoints establecerConexionOauth( ){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20 , TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        //  ongoing.addHeader("Accept", "application/json;versions=1");
                        ongoing.addHeader("Authorization", ConstantesRestApi.basicAuth);
                        ongoing.addHeader("Content-Type", "application/json");
                        ongoing.addHeader("Accept", "application/json");

                        return chain.proceed(ongoing.build());
                    }
                })
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return retrofit.create(Endpoints.class);
    }
    //peticiones privadas a la nube
    public Endpoints establecerConexionRest( ){
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20 , TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Authorization", "Bearer "+ CredentialValues.getAccessTokenString());
                        return chain.proceed(ongoing.build());
                    }
                })
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return retrofit.create(Endpoints.class);
    }

    //peticiones publicas a la nube
    public Endpoints establecerPublicConexionRest( ){
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(20 , TimeUnit.MINUTES)
                .readTimeout(20, TimeUnit.MINUTES)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantesRestApi.ROOT_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        return retrofit.create(Endpoints.class);
    }





}
