package py.com.ideasweb.perseo.adapter;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import py.com.ideasweb.perseo.models.Facturacablog;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.activities.EditarFacturaActivity;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.ui.activities.MainStepper;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 10/04/17.
 */

public class FacturaLogAdapter extends RecyclerView.Adapter<FacturaLogAdapter.ResultadoViewHolder>{
    private Context context;
    private List<Facturacablog> lista;
    private ArrayList<Facturacablog> listCopy;
    private static RecyclerView rv;
    RecyclerView.Adapter adapter = this;



    public FacturaLogAdapter(Context context, List<Facturacablog> lista) {
        this.context = context;
        this.lista = lista;
        this.listCopy = new ArrayList<>();
        listCopy.addAll(lista);

    }


    @Override
    public FacturaLogAdapter.ResultadoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_factura_log, parent, false);
        return new FacturaLogAdapter.ResultadoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FacturaLogAdapter.ResultadoViewHolder holder, final int position) {
        final Facturacablog task = lista.get(position);
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
        final CardView button = holder.card;
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PopupMenu popup = new PopupMenu(context, button);

                popup.inflate(R.menu.log_menu);

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
                                        .positiveText("Cerrar").build();


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

                                List<FacturaDet> detList = new ArrayList<>();
                                for (Facturadetlog detlog: task.getFacturadet()){
                                    FacturaDet det = new FacturaDet(detlog);
                                    detList.add(det);
                                }

                                generarLineaLayoutVertical(v.getContext());
                                inicializarAdaptadorRV(crearAdaptador(v.getContext(), detList));

                                return true;
                            case R.id.cambiar:


                                new MaterialDialog.Builder(v.getContext())
                                        .icon(v.getContext().getResources().getDrawable(R.drawable.help_48))
                                        .title("Editar numeracion (Actual "+task.getNumeroFactura()+")")
                                        .content("Al aceptar, la factura aparecera entre la lista a sincronizar.")
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
                                                restaurar(task, v.getContext());

                                            }
                                        }).show();

                                return true;
                            case R.id.editar:

                                EditarFacturaActivity.factura = task;

                                Intent intent = new Intent(context, EditarFacturaActivity.class);
                                context.startActivity(intent);


                                return true;
                            case R.id.restaurar:

                                new MaterialDialog.Builder(v.getContext())
                                        .title("Desea restaurar la factura?")
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
     //   private LinearLayout layoutOptions;
        private CardView card;



        public ResultadoViewHolder(final View itemView) {
            super(itemView);

            cliente = (TextView) itemView.findViewById(R.id.cliente);
            nro = (TextView) itemView.findViewById(R.id.nro);
            fecha= (TextView) itemView.findViewById(R.id.fecha);
            monto= (TextView) itemView.findViewById(R.id.monto);
            documento= (TextView) itemView.findViewById(R.id.documento);
            anulado = (TextView) itemView.findViewById(R.id.anulado);
         //   layoutOptions = (LinearLayout) itemView.findViewById(R.id.layoutOptions);
            card = (CardView) itemView.findViewById(R.id.cvPedido);



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
        lista.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, lista.size());
    }



    public static void restaurar(final Facturacablog cab, final Context context){

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        ConstructorFactura constructorFac = new ConstructorFactura();
        ConstructorFacturaLog cLog = new ConstructorFacturaLog();


        FacturaCab restaurar = new FacturaCab(cab);


        List<FacturaDet> detList = new ArrayList<>();
        for (Facturadetlog detlog: cab.getFacturadet()){
            FacturaDet det = new FacturaDet(detlog);
            detList.add(det);
        }
        restaurar.setFacturadet(detList);

        // insertan nuevamente la factura
        restaurar.setSincronizadoCore(false);
        constructorFac.insertar(restaurar);



        dialog.dismiss();

        ((MainActivity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(context)
                        .title(context.getString(R.string.procesoExitoso))
                        .content("Factura nro " + cab.getNumeroFactura() + " restaurada correctamente")
                        .icon(context.getResources().getDrawable(R.drawable.checked_48))
                        .titleColor(context.getResources().getColor(R.color.colorPrimaryDark))
                        .positiveText("Aceptar")
                        .show();

            }
        });

        // borra la factura en el log
        cLog.borrarFactura(cab);


    }



    public void filter(CharSequence sequence) {

        try {

            ArrayList<Facturacablog> temp = new ArrayList<>();
            if (sequence != null) {
                System.out.println("BUSQUEDA: " + sequence.toString());
                for (Facturacablog s : lista) {
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
}
