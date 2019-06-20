package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Perfilusuario;

public class ConstructorPerfil {

    public void insertar(ArrayList<Perfil> lista){
        System.out.println("Insertando perfiles en el contrucutor perfil");
        int cont = 0;

        LitePal.deleteAll(Perfil.class);
        for (Perfil data: lista) {
            data.save();
            cont++;
        }
        System.out.println("Total perfiles insertados: " + cont);
    }

    public void insertarPerfilByUsuario(ArrayList<Perfilusuario> lista){
        System.out.println("Insertando perfiles por usuario en el contrucutor perfil");
        int cont = 0;


        for (Perfilusuario data: lista) {
            data.save();
            cont++;
        }
        System.out.println("Total perfiles por usuario insertados: " + cont);
    }

    public List<Perfilusuario> getPerfilesByUsuario(Integer idUsuario){


        final List<Perfilusuario> busqueda = LitePal.where("idusuario = ?" ,
                String.valueOf(idUsuario))
                .find(Perfilusuario.class);
        return busqueda;

    }
}
