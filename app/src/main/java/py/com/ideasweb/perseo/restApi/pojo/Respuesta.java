package py.com.ideasweb.perseo.restApi.pojo;

public class Respuesta {

	String estado;
	String error;
	Object datos;

	public Respuesta() {
	}

	public Respuesta(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Object getDatos() {
		return datos;
	}

	public void setDatos(Object datos) {
		this.datos = datos;
	}

	@Override
	public String toString() {
		return "Respuesta{" +
				"estado='" + estado + '\'' +
				", error='" + error + '\'' +
				", datos=" + datos +
				'}';
	}
}
