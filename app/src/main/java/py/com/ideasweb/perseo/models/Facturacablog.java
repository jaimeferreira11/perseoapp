package py.com.ideasweb.perseo.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class Facturacablog extends LitePalSupport {


    private int id;

    private int idFacturaCab;
    private String fecha;
    private Integer idCliente;

    private Integer idUsuario;
    private String nombreCliente;
    private String nroDocumentoCliente;
    private String direccionCliente;
    private String telefonoCliente;
    private String timbrado;
    private Integer establecimiento;//001
    private Integer puntoExpedicion;//001
    private Integer numeroFactura;//0000001
    private String tipoFactura;//CONTADO-CREDITO
    private Double importe;
    private Boolean estado;

    private List<Facturadetlog> facturadet;

    @Column(ignore = true)
    private Cliente cliente;

    @Column(ignore = true)
    private Usuario usuario;

    private Double porcDescuento;
    private String comentario;
    private Integer idEmpresa;


    public Facturacablog(FacturaCab cab) {
        this.idFacturaCab = cab.getIdFacturaCab();
        this.fecha = cab.getFecha();
        this.idCliente = cab.getIdCliente();
        this.idUsuario = cab.getIdUsuario();
        this.nombreCliente = cab.getNombreCliente();
        this.nroDocumentoCliente = cab.getNroDocumentoCliente();
        this.direccionCliente = cab.getDireccionCliente();
        this.telefonoCliente = cab.getTelefonoCliente();
        this.timbrado = cab.getTimbrado();
        this.establecimiento = cab.getEstablecimiento();
        this.puntoExpedicion = cab.getPuntoExpedicion();
        this.numeroFactura = cab.getNumeroFactura();
        this.tipoFactura = cab.getTipoFactura();
        this.importe = cab.getImporte();
        this.estado = cab.getEstado();
        this.facturadet = new ArrayList<>();
        this.cliente = cab.getCliente();
        this.usuario = cab.getUsuario();
        this.porcDescuento = cab.getPorcDescuento();
        this.idEmpresa = cab.getIdEmpresa();
        this.comentario = cab.getComentario();
    }



    public int getIdFacturaCab() {
        return idFacturaCab;
    }

    public void setIdFacturaCab(int idFacturaCab) {
        this.idFacturaCab = idFacturaCab;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNroDocumentoCliente() {
        return nroDocumentoCliente;
    }

    public void setNroDocumentoCliente(String nroDocumentoCliente) {
        this.nroDocumentoCliente = nroDocumentoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public Integer getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Integer establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Integer getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(Integer puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public Integer getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(Integer numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public String getTipoFactura() {
        return tipoFactura;
    }

    public void setTipoFactura(String tipoFactura) {
        this.tipoFactura = tipoFactura;
    }

    public Double getImporte() {
        return importe;
    }

    public void setImporte(Double importe) {
        this.importe = importe;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public List<Facturadetlog> getFacturadet() {
        return facturadet;
    }

    public void setFacturadet(List<Facturadetlog> facturadet) {
        this.facturadet = facturadet;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getPorcDescuento() {
        return porcDescuento;
    }

    public void setPorcDescuento(Double porcDescuento) {
        this.porcDescuento = porcDescuento;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public String toString() {
        return "FacturaCab{" +
                "id=" + id +
                ", idFacturaCab=" + idFacturaCab +
                ", fecha='" + fecha + '\'' +
                ", idCliente=" + idCliente +
                ", idUsuario=" + idUsuario +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", nroDocumentoCliente='" + nroDocumentoCliente + '\'' +
                ", direccionCliente='" + direccionCliente + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", timbrado='" + timbrado + '\'' +
                ", establecimiento=" + establecimiento +
                ", puntoExpedicion=" + puntoExpedicion +
                ", numeroFactura=" + numeroFactura +
                ", tipoFactura='" + tipoFactura + '\'' +
                ", importe=" + importe +
                ", estado=" + estado +
                ", facturadet=" + facturadet +
                ", cliente=" + cliente +
                ", usuario=" + usuario +
                ", porcDescuento=" + porcDescuento +
                ", comentario='" + comentario + '\'' +
                '}';
    }
}
