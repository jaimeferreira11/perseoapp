package py.com.ideasweb.perseo.models;

import org.litepal.crud.LitePalSupport;


public class Perfil extends LitePalSupport {


    int idPerfil;
    String descripcion;


    public Perfil() {
    }

    public Perfil(int idperfil, String descripcion) {
        this.idPerfil = idperfil;
        this.descripcion = descripcion;
    }

    public int getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return  getIdPerfil()+"-"+getDescripcion();
    }

}
