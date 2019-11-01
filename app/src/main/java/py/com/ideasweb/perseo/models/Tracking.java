package py.com.ideasweb.perseo.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Tracking extends LitePalSupport {

    private Integer idTrancking;
    private String coordenadas;
    private String direccion;
    private String ciudad;
    private String fechaHora;
    private Integer idusuario;
    @Column(defaultValue = "0") // false
    private boolean marcador;


    public Tracking() {
    }

    public Integer getIdTrancking() {
        return idTrancking;
    }

    public void setIdTrancking(Integer idTrancking) {
        this.idTrancking = idTrancking;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public boolean isMarcador() {
        return marcador;
    }

    public void setMarcador(boolean marcador) {
        this.marcador = marcador;
    }

    @Override
    public String toString() {
        return "Tracking{" +
                "idTrancking=" + idTrancking +
                ", coordenadas='" + coordenadas + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", fechaHora='" + fechaHora + '\'' +
                ", idusuario=" + idusuario +
                ", marcador=" + marcador +
                '}';
    }
}
