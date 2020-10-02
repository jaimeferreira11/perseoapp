package py.com.ideasweb.perseo.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


public class FacturaDet extends LitePalSupport {


    private int id;
    //@Column(unique = true)
    //int idFacturaDet;
    String concepto;//nombre del articulo
    Double cantidad;
    Double precioVenta;
    Double tasaIva;
    Double impuesto;
    Integer idArticulo;
    Integer facturacab_id;
    Double subTotal;


    @Column(ignore = true)
    Articulo articulo;

    /*public int getIdFacturaDet() {
        return idFacturaDet;
    }

    public void setIdFacturaDet(int idFacturaDet) {
        this.idFacturaDet = idFacturaDet;
    }*/

    public FacturaDet() {
    }

    public FacturaDet(Facturadetlog log) {
        this.concepto = log.getConcepto();
        this.cantidad = log.getCantidad();
        this.precioVenta = log.getPrecioVenta();
        this.tasaIva = log.getTasaIva();
        this.impuesto = log.getImpuesto();
        this.idArticulo = log.getIdArticulo();
        this.subTotal = log.getSubTotal();
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Double getTasaIva() {
        return tasaIva;
    }

    public void setTasaIva(Double tasaIva) {
        this.tasaIva = tasaIva;
    }

    public Double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Double impuesto) {
        this.impuesto = impuesto;
    }

    /*public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }*/

    public Integer getFacturacab_id() {
        return facturacab_id;
    }

    public void setFacturacab_id(Integer facturacab_id) {
        this.facturacab_id = facturacab_id;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public Articulo getArticulo() {
        return articulo;
    }

    public void setArticulo(Articulo articulo) {
        this.articulo = articulo;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "FacturaDet{" +
                "id=" + id +
                ", concepto='" + concepto + '\'' +
                ", cantidad=" + cantidad +
                ", precioVenta=" + precioVenta +
                ", tasaIva=" + tasaIva +
                ", impuesto=" + impuesto +
                ", idArticulo=" + idArticulo +
                ", facturacab_id=" + facturacab_id +
                ", subTotal=" + subTotal +
                ", articulo=" + articulo +
                '}';
    }
}
