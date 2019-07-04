package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorArticulos {



    public void insertar(ArrayList<Articulo> lista){
        System.out.println("Insertando articulos en el contrucutor articulo");
        int cont = 0;

        LitePal.deleteAll(Articulo.class);
        for (Articulo data: lista) {
            data.save();
            cont++;
        }
        System.out.println("Total articulos insertados: " + cont);
    }



    public List<Articulo> obtenerArticulosNuevos(){

        final List<Articulo> busqueda = LitePal.where(" sincronizar = ? " , "true" )
                .find(Articulo.class);

        UtilLogger.info("Articulos nuevos o modificados: " + busqueda.size());
        return  busqueda;
    }


    public List<Articulo> getArticuloByParam(String param){



        final List<Articulo> busqueda = LitePal.where("upper(codigoBarra) like ? or upper(descripcion) like ?" ,
                "%"+param.toUpperCase().trim()+"%", "%"+param.toUpperCase().trim()+"%")
                .find(Articulo.class);



        return  busqueda;
    }

    public List<Articulo> getArticulosByCodigoBarra(String param){



        final List<Articulo> busqueda = LitePal.where("upper(codigoBarra) like ? " ,
                "%"+param.toUpperCase().trim()+"%")
                .find(Articulo.class);



        return  busqueda;
    }


    public List<Articulo> getArticuloByParamAndCantidad(String param){



        final List<Articulo> busqueda = LitePal.where("cantidad > 0 and (upper(codigoBarra) like ? or upper(descripcion) like ?)" ,
                "%"+param.toUpperCase().trim()+"%", "%"+param.toUpperCase().trim()+"%")
                .find(Articulo.class);



        return  busqueda;
    }


    public List<Articulo> getArticulosByCodigoBarraCantidad(String param){



        final List<Articulo> busqueda = LitePal.where("cantidad > 0 and upper(codigoBarra) like ? " ,
                "%"+param.toUpperCase().trim()+"%")
                .find(Articulo.class);



        return  busqueda;
    }


    public void grabar(Articulo articulo){
        articulo.save();
    }


}
