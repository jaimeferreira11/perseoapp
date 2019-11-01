package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Perfilusuario;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.utilities.MD5Generator;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.o
 */

public class ConstructorUsuario {


    /*Login usuario*/
   public Boolean login (Usuario usuario){


       List<Usuario> userDB = LitePal.where("login like upper(?) " , usuario.getLogin()).find(Usuario.class);

       if (userDB.size() == 0)
           return false;

       for (Usuario u: userDB ) {

           if (MD5Generator.MD5(usuario.getPassword()).trim().equals(u.getPassword())) {
               UtilLogger.info("las contrasenas coinciden...");

               ConstructorPerfil cp = new ConstructorPerfil();
               u.setPerfiles(cp.getPerfilesByUsuario(u.getIdUsuario()));

               LoginData login = new LoginData();
               login.setUsuario(u);
               CredentialValues.setLoginData(login);
               return true;
           }
       }
       return false;
    }


    public void insertarUsuarios(ArrayList<Usuario> usuarios){
        System.out.println("Insertando usuarios en el contrucutor usuario");
        int cont = 0;

        LitePal.deleteAll(Usuario.class);
        LitePal.deleteAll(Perfilusuario.class);
        ConstructorPerfil cp = new ConstructorPerfil();

        for (Usuario user: usuarios) {

            user.save();

            if(user.getPerfiles() != null){
                cp.insertarPerfilByUsuario((ArrayList<Perfilusuario>) user.getPerfiles());
            }
            cont++;

        }
        System.out.println("Total usuarios insertados: " + cont);
    }


    public void insertarUsuario(Usuario usuario){
        System.out.println("Insertando usuario ");

        ConstructorPerfil cp = new ConstructorPerfil();


        if(usuario.getId() > 0){
            update(usuario);

        }else{

            usuario.save();
        }

        // actualiza los perfiles
        // no tenemos el id
        if(usuario.getPerfiles().size() > 0 ){
            cp.deleteByUsuario(usuario.getIdUsuario());
            cp.insertarPerfilByUsuario((ArrayList<Perfilusuario>) usuario.getPerfiles());
        }






    }

    public void update(Usuario user){

        System.out.println("Actualiza el usuario " + user.getLogin());
       user.update(user.getId());

    }

    public List<Usuario> getUsuarioByParam(String param){



        final List<Usuario> busqueda = LitePal.where("upper(login) like ? or upper(nombreApellido) like ?" ,
                "%"+param.toUpperCase().trim()+"%", "%"+param.toUpperCase().trim()+"%")
                .find(Usuario.class);


        ConstructorPerfil cp = new ConstructorPerfil();

        for (Usuario user: busqueda ) {

            user.setPerfiles(cp.getPerfilesByUsuario(user.getIdUsuario()));
        }


        return  busqueda;
    }

    public Usuario getById(Integer idUsuairo){

        List<Usuario> userDB = LitePal.where("idUsuario = ?  " , String.valueOf(idUsuairo)).find(Usuario.class);

        if (userDB.size() == 0)
            return null;

        return userDB.get(0);
    }

}
