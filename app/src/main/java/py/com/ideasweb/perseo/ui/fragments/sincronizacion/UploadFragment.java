package py.com.ideasweb.perseo.ui.fragments.sincronizacion;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.SincronizacionAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.constructor.ConstructorFacturaLog;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.SincronizacionItem;
import py.com.ideasweb.perseo.restApi.manager.ArticuloManager;
import py.com.ideasweb.perseo.restApi.manager.ClienteManager;
import py.com.ideasweb.perseo.restApi.manager.FacturaManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView tasks;

    @BindView(R.id.sincronizar)
    Button sincronizar;

    Unbinder unbinder;
    AlertDialog dialog;
    boolean error = false;


    private ArrayList<SincronizacionItem> list = new ArrayList<>();


    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        unbinder = ButterKnife.bind(this, view);


        //armar el reciclerview
        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador(obtenerLista()));

        // Constructores
        ConstructorCliente cc =  new ConstructorCliente();

        List<Facturacab> allSongs = LitePal.findAll(Facturacab.class);

        System.out.println("Todas Facturas " + allSongs.size());




        sincronizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                error = false;
                dialog = new SpotsDialog.Builder()
                        .setContext(view.getContext())
                        .setTheme(R.style.spots)
                        .setMessage("Subiendo datos...")
                        .build();



                for (SincronizacionItem item: list) {

                    if(item.getTipo().equals("cliente")){
                        /*for (Cliente cliente: item.getClientes()) {
                            // subir
                            subirCliente(cliente);
                        }*/
                        subirListaCliente(item.getClientes());

                    }else if(item.getTipo().equals("venta")){
                        /*for (Facturacab facturacab: item.getFacturas()) {
                            // subir
                            subirFactura(facturacab);
                        }*/
                        subirListaFactura(item.getFacturas());

                    }
                    /*else if(item.getTipo().equals("articulo")){
                        for (Articulo articulo: item.getArticulos()) {
                            // subir
                            subirArticulo(articulo);
                        }
                        list.remove(item);
                    }*/

                }

                //dialog.dismiss();

               /* if(error){
                    new MaterialDialog.Builder(view.getContext())
                            .icon(getResources().getDrawable(R.drawable.ic_error_black_24dp))
                            .title(getResources().getString(R.string.error))
                            .content("Intentelo mas tarde")
                            .show();
                }else{
                    new MaterialDialog.Builder(view.getContext())
                            .icon(getResources().getDrawable(R.drawable.checked_48))
                            .title(getResources().getString(R.string.procesoExitoso))
                            .content("Se sincronizaron los datos!")
                            .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                            .positiveText("Aceptar")
                            .show();

                }*/


                //bajarDatos();


            }
        });


        return view;
    }

    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tasks.setLayoutManager(llm);
    }


    public SincronizacionAdapter crearAdaptador(ArrayList<SincronizacionItem> taskList) {
        SincronizacionAdapter adaptador = new SincronizacionAdapter(getContext(), taskList, "upload");
        return adaptador;
    }

    public void inicializarAdaptadorRV(SincronizacionAdapter adapatador) {
        tasks.setAdapter(adapatador);

    }

    private ArrayList<SincronizacionItem>  obtenerLista(){

        ConstructorCliente cc = new ConstructorCliente();
        List<Cliente> clienteList = cc.obtenerClientesNuevos();

        if (clienteList.size() > 0)
            list.add(new SincronizacionItem("Clientes", clienteList.size(), "cliente" , null ,clienteList));

        ConstructorFactura cf = new ConstructorFactura();
        //List<Facturacab> facturaList = LitePal.where("sincronizadocore = ? " , "0").find(Facturacab.class);
        List<Facturacab> facturaList = cf.getPendientes();

        if (facturaList.size() > 0)
            list.add(new SincronizacionItem("Factura de ventas", facturaList.size(), "venta", facturaList, null));


       /* ConstructorArticulos ca = new ConstructorArticulos();
        List<Articulo> articuloList = ca.obtenerArticulosNuevos();

        if(articuloList.size() > 0)
            list.add(new SincronizacionItem("Articulos", articuloList.size(), "articulo", articuloList));*/

        UtilLogger.info("Lista para sincronizar: " + list.size());

        return list;
    }


    private void subirCliente(final Cliente cliente){

        final ConstructorCliente cc = new ConstructorCliente();
        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.addCliente(cliente);

                    if(respuesta.getEstado() == "OK"){
                        cliente.delete();
                        cc.grabar((Cliente) respuesta.getDatos());
                    }else{
                        error = true;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.ic_error_black_24dp))
                                        .title(getResources().getString(R.string.error))
                                        .content("No se sincronizo los clientes")
                                        .show();
                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void subirFactura(final Facturacab factura){

        final ConstructorFactura cf = new ConstructorFactura();
        final ConstructorFacturaLog clog = new ConstructorFacturaLog();
        final FacturaManager manager2 = new FacturaManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager2.grabarfactura(factura);

                    System.out.println(respuesta.toString());
                    if(respuesta.getEstado() == "OK"){
                        // inserta en el log
                        //clog.insertar(factura);
                        // borra el original
                        //cf.actualizar(factura);
                    }else{

                        error = true;

                        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.ic_error_black_24dp))
                                        .title(getResources().getString(R.string.error))
                                        .content("No se sincronizo las facturas")
                                        .positiveText("Aceptar")
                                        .show();
                            }
                        });


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }


    private void subirListaFactura(final List<Facturacab> facturas){

        if(!dialog.isShowing())
            dialog.show();


        final ConstructorFactura cf = new ConstructorFactura();
        final ConstructorFacturaLog clog = new ConstructorFacturaLog();
        final FacturaManager manager2 = new FacturaManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Grabando desde el fragment");
                    final Respuesta respuesta = manager2.grabarListafactura(facturas);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    });

                    System.out.println(respuesta.toString());
                    if(respuesta.getEstado() == "OK"){
                        // inserta en el log
                        for (Facturacab factura: facturas) {
                            System.out.println("Grabando la factura: " + factura.toString());
                            factura.setSincronizadoCore(true);
                            // actualiza la factura
                            cf.actualizar(factura);
                            clog.insertar(factura);

                        }

                        list.remove(facturas);
                        Utilities.setUltSync(getContext(), Utilities.getCurrentDateTime(), null);
                        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.checked_48))
                                        .title(getResources().getString(R.string.procesoExitoso))
                                        .content("Se sincronizo " + facturas.size() + " facturas")
                                        .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                                        .positiveText("Aceptar")
                                        .show();
                            }
                        });


                    }else{

                        error = true;

                        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.ic_error_black_24dp))
                                        .title(getResources().getString(R.string.error))
                                        .content("No se sincronizo las facturas")
                                        .positiveText("Aceptar")
                                        .show();
                            }
                        });


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }


    private void subirListaCliente(final List<Cliente> clientes){

        if(!dialog.isShowing())
            dialog.show();


        final ConstructorCliente cc = new ConstructorCliente();
        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    final Respuesta respuesta = manager.grabarListaClientes(clientes);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if(dialog.isShowing())
                                dialog.dismiss();
                        }
                    });
                    if(respuesta.getEstado() == "OK"){

                        for (Cliente cliente: clientes) {
                            cliente.delete();
                            // subir
                            // ?
                        }

                        list.remove(clientes);
                        Utilities.setUltSync(getContext(), Utilities.getCurrentDateTime(), null);
                        ((MainActivity)getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.checked_48))
                                        .title(getResources().getString(R.string.procesoExitoso))
                                        .content(respuesta.getDatos().toString())
                                        .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                                        .positiveText("Aceptar")
                                        .show();
                            }
                        });

                    }else{
                        error = true;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.ic_error_black_24dp))
                                        .title(getResources().getString(R.string.error))
                                        .content("No se sincronizo los clientes")
                                        .show();
                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

   /* private void subirArticulo(final Articulo articulo){

        final ConstructorArticulos cc = new ConstructorArticulos();
        final ArticuloManager manager = new ArticuloManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.addArticulo(articulo);

                    if(respuesta.getEstado() == "OK"){
                        articulo.delete();
                        cc.grabar((Articulo) respuesta.getDatos());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }*/



    private void bajarDatos() {

        /*final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage("Descargando los nuevos datos...")
                .build();

        dialog.show();*/


        UtilLogger.info("DESCARGANDO DATOS");

        for (SincronizacionItem item: list) {

            if(item.getTipo().equals("cliente")){
                descargarCliente();;
            }else if(item.getTipo().equals("articulo")){
                descargarArticulos();
            }

        }

        //cliente


        list = new ArrayList<>();
        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador(list));

        Utilities.setUltSync(getContext(), null ,Utilities.getCurrentDateTime() );

       // dialog.dismiss();


    }


    public void descargarCliente(){

        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.getClientesByEmpresa();
                    // Respuesta respuesta = manager.getClientesByUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());

                    if(respuesta.getEstado() == "OK"){
                        ConstructorCliente cu = new ConstructorCliente();
                        ArrayList<Cliente> clienteList = (ArrayList<Cliente>) respuesta.getDatos();

                        if (clienteList.size() > 0){
                            cu.insertar(clienteList);
                        }


                    }

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
                        ArrayList<Articulo> list = (ArrayList<Articulo>) respuesta.getDatos();

                        if (list.size() > 0){
                            cu.insertar(list);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    @Override
    public void onStop() {
        super.onStop();
        //bajarDatos();
    }
}
