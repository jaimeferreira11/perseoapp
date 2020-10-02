package py.com.ideasweb.perseo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chauthai.swipereveallayout.SwipeRevealLayout;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.constructor.ConstructorFacturaLog;
import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.FacturaDet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.ui.activities.MainStepper;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 10/04/17.
 */

public class FacturaAdapter extends RecyclerView.Adapter<FacturaAdapter.ResultadoViewHolder>{
    private Context context;
    private List<FacturaCab> lista;
    private ArrayList<FacturaCab> listCopy;
    private static RecyclerView rv;
    RecyclerView.Adapter adapter = this;
    String state;



    public FacturaAdapter(Context context, List<FacturaCab> lista, String state) {
        this.context = context;
        this.lista = lista;
        this.state = state;
        this.listCopy = new ArrayList<>();
        listCopy.addAll(lista);

    }


    @Override
    public FacturaAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_pedido, parent, false);
        return new FacturaAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FacturaAdapter.ResultadoViewHolder holder, final int position) {
        final FacturaCab task = lista.get(position);
        Double aux = new Double(1);
        if(task.getPorcDescuento() != null){
            if(task.getPorcDescuento() > 0){
                aux = (100 - task.getPorcDescuento()) / 100 ;
            }
        }
        final Formatter fmt = new Formatter();

        holder.cliente.setText(task.getNombreCliente());
        holder.nro.setText(String.valueOf(fmt.format("%08d",task.getNumeroFactura())));
        if(task.getPorcDescuento() != null)
        holder.documento.setText(task.getNroDocumentoCliente());
        holder.fecha.setText(task.getFecha());
        holder.monto.setText(Utilities.toStringFromDoubleWithFormat(task.getImporte() * aux) + " Gs.");

        if(!task.getEstado())
            holder.anulado.setVisibility(View.VISIBLE);



        // boton de opciones
        final CardView button = holder.cvPedido;
        holder.cvPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(context, button);


                if(state.equalsIgnoreCase("Pendientes")) {
                    popup.inflate(R.menu.factura_menu);
                }else if(state.equalsIgnoreCase("Sincronizados")){
                    popup.inflate(R.menu.factura_menu_sinc);
                }else {
                    popup.inflate(R.menu.factura_menu2);
                }


                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.ver:

                                System.out.println(task.toString());
                                boolean wrapInScrollView = true;
                                MaterialDialog dialog = new MaterialDialog.Builder(context)
                                        //.title("Mensaje")
                                        .customView(R.layout.layout_detalle_pedido, wrapInScrollView)
                                        .negativeText("Reimprimir")
                                        .positiveText("Cerrar")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Toast.makeText(v.getContext(), "Reimprimiendo la factura", Toast.LENGTH_SHORT).show();


                                                ((MainActivity)context).buscarImpresora(task);

                                            }
                                        }).build();

                                if(state.equals("Anulados")){

                                    dialog = new MaterialDialog.Builder(context)
                                            //.title("Mensaje")
                                            .customView(R.layout.layout_detalle_pedido, wrapInScrollView)
                                            .positiveText("Cerrar")
                                            .build();
                                }

                                dialog.show();

                                final View layout = dialog.getCustomView();
                                TextView detcliente = (TextView) layout.findViewById(R.id.detCliente);
                                TextView dettotal = (TextView) layout.findViewById(R.id.detTotal);
                                TextView detNro = (TextView) layout.findViewById(R.id.detNro);
                                rv = (RecyclerView) layout.findViewById(R.id.rvDetallePedido);

                                detcliente.setText(holder.cliente.getText().toString());
                                dettotal.setText(holder.monto.getText().toString());
                                detNro.setText("Detalles factura: " +new Formatter().format("%08d",task.getNumeroFactura()));

                                //buscar el detalle por el cod
                                List<FacturaDet> detalles =  task.getFacturadet();


                                generarLineaLayoutVertical(v.getContext());
                                inicializarAdaptadorRV(crearAdaptador(v.getContext(), detalles));

                                return true;
                            case R.id.cambiar:


                                new MaterialDialog.Builder(v.getContext())
                                        .icon(v.getContext().getResources().getDrawable(R.drawable.help_48))
                                        .title("Editar numeracion (Nro. actual: "+task.getNumeroFactura()+")")
                                        .negativeText(R.string.cancelar)
                                        .positiveText("Si")
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .inputType(InputType.TYPE_CLASS_NUMBER )
                                        .input("Ingrese el nuevo nro. de factura", "", new MaterialDialog.InputCallback() {
                                            @Override
                                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                                // Do something

                                                if(input.toString().equals("") || !Utilities.isNumeric(input.toString().trim()) ){
                                                    return;
                                                }
                                                task.setNumeroFactura(Integer.parseInt(input.toString().trim()));
                                                task.update(task.getId());
                                                holder.nro.setText(input.toString().trim());


                                                new MaterialDialog.Builder(context)
                                                        .title("Nro de factura modificado  correctamente")
                                                        .content("Favor verifique la consistencia del talonario")
                                                        .icon(context.getResources().getDrawable(R.drawable.checked_48))
                                                        .titleColor(context.getResources().getColor(R.color.colorPrimaryDark))
                                                        .positiveText("Aceptar")
                                                        .show();




                                            }
                                        }).show();


                                return true;
                            case R.id.editar:


                                if(!task.getSincronizadoCore()){
                                    new MaterialDialog.Builder(v.getContext())
                                            .title("Deseas editar esta factura?")
                                            .icon(v.getResources().getDrawable(R.drawable.help_48))
                                            .positiveText("Editar")
                                            .negativeText(v.getResources().getString(R.string.cancelar))
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    Intent intent = new Intent(v.getContext(), MainStepper.class);
                                                    LoginData.setFactura(task);
                                                    v.getContext().startActivity(intent);
                                                    ((MainActivity)v.getContext()).finish();

                                                }
                                            })
                                            .show();
                                }else{
                                    Toast.makeText(context, "No puedes editar una factura sincronizada", Toast.LENGTH_LONG).show();
                                }


                                return true;
                            case R.id.anular:

                                if(!task.getSincronizadoCore()){
                                    new MaterialDialog.Builder(v.getContext())
                                            .title("Anular Factura?")
                                            .icon(v.getResources().getDrawable(R.drawable.help_48))
                                            .positiveText("Anular")
                                            .negativeText(v.getResources().getString(R.string.cancelar))
                                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                @Override
                                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                    removeItem(position);
                                                    //recalcula el total
                                                }
                                            })
                                            .show();
                                }else{
                                    Toast.makeText(context, "No puedes anular una factura sincronizada", Toast.LENGTH_LONG).show();
                                }

                                return true;

                            case R.id.restaurar:

                                new MaterialDialog.Builder(v.getContext())
                                        .title("Desea marcar como NO sincronizado?")
                                        .content("La misma aparecerá en la lista para sincronizar nuevamente.")
                                        .icon(v.getContext().getDrawable(R.drawable.help_48))
                                        .positiveText(v.getContext().getString(R.string.aceptar))
                                        .negativeText(v.getContext().getString(R.string.cancelar))
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                restaurar(task, v.getContext());
                                            }
                                        })
                                        .show();


                                return true;
                        }
                        return false;
                    }
                });

                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ResultadoViewHolder extends RecyclerView.ViewHolder{

        private TextView cliente;
        private TextView nro;
        private TextView fecha;
        private TextView monto;
        private TextView documento;
        private TextView anulado;
        private CardView cvPedido;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            cliente = (TextView) itemView.findViewById(R.id.cliente);
            nro = (TextView) itemView.findViewById(R.id.nro);
            fecha= (TextView) itemView.findViewById(R.id.fecha);
            monto= (TextView) itemView.findViewById(R.id.monto);
            documento= (TextView) itemView.findViewById(R.id.documento);
            anulado = (TextView) itemView.findViewById(R.id.anulado);
            cvPedido = (CardView) itemView.findViewById(R.id.cvPedido);



        }
    }


    public static void generarLineaLayoutVertical(Context context) {
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
    }


    public static FacturaDetAdapter2 crearAdaptador(Context context, List<FacturaDet> lista) {
        FacturaDetAdapter2 adaptador = new FacturaDetAdapter2(context, lista);
        return adaptador;
    }

    public static void inicializarAdaptadorRV(FacturaDetAdapter2 adapatador) {
        rv.setAdapter(adapatador);

    }

    private void removeItem(int position) {
        // cancelar el pedido por api
        cancelarPedido(lista.get(position), context);
        lista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lista.size());
    }



    public static void cancelarPedido(final FacturaCab cab, final Context context){

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        ConstructorFactura constructorFac = new ConstructorFactura();

        cab.setEstado(false);
        constructorFac.actualizar(cab);

        dialog.dismiss();

        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.procesoExitoso))
                        .content("Factura nro " + cab.getNumeroFactura() + " anulado correctamente")
                        .icon(context.getResources().getDrawable(R.drawable.checked_48))
                        .titleColor(context.getResources().getColor(R.color.colorPrimaryDark))
                        .positiveText("Aceptar")
                        .show();

            }
        });


    }



    public void filter(CharSequence sequence) {

        try {

            ArrayList<FacturaCab> temp = new ArrayList<>();
            if (sequence != null) {
                System.out.println("BUSQUEDA: " + sequence.toString());
                for (FacturaCab s : lista) {
                    if (s.getNombreCliente().toLowerCase().contains(sequence) || String.valueOf(s.getNumeroFactura()).contains(sequence)) {
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

    public static void restaurar(final FacturaCab cab, final Context context){

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        ConstructorFactura constructorFac = new ConstructorFactura();

        // se marca como no sincronizado
        cab.setSincronizadoCore(false);
        constructorFac.actualizar(cab);


        dialog.dismiss();

        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.procesoExitoso))
                        .content("Factura nro " + cab.getNumeroFactura() + " restaurada correctamente. Recargue la lista")
                        .icon(context.getResources().getDrawable(R.drawable.checked_48))
                        .titleColor(context.getResources().getColor(R.color.colorPrimaryDark))
                        .positiveText("Aceptar")
                        .show();

            }
        });

    }
}
