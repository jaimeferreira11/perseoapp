package py.com.ideasweb.perseo.restApi.pojo;

import java.sql.Date;
import java.util.List;

import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.Usuario;

/**
 * Created by jaime on 09/11/17.
 */

public class PedidoCabecera {


    private Integer codPedidoCab;

    private Usuario usuario;

    private Date fechaPedido;

    private Cliente cliente;

    private Double porcDescuento;

    private Double importeTotal;

    private String estado;

    private String mensaje;

    private List<PedidoDetalle> pedidoDetalle;

    private String coordenadas;

    private TransportadoraDTO transportadora;

    /**
     * PE,PT,AN
     */


    public PedidoCabecera(String mensaje, Cliente cliente) {
        this.mensaje = mensaje;
        this.cliente = cliente;
    }

    public PedidoCabecera() {
    }

    public Integer getCodPedidoCab() {
        return codPedidoCab;
    }

    public void setCodPedidoCab(Integer codPedidoCab) {
        this.codPedidoCab = codPedidoCab;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
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

    public Double getImporteTotal() {
        return importeTotal;
    }

    public void setImporteTotal(Double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<PedidoDetalle> getPedidoDetalle() {
        return pedidoDetalle;
    }

    public void setPedidoDetalle(List<PedidoDetalle> pedidoDetalle) {
        this.pedidoDetalle = pedidoDetalle;
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }

    public TransportadoraDTO getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(TransportadoraDTO transportadora) {
        this.transportadora = transportadora;
    }

    @Override
    public String toString() {
        return "PedidoCabecera{" +
                "codPedidoCab=" + codPedidoCab +
                ", usuario=" + usuario +
                ", fechaPedido=" + fechaPedido +
                ", cliente=" + cliente +
                ", porcDescuento=" + porcDescuento +
                ", importeTotal=" + importeTotal +
                ", estado='" + estado + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", pedidoDetalle=" + pedidoDetalle +
                ", coordenadas='" + coordenadas + '\'' +
                ", transportadora=" + transportadora +
                '}';
    }
}
