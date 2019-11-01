package py.com.ideasweb.perseo.repo;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.HomeItem;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;

public class HomeItemRepo {

    public static  List<HomeItem> lista = new ArrayList<>();

    public static List<HomeItem> getModulos(){

        lista = new ArrayList<>();



        switch (CredentialValues.getLoginData().getPerfilactual().getIdPerfil()){
            case 1:
                // administrador
                lista.add(new HomeItem(1 ,"Descargar Datos" ,"py.com.ideasweb.perseo.ui.fragments.sincronizacion.DownloadFragment" ,R.drawable.dow,3, ""));
                lista.add(new HomeItem(2 ,"Mantenimiento de usuarios" ,"py.com.ideasweb.perseo.ui.fragments.usuarios.RegistroUsuarioFragment", R.drawable.new_user ,3,"F"));
                lista.add(new HomeItem(3 ,"Mantenimiento de clientes" ,"py.com.ideasweb.perseo.ui.fragments.cliente.RegistroClienteFragment", R.drawable.new_user ,3,"F"));
                lista.add(new HomeItem(4 ,"Mantenimiento de articulos" ,"py.com.ideasweb.perseo.ui.fragments.articulos.RegistroArticuloFragment", R.drawable.new_user ,3,"F"));
                lista.add(new HomeItem(5 ,"Mantenimiento de Talonarios" ,"py.com.ideasweb.perseo.ui.fragments.talonario.TalonarioFragment", R.drawable.talonario ,3,"F"));
                lista.add(new HomeItem(3 ,"Listado de Clientes" ,"py.com.ideasweb.perseo.ui.fragments.cliente.ListClienteFragment", R.drawable.icons8_address_book_2_100 ,3,"F"));
                lista.add(new HomeItem(3 ,"Listado de Articulos" ,"py.com.ideasweb.perseo.ui.fragments.articulos.ListArticuloFragment", R.drawable.icons8_open_box_100 ,3,"F"));
                lista.add(new HomeItem(3 ,"Listado de Historico Facturas" ,"py.com.ideasweb.perseo.ui.fragments.facturas.ListLogFacturasFragment", R.drawable.faccturas ,3,"F"));
                lista.add(new HomeItem(3 ,"Restaurar Datos" ,"", R.drawable.ic_refresh_black_24dp ,4,""));


                break;
            case 2:
                //facturacion
                lista.add(new HomeItem(1 ,"Descargar Datos" ,"py.com.ideasweb.perseo.ui.fragments.sincronizacion.DownloadFragment" ,R.drawable.dow,3, ""));
                lista.add(new HomeItem(4 ,"Mantenimiento de Talonarios" ,"py.com.ideasweb.perseo.ui.fragments.talonario.TalonarioFragment", R.drawable.talonario ,3,"F"));
                lista.add(new HomeItem(3 ,"Registro de cliente" ,"py.com.ideasweb.perseo.ui.fragments.cliente.RegistroClienteFragment", R.drawable.new_user ,3,"F"));
                lista.add(new HomeItem(5 ,"Registar Factura" ,"py.com.ideasweb.perseo.ui.activities.MainStepper", R.drawable.registro_de_facturas ,1,"A"));
                lista.add(new HomeItem(6 ,"Facturas Registradas" ,"py.com.ideasweb.perseo.ui.fragments.pedidos.PedidosListFragment", R.drawable.faccturas ,3,"F"));
                lista.add(new HomeItem(2 ,"Subir Datos" ,"py.com.ideasweb.perseo.ui.fragments.sincronizacion.UploadFragment", R.drawable.up ,3,"F"));
                lista.add(new HomeItem(3 ,"Listado de Clientes" ,"py.com.ideasweb.perseo.ui.fragments.cliente.ListClienteFragment", R.drawable.icons8_address_book_2_100 ,3,"F"));
                lista.add(new HomeItem(3 ,"Listado de Articulos" ,"py.com.ideasweb.perseo.ui.fragments.articulos.ListArticuloFragment", R.drawable.icons8_open_box_100 ,3,"F"));
                //lista.add(new HomeItem(3 ,"Marcaciones" ,"py.com.ideasweb.perseo.ui.fragments.marcacion.TabMarcacionFragment", R.drawable.faccturas ,3,"F"));

                //lista.add(new HomeItem(3 ,"Tracking" ,"py.com.ideasweb.perseo.ui.fragments.MapsFragment", R.drawable.faccturas ,3,"F"));



                break;
            case 3:
                //cobranzas

                break;

            default:
                break;
        }


        return  lista;
    }


}
