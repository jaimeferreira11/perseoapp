package py.com.ideasweb.perseo.adapter;

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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chauthai.swipereveallayout.SwipeRevealLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;
/**
 * Created by jaime on 10/04/17.
 */

public class FacturaDetAdapter extends RecyclerView.Adapter<FacturaDetAdapter.ResultadoViewHolder>{
    private Context context;
    private List<FacturaDet> lista;
    private  View view;


    public FacturaDetAdapter(Context context, List<FacturaDet> lista, View view) {
        this.context = context;
        this.lista = lista;
        this.view = view;

    }


    @Override
    public FacturaDetAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_detalle_pedido, parent, false);
        return new FacturaDetAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FacturaDetAdapter.ResultadoViewHolder holder, final int position) {
        final FacturaDet task = lista.get(position);

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
                                // se movio a un metodo
                                /*Double tot = new Double(0);
                                for (FacturaDet element: LoginData.getFactura().getFacturadet()) {
                                    tot += element.getPrecioVenta() * element.getCantidad();
                                }
                                LoginData.getFactura().setImporte(tot);*/

                                modificarTotal();

                                // modificar el total del step
                                TextView total = (TextView) view.findViewById(R.id.step3Total);
                                String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getImporte());
                                total.setText(Html.fromHtml(sTotal));
                            }
                        })
                        .show();
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
                                LoginData.getFactura().getFacturadet().get(position).setCantidad(Double.valueOf(cantidad2.getText().toString()));
                                LoginData.getFactura().getFacturadet().get(position).setPrecioVenta(Double.valueOf(holder.precio.getText().toString().replace(".", "")));
                                LoginData.getFactura().getFacturadet().get(position).setSubTotal(
                                        LoginData.getFactura().getFacturadet().get(position).getCantidad() * LoginData.getFactura().getFacturadet().get(position).getPrecioVenta());


                                // modificar el iva en el detalle
                                if(task.getTasaIva() == 10){
                                    LoginData.getFactura().getFacturadet().get(position).setImpuesto(task.getSubTotal() / 11);
                                }else if(task.getTasaIva() == 5){
                                    LoginData.getFactura().getFacturadet().get(position).setImpuesto(task.getSubTotal() / 21);
                                }else{
                                    LoginData.getFactura().getFacturadet().get(position).setImpuesto(task.getSubTotal());
                                }

                                //modifica la cabecera
                                // se mudo a un metodo a parte
                               /* Double tot = new Double(0);
                                for (FacturaDet element: LoginData.getFactura().getFacturadet()) {
                                    tot += element.getPrecioVenta() * element.getCantidad();
                                }
                                LoginData.getFactura().setImporte(tot);*/

                                modificarTotal();


                                holder.monto.setText(Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getFacturadet().get(position).getSubTotal()));
                               // holder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(Double.valueOf(LoginData.getFactura().getFacturadet().get(position).getCantidad())));

                                if (Utilities.isEntero(LoginData.getFactura().getFacturadet().get(position).getCantidad())) {
                                    // si es entero
                                    holder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getFacturadet().get(position).getCantidad()));
                                }else{
                                    // si no es entero
                                    holder.cantidad.setText(String.format("%.3f", LoginData.getFactura().getFacturadet().get(position).getCantidad()));
                                }



                                // modificar el total del step
                               TextView total = (TextView) view.findViewById(R.id.step3Total);
                                String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getImporte());
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

               /* down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double c = Double.valueOf(cantidad1.getText().toString());
                        c = c-1;
                        if(c > 0){
                            cantidad1.setText(String.valueOf(c));
                            Double m = new Double(0);
                            m = Double.parseDouble(precio1.getText().toString().replace(".", "")) * c;
                            monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                        }


                    }
                });

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double c = Double.valueOf(cantidad1.getText().toString());
                        c = c+1;
                        cantidad1.setText(String.valueOf(c));
                        Double m = new Double(0);
                        m = Double.parseDouble(precio1.getText().toString().replace(".", "")) * c;
                        monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                    }
                });*/

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
                                    Double.parseDouble(editable.toString().trim()) * LoginData.getFactura().getFacturadet().get(position).getPrecioVenta() ));
                        }
                    }
                });

            }
        });


    }

    private void modificarTotal(){

        Double tot = new Double(0);
        for (FacturaDet element: LoginData.getFactura().getFacturadet()) {
            tot += element.getPrecioVenta() * element.getCantidad();
        }
        LoginData.getFactura().setImporte(tot);

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
        System.out.println("detalles antes de eliminar " + LoginData.getFactura().getFacturadet().size() );
        lista.remove(position);
        LoginData.getFactura().setFacturadet(lista);
        System.out.println("detalles despues de eliminar " + LoginData.getFactura().getFacturadet().size() );
//        lista.remove(position);
        notifyDataSetChanged();



    }
}
