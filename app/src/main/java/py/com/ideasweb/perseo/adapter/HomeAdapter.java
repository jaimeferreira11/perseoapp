package py.com.ideasweb.perseo.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.litepal.LitePal;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.models.Facturacablog;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.models.HomeItem;
import py.com.ideasweb.perseo.models.Talonario;
import py.com.ideasweb.perseo.models.Tracking;
import py.com.ideasweb.perseo.models.TrackingConfig;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeItem> list;
    private Activity activity;
    private Context context;

    private static final long SPLASH_SCREEN_DELAY = 2000;


    public HomeAdapter(List<HomeItem> list, Activity activity, Context context) {
        this.list = list;
        this.activity = activity;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        return new HomeAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final HomeItem element = list.get(position);

        holder.text.setText(element.getTexto());
        if(element.getImagen() != null)
            holder.imageIcono.setImageResource(element.getImagen());



        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (element.getAccion() != null){
                    switch (element.getAccion()){
                        case 1:
                            if(element.getIdHomeItem() == 5){
                                UtilLogger.info("Ingresando a registro de factura");
                                if(LoginData.getTalonario() == null){
                                    System.out.println("El Talonario es nulo");

                                    Toast.makeText(context, "Agregue un talonario antes de registrar alguna factura", Toast.LENGTH_LONG).show();
                                    return;
                                }else{

                                    try {
                                        Date fechaValido = Utilities.toDateDBFromString(LoginData.getTalonario().getValidoHasta() + " 23:59");
                                        if (fechaValido.before(new Date(System.currentTimeMillis()))) {
                                            Toast.makeText(context, "El talonario esta vencido. Favor actualice", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                    }catch (ParseException e){
                                        Toast.makeText(context, "El talonario esta vencido. Favor actualice", Toast.LENGTH_LONG).show();
                                        return;
                                    }

                                    if(LoginData.getTalonario().getNumeroActual() == LoginData.getTalonario().getNumeroFinal()){
                                        Toast.makeText(context, "El talonario ya no tiene numeros disponibles. Favor actualice", Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                }
                            }
                            irActivityFinish(element.getUrl());
                            break;
                        case 2 :
                            irActivity(element.getUrl());
                            break;

                        case 3 :
                            irFragment(element.getUrl());
                            break;
                        case 4 :
                            restaurar();
                            break;

                        case 5 :
                            borrarSincronizadas();
                            break;

                         default:
                                break;
                    }
                }

            }
        });

    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //  private TextView pos;
        private CardView card;
        private TextView text;
        private ImageView imageFlecha;
        private ImageView imageIcono;



        public ViewHolder(View itemView) {
            super(itemView);

            // pos = (TextView) itemView.findViewById(R.id.pos);
            card = (CardView) itemView.findViewById(R.id.card);
            text = (TextView) itemView.findViewById(R.id.text);
            imageFlecha = (ImageView) itemView.findViewById(R.id.image_flecha);
            imageIcono = (ImageView) itemView.findViewById(R.id.image_icono);

        }
    }



    private void irActivity(String url){
        MainActivity.inHome = false;
        System.out.println("Entro en activity finish");
        Intent intent = new Intent();
        intent.setClassName(context, url);
        context.startActivity(intent);

    }

    private void irActivityFinish(String url){

        MainActivity.inHome = false;
        System.out.println("Entro en activity finish");
        Intent intent = new Intent();
        intent.setClassName(context, url);
        context.startActivity(intent);
          ((MainActivity) context).finish();

    }

    private void irFragment(String url){

        MainActivity.inHome = false;
        Intent ip = new Intent(context, MainActivity.class);
        ip.putExtra("url", url);
        context.startActivity(ip);



    }

    /*private void irWebView(String url){
        System.out.println("Entro en webview");
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
        //finish();
    }*/

    private void borrarSincronizadas(){

        new MaterialDialog.Builder(context)
                .title("Desea borrar las facturas sincronizadas?")
                .content("Los datos ya no estaran en el dispositivo")
                .icon(context.getResources().getDrawable(R.drawable.help_48))
                .positiveText(context.getResources().getString(R.string.aceptar))
                .negativeText(context.getResources().getString(R.string.cancelar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {


                        ConstructorFactura cf = new ConstructorFactura();
                        cf.borrarFacturasSincronizadas();

                        Utilities.setUltSync(context, "", "" );

                        new MaterialDialog.Builder(context)
                                .icon(context.getResources().getDrawable(R.drawable.checked_48))
                                .title(context.getResources().getString(R.string.procesoExitoso))
                                .content("Se borraron los datos del telefono")
                                .titleColor(context.getResources().getColor(R.color.colorPrimaryDark))
                                .positiveText("Aceptar")
                                .show();

                    }
                })
                .show();




    }


    private void restaurar(){

        new MaterialDialog.Builder(context)
                .title("Desea reestablecer los datos de la app?")
                .icon(context.getResources().getDrawable(R.drawable.help_48))
                .positiveText(context.getResources().getString(R.string.aceptar))
                .negativeText(context.getResources().getString(R.string.cancelar))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        LitePal.deleteAll(FacturaCab.class);
                        LitePal.deleteAll(Facturacablog.class);
                        LitePal.deleteAll(FacturaDet.class);
                        LitePal.deleteAll(Facturadetlog.class);
                        LitePal.deleteAll(Talonario.class);
                        LitePal.deleteAll(Cliente.class);
                        LitePal.deleteAll(Articulo.class);
                        LitePal.deleteAll(Tracking.class);
                        LitePal.deleteAll(TrackingConfig.class);
                        dialog.dismiss();

                        Utilities.setUltSync(context, "", "" );

                        new MaterialDialog.Builder(context)
                                .icon(context.getResources().getDrawable(R.drawable.checked_48))
                                .title(context.getResources().getString(R.string.procesoExitoso))
                                .content("Se restauro los datos del telefono")
                                .titleColor(context.getResources().getColor(R.color.colorPrimaryDark))
                                .positiveText("Aceptar")
                                .show();

                    }
                })
                .show();




    }


}
