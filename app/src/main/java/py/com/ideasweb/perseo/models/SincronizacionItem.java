package py.com.ideasweb.perseo.models;

import java.util.List;

public class SincronizacionItem {

    private String titulo;

    private Integer cantidad;

    private String tipo;

    private List<FacturaCab> facturas;

    private List<Cliente> clientes;

    private List<Articulo> articulos;

    public SincronizacionItem(String titulo, Integer cantidad, String tipo) {
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    public SincronizacionItem(String titulo, Integer cantidad, String tipo, List<FacturaCab> facturas, List<Cliente> clientes) {
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.facturas = facturas;
        this.clientes = clientes;
    }

    public SincronizacionItem(String titulo, Integer cantidad, String tipo, List<Articulo> articulos) {
        this.titulo = titulo;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.articulos = articulos;
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

    public List<FacturaCab> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<FacturaCab> facturas) {
        this.facturas = facturas;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    public List<Articulo> getArticulos() {
        return articulos;
    }

    public void setArticulos(List<Articulo> articulos) {
        this.articulos = articulos;
    }
}
