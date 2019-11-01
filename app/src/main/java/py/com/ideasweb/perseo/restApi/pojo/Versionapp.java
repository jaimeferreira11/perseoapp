package py.com.ideasweb.perseo.restApi.pojo;

import java.sql.Date;


public class Versionapp {

	private Integer idversion;
	private String version_name;
	private Integer version_code;
	private Date fecha_actualizacion;
	
	public Versionapp() {

	}

	public Integer getIdversion() {
		return idversion;
	}

	public void setIdversion(Integer idversion) {
		this.idversion = idversion;
	}

	public String getVersion_name() {
		return version_name;
	}

	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}

	public Integer getVersion_code() {
		return version_code;
	}

	public void setVersion_code(Integer version_code) {
		this.version_code = version_code;
	}

	public Date getFecha_actualizacion() {
		return fecha_actualizacion;
	}

	public void setFecha_actualizacion(Date fecha_actualizacion) {
		this.fecha_actualizacion = fecha_actualizacion;
	}
	
	
	
	

}
