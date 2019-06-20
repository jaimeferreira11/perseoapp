package py.com.ideasweb.perseo.ui.fragments.sincronizacion;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.SincronizacionAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.SincronizacionItem;
import py.com.ideasweb.perseo.restApi.manager.ArticuloManager;
import py.com.ideasweb.perseo.restApi.manager.ClienteManager;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.descargar)
    Button descargar;

    Unbinder unbinder;
    private ArrayList<SincronizacionItem> list = new ArrayList<>();
    private static final long SPLASH_SCREEN_DELAY = 2000;


    public DownloadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        unbinder = ButterKnife.bind(this, view);


        descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bajarDatos();
            }
        });

        return view;
    }


    private void bajarDatos() {

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage("Descargando datos...")
                .build();

        dialog.show();


        UtilLogger.info("DESCARGANDO DATOS");

        //cliente
        list = new ArrayList<>();
        descargarCliente(dialog);
       Utilities.setUltSync(getContext(), null ,Utilities.getCurrentDateTime() );


    }



    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
    }


    public SincronizacionAdapter crearAdaptador(ArrayList<SincronizacionItem> taskList) {
        SincronizacionAdapter adaptador = new SincronizacionAdapter(getContext(), taskList, "download");
        return adaptador;
    }

    public void inicializarAdaptadorRV(SincronizacionAdapter adapatador) {
        recycler.setAdapter(adapatador);

    }

    public void descargarCliente(final AlertDialog dialog){

        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.getaAll();
                    // Respuesta respuesta = manager.getClientesByUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());

                    if(respuesta.getEstado() == "OK"){
                        ConstructorCliente cu = new ConstructorCliente();
                        ArrayList<Cliente> clienteList = (ArrayList<Cliente>) respuesta.getDatos();

                        if (clienteList.size() > 0){
                            cu.insertar(clienteList);
                            list.add(new SincronizacionItem("Clientes descargados", clienteList.size(), "cliente"));
                        }


                    }
                    // articulo
                    descargarArticulos(dialog);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void descargarArticulos(final AlertDialog dialog){

        final ArticuloManager manager2 = new ArticuloManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager2.getaAll();

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
                            generarLineaLayoutVertical();
                            inicializarAdaptadorRV(crearAdaptador(list));

                            dialog.dismiss();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }



}
