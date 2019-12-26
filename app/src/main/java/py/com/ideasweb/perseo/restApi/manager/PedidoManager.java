package py.com.ideasweb.perseo.restApi.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import okhttp3.RequestBody;
import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.restApi.Endpoints;
import py.com.ideasweb.perseo.restApi.adapter.RestApiAdapter;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaime on 12/04/17.
 */

public class PedidoManager {

    public Respuesta getpedidos() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        System.out.println("GET PEDIDOS");
        //se invoca al metodo del endpoints
        Call<Respuesta> tokenCall = endpoints.getPedidos();

        tokenCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    respuesta.setEstado( response.body().getEstado());
                    respuesta.setError(gson.toJson(response.body().getError()));
                    respuesta.setDatos(gson.toJson(response.body().getDatos()));
                    System.out.println(respuesta.toString());

                    String jsonInString = gson.toJson(response.body().getDatos());
                    Type listType = new TypeToken<List<FacturaCab>>() {}.getType();
                    //setenado en login en el credentials
                    //respuesta.setDatos((ArrayList<PedidoCabecera>) new Gson().fromJson(jsonInString, listType));
                    LoginData.setListVentas((ArrayList<FacturaCab>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }

    public Respuesta getpedidosByUsuario() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        System.out.println("GET PEDIDOS");
        //se invoca al metodo del endpoints
        Call<Respuesta> tokenCall = endpoints.getPedidosByUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario().intValue());

        tokenCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                    respuesta.setEstado( response.body().getEstado());
                    respuesta.setError(gson.toJson(response.body().getError()));
                    respuesta.setDatos(gson.toJson(response.body().getDatos()));
                    System.out.println(respuesta.toString());

                    String jsonInString = gson.toJson(response.body().getDatos());
                    Type listType = new TypeToken<List<FacturaCab>>() {}.getType();
                    //setenado en login en el credentials
                    //respuesta.setDatos((ArrayList<PedidoCabecera>) new Gson().fromJson(jsonInString, listType));
                    LoginData.setListVentas((ArrayList<FacturaCab>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta addPedido() throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();


        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        String jsonCliente =  gson.toJson(LoginData.getFactura());

        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonCliente);

        System.out.println(jsonCliente);
        Call<Respuesta> tokenCall = endpoints.addPedido(body);

        tokenCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println("Codigo de respuesta "+ response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");

                }else{

                    System.out.println("body info grabar: " + response.body());
                    Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

                    respuesta.setEstado( response.body().getEstado().toString());
                    respuesta.setError(gson.toJson(response.body().getError()));

                    String jsonInString = gson.toJson(response.body().getDatos());
                    Type listType = new TypeToken<FacturaCab>() {}.getType();

                    respuesta.setDatos(gson.fromJson(jsonInString, listType));
                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                // blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta deletePedido(Integer codpedido) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        Call<Respuesta> tokenCall = endpoints.deletePedido(codpedido);

        tokenCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");

                }else{
                    Gson gson = new Gson();
                    System.out.println("body info delete: " + response.body());

                    respuesta.setEstado( response.body().getEstado().toString());
                    respuesta.setError(gson.toJson(response.body().getError()));
                    respuesta.setDatos(gson.toJson(response.body().getDatos()));

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
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
