package py.com.ideasweb.perseo.restApi.pojo;


public class Ciudad {

	Long codCiudad;

	String descripcion;
	
	public Ciudad(){}

	public Ciudad(Long codCiudad, String descripcion) {
		super();
		this.codCiudad = codCiudad;
		this.descripcion = descripcion;
	}


	public Long getCodCiudad() {
		return codCiudad;
	}

	public void setCodCiudad(Long codCiudad) {
		this.codCiudad = codCiudad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return  descripcion ;
	}
	
}
