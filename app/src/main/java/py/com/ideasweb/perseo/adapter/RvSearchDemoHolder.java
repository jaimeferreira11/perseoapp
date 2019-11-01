package py.com.ideasweb.perseo.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import net.steamcrafted.materialiconlib.MaterialIconView;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.utilities.Utilities;


/**
 * Created by lj on 3/28/2017.
 */

public class RvSearchDemoHolder extends RecyclerView.ViewHolder {

      TextView descripcion;
     ImageView image;
     TextView iva;
     TextView codigobarra;
     TextView precio;

    public RvSearchDemoHolder(final View itemView) {
        super(itemView);
        descripcion = (TextView) itemView.findViewById(R.id.ca_descripion);
        iva = (TextView) itemView.findViewById(R.id.ca_iva);
        codigobarra = (TextView) itemView.findViewById(R.id.ca_codigobarra);
        precio = (TextView) itemView.findViewById(R.id.ca_precio);
        image = (ImageView) itemView.findViewById(R.id.ca_image);


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


                            }
                        })
                        .negativeText(R.string.cancelar)
                        .show();


                // manipular el layout del dialog
                final View layout = dialog.getCustomView();
                int cont = 0;

                TextView descripcion = (TextView) layout.findViewById(R.id.aa_descripcion);
                final TextView precio= (TextView) layout.findViewById(R.id.aa_precio);
                final TextView monto= (TextView) layout.findViewById(R.id.aa_monto);
                final TextView cantidad= (TextView) layout.findViewById(R.id.aa_cantidad);
                TextView iva= (TextView) layout.findViewById(R.id.aa_id);

                MaterialIconView down = (MaterialIconView) layout.findViewById(R.id.aa_down);
                MaterialIconView up = (MaterialIconView) layout.findViewById(R.id.aa_up);

                down.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // TextView cantidad= (TextView) layout.findViewById(R.id.aa_cantidad);
                        int c = Integer.parseInt(cantidad.getText().toString());
                        c = c-1;
                        if(c > 0){
                            cantidad.setText(String.valueOf(c));
                            int m = 0;
                            System.out.println("precio:" + precio.getText().toString().replace(".", "") + " x " + c);
                            m = Integer.parseInt(precio.getText().toString().replace(".", "")) * c;
                            monto.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                        }


                    }
                });

                up.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int c = Integer.parseInt(cantidad.getText().toString());
                        c = c+1;
                        cantidad.setText(String.valueOf(c));
                        int m = 0;
                        System.out.println("precio:" + precio.getText().toString().replace(".", "") + " x " + c);
                        m = Integer.parseInt(precio.getText().toString().replace(".", "")) * c;
                        monto.setText(Utilities.stringNumberFormat(String.valueOf(m)));
                    }
                });






            }


        });
    }

    /*public void bind(String name) {
        tvName.setText(name);
    }*/
}



