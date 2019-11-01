package py.com.ideasweb.perseo.restApi.pojo;


public class Departamento {

	Long codDepartamento;
	
	String descripcion;
	
	public Departamento(){}

	public Departamento(Long codDepartamento, String descripcion) {
		super();
		this.codDepartamento = codDepartamento;
		this.descripcion = descripcion;
	}


	public Long getCodDepartamento() {
		return codDepartamento;
	}

	public void setCodDepartamento(Long codDepartamento) {
		this.codDepartamento = codDepartamento;
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
