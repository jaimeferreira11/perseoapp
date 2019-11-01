package py.com.ideasweb.perseo.models;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;


public class Facturadetlog extends LitePalSupport {


    //@Column(unique = true)
    //int idFacturaDet;
    String concepto;//nombre del articulo
    Double cantidad;
    Double precioVenta;
    Double tasaIva;
    Double impuesto;
    Integer idArticulo;
    // Integer idFacturaCab;
    Double subTotal;

    @Column(ignore = true)
    Articulo articulo;

    public Facturadetlog() {
    }

    public Facturadetlog(Facturadet det) {
        this.concepto = det.getConcepto();
        this.cantidad = det.getCantidad();
        this.precioVenta = det.getPrecioVenta();
        this.tasaIva = det.getTasaIva();
        this.impuesto = det.getImpuesto();
        this.idArticulo = det.getIdArticulo();
        this.subTotal = det.getSubTotal();
        this.articulo = det.getArticulo();
    }

    /*public int getIdFacturaDet() {
        return idFacturaDet;
    }

    public void setIdFacturaDet(int idFacturaDet) {
        this.idFacturaDet = idFacturaDet;
    }*/

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

    /*public Integer getIdFacturaCab() {
        return idFacturaCab;
    }

    public void setIdFacturaCab(Integer idFacturaCab) {
        this.idFacturaCab = idFacturaCab;
    }*/

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


    @Override
    public String toString() {
        return "Facturadet{" +
                "concepto='" + concepto + '\'' +
                ", cantidad=" + cantidad +
                ", precioVenta=" + precioVenta +
                ", tasaIva=" + tasaIva +
                ", impuesto=" + impuesto +
                ", idArticulo=" + idArticulo +
                ", subTotal=" + subTotal +
                ", articulo=" + articulo +
                '}';
    }
}
