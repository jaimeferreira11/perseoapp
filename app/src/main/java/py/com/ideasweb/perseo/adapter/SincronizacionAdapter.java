package py.com.ideasweb.perseo.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.HashMap;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.SincronizacionItem;
import py.com.ideasweb.perseo.restApi.pojo.PedidoCabecera;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 10/04/17.
 */

public class SincronizacionAdapter extends RecyclerView.Adapter<SincronizacionAdapter.ResultadoViewHolder>{
    private Context context;
    private List<SincronizacionItem> list;
    private String tipo;

    public SincronizacionAdapter(Context context, List<SincronizacionItem> list, String tipo) {
        this.context = context;
        this.list = list;
        this.tipo = tipo;

    }


    @Override
    public SincronizacionAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_sinc, parent, false);
        return new SincronizacionAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(SincronizacionAdapter.ResultadoViewHolder holder, int position) {
        SincronizacionItem item = list.get(position);



        if(tipo.equals("download"))
            holder.subir.setVisibility(View.GONE);

        holder.titulo.setText(item.getTitulo());

        holder.cantidad.setText(Utilities.toStringFromIntWithFormat(item.getCantidad()));

        holder.subir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView titulo;
        private TextView cantidad;
        private ImageButton subir;
        private CardView card;




        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            titulo = (TextView) itemView.findViewById(R.id.titulo);
            cantidad = (TextView) itemView.findViewById(R.id.cantidad);
            subir= (ImageButton) itemView.findViewById(R.id.subir);
            card= (CardView) itemView.findViewById(R.id.card);




        }
    }
}
