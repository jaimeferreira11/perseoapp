package py.com.ideasweb.perseo.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


public class Perfilusuario extends LitePalSupport {

    Integer idusuario;
    Integer iderfil;
    String descripcion;




    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public Integer getIderfil() {
        return iderfil;
    }

    public void setIderfil(Integer iderfil) {
        this.iderfil = iderfil;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
