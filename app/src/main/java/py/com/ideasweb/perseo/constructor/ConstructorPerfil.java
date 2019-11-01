package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Perfilusuario;

public class ConstructorPerfil {

    public static List<Perfil> perfiles = new ArrayList<>();


    public List<Perfil> getPerfilesDisponibles(){

        if(perfiles.size() == 0){
            perfiles.add(new Perfil(1, "ADMINISTRADOR"));
            perfiles.add(new Perfil(2, "FACTURACION"));
            perfiles.add(new Perfil(3, "COBRANZA"));
        }

        return perfiles;
    }
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
        System.out.println("Insertando perfiles por usuario en el constructor perfil");
        int cont = 0;


        for (Perfilusuario data: lista) {
            data.setDescripcion(data.getPerfil().getDescripcion());
            data.setIdperfil(data.getPerfil().getIdPerfil());
            data.save();
            cont++;
        }
        System.out.println("Total perfiles por usuario insertados: " + cont);
    }

    public List<Perfilusuario> getPerfilesByUsuario(Integer idUsuario){


        List<Perfilusuario> busqueda = LitePal.where("idusuario = ?" ,
                String.valueOf(idUsuario))
                .find(Perfilusuario.class);

        if(busqueda == null)
            busqueda= new ArrayList<>();

        return busqueda;

    }

    public void deleteByUsuario(Integer idusuario){

        LitePal.deleteAll(Perfilusuario.class, "idusuario = ?" , String.valueOf(idusuario));
    }
}
