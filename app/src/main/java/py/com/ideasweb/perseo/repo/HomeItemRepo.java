package py.com.ideasweb.perseo.repo;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.HomeItem;

public class HomeItemRepo {

    public static  List<HomeItem> lista = new ArrayList<>();
    public static  List<HomeItem> listaUser = new ArrayList<>();


    public static List<HomeItem> getListaFacturacion(){

        if(lista.size() == 0){
            lista.add(new HomeItem(1 ,"Descargar Datos" ,"py.com.ideasweb.perseo.ui.fragments.sincronizacion.DownloadFragment" ,R.drawable.dow,3, ""));
            lista.add(new HomeItem(4 ,"Mantenimiento de Talonarios" ,"py.com.ideasweb.perseo.ui.fragments.talonario.TalonarioFragment", R.drawable.talonario ,3,"F"));
            lista.add(new HomeItem(3 ,"Registro de cliente" ,"py.com.ideasweb.perseo.ui.fragments.cliente.RegistroClienteFragment", R.drawable.new_user ,3,"F"));
            lista.add(new HomeItem(5 ,"Registar Factura" ,"py.com.ideasweb.perseo.ui.activities.MainStepper", R.drawable.registro_de_facturas ,1,"A"));
            lista.add(new HomeItem(6 ,"Facturas Registradas" ,"py.com.ideasweb.perseo.ui.fragments.pedidos.PedidosListFragment", R.drawable.faccturas ,3,"F"));
            lista.add(new HomeItem(2 ,"Subir Datos" ,"py.com.ideasweb.perseo.ui.fragments.sincronizacion.UploadFragment", R.drawable.up ,3,"F"));
            // lista.add(new HomeItem(7 ,"Reimpresion de  Facturas" ,"", null ,null,"F"));


        }


        return  lista;
    }


}
