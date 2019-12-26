package py.com.ideasweb.perseo.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import harmony.java.awt.Color;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.constructor.ConstructorEmpresa;
import py.com.ideasweb.perseo.constructor.ConstructorUsuario;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.Empresa;
import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.manager.ArticuloManager;
import py.com.ideasweb.perseo.restApi.manager.ClienteManager;
import py.com.ideasweb.perseo.restApi.manager.EmpresaManager;
import py.com.ideasweb.perseo.restApi.manager.UsuarioManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.work.MyPeriodicWork;


/**
 * Created by root on 05/12/16.
 */

public class Utilities {
    public static Integer version_code = 0;
    public static  String blank ="";

    public Double getRedondeo(Double valor) {
        BigDecimal bd = new BigDecimal(valor);
        bd = bd.setScale(0, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    public String toDouble(Double value ) {
        return new DecimalFormat("#").format(value);
    }


    public static String toStringFromDate(Date date ){
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.format(date);
    }

    public static Date toDateFromString(String stringDate ) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        return sd.parse(stringDate);
    }

    public static String toStringFromDoubleWithFormat(Double value ){
        try {

            String pattern = "###,###";
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat(pattern, symbols);
            return  df.format(value);
        }catch (Exception e){
            return  "0";
        }

    }

    public static String toStringFromIntWithFormat(Integer value ){
        try {

            String pattern = "###,###";
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            symbols.setGroupingSeparator('.');
            DecimalFormat df = new DecimalFormat(pattern, symbols);
            return  df.format(value);
        }catch (Exception e){
            return  "0";
        }

    }

    public static String toStringFromFloatWithFormat(Float value ){
        String pattern = "###,###";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        DecimalFormat df = new DecimalFormat(pattern, symbols);
        return  df.format(value);
    }

    public static String stringNumberFormat(String s){
        StringBuilder finalValue = new StringBuilder();
        UtilLogger.info("PARAMENTRO: " + s );
        try {
            String value = s.toString().replace(".", "");
            String reverseValue = new StringBuilder(value).reverse()
                    .toString();

            for (int i = 1; i <= reverseValue.length(); i++) {
                char val = reverseValue.charAt(i - 1);
                finalValue.append(val);
                if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                    finalValue.append(".");
                }
            }

        } catch (Exception e) {
            // Do nothing since not a number
        }
        UtilLogger.info("RESULTADO: " + finalValue.toString() );

        return finalValue.reverse().toString();

    }

