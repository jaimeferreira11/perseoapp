package py.com.ideasweb.perseo.restApi.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.Talonario;
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


    public Respuesta grabarfactura(FacturaCab factura) throws  Exception{

        try {

            final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
            RestApiAdapter restApiAdapter = new RestApiAdapter();
            Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

            Call<Void> tokenCall = endpoints.grabarFactura(factura);

            System.out.println("Grabando factura");
            tokenCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Respuesta respuesta = new Respuesta();
                    System.out.println("Codigo grabar " + response.code());
                    if (response.code() >= 400) {
                        respuesta.setEstado("ERROR");
                        respuesta.setError("Ha ocurrido un error");

                    }else{
                        Gson gson = new Gson();
                        System.out.println("body info grabar: " + response.body());
                        respuesta.setEstado( "OK");
                        String jsonInString = gson.toJson(response.body());
                        Type listType = new TypeToken<FacturaCab>() {}.getType();
                        //setenado en login en el credentials
                        // respuesta.setDatos((FacturaCab) gson.fromJson(jsonInString, listType));
                        //deserealizando

                    }
                    blockingQueue.add(respuesta);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    System.out.println("ERROR EN FACTURA");
                    System.out.println(t.getMessage());
                    Respuesta respuesta = new Respuesta();
                    t.printStackTrace();
                    respuesta.setEstado("Error");
                    respuesta.setError(t.getMessage());
                     blockingQueue.add(respuesta);
                }
            });

            return blockingQueue.take();

        }catch (Exception e){
            e.printStackTrace();
            throw  e;
        }


    }

    public Respuesta grabarListafactura(List<FacturaCab> facturas) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        Call<Void> tokenCall = endpoints.grabarListaFactura(facturas);

        tokenCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println("Codigo grabar " + response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");

                }else{
                    Gson gson = new Gson();
                    System.out.println("body info grabar: " + response.body());
                    respuesta.setEstado( "OK");
                    //respuesta.setDatos(response.body());
                    //String jsonInString = gson.toJson(response.body());
                    //Type listType = new TypeToken<FacturaCab>() {}.getType();
                    //setenado en login en el credentials
                    // respuesta.setDatos((FacturaCab) gson.fromJson(jsonInString, listType));
                    //deserealizando

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
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
        Call<List<FacturaCab>> tokenCall = endpoints.getFacturas();

        tokenCall.enqueue(new Callback<List<FacturaCab>>() {
            @Override
            public void onResponse(Call<List<FacturaCab>> call, Response<List<FacturaCab>> response) {
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
                    Type listType = new TypeToken<List<FacturaCab>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<FacturaCab>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<FacturaCab>> call, Throwable t) {
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Ha ocurrido un error");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }



    public Respuesta getFacturasByUsuario() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints
        Call<List<FacturaCab>> tokenCall = endpoints.getFacturasByUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());

        tokenCall.enqueue(new Callback<List<FacturaCab>>() {
            @Override
            public void onResponse(Call<List<FacturaCab>> call, Response<List<FacturaCab>> response) {
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
                    Type listType = new TypeToken<List<FacturaCab>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<FacturaCab>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<FacturaCab>> call, Throwable t) {
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
