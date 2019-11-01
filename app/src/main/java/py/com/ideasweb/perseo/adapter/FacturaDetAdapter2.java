package py.com.ideasweb.perseo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.restApi.pojo.PedidoDetalle;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 10/04/17.
 */

public class FacturaDetAdapter2 extends RecyclerView.Adapter<FacturaDetAdapter2.ResultadoViewHolder>{
    private Context context;
    private List<Facturadet> lista;


    public FacturaDetAdapter2(Context context, List<Facturadet> lista) {
        this.context = context;
        this.lista = lista;


    }


    @Override
    public FacturaDetAdapter2.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_detalle_pedido2, parent, false);
        return new FacturaDetAdapter2.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(FacturaDetAdapter2.ResultadoViewHolder holder, int position) {
        Facturadet task = lista.get(position);

        holder.descripcion.setText(task.getConcepto());
        holder.precio.setText(Utilities.toStringFromDoubleWithFormat(task.getPrecioVenta()) + " Gs.");
        holder.monto.setText(Utilities.toStringFromDoubleWithFormat(task.getSubTotal()) + " Gs.");
        holder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(Double.valueOf(task.getCantidad())));

        if(task.getCantidad() < 1 ){
            holder.cantidad.setText(String.format("%.3f", task.getCantidad()));
        }

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView descripcion;
        private TextView precio;
        private TextView monto;
        private TextView cantidad;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            descripcion = (TextView) itemView.findViewById(R.id.cdp_descripion);
            precio= (TextView) itemView.findViewById(R.id.cdp_precio);
            monto= (TextView) itemView.findViewById(R.id.cdp_monto);
            cantidad= (TextView) itemView.findViewById(R.id.cdp_cantidad);


        }
    }
}
