package py.com.ideasweb.perseo.restApi.pojo;


import py.com.ideasweb.perseo.models.Articulo;

public class PedidoDetalle {

	private Integer codPedidoDet;

	private PedidoCabecera pedidoCabecera;

	private Articulo articulo;

	private Double cantidad;

	private Double codIva;

	private Double precioBruto;

	private Double precioNeto;

	private Double importeIva;

	private Double subTotal;
	


	public PedidoDetalle() {
	}

	public PedidoDetalle(Articulo articulo, Double cantidad) {
		this.articulo = articulo;
		this.cantidad = cantidad;
	}

	public Integer getCodPedidoDet() {
		return codPedidoDet;
	}

	public void setCodPedidoDet(Integer codPedidoDet) {
		this.codPedidoDet = codPedidoDet;
	}

	public PedidoCabecera getPedidoCabecera() {
		return pedidoCabecera;
	}

	public void setPedidoCabecera(PedidoCabecera pedidoCabecera) {
		this.pedidoCabecera = pedidoCabecera;
	}

	public Articulo getArticulo() {
		return articulo;
	}

	public void setArticulo(Articulo articulo) {
		this.articulo = articulo;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Double getCodIva() {
		return codIva;
	}

	public void setCodIva(Double codIva) {
		this.codIva = codIva;
	}

	public Double getPrecioBruto() {
		return precioBruto;
	}

	public void setPrecioBruto(Double precioBruto) {
		this.precioBruto = precioBruto;
	}

	public Double getPrecioNeto() {
		return precioNeto;
	}

	public void setPrecioNeto(Double precioNeto) {
		this.precioNeto = precioNeto;
	}

	public Double getImporteIva() {
		return importeIva;
	}

	public void setImporteIva(Double importeIva) {
		this.importeIva = importeIva;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}


	@Override
	public String toString() {
		return "PedidoDetalle{" +
				"codPedidoDet=" + codPedidoDet +
				", pedidoCabecera=" + pedidoCabecera +
				", articulo=" + articulo +
				", cantidad=" + cantidad +
				", codIva=" + codIva +
				", precioBruto=" + precioBruto +
				", precioNeto=" + precioNeto +
				", importeIva=" + importeIva +
				", subTotal=" + subTotal +
				'}';
	}
}
