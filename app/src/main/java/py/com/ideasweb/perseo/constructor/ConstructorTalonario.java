package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.List;

import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.models.Talonario;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorTalonario {



    public void insertar(Talonario data){
        System.out.println("Insertando talonario");
        data.save();

        LoginData.setTalonario(data);

    }

    public void aumentar(){

        LoginData.getTalonario().setNumeroActual(LoginData.getTalonario().getNumeroActual()+ 1);
        LoginData.getTalonario().save();
    }

    public void borrar(Talonario cab){
        cab.delete();
    }


    public Talonario cargarTalonario(){

        Talonario busqueda = LitePal.findLast(Talonario.class);

        if(busqueda != null)
        LoginData.setTalonario(busqueda);

        return busqueda;

    }






}
