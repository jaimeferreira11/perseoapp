package py.com.ideasweb.perseo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andremion.counterfab.CounterFab;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
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
    private ArrayList<Articulo> listCopy;


    public ArticuloAdapter(Context context, List<Articulo> lista, CounterFab counterFab) {
        this.context = context;
        this.lista = lista;
        this.counterFab = counterFab;
        this.listCopy = new ArrayList<>();
        listCopy.addAll(lista);
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
        holder.cod.setText("Cod. "+entity.getCodigoBarra());
        holder.precio.setText(Utilities.toStringFromDoubleWithFormat(entity.getPrecioVenta()));
        holder.moneda.setText(" Gs.");
        holder.idArticulo.setText(String.valueOf(entity.getIdArticulo()));



    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView descripcion;
        private TextView iva;
        private TextView cod;
        private TextView idArticulo;
        private TextView precio;
        private TextView moneda;
        private ImageView image;
        private CardView card;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            descripcion = (TextView) itemView.findViewById(R.id.ca_descripion);
            iva = (TextView) itemView.findViewById(R.id.ca_iva);
            cod= (TextView) itemView.findViewById(R.id.ca_codigobarra);
            idArticulo= (TextView) itemView.findViewById(R.id.ca_id);
            precio= (TextView) itemView.findViewById(R.id.ca_precio);
            moneda= (TextView) itemView.findViewById(R.id.ca_moneda);
            image = (ImageView) itemView.findViewById(R.id.ca_image);
            card = (CardView) itemView.findViewById(R.id.card);


            if(counterFab != null){

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

                                        if (cantidad2.getText().toString().length() == 0){
                                            cantidad2.setText("1");
                                        }

                                        articuloDTO.setIdArticulo(Integer.parseInt(idArticulo.getText().toString().trim()));
                                        articuloDTO.setTasaIva(Double.parseDouble(iva.getText().toString()));
                                        articuloDTO.setCodigoBarra(cod.getText().toString());
                                        articuloDTO.setDescripcion(descripcion.getText().toString());
                                        detalleDTO.setCantidad(Double.valueOf(cantidad2.getText().toString().replace(".", "")));
                                        detalleDTO.setPrecioVenta(Double.valueOf(precio.getText().toString().replace(".", "")));
                                        detalleDTO.setSubTotal(detalleDTO.getCantidad() * detalleDTO.getPrecioVenta());


                                        detalleDTO.setArticulo(articuloDTO);
                                        detalleDTO.setIdArticulo(articuloDTO.getIdArticulo());

                                       // int maxLength = (articuloDTO.getDescripcion().length() < 20)?articuloDTO.getDescripcion().length():20;
                                        //detalleDTO.setConcepto(articuloDTO.getDescripcion().substring(0, maxLength));
                                        detalleDTO.setConcepto(articuloDTO.getDescripcion());
                                        detalleDTO.setTasaIva(Double.parseDouble(iva.getText().toString()));

                                        if(detalleDTO.getTasaIva().intValue() == 10){
                                            detalleDTO.setImpuesto(detalleDTO.getSubTotal() / 11);
                                        }else if(detalleDTO.getTasaIva().intValue() == 5){
                                            detalleDTO.setImpuesto(detalleDTO.getSubTotal() / 21);
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
                        final EditText cantidad1= (EditText) layout.findViewById(R.id.aa_cantidad);
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


                                if(cantidad1.getText().toString().length() > 0){

                                    Double c = Double.parseDouble(cantidad1.getText().toString());
                                    c = c-1;
                                    if(c > 0){
                                        cantidad1.setText(String.valueOf(c));
                                        Double m = new Double(0);
                                        System.out.println("precio:" + precio1.getText().toString().replace(".", "") + " x " + c);
                                        m = Double.parseDouble(precio1.getText().toString().replace(".", "")) * c;
                                        monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                                    }
                                }


                            }
                        });

                        up.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(cantidad1.getText().toString().length() == 0){
                                    cantidad1.setText("0");
                                }

                                if(cantidad1.getText().toString().length() > 0){

                                    Double c = Double.parseDouble(cantidad1.getText().toString());
                                    c = c+1;
                                    cantidad1.setText(String.valueOf(c));
                                    Double m = new Double(0);
                                    System.out.println("precio:" + precio1.getText().toString().replace(".", "") + " x " + c);
                                    m = Double.parseDouble(precio1.getText().toString().replace(".", "")) * c;
                                    monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                                }
                            }
                        });




                        cantidad1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {

                                System.out.println("Cambio el foco? " + b);
                                if(!b){
                                    if(cantidad1.getText().toString().length() == 0){
                                        cantidad1.setText("1");
                                    }
                                }
                            }
                        });



                        cantidad1.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                System.out.println("BEFORE " + charSequence.toString());
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                System.out.println("ONTEXCHANGED " + charSequence.toString());
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {

                                System.out.println("AFTER " + editable.toString());

                                if(editable.toString().length() > 0){

                                    Double c = Double.parseDouble(editable.toString());
                                    Double m = new Double(0);
                                    System.out.println("precio:" + precio1.getText().toString().replace(".", "") + " x " + c);
                                    System.out.println(Integer.parseInt(precio1.getText().toString().replace(".", "")) * c);
                                    m = Double.parseDouble(precio1.getText().toString().replace(".", "")) * c;
                                    System.out.println(" = " + m.intValue());
                                    monto1.setText(Utilities.stringNumberFormat(String.valueOf(m.intValue())));
                                }


                            }
                        });



                    }


                });
            }


        }
    }


    public void filter(CharSequence sequence) {

        try {

            ArrayList<Articulo> temp = new ArrayList<>();
            if (sequence != null) {
                System.out.println("BUSQUEDA: " + sequence.toString());
                for (Articulo s : lista) {

                    if (s.getDescripcion().toLowerCase().contains(sequence) || s.getCodigoBarra().toLowerCase().contains(sequence)) {
                        temp.add(s);
                    }
                }
            } else {
                System.out.println("esta vacio");
                temp.addAll(listCopy);
            }
            System.out.println("Resultado: " + temp.size());
            lista.clear();
            lista.addAll(temp);
            notifyDataSetChanged();
            temp.clear();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
