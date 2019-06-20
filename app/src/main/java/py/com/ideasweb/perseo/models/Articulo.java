package py.com.ideasweb.perseo.models;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Articulo extends LitePalSupport {

	@Column(unique = true)
	private Integer idArticulo;
	private String codigoBarra;
	private String descripcion;
	private Double tasaIva;
	private Double precioVenta;
	private Double cantidad;
	private Integer idDeposito;
	private Integer idSucursal;
	
	public Articulo(){}

	public Integer getIdArticulo() {
		return idArticulo;
	}

	public void setIdArticulo(Integer idArticulo) {
		this.idArticulo = idArticulo;
	}

	public String getCodigoBarra() {
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getTasaIva() {
		return tasaIva;
	}

	public void setTasaIva(Double tasaIva) {
		this.tasaIva = tasaIva;
	}

	public Double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Double getCantidad() {
		return cantidad;
	}

	public void setCantidad(Double cantidad) {
		this.cantidad = cantidad;
	}

	public Integer getIdDeposito() {
		return idDeposito;
	}

	public void setIdDeposito(Integer idDeposito) {
		this.idDeposito = idDeposito;
	}

	public Integer getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(Integer idSucursal) {
		this.idSucursal = idSucursal;
	}

	@Override
	public String toString() {
		return "Articulo{" +
				"idArticulo=" + idArticulo +
				", codigoBarra='" + codigoBarra + '\'' +
				", descripcion='" + descripcion + '\'' +
				", tasaIva=" + tasaIva +
				", precioVenta=" + precioVenta +
				", cantidad=" + cantidad +
				", idDeposito=" + idDeposito +
				", idSucursal=" + idSucursal +
				'}';
	}
}
