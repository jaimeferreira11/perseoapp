package py.com.ideasweb.perseo.restApi.manager;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import okhttp3.RequestBody;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.Endpoints;
import py.com.ideasweb.perseo.restApi.adapter.RestApiAdapter;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaime on 12/04/17.
 */

public class ArticuloManager {


    public Respuesta getaAll() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints
        Call<List<Articulo>> tokenCall = endpoints.getAllArticulos();

        tokenCall.enqueue(new Callback<List<Articulo>>() {
            @Override
            public void onResponse(Call<List<Articulo>> call, Response<List<Articulo>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{

                    UtilLogger.info("ARTICULOS: " + response.body());
                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                    //deserealizando
                    // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<List<Articulo>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Articulo>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Articulo>> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta getByEmpresa() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints
        Call<List<Articulo>> tokenCall = endpoints.getArticulosByEmpresa(ConstantesRestApi.ID_EMPRESA);

        tokenCall.enqueue(new Callback<List<Articulo>>() {
            @Override
            public void onResponse(Call<List<Articulo>> call, Response<List<Articulo>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{

                    UtilLogger.info("ARTICULOS: " + response.body());
                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                    //deserealizando
                    // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<List<Articulo>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Articulo>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Articulo>> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }



    public Respuesta getArticuloByParam(final Context context, String param) throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

 /*       String jsonInString = "[{\"descripcion\":\"Remera Polo T shirt\",\"codigoArticulo\":\"5525-TG\",\"codigoEmpresa\":1,\"precioVenta\":87000,\"codigoBarraEan\":\"789887445444\",\"codigoIva\":10},{\"descripcion\":\"Remera Mujer T shirt\",\"codigoArticulo\":\"5525-TG\",\"codigoEmpresa\":1,\"precioVenta\":100000,\"codigoBarraEan\":\"989887445444\",\"codigoIva\":10},{\"descripcion\":\"Remera Home T shirt\",\"codigoArticulo\":\"5525-TG\",\"codigoEmpresa\":1,\"precioVenta\":50000,\"codigoBarraEan\":\"889887445344\",\"codigoIva\":10}]";
        Type listType = new TypeToken<List<Articulo>>() {}.getType();
        Respuesta respuesta = new Respuesta();
        respuesta.setDatos((ArrayList<Articulo>) new Gson().fromJson(jsonInString, listType));*/

        //se invoca al metodo del endpoints
        Call<List<Articulo>> tokenCall = endpoints.getArticulosByCod(param.toUpperCase());

        tokenCall.enqueue(new Callback<List<Articulo>>() {
            @Override
            public void onResponse(Call<List<Articulo>> call, Response<List<Articulo>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");



                }else{


                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson = new GsonBuilder().
                            registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {

                                @Override
                                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                                    if(src == src.longValue())
                                        return new JsonPrimitive(src.longValue());
                                    return new JsonPrimitive(src);
                                }
                            }).create();

                    //deserealizando
                   // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<List<Articulo>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Articulo>) new Gson().fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Articulo>> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }



    public Respuesta getArticuloByCodigoBarra(final Context context, String param) throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

 /*       String jsonInString = "[{\"descripcion\":\"Remera Polo T shirt\",\"codigoArticulo\":\"5525-TG\",\"codigoEmpresa\":1,\"precioVenta\":87000,\"codigoBarraEan\":\"789887445444\",\"codigoIva\":10},{\"descripcion\":\"Remera Mujer T shirt\",\"codigoArticulo\":\"5525-TG\",\"codigoEmpresa\":1,\"precioVenta\":100000,\"codigoBarraEan\":\"989887445444\",\"codigoIva\":10},{\"descripcion\":\"Remera Home T shirt\",\"codigoArticulo\":\"5525-TG\",\"codigoEmpresa\":1,\"precioVenta\":50000,\"codigoBarraEan\":\"889887445344\",\"codigoIva\":10}]";
        Type listType = new TypeToken<List<Articulo>>() {}.getType();
        Respuesta respuesta = new Respuesta();
        respuesta.setDatos((ArrayList<Articulo>) new Gson().fromJson(jsonInString, listType));*/

        //se invoca al metodo del endpoints
        Call<List<Articulo>> tokenCall = endpoints.getArticulosByCodBarra(param.toUpperCase());

        tokenCall.enqueue(new Callback<List<Articulo>>() {
            @Override
            public void onResponse(Call<List<Articulo>> call, Response<List<Articulo>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");



                }else{


                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson = new GsonBuilder().
                            registerTypeAdapter(Double.class,  new JsonSerializer<Double>() {

                                @Override
                                public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                                    if(src == src.longValue())
                                        return new JsonPrimitive(src.longValue());
                                    return new JsonPrimitive(src);
                                }
                            }).create();

                    //deserealizando
                    // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<List<Articulo>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Articulo>) new Gson().fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Articulo>> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }



    public Respuesta addArticulo(Articulo articulo) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints

        articulo.setDescripcion(articulo.getDescripcion().toUpperCase());
        Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy").create();
        String jsonCliente =  gson.toJson(articulo);

        UtilLogger.info("ARTICULO: " + jsonCliente);
        UtilLogger.info("LOGIN: " + CredentialValues.getLoginData().toString());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonCliente);


        Call<Articulo> tokenCall = endpoints.addArticulo(body);

        tokenCall.enqueue(new Callback<Articulo>() {
            @Override
            public void onResponse(Call<Articulo> call, Response<Articulo> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");

                }else{
                    Gson gson = new Gson();
                    System.out.println("body info grabar: " + response.body());

                    respuesta.setEstado("OK");
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<Articulo>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((Articulo) gson.fromJson(jsonInString, listType));

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Articulo> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                // blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }



}
