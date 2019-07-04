package py.com.ideasweb.perseo.models;

import org.litepal.crud.LitePalSupport;


public class Perfil extends LitePalSupport {


    int idperfil;
    String descripcion;
    private Integer idEmpresa;


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

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
