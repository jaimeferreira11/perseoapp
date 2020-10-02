package py.com.ideasweb.perseo.restApi.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import py.com.ideasweb.perseo.models.Perfilusuario;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
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

public class UsuarioManager {

    public Respuesta getaAll() throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints
       Call<List<Usuario>> tokenCall = endpoints.getAllUsuarios();

       UtilLogger.info("Obteniendo usuarios...");

        tokenCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println("USUARIOS: " + response.code());
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
                    Type listType = new TypeToken<List<Usuario>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Usuario>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {
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
        Call<List<Usuario>> tokenCall = endpoints.getUsuariosByEmpresa(ConstantesRestApi.ID_EMPRESA);

        UtilLogger.info("Obteniendo usuarios de la empresa..." + ConstantesRestApi.ID_EMPRESA);

        tokenCall.enqueue(new Callback<List<Usuario>>() {
            @Override
            public void onResponse(Call<List<Usuario>> call, Response<List<Usuario>> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println("USUARIOS: " + response.code());
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
                    Type listType = new TypeToken<List<Usuario>>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((ArrayList<Usuario>) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<List<Usuario>> call, Throwable t) {

                System.out.println("ERROR EN USUARIO");
                System.out.println(t.getMessage());
                Respuesta respuesta = new Respuesta();
                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError(t.getMessage());
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();
    }


    public Respuesta grabarUsuario(Usuario usuario) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();


        Call<Usuario> tokenCall = endpoints.grabarUsuario(usuario);

        tokenCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
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
                    Type listType = new TypeToken<Usuario>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((Usuario) gson.fromJson(jsonInString, listType));

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
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
