package py.com.ideasweb.perseo.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.github.fcannizzaro.materialstepper.style.TextStepper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.fragments.pedidos.steps.Step1;
import py.com.ideasweb.perseo.ui.fragments.pedidos.steps.Step2;
import py.com.ideasweb.perseo.ui.fragments.pedidos.steps.Step3;
import py.com.ideasweb.perseo.utilities.MiUbicacion;
import py.com.ideasweb.perseo.utilities.UnicodeFormatter;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 09/11/17.
 */

public class MainStepper extends TextStepper implements Runnable {

    View view;



    // IMPRESION
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    protected static final String TAG = "PRINT";
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setErrorTimeout(1000);
        setTitle(getResources().getString(R.string.registrar_pedido));

        addStep(createFragment(new Step1()));
        addStep(createFragment(new Step2()));
        addStep(createFragment(new Step3()));


        Bundle param = getIntent().getExtras();

        //si la accion viene de la lista de tareas
       /* ArrayList<PedidoDetalle> det = new ArrayList<>();
        LoginData.getPedido().setPedidoDetalle(det);*/



        super.onCreate(savedInstanceState);

        view = getCurrentFocus();

       // LoginData.getPedido().setPorcDescuento(new Double(0));

         dialog = new SpotsDialog.Builder()
                .setContext(MainStepper.this)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();



    }

    private AbstractStep createFragment(AbstractStep fragment) {
        Bundle b = new Bundle();
     //   b.putInt("position", idsolicitud);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        LoginData.getFactura().setEstado(true);
        LoginData.getFactura().setSincronizadoCore(false);
       // LoginData.getFactura().setTipoFactura("CONTADO");
        LoginData.getFactura().setIdUsuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());
        LoginData.getFactura().setEstablecimiento(LoginData.getTalonario().getEstablecimiento());
        LoginData.getFactura().setPuntoExpedicion(LoginData.getTalonario().getPuntoExpedicion());
        LoginData.getFactura().setNumeroFactura(LoginData.getTalonario().getNumeroActual() + 1);
        LoginData.getFactura().setTimbrado(LoginData.getTalonario().getTimbrado());
        LoginData.getFactura().setCoordendas(MiUbicacion.getCoordenadasActual());
        LoginData.getFactura().setIdEmpresa(ConstantesRestApi.ID_EMPRESA);



        System.out.println("completed");
        //view = getCurrentFocus();



        grabar();

        // COMENTARIO

        /*MaterialDialog dialog = new MaterialDialog.Builder(MainStepper.this)
                .title(getResources().getString(R.string.grabar_continuar))
                .icon(getResources().getDrawable(R.drawable.help_48))
                .positiveText(getResources().getString(R.string.aceptar))
                .negativeText(getResources().getString(R.string.cancelar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // LoginData.getPedido().setCoordenadas(MiUbicacion.getCoordenadasActual());

                        // agregar transportadora
                        new MaterialDialog.Builder(MainStepper.this)
                                .title("Agregar comentario")
                                .content("Ingresu su comentario (opcional)")
                                .inputType(InputType.TYPE_CLASS_TEXT)
                                .positiveText(getResources().getString(R.string.aceptar))
                                .cancelable(false)
                                .input("", "", new MaterialDialog.InputCallback() {
                                    @Override
                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                        // Do something
                                        if(input.toString().length() > 0) LoginData.getFactura().setComentario(input.toString());
                                         grabar();
                                        // prueba de impresion
                                        //buscarImpresora();
                                    }
                                }).show();




                    }
                })
                .show();*/

    }




    public void grabar(){


        LoginData.getFactura().setCoordendas(MiUbicacion.getCoordenadasActual());
        ConstructorFactura constructorFact = new ConstructorFactura();
        constructorFact.insertarNuevaFactura(LoginData.getFactura());

        //buscarImpresora();


        imprimirFactura();


        //  Utilities.generarPDF(getApplicationContext());
        new MaterialDialog.Builder(MainStepper.this)
                .title("Factura grabada correctamente!")
                .content("Imprimir el comprobante?")
                .icon(getResources().getDrawable(R.drawable.checked_48))
                .positiveText("Si, Imprimir")
                .negativeText("No")
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                       // dialog.dismiss();
                        buscarImpresora();
                        //imprimirFactura();

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {




                        Utilities.sendToast(getApplicationContext(), "Factura Registrada" , "success");
                        Utilities.deleteFacturaLoginData();
                        Intent intent = new Intent(MainStepper.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();


            // dialog.dismiss();

    }

    @Override
    public void onBackPressed() {

        new MaterialDialog.Builder(MainStepper.this)
                .title(getResources().getString(R.string.salir))
                .icon(getResources().getDrawable(R.drawable.help_48))
                .positiveText(getResources().getString(R.string.aceptar))
                .negativeText(getResources().getString(R.string.cancelar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        UtilLogger.info("Back en MainActivity");
                        //impresion
                        try {
                            if (mBluetoothSocket != null)
                                mBluetoothSocket.close();
                        } catch (Exception e) {
                            Log.e("Tag", "Exe ", e);
                        }
                        setResult(RESULT_CANCELED);
                        //
                        Utilities.deleteFacturaLoginData();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("url", "py.com.ideasweb.perseo.ui.fragments.HomeFragment");
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
        // super.onBackPressed();

    }

    /*
    * Metodos para impresion
    * */

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    private void imprimirFactura(){
        UtilLogger.info("IMPRIMIENDO FACTURA");
        // Toast.makeText(getApplicationContext(), "Imprimiendo..." , Toast.LENGTH_SHORT).show();
        Thread t = new Thread() {
            public void run() {
                try {
                    OutputStream os = mBluetoothSocket
                            .getOutputStream();

                    System.out.println(LoginData.getTalonario().toString());
                    String establecimiento =  String.format("%03d", LoginData.getTalonario().getEstablecimiento());
                    System.out.println("Estable " + establecimiento);
                    String punto =  String.format("%03d", LoginData.getTalonario().getPuntoExpedicion());
                    System.out.println("Punto " + punto);
                    String numero = String.format("%07d", LoginData.getTalonario().getNumeroActual());
                    System.out.println("Numero " + numero);



                    String BILL = "";
                    BILL =
                          //  " "+getResources().getString(R.string.empresa) +"\n"
                            " "+CredentialValues.getLoginData().getEmpresa().getDescripcion() +"\n"
                       //     + " "+getResources().getString(R.string.direccion_empresa) +"\n"
                            + " "+CredentialValues.getLoginData().getEmpresa().getDireccion() +"\n"
                           // + "   Tel: "+getResources().getString(R.string.telefono_empresa) +"\n"
                            + "   Tel: "+CredentialValues.getLoginData().getEmpresa().getTelefono() +"\n"
                         //   + "   RUC.: "+getResources().getString(R.string.ruc_empresa) +"\n"
                            + "   RUC.: "+CredentialValues.getLoginData().getEmpresa().getRuc() +"\n"
                            +" Timbrado Nro.: "+LoginData.getTalonario().getTimbrado() +"\n"
                            +" Validoshasta: "+LoginData.getTalonario().getValidoHasta() +"\n"
                            +" Condicion: "+ LoginData.getFactura().getTipoFactura() +"\n"
                            +" Factura.: "+ establecimiento + "-"+punto+"-"+numero  +"\n"
                            +" Fecha Hora.: "+Utilities.getCurrentDateTime() +"\n"
                            +" Usuario.: "+ CredentialValues.getLoginData().getUsuario().getLogin().toUpperCase() +"\n";

                    BILL = BILL
                            + "CLIENTE: " +LoginData.getFactura().getNombreCliente()+"\n";
                    BILL = BILL
                            + "RUC/CI: " +LoginData.getFactura().getNroDocumentoCliente()+"\n";

                    BILL = BILL
                            + "----------------------------\n";


                    BILL = BILL + String.format("%1$-8s %2$-6s %3$-6s %4$-6s %5$-8s", "Desc.", "Cant.", "IVA", "P.U.", "TOTAL");
                    BILL = BILL + "\n";
                    BILL = BILL
                            + "----------------------------\n";

                    Double exenta = new Double(0);
                    Double total10 = new Double(0);
                    Double total5 = new Double(0);

                    Double iva10 = new Double(0);
                    Double iva5 = new Double(0);
                    for (FacturaDet det: LoginData.getFactura().getFacturadet()  ) {
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



                    }


                    BILL = BILL+ "\n";
                    BILL = BILL
                            + "----------------------------\n";
                    BILL = BILL + "\n\n ";

                    BILL = BILL + " TOTAL:          " + Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getImporte()) + " Gs. \n";
                    BILL = BILL
                            + "----------------------------\n";


                    BILL = BILL + " Total exentas:      "+ Utilities.toStringFromDoubleWithFormat(exenta) + " Gs.\n";
                    BILL = BILL + " Total 10%:          "+Utilities.toStringFromDoubleWithFormat(total10) + " Gs.\n";
                    BILL = BILL + " Total 5%:           "+ Utilities.toStringFromDoubleWithFormat(total5) + " Gs.\n";


                    BILL = BILL
                            + "----------------------------\n";
                    BILL = BILL + " Liquidacion IVA     \n";
                    BILL = BILL + " Total 10%:          "+ Utilities.toStringFromDoubleWithFormat(iva10) + " Gs.\n";
                    BILL = BILL + " Total 5%:           "+ Utilities.toStringFromDoubleWithFormat(iva5) + " Gs.\n";
                    BILL = BILL + "\n\n ";
                    BILL = BILL
                            + " --- Gracias por su preferencia ---\n";
                    BILL = BILL + "\n\n\n";




                    dialog.dismiss();


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


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            new MaterialDialog.Builder(MainStepper.this)
                                    .title("Imprimio el comprobante?")
                                    .icon(getResources().getDrawable(R.drawable.checked_48))
                                    .positiveText("Si")
                                    .negativeText("No, reimprimir")
                                    .cancelable(false)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            dialog.dismiss();
                                            Utilities.deleteFacturaLoginData();
                                            Intent intent = new Intent(MainStepper.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                            // dialog.dismiss();
                                            buscarImpresora();
                                            //imprimirFactura();
                                        }
                                    })
                                    .show();
                        }
                    });



                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("MainActivity", "Exe ", e);
                }
            }
        };
        t.start();
    }

    private void buscarImpresora(){

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(MainStepper.this, "No tiene soporte de bluetooht", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                Intent connectIntent = new Intent(MainStepper.this,
                        DeviceListActivity.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                    System.out.println("REQUEST_CONNECT_DEVICE");
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(MainStepper.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    System.out.println("REQUEST_ENABLE_BT");

                    //grabar();
                    imprimirFactura();
                } else {
                    Toast.makeText(MainStepper.this, "onActivityResult REQUEST_ENABLE_BT", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
            System.out.println("RUN");


        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            reImprimir();
            closeSocket(mBluetoothSocket);
            return;
        }
    }



    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(MainStepper.this, "Impresora conectada.", Toast.LENGTH_SHORT).show();
            imprimirFactura();
        }
    };

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();

            if(mBluetoothConnectProgressDialog.isShowing())
                mBluetoothConnectProgressDialog.dismiss();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }
    private void reImprimir() {

       /* SendMail sm = new SendMail( "DrClick App - Error", getTicket() , "mcespedeso@gmail.com" , "jaimeferreira11@gmail.com");
        sm.execute();*/

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                new MaterialDialog.Builder(MainStepper.this)
                        .title("No se pudo conectar con la impresora? Avanzar de todas maneras?")
                        .icon(getResources().getDrawable(R.drawable.checked_48))
                        .positiveText("Si")
                        .negativeText("No, reimprimir")
                        .cancelable(false)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                dialog.dismiss();
                                Utilities.deleteFacturaLoginData();
                                Intent intent = new Intent(MainStepper.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                // dialog.dismiss();
                                buscarImpresora();
                                //imprimirFactura();
                            }
                        })
                        .show();
            }
        });


    }


    public String getTicket(){


        System.out.println(LoginData.getTalonario().toString());
        String establecimiento =  String.format("%03d", LoginData.getTalonario().getEstablecimiento());
        System.out.println("Estable " + establecimiento);
        String punto =  String.format("%03d", LoginData.getTalonario().getPuntoExpedicion());
        System.out.println("Punto " + punto);
        String numero = String.format("%07d", LoginData.getTalonario().getNumeroActual());
        System.out.println("Numero " + numero);



        String BILL = "";
        BILL =
                //  " "+getResources().getString(R.string.empresa) +"\n"
                " "+CredentialValues.getLoginData().getEmpresa().getDescripcion() +"\n"
                        //     + " "+getResources().getString(R.string.direccion_empresa) +"\n"
                        + " "+CredentialValues.getLoginData().getEmpresa().getDireccion() +"\n"
                        // + "   Tel: "+getResources().getString(R.string.telefono_empresa) +"\n"
                        + "   Tel: "+CredentialValues.getLoginData().getEmpresa().getTelefono() +"\n"
                        //   + "   RUC.: "+getResources().getString(R.string.ruc_empresa) +"\n"
                        + "   RUC.: "+CredentialValues.getLoginData().getEmpresa().getRuc() +"\n"
                        +" Timbrado Nro.: "+LoginData.getTalonario().getTimbrado() +"\n"
                        +" Validoshasta: "+LoginData.getTalonario().getValidoHasta() +"\n"
                        +" Condicion: "+ LoginData.getFactura().getTipoFactura() +"\n"
                        +" Factura.: "+ establecimiento + "-"+punto+"-"+numero  +"\n"
                        +" Fecha Hora.: "+Utilities.getCurrentDateTime() +"\n"
                        +" Usuario.: "+ CredentialValues.getLoginData().getUsuario().getLogin().toUpperCase() +"\n";

        BILL = BILL
                + "CLIENTE: " +LoginData.getFactura().getNombreCliente()+"\n";
        BILL = BILL
                + "RUC/CI: " +LoginData.getFactura().getNroDocumentoCliente()+"\n";

        BILL = BILL
                + "----------------------------\n";


        BILL = BILL + String.format("%1$-8s %2$-6s %3$-6s %4$-6s %5$-8s", "Desc.", "Cant.", "IVA", "P.U.", "TOTAL");
        BILL = BILL + "\n";
        BILL = BILL
                + "----------------------------\n";

        Double exenta = new Double(0);
        Double total10 = new Double(0);
        Double total5 = new Double(0);

        Double iva10 = new Double(0);
        Double iva5 = new Double(0);
        for (FacturaDet det: LoginData.getFactura().getFacturadet()  ) {
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



        }


        BILL = BILL+ "\n";
        BILL = BILL
                + "----------------------------\n";
        BILL = BILL + "\n\n ";

        BILL = BILL + " TOTAL:          " + Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getImporte()) + " Gs. \n";
        BILL = BILL
                + "----------------------------\n";


        BILL = BILL + " Total exentas:      "+ Utilities.toStringFromDoubleWithFormat(exenta) + " Gs.\n";
        BILL = BILL + " Total 10%:          "+Utilities.toStringFromDoubleWithFormat(total10) + " Gs.\n";
        BILL = BILL + " Total 5%:           "+ Utilities.toStringFromDoubleWithFormat(total5) + " Gs.\n";


        BILL = BILL
                + "----------------------------\n";
        BILL = BILL + " Liquidacion IVA     \n";
        BILL = BILL + " Total 10%:          "+ Utilities.toStringFromDoubleWithFormat(iva10) + " Gs.\n";
        BILL = BILL + " Total 5%:           "+ Utilities.toStringFromDoubleWithFormat(iva5) + " Gs.\n";
        BILL = BILL + "\n\n ";
        BILL = BILL
                + " --- Gracias por su preferencia ---\n";
        BILL = BILL + "\n\n\n";

        return BILL;
    }

}