    //Comprueba que este conectado a la red
    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null || !info.isConnected() || !info.isAvailable()) {
            Toast.makeText(context, ConstantesMensajes.INTERNET_NO_CONEXION , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


    // redimensionar imagen
    public static Bitmap decodeFile(File f, int WIDTH, int HIGHT){
        try {
            //Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //The new size we want to scale to
            final int REQUIRED_WIDTH=WIDTH;
            final int REQUIRED_HIGHT=HIGHT;
            //Find the correct scale value. It should be the power of 2.
            int scale=1;
            while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
                scale*=2;

            //Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }


    public static TextWatcher numberFormat( final EditText editText){
        TextWatcher tw = new TextWatcher() {
            boolean isManualChange = false;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                try {
                    String value = s.toString().replace(".", "");
                    String reverseValue = new StringBuilder(value).reverse()
                            .toString();
                    StringBuilder finalValue = new StringBuilder();
                    for (int i = 1; i <= reverseValue.length(); i++) {
                        char val = reverseValue.charAt(i - 1);
                        finalValue.append(val);
                        if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                            finalValue.append(".");
                        }
                    }
                    isManualChange = true;
                    editText.setText(finalValue.reverse());
                    editText.setSelection(finalValue.length());
                } catch (Exception e) {
                    // Do nothing since not a number
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        return tw;

    }

    public static void maskDate(final EditText editText){

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "DDMMAAAA";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        editText.addTextChangedListener(tw);
    }

    public static void deleteLoginData(){

        CredentialValues.setLoginData(null);

    }

    public static void deleteFacturaLoginData(){

        //CredentialValues.setLoginData(null);
        LoginData.setFactura(new FacturaCab());
        LoginData.getFactura().setPorcDescuento(new Double(0));

    }

    public static void sincronizacionInicial(){

        // usuarios
        final UsuarioManager manager = new UsuarioManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.getByEmpresa();

                    if(respuesta.getEstado() == "OK"){
                        ConstructorUsuario cu = new ConstructorUsuario();
                        cu.insertarUsuarios((ArrayList<Usuario>) respuesta.getDatos());


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

        // usuarios
        final EmpresaManager empresaManager = new EmpresaManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = empresaManager.getById(ConstantesRestApi.ID_EMPRESA);

                    if(respuesta.getEstado() == "OK"){
                        ConstructorEmpresa cu = new ConstructorEmpresa();
                        cu.insertar((Empresa) respuesta.getDatos());

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

        // perfiles
        /*final PerfilManager pmanager = new PerfilManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = pmanager.getaAll();

                    if(respuesta.getEstado() == "OK"){
                        ConstructorPerfil cu = new ConstructorPerfil();
                        cu.insertar((ArrayList<Perfil>) respuesta.getDatos());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();*/
    }

    public static void bajarDatos(final Context context){


        UtilLogger.info("DESCARGANDO DATOS");

        //cliente
        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.getClientesByEmpresa();
                    //Respuesta respuesta = manager.getClientesByUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());

                    if(respuesta.getEstado() == "OK"){
                        ConstructorCliente cu = new ConstructorCliente();
                        cu.insertar((ArrayList<Cliente>) respuesta.getDatos());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

        // articulo
        final ArticuloManager manager2 = new ArticuloManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager2.getByEmpresa();

                    if(respuesta.getEstado() == "OK"){
                        ConstructorArticulos cu = new ConstructorArticulos();
                        cu.insertar((ArrayList<Articulo>) respuesta.getDatos());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();

        setUltSync(context, null ,Utilities.getCurrentDateTime() );
    }


    public static String getUltUpload(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("perseo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("fecha-upload", "");
    }
    public static String getUltDownload(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("perseo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("fecha-download", "");
    }


    public static  void setUltSync(Context context , String upload, String download){
        SharedPreferences SPUbicacion =context.getSharedPreferences("perseo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();

        if(upload != null){
            editor.putString("fecha-upload", upload);
            editor.commit();
        }
        if(download != null){
            editor.putString("fecha-download", download);
            editor.commit();
        }
    }

    public static boolean purgreDB(Context context)  {
        Boolean ban = false;
        try{

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String date = getUltUpload(context);
            if(!date.equals("")){
                //fecha database
                Date convertedDate = new Date();
                convertedDate = dateFormat.parse(date);

                //fecha actual
                Date d = new Date();
                CharSequence now  = DateFormat.format("dd/MM/yyyy ", d.getTime());

                int dias=(int) ((d.getTime()-convertedDate.getTime())/86400000);


                UtilLogger.info(now.toString() + " - " + date + " = " + dias );
                if(dias > 29){
                    ban = true;
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return ban;


    }


    private static String getSizeName(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return "xlarge";
            default:
                return "undefined";
        }
    }


    /*Actualizacion del sistema disponible*/
    public static boolean updateAvailable(Context context){
        PackageInfo pinfo = null;
        try {
            pinfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        int versionNumber = pinfo.versionCode;
        System.out.println("Codigo de version" + versionNumber);
        System.out.println("Utilities code" + Utilities.version_code);
        if( Utilities.version_code > versionNumber &&  Utilities.version_code > 0){
            return true;
        }else{
            return false;
        }


    }


    public static String getCurrentDate(){

        String s = new SimpleDateFormat("dd-MM-yyyy").format(new Timestamp(System.currentTimeMillis()).getTime());

        return s;
    }

    public static String getCurrentDateTime(){

        String s = new SimpleDateFormat("dd-MM-yyyy HH:mm").format(new Timestamp(System.currentTimeMillis()).getTime());

        return s;
    }

    public static String getCurrentDateTimeBD(){

        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Timestamp(System.currentTimeMillis()).getTime());

        return s;
    }


   /* public static void sendErrorlog(){
        List<Errorlog> list = new ArrayList<>();

        list = DaoFactory.getErrorLog().getAll();
        if(list != null){
            if(!list.isEmpty()){

                for (Errorlog error: list ) {

                    if(error.getLog() != null){
                        SendMail sm = new SendMail(
                                "Log de Error App Semafoto",
                                error.getLog()
                                );
                        //Executing sendmail to send email
                        sm.execute();
                        DaoFactory.getErrorLog().delete(error);
                    }
                }
            }
        }

    }*/


    /**
     * Crea un fichero con el nombre que se le pasa a la función y en la ruta
     * especificada.
     *
     * @param nombreFichero
     * @return
     * @throws IOException
     */
    public static File crearFichero(String nombreFichero) throws IOException {
        File ruta = getRuta();
        File fichero = null;
        if (ruta != null)
            fichero = new File(ruta, nombreFichero);
        return fichero;
    }

    /**
     * Obtenemos la ruta donde vamos a almacenar el fichero.
     *
     * @return
     */
    public static File getRuta() {

        // El fichero será almacenado en un directorio dentro del directorio
        // Descargas
        File ruta = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            ruta = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "");

            if (ruta != null) {
                if (!ruta.mkdirs()) {
                    if (!ruta.exists()) {
                        return null;
                    }
                }
            }
        } else {
        }

        return ruta;
    }

    public static String obtenerIdPerfil(String descripcion, int n) {
        String[] parts = descripcion.split("-");
        return parts[n].trim();
    }


    public static void generarPDF(Context context) {

        // Creamos el documento.
        Document documento = new Document();
        try {

            // Creamos el fichero con el nombre que deseemos.
            File f = crearFichero("Cod-" + LoginData.getFactura().getCliente().getIdCliente()+"-"+String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()) + ".pdf");

            // Creamos el flujo de datos de salida para el fichero donde
            // guardaremos el pdf.

            FileOutputStream ficheroPdf = new FileOutputStream(
                    f.getAbsolutePath());

            // Asociamos el flujo que acabamos de crear al documento.
            PdfWriter writer = PdfWriter.getInstance(documento, ficheroPdf);

            // Incluimos el píe de página y una cabecera
            HeaderFooter cabecera = new HeaderFooter(new Phrase(
                    "Vagos App - Pedidos"), false);
            HeaderFooter pie = new HeaderFooter(new Phrase(
                    "Usuario: " + CredentialValues.getLoginData().getUsuario().getLogin()), false);

            documento.setHeader(cabecera);
            documento.setFooter(pie);

            // Abrimos el documento.
            documento.open();

            // Añadimos un título con la fuente por defecto.
           // documento.add(new Paragraph(pdfFile.getTitle()));

            // Añadimos un título con una fuente personalizada.
            Font font = FontFactory.getFont(FontFactory.HELVETICA, 22,
                    Font.BOLD, Color.BLACK);

            documento.add(new Paragraph("Datos del Pedido nro: "+LoginData.getFactura().getIdFacturaCab()  , font) );
            documento.add( Chunk.NEWLINE );
            documento.add(new Paragraph("Fecha : " + getCurrentDate() ));
            documento.add(new Paragraph("Cliente: " + LoginData.getFactura().getCliente().getNombreApellido() ));
            documento.add(new Paragraph("Telefono: " + LoginData.getFactura().getCliente().getTelefono() ));


            documento.add(new Paragraph("Comentario: " + LoginData.getFactura().getComentario() ));


            documento.add(new Paragraph("SubTotal: " + toStringFromDoubleWithFormat(LoginData.getFactura().getImporte()) + " Gs"));
            documento.add(new Paragraph("Descuento (%): " + toStringFromDoubleWithFormat(LoginData.getFactura().getPorcDescuento())));


            Double totalAux = LoginData.getFactura().getImporte();
            if(LoginData.getFactura().getPorcDescuento() > 0){
                totalAux = (totalAux * LoginData.getFactura().getPorcDescuento()) / 100;
                totalAux = LoginData.getFactura().getImporte() - totalAux;
            }
            Font font3 = FontFactory.getFont(FontFactory.HELVETICA, 20,
                    Font.BOLD, Color.BLACK);
            documento.add(new Paragraph("TOTAL: " + toStringFromDoubleWithFormat(totalAux) + " Gs", font3));


            // Insertamos una imagen que se encuentra en los recursos de la
            // aplicación.

            /*Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.vagos_logo);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            Image imagen = Image.getInstance(stream.toByteArray());
            documento.add(imagen);*/

            documento.add( Chunk.NEWLINE );
            documento.add(new Paragraph("Detalles", font));
            documento.add( Chunk.NEWLINE );
            // Insertamos una tabla.
            PdfPTable tabla = new PdfPTable(5);
            int aux = LoginData.getFactura().getFacturadet().size();

            Font font2 = FontFactory.getFont(FontFactory.HELVETICA, 16,
                    Font.BOLD, Color.DARK_GRAY);


            tabla.addCell(new Phrase("Item", font2));
            tabla.addCell(new Phrase("Codigo", font2));
            tabla.addCell(new Phrase("Articulo", font2));
            tabla.addCell(new Phrase("Cantidad", font2));
            tabla.addCell(new Phrase("Sub-Total", font2));


            for (int i = 0; i < aux; i++) {
                tabla.addCell(String.valueOf((i+1)));
                tabla.addCell(LoginData.getFactura().getFacturadet().get(i).getArticulo().getIdArticulo().toString());
                tabla.addCell(LoginData.getFactura().getFacturadet().get(i).getArticulo().getDescripcion());
                tabla.addCell(toStringFromDoubleWithFormat(LoginData.getFactura().getFacturadet().get(i).getCantidad()));
                tabla.addCell(toStringFromDoubleWithFormat(LoginData.getFactura().getFacturadet().get(i).getSubTotal()));

            }
            documento.add(tabla);

            Toast.makeText(context, "Pdf generado.. Ver en descargas", Toast.LENGTH_LONG).show();

            // Agregar marca de agua
           /* font = FontFactory.getFont(FontFactory.HELVETICA, 42, Font.BOLD,
                    Color.GRAY);
            ColumnText.showTextAligned(writer.getDirectContentUnder(),
                    com.lowagie.text.Element.ALIGN_CENTER, new Paragraph(
                            "amatellanes.wordpress.com", font), 297.5f, 421,
                    writer.getPageNumber() % 2 == 1 ? 45 : -45);*/

        } catch (DocumentException e) {

            Log.e(ConstantesRestApi.ETIQUETA_ERROR, e.getMessage());

        } catch (Exception e) {

            Log.e(ConstantesRestApi.ETIQUETA_ERROR, e.getMessage());

        } finally {

            // Cerramos el documento.
            UtilLogger.info("CERRANDO....");
            documento.close();
        }
    }


    public static String obtenerIdLista(String descripcion, int n) {
        String[] parts = descripcion.split("\\|");
        return parts[n].trim();
    }


    public static void imprimirFactura(final FacturaCab factura, final Context context){
        UtilLogger.info("IMPRIMIENDO FACTURA");
        Toast.makeText(context, "Imprimiendo..." , Toast.LENGTH_SHORT).show();
        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = MainActivity.mBluetoothSocket
                            .getOutputStream();


                    System.out.println(factura.toString());
                    String establecimiento =  String.format("%03d", factura.getEstablecimiento());
                    System.out.println("Estable " + establecimiento);
                    String punto =  String.format("%03d",factura.getPuntoExpedicion());
                    System.out.println("Punto " + punto);
                    String numero = String.format("%07d", factura.getNumeroFactura());
                    System.out.println("Numero " + numero);

                    ConstructorUsuario cu = new ConstructorUsuario();

                    Usuario usuario = cu.getById(factura.getIdUsuario());

                    System.out.println("Talonario: " + establecimiento + "-"+punto+"-"+numero);

                    String BILL = "";
                    BILL =
                            " "+CredentialValues.getLoginData().getEmpresa().getDescripcion() +"\n"
                                    //     + " "+getResources().getString(R.string.direccion_empresa) +"\n"
                                    + " "+CredentialValues.getLoginData().getEmpresa().getDireccion() +"\n"
                                    // + "   Tel: "+getResources().getString(R.string.telefono_empresa) +"\n"
                                    + "   Tel: "+CredentialValues.getLoginData().getEmpresa().getTelefono() +"\n"
                                    //   + "   RUC.: "+getResources().getString(R.string.ruc_empresa) +"\n"
                                    + "   RUC.: "+CredentialValues.getLoginData().getEmpresa().getRuc() +"\n"
                            +" Timbrado Nro.: "+LoginData.getTalonario().getTimbrado() +"\n"
                            +" Valido hasta: "+LoginData.getTalonario().getValidoHasta() +"\n"
                            +" Condicion: "+ factura.getTipoFactura() +"\n"
                            +" Factura.: "+ establecimiento + "-"+punto+"-"+numero  +"\n"
                            +" Fecha Hora.: "+Utilities.getCurrentDateTime() +"\n"
                            +" Usuario.: "+ usuario.getLogin().toUpperCase() +"\n";

                    BILL = BILL
                            + "CLIENTE: " +factura.getNombreCliente()+"\n";
                    BILL = BILL
                            + "RUC/CI: " +factura.getNroDocumentoCliente()+"\n";

                    BILL = BILL
                            + "-------------------------------\n";

                    BILL = BILL + String.format("%1$-8s %2$-6s %3$-6s %4$-6s %5$-8s", "Desc.", "Cant.", "IVA", "P.U.", "TOTAL");
                   // BILL = BILL + String.format("%1$5s %2$10s %3$10s %4$10s %5$10s", "Cant.", "Descr.", "Tasa", "P. Unit", "Total");
                    BILL = BILL + "\n";
                    BILL = BILL
                            + "-------------------------------\n";

                    Double exenta = new Double(0);
                    Double total10 = new Double(0);
                    Double total5 = new Double(0);

                    Double iva10 = new Double(0);
                    Double iva5 = new Double(0);
                    for (FacturaDet det: factura.getFacturadet()  ) {
                        if(det.getTasaIva().intValue() == 10){
                            iva10 += det.getImpuesto();
                            total10 += det.getSubTotal();
                        }else if(det.getTasaIva().intValue() == 5){
                            iva5 += det.getImpuesto();
                            total5 += det.getSubTotal();
                        }else if(det.getTasaIva().intValue() == 0){
                            exenta += det.getSubTotal();
                        }

                        BILL = BILL + "\n " + String.format("%1$-20s %2$4s", det.getConcepto(), det.getTasaIva().intValue()+"%" );

                        BILL = BILL + "\n " + String.format("%1$5s %2$14s %3$15s",
                                String.format("%.2f", det.getCantidad()).replace(".",","), Utilities.toStringFromDoubleWithFormat(det.getPrecioVenta()), Utilities.toStringFromDoubleWithFormat(det.getSubTotal()));


                        /*BILL = BILL + "\n " + String.format("%1$5s %2$10s %3$10s %4$10s %5$10s",
                                det.getCantidad().intValue(), det.getConcepto(), det.getTasaIva().intValue(), Utilities.toStringFromDoubleWithFormat(det.getPrecioVenta()), Utilities.toStringFromDoubleWithFormat(det.getSubTotal()));*/
                    }

                    BILL = BILL+ "\n";
                    BILL = BILL
                            + "-------------------------------\n";
                    BILL = BILL + "\n\n ";

                    BILL = BILL + " TOTAL:          "+ Utilities.toStringFromDoubleWithFormat(factura.getImporte()) + " Gs. \n";
                    BILL = BILL
                            + "-------------------------------\n";

                    BILL = BILL + " Total exentas:      "+ Utilities.toStringFromDoubleWithFormat(exenta) + " Gs.\n";
                    BILL = BILL + " Total 10%:          "+Utilities.toStringFromDoubleWithFormat(total10) + " Gs.\n";
                    BILL = BILL + " Total 5%:           "+ Utilities.toStringFromDoubleWithFormat(total5) + " Gs.\n";


                    BILL = BILL
                            + "-------------------------------\n";
                    BILL = BILL + " Liquidacion IVA     \n";
                    BILL = BILL + " Total 10%:          "+ Utilities.toStringFromDoubleWithFormat(iva10) + " Gs.\n";
                    BILL = BILL + " Total 5%:           "+ Utilities.toStringFromDoubleWithFormat(iva5) + " Gs.\n";
                    BILL = BILL + "\n\n ";
                    BILL = BILL
                            + " --- Gracias por su preferencia ---\n\n\n\n";
                    BILL = BILL + "\n\n\n";
                    os.write(BILL.getBytes());
                    //This is printer specific code you can comment ==== > Start

                    // Setting height
                    int gs = 29;
                    os.write(intToByteArray(gs));
                    int h = 104;
                    os.write(intToByteArray(h));
                    int n = 162;
                    os.write(intToByteArray(n));

                    // Setting Width
                    int gs_width = 29;
                    os.write(intToByteArray(gs_width));
                    int w = 119;
                    os.write(intToByteArray(w));
                    int n_width = 2;
                    os.write(intToByteArray(n_width));




                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }


    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }


    public static String reemplazarAcentos(String texto){

        texto = texto.replaceAll("á" , "a");
        texto = texto.replaceAll("é" , "e");
        texto = texto.replaceAll("í" , "i");
        texto = texto.replaceAll("ó" , "o");
        texto = texto.replaceAll("ú" , "u");

        return texto;
    }


    public static void sendToast(Context context, String messsage, String type) {

        try {
            Toast toast = Toast.makeText(context, messsage, Toast.LENGTH_LONG);
            //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            View toastView = toast.getView();
            switch (type) {
                case "error":
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.error));
                    break;
                case "warning":
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.warning));
                    break;
                case "info":
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.info));
                    break;
                case "success":
                    toastView.setBackgroundColor(context.getResources().getColor(R.color.success));
                    break;
                default:
                    break;

            }

            toast.show();

        }catch (Exception e){

        }
    }

    public static void iniciarTareaPeriodica(){
        PeriodicWorkRequest mPeriodicWorkRequest = new PeriodicWorkRequest.Builder(MyPeriodicWork.class,
                15, TimeUnit.MINUTES)
                .addTag("periodicWorkRequest")
                .build();


        WorkManager.getInstance().enqueue(mPeriodicWorkRequest);
    }

    public static void finalizarTareaPeriodica(){
        /*UUID getId = mPeriodicWorkRequest.getId();
        getId = mPeriodicWorkRequest.getId();*/
        WorkManager.getInstance().cancelAllWork();
    }


    public static boolean isEntero(double numero){
       return ((numero == Math.floor(numero)) && !Double.isInfinite(numero));
    }

    public static String getEmailTemplate(){
        String template = "<html>" +
                "<head>" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=utf-8\" />" +
                "</head>" +
                "<body>" +
                "<table bgcolor=\"#113a69\" width=\"100%\">" +
                "<tr>" +
                "<td align=\"center\">" +
                //   "<h2><font color=\"#ffffff\" ><img src=\"http://www.fundacionparaguaya.org.py/wp-content/themes/fundacion/images/logo_fundacionparaguaya.png\" alt=\"Fundación Paraguaya\"></font>" +
                "<br/><font color=\"#ffffff\" ><b>Titulo </b></font></h2>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "<table bgcolor=\"#f2f2f2\" width=\"100%\">" +
                "<tr>" +
                "<td>" +
                "<div>Contenido <br/><br/></div>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "<table bgcolor=\"#f2f2f2\" width=\"100%\">" +
                "<tr>" +
                "<td align=\"center\">" +
                "<h5>" +
                "<font color=\"#262626\"><b>No responda a este mensaje</b>. Si tiene alguna duda respecto al contenido de este correo, favor comunicarse a <a href=\"mailto:CorreoInfo\">CorreoInfo</a> </font><br>"+
                "</h5>"+
                "</td>" +
                "</tr>" +
                "</table>" +
                "<table bgcolor=\"#113a69\" width=\"100%\">" +
                "<tr>" +
                "<td align=\"center\">" +
                "<h6>" +
                "<font color=\"#ffffff\" ><b>&copy; Fundación Paraguaya</b>. Dirección: Manuel Blinder 5589 c/ Tte. Espinoza. Asunción, Paraguay. Tel.: (+595 21) 609 - 277 </font>" +
                "</h6>" +
                "</td>" +
                "</tr>" +
                "</table>" +
                "</body>" +
                "</html>";

        return template;
    }


    public static boolean isNumeric(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }
}
