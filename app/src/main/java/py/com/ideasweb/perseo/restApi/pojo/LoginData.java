package py.com.ideasweb.perseo.restApi.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Talonario;
import py.com.ideasweb.perseo.models.Usuario;

/**
 * Created by jaime on 06/03/17.
 */

public class LoginData implements Serializable {

    private Long idLoginData;
    private Date fechalog;
    private Usuario usuario;
    private String tipo;
    private Perfil perfilactual;
    private static ArrayList<Menuapp> menues;
  //  private static PedidoCabecera pedido = new PedidoCabecera();

    private static Facturacab factura = new Facturacab();
    private static ArrayList<Facturacab> listVentas = new ArrayList<>();


    private static ArrayList<Facturacab> factSincronizadas = new ArrayList<>();
    private static ArrayList<Facturacab> factPendientes = new ArrayList<>();
    private static ArrayList<Facturacab> factAnuladas = new ArrayList<>();


    private static Talonario talonario;

    private static String codigoBarra = null;


    public LoginData() {
    }

   /* public static ArrayList<Ciudad> getCiudades() {
        return ciudades;
    }

    public static void setCiudades(ArrayList<Ciudad> ciudades) {
        LoginData.ciudades = ciudades;
    }

    public static ArrayList<Departamento> getDepartamentos() {
        return departamentos;
    }

    public static void setDepartamentos(ArrayList<Departamento> departamentos) {
        LoginData.departamentos = departamentos;
    }*/

    public Long getIdLoginData() {
        return idLoginData;
    }

    public void setIdLoginData(Long idLoginData) {
        this.idLoginData = idLoginData;
    }

    public Date getFechalog() {
        return fechalog;
    }

    public void setFechalog(Date fechalog) {
        this.fechalog = fechalog;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public  void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public static ArrayList<Menuapp> getMenues() {
        return menues;
    }

    public static void setMenues(ArrayList<Menuapp> menues) {
        LoginData.menues = menues;
    }

    public static ArrayList<Facturacab> getListVentas() {
        return listVentas;
    }

    public static void setListVentas(ArrayList<Facturacab> listVentas) {
        LoginData.listVentas = listVentas;
    }

    public static Talonario getTalonario() {
        return talonario;
    }

    public static void setTalonario(Talonario talonario) {
        LoginData.talonario = talonario;
    }

    public static String getCodigoBarra() {
        return codigoBarra;
    }

    public static void setCodigoBarra(String codigoBarra) {
        LoginData.codigoBarra = codigoBarra;
    }


    public static Facturacab getFactura() {
        return factura;
    }

    public static void setFactura(Facturacab factura) {
        LoginData.factura = factura;
    }

    public static ArrayList<Facturacab> getFactSincronizadas() {
        return factSincronizadas;
    }

    public static void setFactSincronizadas(ArrayList<Facturacab> factSincronizadas) {
        LoginData.factSincronizadas = factSincronizadas;
    }

    public static ArrayList<Facturacab> getFactPendientes() {
        return factPendientes;
    }

    public static void setFactPendientes(ArrayList<Facturacab> factPendientes) {
        LoginData.factPendientes = factPendientes;
    }

    public static ArrayList<Facturacab> getFactAnuladas() {
        return factAnuladas;
    }

    public static void setFactAnuladas(ArrayList<Facturacab> factAnuladas) {
        LoginData.factAnuladas = factAnuladas;
    }

    public Perfil getPerfilactual() {
        return perfilactual;
    }

    public void setPerfilactual(Perfil perfilactual) {
        this.perfilactual = perfilactual;
    }

    @Override
    public String toString() {
        return "LoginData{" +
                "idLoginData=" + idLoginData +
                ", fechalog=" + fechalog +
                ", usuario=" + usuario.toString() +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
