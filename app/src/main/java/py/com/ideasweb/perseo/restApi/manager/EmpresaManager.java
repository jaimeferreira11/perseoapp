package py.com.ideasweb.perseo.restApi.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import py.com.ideasweb.perseo.models.Empresa;
import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.restApi.Endpoints;
import py.com.ideasweb.perseo.restApi.adapter.RestApiAdapter;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaime on 14/11/19.
 */

public class EmpresaManager {


    public Respuesta getById(Integer idEmpresa) throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerPublicConexionRest();

        //se invoca al metodo del endpoints
        Call<Empresa> tokenCall = endpoints.getEmpresaById(idEmpresa);

        tokenCall.enqueue(new Callback<Empresa>() {
            @Override
            public void onResponse(Call<Empresa> call, Response<Empresa> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setError("Ha ocurrido un error");


                }else{

                    UtilLogger.info("EMPRESA: " + response.body());
                    respuesta.setEstado("OK");
                    //deserealizando
                    Gson gson =  new GsonBuilder().setDateFormat("dd/MM/yyyy").create();

                    //deserealizando
                    // Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body());
                    Type listType = new TypeToken<Empresa>() {}.getType();
                    //setenado en login en el credentials
                    respuesta.setDatos((Empresa) gson.fromJson(jsonInString, listType));


                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Empresa> call, Throwable t) {
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
