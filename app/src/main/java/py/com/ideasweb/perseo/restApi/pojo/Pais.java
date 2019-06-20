package py.com.ideasweb.perseo.restApi.pojo;


public class Pais {

	Integer codPais;
	
	String descripcion;
	
	public Pais(){}

	public Pais(Integer codPais, String descripcion) {
		super();
		this.codPais = codPais;
		this.descripcion = descripcion;
	}

	
	public Integer getCodPais() {
		return codPais;
	}

	public void setCodPais(Integer codPais) {
		this.codPais = codPais;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}