package py.com.ideasweb.perseo.models;

public class HomeItem {

    private Integer idHomeItem;
    private String texto;
    private String url;
    private Integer imagen;
    private Integer accion;
    private String tipo;

    public HomeItem() {
    }

    public HomeItem(String texto) {
        this.texto = texto;
    }


    public HomeItem(Integer idHomeItem, String texto, String url, Integer imagen, Integer accion, String tipo) {
        this.idHomeItem = idHomeItem;
        this.texto = texto;
        this.url = url;
        this.imagen = imagen;
        this.accion = accion;
        this.tipo = tipo;
    }

    public Integer getIdHomeItem() {
        return idHomeItem;
    }

    public void setIdHomeItem(Integer idHomeItem) {
        this.idHomeItem = idHomeItem;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getImagen() {
        return imagen;
    }

    public void setImagen(Integer imagen) {
        this.imagen = imagen;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getAccion() {
        return accion;
    }

    public void setAccion(Integer accion) {
        this.accion = accion;
    }
}

