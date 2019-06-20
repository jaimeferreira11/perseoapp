package py.com.ideasweb.perseo.models;

import org.litepal.crud.LitePalSupport;


public class Perfil extends LitePalSupport {


    int idperfil;
    String descripcion;


    public int getIdperfil() {
        return idperfil;
    }

    public void setIdperfil(int idperfil) {
        this.idperfil = idperfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
