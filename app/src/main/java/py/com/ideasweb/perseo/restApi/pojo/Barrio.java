package py.com.ideasweb.perseo.restApi.pojo;

public class Barrio {

	private String codBarrio;
	
	private String descripcion;

	public String getCodBarrio() {
		return codBarrio;
	}

	public void setCodBarrio(String codBarrio) {
		this.codBarrio = codBarrio;
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
