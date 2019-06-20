package py.com.ideasweb.perseo.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Barrio extends LitePalSupport {


    @Column(unique = true)
    private int idbarrio;

    private String descripcion;


    public int getIdbarrio() {
        return idbarrio;
    }

    public void setIdbarrio(int idbarrio) {
        this.idbarrio = idbarrio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
