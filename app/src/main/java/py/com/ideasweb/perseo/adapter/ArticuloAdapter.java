package py.com.ideasweb.perseo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andremion.counterfab.CounterFab;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 10/04/17.
 */

public class ArticuloAdapter extends RecyclerView.Adapter<ArticuloAdapter.ResultadoViewHolder>{
    private Context context;
    private List<Articulo> lista;
    static CounterFab counterFab;


    public ArticuloAdapter(Context context, List<Articulo> lista, CounterFab counterFab) {
        this.context = context;
        this.lista = lista;
        this.counterFab = counterFab;
    }


    @Override
    public ArticuloAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_articulo, parent, false);
        return new ArticuloAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticuloAdapter.ResultadoViewHolder holder, int position) {
        Articulo entity = lista.get(position);

        holder.iva.setText(String.valueOf(entity.getTasaIva()));
        holder.descripcion.setText( entity.getDescripcion());
        holder.cod.setText(entity.getCodigoBarra());
        holder.precio.setText(Utilities.toStringFromDoubleWithFormat(entity.getPrecioVenta()));



    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView descripcion;
        private TextView iva;
        private TextView cod;
        private TextView precio;
        private ImageView image;
        private CardView card;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            descripcion = (TextView) itemView.findViewById(R.id.ca_descripion);
            iva = (TextView) itemView.findViewById(R.id.ca_iva);
            cod= (TextView) itemView.findViewById(R.id.ca_codigobarra);
            precio= (TextView) itemView.findViewById(R.id.ca_precio);
            image = (ImageView) itemView.findViewById(R.id.ca_image);
            card = (CardView) itemView.findViewById(R.id.card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean wrapInScrollView = true;
                    MaterialDialog dialog = new MaterialDialog.Builder(itemView.getContext())
                            //.title("Mensaje")
                            .customView(R.layout.layout_agregar_articulo, wrapInScrollView)
                            // .content("Favor, conectese a internet y sincronize")
                            //  .icon(R.drawable.info)
                            .positiveText(itemView.getResources().getString(R.string.aceptar))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    View layout = dialog.getCustomView();
                                    final TextView monto2= (TextView) layout.findViewById(R.id.aa_monto);
                                    final TextView cantidad2= (TextView) layout.findViewById(R.id.aa_cantidad);

                                    Facturadet detalleDTO = new Facturadet();
                                    Articulo articuloDTO = new Articulo();

                                    articuloDTO.setTasaIva(Double.parseDouble(iva.getText().toString()));
                                    articuloDTO.setCodigoBarra(cod.getText().toString());
                                    articuloDTO.setDescripcion(descripcion.getText().toString());
                                    detalleDTO.setCantidad(Double.valueOf(cantidad2.getText().toString().replace(".", "")));
                                    detalleDTO.setPrecioVenta(Double.valueOf(precio.getText().toString().replace(".", "")));
                                    detalleDTO.setSubTotal(Double.valueOf(monto2.getText().toString().replace(".", "")));


                                    detalleDTO.setArticulo(articuloDTO);
                                    detalleDTO.setIdArticulo(articuloDTO.getIdArticulo());
                                    detalleDTO.setConcepto(articuloDTO.getDescripcion());
                                    detalleDTO.setTasaIva(Double.parseDouble(iva.getText().toString()));

                                    if(detalleDTO.getTasaIva().intValue() == 10){
                                        detalleDTO.setImpuesto(detalleDTO.getSubTotal() / 11);
                                    }else if(detalleDTO.getTasaIva().intValue() == 5){
                                        detalleDTO.setImpuesto(detalleDTO.getSubTotal() / 1.1);
                                    }else{
                                        detalleDTO.setImpuesto(detalleDTO.getSubTotal());
                                    }

                                    //agrega el detalle
                                    LoginData.getFactura().getFacturadet().add(detalleDTO);
                                    //modifica la cabecera
                                    Double tot = new Double(0);
                                    for (Facturadet element: LoginData.getFactura().getFacturadet()) {
                                        tot += element.getPrecioVenta() * element.getCantidad();
                                    }
                                    LoginData.getFactura().setImporte(tot);
                                    //actualizar el floating
                                    counterFab.setCount(LoginData.getFactura().getFacturadet().size());

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
                    final TextView cantidad1= (TextView) layout.findViewById(R.id.aa_cantidad);
                    TextView id1= (TextView) layout.findViewById(R.id.aa_id);
                    //
                    descripcion1.setText(descripcion.getText().toString());
                    precio1.setText(precio.getText().toString());
                    monto1.setText(precio1.getText().toString());


                    MaterialIconView down = (MaterialIconView) layout.findViewById(R.id.aa_down);
                    MaterialIconView up = (MaterialIconView) layout.findViewById(R.id.aa_up);

                    down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TextView cantidad= (TextView) layout.findViewById(R.id.aa_cantidad);
                            int c = Integer.parseInt(cantidad1.getText().toString());
                            c = c-1;
                            if(c > 0){
                                cantidad1.setText(String.valueOf(c));
                                int m = 0;
                                System.out.println("precio:" + precio1.getText().toString().replace(".", "") + " x " + c);
                                m = Integer.parseInt(precio1.getText().toString().replace(".", "")) * c;
                                monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                            }


                        }
                    });

                    up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            int c = Integer.parseInt(cantidad1.getText().toString());
                            c = c+1;
                            cantidad1.setText(String.valueOf(c));
                            int m = 0;
                            System.out.println("precio:" + precio1.getText().toString().replace(".", "") + " x " + c);
                            m = Integer.parseInt(precio1.getText().toString().replace(".", "")) * c;
                            monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                        }
                    });



                }


            });

        }
    }
}
