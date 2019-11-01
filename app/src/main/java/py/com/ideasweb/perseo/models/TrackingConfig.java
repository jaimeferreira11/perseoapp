package py.com.ideasweb.perseo.models;

import org.litepal.crud.LitePalSupport;

public class TrackingConfig extends LitePalSupport {

    private Integer idTrackConfig;

    private Integer diaDesde;

    private Integer diaHasta;

    private String horaDesde;

    private String horaHasta;

    private Integer idEmpresa;

    private Boolean estado;


    public Integer getIdTrackConfig() {
        return idTrackConfig;
    }

    public void setIdTrackConfig(Integer idTrackConfig) {
        this.idTrackConfig = idTrackConfig;
    }

    public Integer getDiaDesde() {
        return diaDesde;
    }

    public void setDiaDesde(Integer diaDesde) {
        this.diaDesde = diaDesde;
    }

    public Integer getDiaHasta() {
        return diaHasta;
    }

    public void setDiaHasta(Integer diaHasta) {
        this.diaHasta = diaHasta;
    }

    public String getHoraDesde() {
        return horaDesde;
    }

    public void setHoraDesde(String horaDesde) {
        this.horaDesde = horaDesde;
    }

    public String getHoraHasta() {
        return horaHasta;
    }

    public void setHoraHasta(String horaHasta) {
        this.horaHasta = horaHasta;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }
}
