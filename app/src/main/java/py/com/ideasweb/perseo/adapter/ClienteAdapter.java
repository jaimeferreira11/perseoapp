package py.com.ideasweb.perseo.adapter;

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

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.ui.activities.MainStepper;

/**
 * Created by jaime on 10/04/17.
 */

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ResultadoViewHolder>{
    private Context context;
    private List<Cliente> lista;
    private ArrayList<Cliente> listCopy;




    public ClienteAdapter(Context context, List<Cliente> lista) {
        this.context = context;
        this.lista = lista;
        this.listCopy = new ArrayList<>();
        listCopy.addAll(lista);
;
    }


    @Override
    public ClienteAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_articulo, parent, false);
        return new ClienteAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClienteAdapter.ResultadoViewHolder holder, int position) {
        final Cliente entity = lista.get(position);

        if(entity.getNombreApellido() != null)
        holder.descripcion.setText( entity.getNombreApellido().trim());
        if(entity.getNroDocumento() != null)
        holder.cod.setText(entity.getCodTipoDocumento() + " : " + entity.getNroDocumento().trim());
        if(entity.getTelefono() != null)
        holder.precio.setText("Tel: "+entity.getTelefono().trim());

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                new MaterialDialog.Builder(v.getContext())
                        .title("Registrar una factura al cliente?")
                        .icon(v.getResources().getDrawable(R.drawable.help_48))
                        .positiveText("Registrar")
                        .negativeText(v.getResources().getString(R.string.cancelar))
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent intent = new Intent(v.getContext(), MainStepper.class);
                                LoginData.getFactura().setIdCliente(entity.getIdCliente());
                                LoginData.getFactura().setNombreCliente(entity.getNombreApellido());
                                LoginData.getFactura().setNroDocumentoCliente(entity.getNroDocumento());
                                LoginData.getFactura().setTelefonoCliente(entity.getTelefono());
                                LoginData.getFactura().setDireccionCliente(entity.getDireccion());
                                LoginData.getFactura().setFacturadet(new ArrayList<FacturaDet>());
                                v.getContext().startActivity(intent);
                                ((MainActivity)v.getContext()).finish();

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView descripcion;
        private TextView cod;
        private TextView precio;
        private ImageView image;
        private CardView card;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            descripcion = (TextView) itemView.findViewById(R.id.ca_descripion);
            cod= (TextView) itemView.findViewById(R.id.ca_codigobarra);
            precio= (TextView) itemView.findViewById(R.id.ca_precio);
            image = (ImageView) itemView.findViewById(R.id.ca_image);
            card = (CardView) itemView.findViewById(R.id.card);



        }
    }


    public void filter(CharSequence sequence) {

        try {

            ArrayList<Cliente> temp = new ArrayList<>();
            if (sequence != null) {
                System.out.println("BUSQUEDA: " + sequence.toString());
                for (Cliente s : lista) {
                    if(s.getNroDocumento() != null){
                        if (s.getNombreApellido().toLowerCase().contains(sequence) || s.getNroDocumento().toLowerCase().contains(sequence)) {
                            temp.add(s);
                        }
                    }else{
                        if (s.getNombreApellido().toLowerCase().contains(sequence)) {
                            temp.add(s);
                        }
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
