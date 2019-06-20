package py.com.ideasweb.perseo.models;

import com.yahoo.squidb.annotations.ColumnSpec;
import com.yahoo.squidb.annotations.PrimaryKey;
import com.yahoo.squidb.annotations.TableModelSpec;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.sql.Date;
import java.sql.Timestamp;


public class Talonario extends LitePalSupport {

    @Column(unique = true)
    private Integer idTalonario;
    private String timbrado;
    private String validoDesde;
    private String validoHasta;
    private Integer numeroInicial;
    private Integer numeroFinal;
    private Integer establecimiento;
    private Integer puntoExpedicion;
    private Integer numeroActual;


    public Integer getIdTalonario() {
        return idTalonario;
    }

    public void setIdTalonario(Integer idTalonario) {
        this.idTalonario = idTalonario;
    }

    public String getTimbrado() {
        return timbrado;
    }

    public void setTimbrado(String timbrado) {
        this.timbrado = timbrado;
    }

    public String getValidoDesde() {
        return validoDesde;
    }

    public void setValidoDesde(String validoDesde) {
        this.validoDesde = validoDesde;
    }

    public String getValidoHasta() {
        return validoHasta;
    }

    public void setValidoHasta(String validoHasta) {
        this.validoHasta = validoHasta;
    }

    public Integer getNumeroInicial() {
        return numeroInicial;
    }

    public void setNumeroInicial(Integer numeroInicial) {
        this.numeroInicial = numeroInicial;
    }

    public Integer getNumeroFinal() {
        return numeroFinal;
    }

    public void setNumeroFinal(Integer numeroFinal) {
        this.numeroFinal = numeroFinal;
    }

    public Integer getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Integer establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Integer getPuntoExpedicion() {
        return puntoExpedicion;
    }

    public void setPuntoExpedicion(Integer puntoExpedicion) {
        this.puntoExpedicion = puntoExpedicion;
    }

    public Integer getNumeroActual() {
        return numeroActual;
    }

    public void setNumeroActual(Integer numeroActual) {
        this.numeroActual = numeroActual;
    }

    @Override
    public String toString() {
        return "Talonario{" +
                "idTalonario=" + idTalonario +
                ", timbrado='" + timbrado + '\'' +
                ", validoDesde='" + validoDesde + '\'' +
                ", validoHasta='" + validoHasta + '\'' +
                ", numeroInicial=" + numeroInicial +
                ", numeroFinal=" + numeroFinal +
                ", establecimiento=" + establecimiento +
                ", puntoExpedicion=" + puntoExpedicion +
                ", numeroActual=" + numeroActual +
                '}';
    }
}
