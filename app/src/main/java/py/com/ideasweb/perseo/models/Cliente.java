package py.com.ideasweb.perseo.models;


import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class Cliente extends LitePalSupport {

	private int id;
	private Integer idCliente;
	private String nombreApellido;
	private String codTipoDocumento;
	private String nroDocumento;
	private String direccion;
	private String telefono;
	private String ciudad;
	private String barrio;
	private String coordenadas;
	private Integer idEmpresa;
	private Integer idUsuario;
    @Column(defaultValue = "0") //false
    private Boolean sincronizar;



	public Cliente() {
	}

	public Cliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public Cliente(Integer idCliente, String nombreApellido) {
		this.idCliente = idCliente;
		this.nombreApellido = nombreApellido;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}

	public String getNombreApellido() {
		return nombreApellido;
	}

	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}

	public String getCodTipoDocumento() {
		return codTipoDocumento;
	}

	public void setCodTipoDocumento(String codTipoDocumento) {
		this.codTipoDocumento = codTipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getBarrio() {
		return barrio;
	}

	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public String getCoordenadas() {
		return coordenadas;
	}

	public void setCoordenadas(String coordenadas) {
		this.coordenadas = coordenadas;
	}

	public Integer getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

    public Boolean getSincronizar() {
        return sincronizar;
    }

    public void setSincronizar(Boolean sincronizar) {
        this.sincronizar = sincronizar;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}



	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Override
	public String toString() {
		return  nroDocumento + " - "+ nombreApellido ;
	}
}
