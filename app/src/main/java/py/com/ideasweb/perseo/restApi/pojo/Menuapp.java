package py.com.ideasweb.perseo.restApi.pojo;

/**
 * Created by jaime on 30/03/17.
 */

public class Menuapp {

    private static final long serialVersionUID = 1L;
    private Integer  idmenuapp ;
    private String descripcion ;
    private Integer orden ;
    private String tipo ;
    private String path ;
    private Boolean estado;
    private Integer idversion;

    public Menuapp() {
    }

    public Menuapp(Integer idmenuapp, String descripcion, Integer orden, String tipo, String path, Boolean estado) {
        this.idmenuapp = idmenuapp;
        this.descripcion = descripcion;
        this.orden = orden;
        this.tipo = tipo;
        this.path = path;
        this.estado = estado;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getIdmenuapp() {
        return idmenuapp;
    }

    public void setIdmenuapp(Integer idmenuapp) {
        this.idmenuapp = idmenuapp;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Integer getIdversion() {
        return idversion;
    }

    public void setIdversion(Integer idversion) {
        this.idversion = idversion;
    }

    @Override
    public String toString() {
        return  descripcion ;
    }
}
