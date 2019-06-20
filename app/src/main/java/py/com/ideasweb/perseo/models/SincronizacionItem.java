package py.com.ideasweb.perseo.models;

import java.util.List;

public class SincronizacionItem {

    private String titulo;

    private Integer cantidad;

    private String tipo;

    private List<Facturacab> facturas;

    private List<Cliente> clientes;

    public SincronizacionItem(String titulo, Integer cantidad, String tipo) {
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public SincronizacionItem(String titulo, Integer cantidad, String tipo, List<Facturacab> facturas, List<Cliente> clientes) {
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.facturas = facturas;
        this.clientes = clientes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Facturacab> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Facturacab> facturas) {
        this.facturas = facturas;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }
}
