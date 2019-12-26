package py.com.ideasweb.perseo.restApi.pojo;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;

import py.com.ideasweb.perseo.models.Empresa;
import py.com.ideasweb.perseo.models.FacturaCab;
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
    private Empresa empresa;
    private static ArrayList<Menuapp> menues;
  //  private static PedidoCabecera pedido = new PedidoCabecera();

    private static FacturaCab factura = new FacturaCab();
    private static ArrayList<FacturaCab> listVentas = new ArrayList<>();


    private static ArrayList<FacturaCab> factSincronizadas = new ArrayList<>();
    private static ArrayList<FacturaCab> factPendientes = new ArrayList<>();
    private static ArrayList<FacturaCab> factAnuladas = new ArrayList<>();


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

    public static ArrayList<FacturaCab> getListVentas() {
        return listVentas;
    }

    public static void setListVentas(ArrayList<FacturaCab> listVentas) {
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


    public static FacturaCab getFactura() {
        return factura;
    }

    public static void setFactura(FacturaCab factura) {
        LoginData.factura = factura;
    }

    public static ArrayList<FacturaCab> getFactSincronizadas() {
        return factSincronizadas;
    }

    public static void setFactSincronizadas(ArrayList<FacturaCab> factSincronizadas) {
        LoginData.factSincronizadas = factSincronizadas;
    }

    public static ArrayList<FacturaCab> getFactPendientes() {
        return factPendientes;
    }

    public static void setFactPendientes(ArrayList<FacturaCab> factPendientes) {
        LoginData.factPendientes = factPendientes;
    }

    public static ArrayList<FacturaCab> getFactAnuladas() {
        return factAnuladas;
    }

    public static void setFactAnuladas(ArrayList<FacturaCab> factAnuladas) {
        LoginData.factAnuladas = factAnuladas;
    }

    public Perfil getPerfilactual() {
        return perfilactual;
    }

    public void setPerfilactual(Perfil perfilactual) {
        this.perfilactual = perfilactual;
    }


    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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
