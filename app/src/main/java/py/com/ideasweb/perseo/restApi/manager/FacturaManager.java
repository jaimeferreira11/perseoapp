package py.com.ideasweb.perseo.restApi.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.Talonario;
import py.com.ideasweb.perseo.restApi.Endpoints;
import py.com.ideasweb.perseo.restApi.adapter.RestApiAdapter;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaime on 12/04/17.
 */

public class FacturaManager {

    public Respuesta getTalonario() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRest();

        //se invoca al metodo del endpoints
       Call<Talonario> tokenCall = endpoints.getTalonario();

        tokenCall.enqueue(new Callback<Talonario>() {
            @Override
            public void onResponse(Call<Talonario> call, Response<Talonario> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{

                    UtilLogger.info("USUARIOS: " + response.body());
                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                    //deserealizando
                   // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<Talonario>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((Talonario) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Talonario> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta grabarTalonario(Talonario talonario) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRest();

        Call<Talonario> tokenCall = endpoints.grabarTalonario(talonario);

        tokenCall.enqueue(new Callback<Talonario>() {
            @Override
            public void onResponse(Call<Talonario> call, Response<Talonario> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");

                }else{
                    Gson gson = new Gson();
                    System.out.println("body info grabar: " + response.body());
                    respuesta.setEstado( "OK");
                    respuesta.setDatos(gson.toJson(response.body()));
                    //deserealizando

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Talonario> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                // blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta grabarfactura(Facturacab factura) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionRest();

        Call<Facturacab> tokenCall = endpoints.grabarFactura(factura);

        tokenCall.enqueue(new Callback<Facturacab>() {
            @Override
            public void onResponse(Call<Facturacab> call, Response<Facturacab> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");

                }else{
                    Gson gson = new Gson();
                    System.out.println("body info grabar: " + response.body());
                    respuesta.setEstado( "OK");
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<Facturacab>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((Facturacab) gson.fromJson(jsonInString, listType));
                    //deserealizando

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Facturacab> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                // blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta getFacturas() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints
        Call<List<Facturacab>> tokenCall = endpoints.getFacturas();

        tokenCall.enqueue(new Callback<List<Facturacab>>() {
            @Override
            public void onResponse(Call<List<Facturacab>> call, Response<List<Facturacab>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{

                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                    //deserealizando
                    // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<List<Facturacab>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Facturacab>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Facturacab>> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }



}
