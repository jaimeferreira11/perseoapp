package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.List;

import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.models.Facturacablog;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorFactura {



    public void insertarNuevaFactura(FacturaCab cab){
        System.out.println("Insertando nueva factura " + cab.toString());
        try {


            cab.save();
            System.out.println("CANTIDAD DE DETALLES: " + cab.getFacturadet().size());
            for (FacturaDet det: cab.getFacturadet() ) {
                det.save();
            }


            // aumentar el talonario
            ConstructorTalonario ct = new ConstructorTalonario();
            ct.aumentar();
        }catch (Exception e){

            e.printStackTrace();
            throw  e;
        }

    }

    public void insertar(FacturaCab cab){
        System.out.println("Insertando factura");
        try {
            cab.save();
            System.out.println("CANTIDAD DE DETALLES: " + cab.getFacturadet().size());
            for (FacturaDet det: cab.getFacturadet() ) {

                det.save();
            }

        }catch (Exception e){

            e.printStackTrace();
            throw  e;
        }

    }

    public void actualizar(FacturaCab cab){


        System.out.println("Actualizando la factura: " + cab.toString());
        cab.update(cab.getId());

        System.out.println("Actualizado factura: " + cab.toString());


    }


    public List<FacturaCab> getPendientes(){



        final List<FacturaCab> busqueda = LitePal.where(" sincronizadocore = ? and estado = ? and idUsuario = ?" ,
                "0" , "1" , String.valueOf(CredentialValues.getLoginData().getUsuario().getIdUsuario()))
                .find(FacturaCab.class);

        for (FacturaCab cab: busqueda ) {
            List<FacturaDet> detalles = LitePal.where(" facturacab_id = ? "  , String.valueOf(cab.getId()))
                    .find(FacturaDet.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas pendientes: " + busqueda.size());
        return  busqueda;
    }

    public List<FacturaCab> getAnulados(){

        final List<FacturaCab> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?  and idUsuario = ?",
                "0", "0",String.valueOf(CredentialValues.getLoginData().getUsuario().getIdUsuario()) )
                .find(FacturaCab.class);

        for (FacturaCab cab: busqueda ) {
            List<FacturaDet> detalles = LitePal.where(" facturacab_id = ? "  , String.valueOf(cab.getId()))
                    .find(FacturaDet.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas anulados: " + busqueda.size());
        return  busqueda;
    }

    public List<FacturaCab> getSincronizados(){

        final List<FacturaCab> busqueda = LitePal.where(" sincronizadocore = ? and idUsuario = ?" ,
                "1" , String.valueOf(CredentialValues.getLoginData().getUsuario().getIdUsuario()) )
                .find(FacturaCab.class);

        for (FacturaCab cab: busqueda ) {
            List<FacturaDet> detalles = LitePal.where(" facturacab_id = ? "  , String.valueOf(cab.getId()))
                    .find(FacturaDet.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas sincronizados: " + busqueda.size());
        return  busqueda;
    }


    public void borrarFactura(FacturaCab cab){

        System.out.println("Borrando la factura " + cab.getId());
        for (FacturaDet det: cab.getFacturadet() ) {
            LitePal.delete(FacturaDet.class, det.getId());
        }

        LitePal.delete(FacturaCab.class, cab.getId());


    }



    public void borrarFacturasSincronizadas() {
        ConstructorFactura cf = new ConstructorFactura();
        List<FacturaCab> lista = cf.getSincronizados();

        for (FacturaCab cab : lista) {
            cf.borrarFactura(cab);
        }
        LitePal.deleteAll(Facturacablog.class);
        LitePal.deleteAll(Facturadetlog.class);


    }


}
