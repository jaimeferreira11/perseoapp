package py.com.ideasweb.perseo.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chauthai.swipereveallayout.SwipeRevealLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.constructor.ConstructorFacturaLog;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.ui.activities.EditarFacturaActivity;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 10/04/17.
 */

public class FacturaDetLogAdapter extends RecyclerView.Adapter<FacturaDetLogAdapter.ResultadoViewHolder>{
    private Context context;
    private List<Facturadetlog> lista;
    private  Activity view;


    public FacturaDetLogAdapter(Context context, List<Facturadetlog> lista, Activity view) {
        this.context = context;
        this.lista = lista;
        this.view = view;

    }


    @Override
    public FacturaDetLogAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_detalle_pedido, parent, false);
        return new FacturaDetLogAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FacturaDetLogAdapter.ResultadoViewHolder holder, final int position) {
        Facturadetlog task = lista.get(position);

        holder.descripcion.setText(task.getConcepto());
        holder.precio.setText(Utilities.toStringFromDoubleWithFormat(task.getPrecioVenta()));
        holder.monto.setText(Utilities.toStringFromDoubleWithFormat(task.getSubTotal()));


        if (Utilities.isEntero(task.getCantidad())) {
            // si es entero
            holder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(task.getCantidad()));
        }else{
            // si no es entero
            holder.cantidad.setText(String.format("%.3f", task.getCantidad()));
        }


        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if(lista.size() > 1){
                    new MaterialDialog.Builder(v.getContext())
                            .title("Eliminar item?")
                            .icon(v.getResources().getDrawable(R.drawable.help_48))
                            .positiveText("Borrar")
                            .negativeText(v.getResources().getString(R.string.cancelar))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    System.out.println("Position: " + position);
                                    removeItem(position);
                                    //recalcula el total
                                    //modifica la cabecera
                                    Double tot = new Double(0);
                                    for (Facturadetlog element:EditarFacturaActivity.factura.getFacturadet()) {
                                        tot += element.getPrecioVenta() * element.getCantidad();
                                    }
                                    EditarFacturaActivity.factura.setImporte(tot);
                                    // modificar el total del step
                                    TextView total = (TextView) view.findViewById(R.id.step3Total);
                                    String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(EditarFacturaActivity.factura.getImporte());
                                    total.setText(Html.fromHtml(sTotal));


                                }
                            })
                            .show();
                }else {
                    Toast.makeText(context, "No se puede borrar esta linea", Toast.LENGTH_LONG).show();
                }

            }
        });

        // editar
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilLogger.info("EDITAR");

                boolean wrapInScrollView = true;
                MaterialDialog dialog = new MaterialDialog.Builder(v.getContext())
                        //.title("Mensaje")
                        .customView(R.layout.layout_agregar_articulo, wrapInScrollView)
                        // .content("Favor, conectese a internet y sincronize")
                        //  .icon(R.drawable.info)
                        .positiveText(v.getResources().getString(R.string.aceptar))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                View layout = dialog.getCustomView();
                                final TextView monto2= (TextView) layout.findViewById(R.id.aa_monto);
                                final TextView cantidad2= (TextView) layout.findViewById(R.id.aa_cantidad);


                                //modificar el detalle
                              EditarFacturaActivity.factura.getFacturadet().get(position).setCantidad(Double.valueOf(cantidad2.getText().toString()));
                              EditarFacturaActivity.factura.getFacturadet().get(position).setPrecioVenta(Double.valueOf(holder.precio.getText().toString().replace(".", "")));

                               EditarFacturaActivity.factura.getFacturadet().get(position).setSubTotal(
                                      EditarFacturaActivity.factura.getFacturadet().get(position).getCantidad() * EditarFacturaActivity.factura.getFacturadet().get(position).getPrecioVenta());

                                //modifica la cabecera
                                Double tot = new Double(0);
                                for (Facturadetlog element: EditarFacturaActivity.factura.getFacturadet()) {
                                    tot += element.getPrecioVenta() * element.getCantidad();
                                }
                               EditarFacturaActivity.factura.setImporte(tot);


                                holder.monto.setText(Utilities.toStringFromDoubleWithFormat(EditarFacturaActivity.factura.getFacturadet().get(position).getSubTotal()));
                               // holder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(Double.valueOf(EditarFacturaActivity.factura().getFacturadet().get(position).getCantidad())));

                                if (Utilities.isEntero(EditarFacturaActivity.factura.getFacturadet().get(position).getCantidad())) {
                                    // si es entero
                                    holder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(EditarFacturaActivity.factura.getFacturadet().get(position).getCantidad()));
                                }else{
                                    // si no es entero
                                    holder.cantidad.setText(String.format("%.3f",EditarFacturaActivity.factura.getFacturadet().get(position).getCantidad()));
                                }



                                // modificar el total del step
                               TextView total = (TextView) view.findViewById(R.id.step3Total);
                                String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(EditarFacturaActivity.factura.getImporte());
                                total.setText(Html.fromHtml(sTotal));

                            }
                        })
                        .negativeText(R.string.cancelar)
                        .show();


                // manipular el layout del dialog
                final View layout = dialog.getCustomView();
                int cont = 0;

                TextView descripcion1 = (TextView) layout.findViewById(R.id.aa_descripcion);
                final TextView precio1= (TextView) layout.findViewById(R.id.aa_precio);
                final TextView monto1= (TextView) layout.findViewById(R.id.aa_monto);
                 TextView cantidad1= (TextView) layout.findViewById(R.id.aa_cantidad);
                TextView id1= (TextView) layout.findViewById(R.id.aa_id);
                //
                descripcion1.setText(holder.descripcion.getText().toString());
                precio1.setText(holder.precio.getText().toString());
                monto1.setText(holder.monto.getText().toString());
                cantidad1.setText(holder.cantidad.getText().toString());


                MaterialIconView down = (MaterialIconView) layout.findViewById(R.id.aa_down);
                MaterialIconView up = (MaterialIconView) layout.findViewById(R.id.aa_up);


                cantidad1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        System.out.println("beforeTextChanged");
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        System.out.println("onTextChanged");
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        System.out.println("afterTextChanged");
                        if(editable.toString().length() > 0){
                            monto1.setText(Utilities.toStringFromDoubleWithFormat(
                                    Double.parseDouble(editable.toString().trim()) *EditarFacturaActivity.factura.getFacturadet().get(position).getPrecioVenta() ));
                        }
                    }
                });

            }
        });


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
        private TextView delete;
        private TextView edit;
        private SwipeRevealLayout swipeLayout;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            descripcion = (TextView) itemView.findViewById(R.id.cdp_descripion);
            precio= (TextView) itemView.findViewById(R.id.cdp_precio);
            monto= (TextView) itemView.findViewById(R.id.cdp_monto);
            cantidad= (TextView) itemView.findViewById(R.id.cdp_cantidad);
            delete = (TextView) itemView.findViewById(R.id.delete_layout);
            edit = (TextView) itemView.findViewById(R.id.edit_layout);
            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);


        }
    }

    public void removeItem(int position) {
        System.out.println("detalles antes de eliminar " +EditarFacturaActivity.factura.getFacturadet().size() );
        ConstructorFacturaLog log = new ConstructorFacturaLog();
        log.borrarDetalle(lista.get(position));
        EditarFacturaActivity.factura.getFacturadet().remove(lista.get(position));
        System.out.println("detalles despues de eliminar " +EditarFacturaActivity.factura.getFacturadet().size() );
//        lista.remove(position);
        notifyDataSetChanged();

    }
}
