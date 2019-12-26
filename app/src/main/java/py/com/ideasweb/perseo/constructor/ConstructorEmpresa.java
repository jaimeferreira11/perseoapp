package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.List;

import py.com.ideasweb.perseo.models.Empresa;


/**
 * Created by jaime on 14/11/19
 */

public class ConstructorEmpresa {


    public void insertar(Empresa empresa){
        System.out.println("Insertando empresa ");
        LitePal.deleteAll(Empresa.class);
        empresa.save();


    }

    public Empresa getById(Integer idEmpresa){

        List<Empresa> userDB = LitePal.where("idEmpresa = ?  " , String.valueOf(idEmpresa)).find(Empresa.class);

        if (userDB.size() == 0)
            return null;

        return userDB.get(0);
    }

}
