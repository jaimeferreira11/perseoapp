package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.models.Facturacablog;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorFacturaLog {

    public void insertar(FacturaCab cab){
        System.out.println("Insertando facturalog");
        try {
            Facturacablog log = new Facturacablog(cab);
            log.save();
            System.out.println("CANTIDAD DE DETALLES LOG: " + cab.getFacturadet().size());
            for (FacturaDet det: cab.getFacturadet() ) {

                Facturadetlog detlog = new Facturadetlog(det);

                detlog.setFacturacablog_id(log.getId());
                detlog.save();
            }

        }catch (Exception e){

            e.printStackTrace();
            throw  e;
        }

    }

    public void actualizar(Facturacablog cab){

        cab.saveOrUpdate();

        for (Facturadetlog det: cab.getFacturadet() ) {
            det.delete();
        }


        System.out.println("CANTIDAD DE DETALLES A ACTUALIZAR: " + cab.getFacturadet().size());
        for (Facturadetlog det: cab.getFacturadet() ) {

            det.setFacturacablog_id(cab.getId());
            det.save();
        }


    }


    public List<FacturaCab> getAll(){

        List<Facturacablog> busqueda = LitePal.findAll(Facturacablog.class);
        List<FacturaCab> result = new ArrayList<>();

        for (Facturacablog cab: busqueda ) {
            List<Facturadetlog> detalles = LitePal.where(" facturacablog_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadetlog.class);
            cab.setFacturadet(detalles);

            List<FacturaDet> detList = new ArrayList<>();
            for (Facturadetlog detlog: detalles){
                FacturaDet det = new FacturaDet(detlog);
                detList.add(det);
            }

            //

            FacturaCab factura = new FacturaCab(cab);
            factura.setFacturadet(detList);
            result.add(factura);
        }
        UtilLogger.info("Todas Facturas Log: " + busqueda.size());
        UtilLogger.info("Todas Facturas Log: " + result.size());
        return  result;
    }

    public List<Facturacablog> getAllLog(){

        List<Facturacablog> busqueda = LitePal.findAll(Facturacablog.class);

        for (Facturacablog cab: busqueda ) {
            List<Facturadetlog> detalles = LitePal.where(" facturacablog_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadetlog.class);
            cab.setFacturadet(detalles);

            List<FacturaDet> detList = new ArrayList<>();
            for (Facturadetlog detlog: detalles){
                FacturaDet det = new FacturaDet(detlog);
                detList.add(det);
            }

        }
        UtilLogger.info("Todas Facturas Log: " + busqueda.size());
        return  busqueda;
    }



    public List<Facturacablog> getPendientes(){

        final List<Facturacablog> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?" , "0" , "1" )
                .find(Facturacablog.class);

        for (Facturacablog cab: busqueda ) {
            List<Facturadetlog> detalles = LitePal.where(" facturacablog_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadetlog.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas pendientes: " + busqueda.size());
        return  busqueda;
    }



    public void borrarFactura(Facturacablog cab){

        for (Facturadetlog det: cab.getFacturadet() ) {
            det.delete();
        }
        cab.delete();

    }

    public void borrarDetalle(Facturadetlog det){


            det.delete();


    }



    public void vaciarTabla(){

        LitePal.deleteAll("Facturacablog");
        LitePal.deleteAll("Facturadetlog");

    }



}
