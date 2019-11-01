package py.com.ideasweb.perseo.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.LitePal;

import java.util.ArrayList;

import butterknife.Unbinder;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.SincronizacionItem;
import py.com.ideasweb.perseo.restApi.manager.ArticuloManager;
import py.com.ideasweb.perseo.restApi.manager.ClienteManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingFragment extends Fragment {

    Unbinder unbinder;
    private ArrayList<SincronizacionItem> list = new ArrayList<>();

    public LoadingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        bajarDatos();
        return  view;
    }



    private void bajarDatos() {


        UtilLogger.info("DESCARGANDO DATOS");

        //cliente
        list = new ArrayList<>();
        descargarCliente();
        Utilities.setUltSync(getContext(), null ,Utilities.getCurrentDateTime() );

    }

    public void descargarCliente(){

        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.getClientesByEmpresa();
                     //Respuesta respuesta = manager.getClientesByUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());

                    if(respuesta.getEstado() == "OK"){
                        ConstructorCliente cu = new ConstructorCliente();
                        LitePal.deleteAll(Cliente.class);
                        ArrayList<Cliente> clienteList = (ArrayList<Cliente>) respuesta.getDatos();

                        if (clienteList.size() > 0){
                            cu.insertar(clienteList);
                            list.add(new SincronizacionItem("Clientes descargados", clienteList.size(), "cliente"));
                        }


                    }
                    // articulo
                    descargarArticulos();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void descargarArticulos(){

        final ArticuloManager manager2 = new ArticuloManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager2.getByEmpresa();

                    if(respuesta.getEstado() == "OK"){
                        ConstructorArticulos cu = new ConstructorArticulos();
                        ArrayList<Articulo> articuloList = (ArrayList<Articulo>) respuesta.getDatos();

                        if (articuloList.size() > 0){
                            cu.insertar(articuloList);
                            list.add(new SincronizacionItem("Articulos descargados", articuloList.size(), "articulo"));
                        }


                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            irHome();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void irHome(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}
