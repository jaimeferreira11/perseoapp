package py.com.ideasweb.perseo.restApi.pojo;



public class CondicionVentaDTO {

	Long codCondicionVenta;
	
	String descripcion;

	public CondicionVentaDTO(){}
	
	public CondicionVentaDTO(Long codCondicionVenta, String descripcion) {
		super();
		this.codCondicionVenta = codCondicionVenta;
		this.descripcion = descripcion;
	}

	public Long getCodCondicionVenta() {
		return codCondicionVenta;
	}

	public void setCodCondicionVenta(Long codCondicionVenta) {
		this.codCondicionVenta = codCondicionVenta;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
