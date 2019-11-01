package py.com.ideasweb.perseo.constructor;

import org.litepal.LitePal;

import java.util.List;

import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.utilities.UtilLogger;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorFactura {



    public void insertarNuevaFactura(Facturacab cab){
        System.out.println("Insertando nueva factura " + cab.getNumeroFactura());
        try {
            cab.save();
            System.out.println("CANTIDAD DE DETALLES: " + cab.getFacturadet().size());
            for (Facturadet det: cab.getFacturadet() ) {
                
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

    public void insertar(Facturacab cab){
        System.out.println("Insertando factura");
        try {
            cab.save();
            System.out.println("CANTIDAD DE DETALLES: " + cab.getFacturadet().size());
            for (Facturadet det: cab.getFacturadet() ) {

                det.save();
            }

        }catch (Exception e){

            e.printStackTrace();
            throw  e;
        }

    }

    public void actualizar(Facturacab cab){
        List<Facturacab> allSongs = LitePal.findAll(Facturacab.class);

        System.out.println("Facturas antes de actualizar " + allSongs.size());

        System.out.println("Actualizando la factura: " + cab.toString());
        cab.update(cab.getId());

        allSongs = LitePal.findAll(Facturacab.class);
        System.out.println("Actualizado factura: " + cab.toString());
        System.out.println("Facturas desdepues de actualizar " + allSongs.size());


    }


    public List<Facturacab> getPendientes(){



        final List<Facturacab> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?" , "0" , "1" )
                .find(Facturacab.class);

        for (Facturacab cab: busqueda ) {
            List<Facturadet> detalles = LitePal.where(" facturacab_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadet.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas pendientes: " + busqueda.size());
        return  busqueda;
    }

    public List<Facturacab> getAnulados(){

        final List<Facturacab> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?", "0", "0" )
                .find(Facturacab.class);

        for (Facturacab cab: busqueda ) {
            List<Facturadet> detalles = LitePal.where(" facturacab_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadet.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas anulados: " + busqueda.size());
        return  busqueda;
    }

    public List<Facturacab> getSincronizados(){

        final List<Facturacab> busqueda = LitePal.where(" sincronizadocore = ? and estado = ?" , "1" , "1")
                .find(Facturacab.class);

        for (Facturacab cab: busqueda ) {
            List<Facturadet> detalles = LitePal.where(" facturacab_id = ? "  , String.valueOf(cab.getId()))
                    .find(Facturadet.class);
            cab.setFacturadet(detalles);
        }

        UtilLogger.info("Facturas sincronizados: " + busqueda.size());
        return  busqueda;
    }


    public void borrarFactura(Facturacab cab){

        System.out.println("Borrando la factura " + cab.getId());
        for (Facturadet det: cab.getFacturadet() ) {
            LitePal.delete(Facturadet.class, det.getId());
        }

        LitePal.delete(Facturacab.class, cab.getId());


    }




}
