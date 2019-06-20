package py.com.ideasweb.perseo.restApi.manager;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


import py.com.ideasweb.perseo.restApi.Endpoints;
import py.com.ideasweb.perseo.restApi.adapter.RestApiAdapter;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.models.Usuario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jaime on 31/03/17.
 */

public class LoginManager {


    public Respuesta login(Usuario usuario) throws Exception{
        return getAccessToken(usuario).take();
    }



    public BlockingQueue<Respuesta> getAccessToken(final Usuario user) throws Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionOauth();
        //se invoca al metodo del endpoints

        Call<Respuesta> tokenCall = endpoints.loginOauthToken(user.getLogin(), user.getPassword());

        tokenCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                Respuesta respuesta = new Respuesta();
                System.out.println("Codigo de respuesta del login: " + response.code());
                System.out.println("Body login: " + response.body());



                if (response.code() >= 400) {
                    respuesta.setError("El codigo del error es: " + response.code() );
                    respuesta.setEstado("ERROR");

                }else{

                    respuesta.setEstado(response.body().getEstado());
                    respuesta.setDatos(response.body().getDatos());
                    respuesta.setError(response.body().getError());

                    Gson gson = new Gson();
                    String jsonInString = gson.toJson(response.body().getDatos());
                    Type listType = new TypeToken<LoginData>() {}.getType();

                    //setenado en login en el credentials
                    LoginData u = (LoginData) new GsonBuilder().setDateFormat("dd/MM/yyyy").create().fromJson(jsonInString, listType);
                    CredentialValues.setLoginData(u);

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Respuesta respuesta = new Respuesta();

                t.printStackTrace();
                respuesta.setEstado("Error");
                respuesta.setError("Servicio no disponible.. Intentelo mas tarde!");
                blockingQueue.add(respuesta);
            }
        });


        return blockingQueue;

    }

    public Respuesta logOut(Usuario usuario) throws  Exception{

        final BlockingQueue<Respuesta> blockingQueue = new ArrayBlockingQueue<>(1);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        Endpoints endpoints = restApiAdapter.establecerConexionOauth();
        //se invoca al metodo del endpoints
        System.out.println("Deslogueando....");
        Call<Respuesta> tokenCall = endpoints.revokeOauthToken(Long.valueOf(usuario.getLogin()));

        tokenCall.enqueue(new Callback<Respuesta>() {
            @Override
            public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
                System.out.println("Respuesta del logout: " + response.code());
                Respuesta respuesta = new Respuesta();
                System.out.println(response.code());
                if (response.code() >= 400) {
                    respuesta.setEstado("ERROR");
                    respuesta.setDatos("Ocurrio un error al desloguear");

                }else{

                    //CredentialValues.setAccessTokenString("error");
                    respuesta.setEstado("OK");
                    respuesta.setDatos("Haz cerrado sesion exitosamente");

                }
                blockingQueue.add(respuesta);
            }

            @Override
            public void onFailure(Call<Respuesta> call, Throwable t) {
                Log.d("ERROR " , t.getMessage().toString());
                t.printStackTrace();
                Respuesta respuesta = new Respuesta();
                respuesta.setEstado("ERROR");
                respuesta.setDatos("Ocurrio un error al desloguear");
                blockingQueue.add(respuesta);
            }
        });

        return blockingQueue.take();

    }


}
