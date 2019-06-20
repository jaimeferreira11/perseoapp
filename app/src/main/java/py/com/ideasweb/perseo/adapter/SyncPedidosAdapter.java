package py.com.ideasweb.perseo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.restApi.pojo.PedidoCabecera;

/**
 * Created by jaime on 10/04/17.
 */

public class SyncPedidosAdapter extends RecyclerView.Adapter<SyncPedidosAdapter.ResultadoViewHolder>{
    private Context context;
    private List<PedidoCabecera> lista;
    private static  String state;
    public static int idsolicitud;
    public static String paramid;
    public static String paramname;

    public SyncPedidosAdapter(Context context, List<PedidoCabecera> lista, String state) {
        this.context = context;
        this.lista = lista;
        this.state = state;

    }


    @Override
    public SyncPedidosAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_sync_pedido, parent, false);
        return new SyncPedidosAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SyncPedidosAdapter.ResultadoViewHolder holder, int position) {
        PedidoCabecera task = lista.get(position);

        /*holder.id.setText(task.getNrodocencuestado());
        holder.nombre.setText( task.getEncuestadonombres() + " " + task.getEncuestadoapellidos());
        holder.idsolic.setText(task.getIdsolicitud().toString());
        holder.tipoencuesta.setText("");pedido


        switch (state){
            case  "pendiente":
                holder.opcion.setVisibility(View.INVISIBLE);

                break;
            case "terminados":
                if(task.getEstado())
                    holder.opcion.setVisibility(View.VISIBLE);
                break;

            case "todos":
                if(task.getEstado())
                    holder.opcion.setVisibility(View.VISIBLE);

                break;


        }*/



    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView descripcion;
        private TextView id;
        private MaterialIconView delete;




        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            descripcion = (TextView) itemView.findViewById(R.id.csp_descripcion);
            id = (TextView) itemView.findViewById(R.id.csp_nro);
            delete= (MaterialIconView) itemView.findViewById(R.id.csp_delete);



            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String mensaje = "";
                    switch (state){
                        case  "pendiente":
                            mensaje = itemView.getResources().getString(R.string.ir_encuesta);

                            break;
                        case "terminados":
                            mensaje = itemView.getResources().getString(R.string.ir_encuesta);
                            break;

                        case "todos":
                            mensaje = itemView.getResources().getString(R.string.ir_encuesta);
                            break;


                    }

                    idsolicitud = Integer.parseInt(idsolic.getText().toString());
                    paramname = nombre.getText().toString();
                    paramid = id.getText().toString();

                    new MaterialDialog.Builder(itemView.getContext())
                            .title(mensaje)
                           // .content("Favor, conectese a internet y sincronize")
                            //  .icon(R.drawable.info)
                            .positiveText(itemView.getResources().getString(R.string.si))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    Intent intent;
                                    if(CredentialValues.getLoginData().getUsuario().getVersionencuesta().getIdversion() == 1){
                                         intent = new Intent(itemView.getContext(), MainStepper.class);
                                    }else{
                                         intent = new Intent(itemView.getContext(), MainActivity.class);
                                    }
                                    LoginData.setIdtipoencuesta(tipo);
                                    intent.putExtra("idsolicitud", idsolicitud);
                                    intent.putExtra("id", paramid);
                                    intent.putExtra("name", paramname);
                                    intent.putExtra("solicitud", "si");
                                    intent.putExtra("back", "ListaTareaFragment");
                                    itemView.getContext().startActivity(intent);
                                  //  ((HomeActivity)itemView.getContext()).finish();

                                }
                            })
                            .negativeText(R.string.no)
                            .show();

                }
            });*/
        }
    }
}
