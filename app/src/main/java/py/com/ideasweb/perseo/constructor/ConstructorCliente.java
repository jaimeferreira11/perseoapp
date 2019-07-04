package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorCliente {




    public void insertar(ArrayList<Cliente> lista){
        System.out.println("Insertando clientes en el contrucutor cliente");
        int cont = 0;

        borraClientesNuevos();
        for (Cliente data: lista) {
            data.save();
            cont++;
        }
        System.out.println("Total clientes insertados: " + cont);
    }


    public void borraClientesNuevos(){

        LitePal.deleteAll(Cliente.class, "sincronizar = ?" , "false");
    }



    public List<Cliente> obtenerClientesNuevos(){

        final List<Cliente> busqueda = LitePal.where(" sincronizar = ? " , "false" )
                .find(Cliente.class);

        UtilLogger.info("Clientes nuevos: " + busqueda.size());
        return  busqueda;
    }


    public void grabar(Cliente cliente){
        cliente.save();
    }


}
