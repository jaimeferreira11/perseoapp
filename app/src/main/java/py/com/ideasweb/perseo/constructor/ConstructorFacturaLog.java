package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.Facturacablog;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorFacturaLog {

    public void insertar(Facturacab cab){
        System.out.println("Insertando facturalog");
        try {
            Facturacablog log = new Facturacablog(cab);
            log.save();
            System.out.println("CANTIDAD DE DETALLES LOG: " + cab.getFacturadet().size());
            for (Facturadet det: cab.getFacturadet() ) {

                Facturadetlog detlog = new Facturadetlog(det);
                detlog.save();
            }

        }catch (Exception e){

            e.printStackTrace();
            throw  e;
        }

    }

    public void actualizar(Facturacablog cab){
        cab.update(cab.getId());
    }


    public List<Facturacab> getAll(){

        List<Facturacablog> busqueda = LitePal.findAll(Facturacablog.class);
        List<Facturacab> result = new ArrayList<>();

        for (Facturacablog cab: busqueda ) {
            List<Facturadetlog> detalles = LitePal.where(" facturacablog_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadetlog.class);
            cab.setFacturadet(detalles);

            List<Facturadet> detList = new ArrayList<>();
            for (Facturadetlog detlog: detalles){
                Facturadet det = new Facturadet(detlog);
                detList.add(det);
            }

            //

            Facturacab factura = new Facturacab(cab);
            factura.setFacturadet(detList);
            result.add(factura);
        }
        UtilLogger.info("Todas Facturas Log: " + busqueda.size());
        UtilLogger.info("Todas Facturas Log: " + result.size());
        return  result;
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

    public List<Facturacablog> getAnulados(){

        final List<Facturacablog> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?", "0", "0" )
                .find(Facturacablog.class);

        for (Facturacablog cab: busqueda ) {
            List<Facturadetlog> detalles = LitePal.where(" Facturacablog_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadetlog.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas anulados: " + busqueda.size());
        return  busqueda;
    }

    public List<Facturacablog> getSincronizados(){

        final List<Facturacablog> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?" , "1" , "1")
                .find(Facturacablog.class);

        for (Facturacablog cab: busqueda ) {
            List<Facturadetlog> detalles = LitePal.where(" Facturacablog_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadetlog.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas sincronizados: " + busqueda.size());
        return  busqueda;
    }


    public void borrarFactura(Facturacablog cab){

        for (Facturadetlog det: cab.getFacturadet() ) {
            det.delete();
        }
        cab.delete();

    }




}
