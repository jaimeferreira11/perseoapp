package py.com.ideasweb.perseo.restApi.manager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.PedidoCabecera;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.ui.activities.MainStepper;
import py.com.ideasweb.perseo.utilities.Utilities;


/**
 * Created by jaime on 14/12/16.
 */

public class ThreadManager extends AsyncTask<Void,Void,Respuesta> {

    //Declaring Variables
    private Context context;
    private String param1;
    private String metodo;
    private String clase;
    private Cliente cliente;
    private View view;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;
    private AlertDialog dialog;

    //Class Constructor
    public ThreadManager(Context context, String clase , String metodo){
        this.context = context;
        this.metodo = metodo;
        this.clase = clase;

    }

    public ThreadManager(Context context, String clase , String metodo, Cliente cliente) {
        this.context = context;
        this.metodo = metodo;
        this.clase = clase;
        this.cliente = cliente;
    }

    //Class Constructor
    public ThreadManager(Context context, String clase , String metodo, View view){
        this.context = context;
        this.metodo = metodo;
        this.clase = clase;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       // progressDialog = ProgressDialog.show(context,context.getResources().getString(R.string.enviandoDatos),context.getResources().getString(R.string.esperar),false,false);
        String msg = "Buscando....";
        if(metodo.equals("add")){
            msg = "Enviando....";
        }
        dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();
    }



    @Override
    protected void onPostExecute(final Respuesta respuesta) {
        super.onPostExecute(respuesta);

        if(respuesta.getEstado() != null) {
            System.out.println(respuesta.getEstado());

            if (respuesta.getEstado().equals("OK")) {
                switch (clase){
                    case "cliente":

                        if(metodo.equals("add")){
                            dialog.dismiss();
                            new MaterialDialog.Builder(context)
                                    .title("Proceso Exitoso!")
                                    .content("Cliente registrado..")
                                    .icon(context.getResources().getDrawable(R.drawable.checked_48))
                                    .positiveText(context.getResources().getString(R.string.aceptar))
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Intent intent = new Intent(context, MainActivity.class);
                                            context.startActivity(intent);
                                            ((MainActivity)context).finish();

                                        }
                                    })
                                    .show();

                        }
                        break;
                    case "articulo":


                        break;

                    case "pedido":
                        if(metodo.equals("add")){
                            dialog.dismiss();
                            PedidoCabecera ped = (PedidoCabecera) respuesta.getDatos();
                            LoginData.getFactura().setIdFacturaCab(ped.getCodPedidoCab());
                            Utilities.generarPDF(context);
                            new MaterialDialog.Builder(context)
                                    .title("Proceso Exitoso!")
                                    .content("Pedido nro "+ped.getCodPedidoCab()+" registrado..")
                                    .icon(context.getResources().getDrawable(R.drawable.checked_48))
                                    .positiveText(context.getResources().getString(R.string.aceptar))
                                    .cancelable(false)
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            Utilities.deleteFacturaLoginData();
                                            Intent intent = new Intent(context, MainStepper.class);
                                            context.startActivity(intent);
                                            ((MainStepper)context).finish();

                                        }
                                    })
                                    .show();

                        }


                        break;
                    default:

                        break;
                }

            }else {
                new MaterialDialog.Builder(context)
                        .title(context.getResources().getString(R.string.error))
                        .content(context.getResources().getString(R.string.mastarde))
                        .titleColor(context.getResources().getColor(R.color.colorPrimary))
                        .icon(context.getResources().getDrawable(R.drawable.cancel_48))
                        .content(respuesta.getError())
                        .positiveText("OK")
                        .show();
            }
        }

        if(dialog.isShowing()) dialog.dismiss();


    }


    @Override
    protected Respuesta doInBackground(Void... params) {

        Respuesta respuesta = new Respuesta();

        try {
            switch (clase){
                case "cliente":
                    ClienteManager clienteManager = new ClienteManager();
                    if(metodo.equals("add")){
                        respuesta = clienteManager.addCliente(cliente);
                    }
                    break;
                case "articulo":
                    ArticuloManager articuloManager = new ArticuloManager();

                    break;

                case "pedido":
                    PedidoManager pedidoManager = new PedidoManager();
                    if(metodo.equals("add")) {
                        respuesta = pedidoManager.addPedido();
                    }

                    break;
                default:

                    break;
            }



        }catch (Exception e){
            e.printStackTrace();

        }

        return respuesta;
    }



}
