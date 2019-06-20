package py.com.ideasweb.perseo.ui.elements;

/**
 * Created by jaime on 11/08/17.
 */

public class Errorlog {

    private Integer id;
    private String log;
    private Boolean enviado;

    public Errorlog() {
    }

    public Errorlog(String log, Boolean enviado) {
        this.log = log;
        this.enviado = enviado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }


    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }
}
