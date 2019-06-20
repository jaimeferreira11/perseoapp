package py.com.ideasweb.perseo.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.HomeItem;
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


}
