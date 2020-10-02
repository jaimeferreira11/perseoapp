package py.com.ideasweb.perseo.repo;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.utilities.Utilities;

public class FacturaDummyRepo {


    public static List<FacturaCab> getFacturasDePrueba(){
        List<FacturaCab> facturas = new ArrayList<>();


        for (int i = 1; i < 501; i++) {
            FacturaCab facturaCab = new FacturaCab();
            facturaCab.setIdCliente(388);
            facturaCab.setTimbrado("12345678");
            facturaCab.setEstablecimiento(99);
            facturaCab.setPuntoExpedicion(99);
            facturaCab.setNumeroFactura(i);
            facturaCab.setNroDocumentoCliente("388");
            facturaCab.setNombreCliente("El torito II");
            facturaCab.setIdUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());
            facturaCab.setFecha(Utilities.getCurrentDateTimeBD());
            facturaCab.setIdEmpresa(ConstantesRestApi.ID_EMPRESA);
            facturaCab.setEstado(true);
            facturaCab.setSincronizadoCore(true);
            facturaCab.setFacturadet(new ArrayList<FacturaDet>());
            facturaCab.setImporte(new Double(3000));
            facturaCab.setTipoFactura("CONTADO");

            for (int j = 1; j < 11 ; j++) {
                FacturaDet det = new FacturaDet();
                det.setIdArticulo(1084794);
                det.setConcepto("YOG. SACHET 500 ML");
                det.setPrecioVenta(new Double(3000));
                det.setCantidad(new Double(1));
                det.setTasaIva(new Double(10));
                det.setSubTotal(new Double(3000));
                det.setImpuesto(new Double(291));

                facturaCab.getFacturadet().add(det);

            }

            facturas.add(facturaCab);

        }

        return facturas;
    }
}
