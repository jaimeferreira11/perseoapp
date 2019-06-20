package py.com.ideasweb.perseo.restApi.pojo;

public class TransportadoraDTO {

    private String codIdent;
    
    private String nroDoc;
    
    private String nombre;
    
    private String direccion;

    public TransportadoraDTO() {
    }

    public TransportadoraDTO(String codIdent, String nroDoc) {
        this.codIdent = codIdent;
        this.nroDoc = nroDoc;
    }

    public TransportadoraDTO(String codIdent, String nroDoc, String nombre) {
        this.codIdent = codIdent;
        this.nroDoc = nroDoc;
        this.nombre = nombre;
    }

    public String getCodIdent() {
        return codIdent;
    }

    public void setCodIdent(String codIdent) {
        this.codIdent = codIdent;
    }

    public String getNroDoc() {
        return nroDoc;
    }

    public void setNroDoc(String nroDoc) {
        this.nroDoc = nroDoc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }


    @Override
    public String toString() {
        return  getCodIdent()+" | "+getNroDoc() +" | "+getNombre();
    }
    
}
