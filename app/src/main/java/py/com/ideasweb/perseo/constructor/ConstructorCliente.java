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

        LitePal.deleteAll(Cliente.class, "sincronizar = ?" , "1");
    }



    public List<Cliente> obtenerClientesNuevos(){

        final List<Cliente> busqueda = LitePal.where(" sincronizar = ? " , "1" )
                .find(Cliente.class);

        UtilLogger.info("Clientes nuevos: " + busqueda.size());
        return  busqueda;
    }

    public List<Cliente> getAll(){

        return LitePal.order("nombreApellido").find(Cliente.class);
    }

    public List<Cliente> getByUsuario(Integer idUsuario){

        final List<Cliente> busqueda = LitePal.where(" idUsuario =  ?  " , String.valueOf(idUsuario))
                .find(Cliente.class);

        return  busqueda;
    }


    public void grabar(Cliente cliente){

        UtilLogger.info("Grabando el cliente..." );
            cliente.save();

    }


    public void update(Cliente cliente){

        UtilLogger.info("Actualizando el cliente..." + cliente.getId() );
        cliente.update(cliente.getId());

    }


}
