package py.com.ideasweb.perseo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.andremion.counterfab.CounterFab;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.PedidoDetalle;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

public class DialogAdapter extends BaseAdapter {

  private LayoutInflater layoutInflater;
  private List<Facturadet> list;
  static CounterFab counterFab;


  public DialogAdapter(Context context, List<Facturadet> list, CounterFab counterFab) {
    layoutInflater = LayoutInflater.from(context);
    this.list =list;
    this.counterFab = counterFab;

  }

  @Override
  public int getCount() {
    return list.size();
  }

  @Override
  public Object getItem(int position) {
    return position;
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    final ViewHolder viewHolder;
    View view = convertView;
    final Facturadet entity = list.get(position);


    if (view == null) {

        view = layoutInflater.inflate(R.layout.cardview_detalle_pedido, parent, false);


      viewHolder = new ViewHolder();
      viewHolder.descripcion = (TextView) view.findViewById(R.id.cdp_descripion);
     // viewHolder.codigo = (TextView) view.findViewById(R.id.cdp_codigobarra);
      viewHolder.precio = (TextView) view.findViewById(R.id.cdp_precio);
      viewHolder.cantidad = (TextView) view.findViewById(R.id.cdp_cantidad);
      viewHolder.monto = (TextView) view.findViewById(R.id.cdp_monto);
      viewHolder.delete = (TextView) view.findViewById(R.id.delete_layout);
      viewHolder.edit = (TextView) view.findViewById(R.id.edit_layout);



      view.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) view.getTag();
    }

    viewHolder.descripcion.setText(entity.getConcepto());
    viewHolder.precio.setText(Utilities.toStringFromDoubleWithFormat(entity.getPrecioVenta()));
   // viewHolder.codigo.setText(entity.getArticulo().getCodigoBarraEan());
    viewHolder.cantidad.setText(Utilities.toStringFromDoubleWithFormat(Double.valueOf(entity.getCantidad())));
    viewHolder.monto.setText(Utilities.toStringFromDoubleWithFormat(entity.getSubTotal()));


    viewHolder.delete.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        UtilLogger.info("BORRAR");
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
                    counterFab.setCount(LoginData.getFactura().getFacturadet().size());
                    //modificar el header y el footer

                    Toast.makeText(v.getContext(), "Eliminado. Se actualizara al cerrar esta ventana" , Toast.LENGTH_SHORT).show();


                  }
                })
                .show();
      }
    });

    viewHolder.edit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
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
                    LoginData.getFactura().getFacturadet().get(position).setCantidad(Double.valueOf(cantidad2.getText().toString().replace(".", "")));
                    LoginData.getFactura().getFacturadet().get(position).setPrecioVenta(Double.valueOf(viewHolder.precio.getText().toString().replace(".", "")));
                    LoginData.getFactura().getFacturadet().get(position).setSubTotal(Double.valueOf(monto2.getText().toString().replace(".", "")));
                    LoginData.getFactura().getFacturadet().get(position).setIdArticulo(entity.getIdArticulo());
                    LoginData.getFactura().getFacturadet().get(position).setConcepto(entity.getConcepto());

                    if(entity.getTasaIva() == 10){
                      LoginData.getFactura().getFacturadet().get(position).setImpuesto(entity.getSubTotal() / 11);
                    }else if(entity.getTasaIva() == 5){
                      LoginData.getFactura().getFacturadet().get(position).setImpuesto(entity.getSubTotal() / 1.1);
                    }else{
                      LoginData.getFactura().getFacturadet().get(position).setImpuesto(entity.getSubTotal());
                    }

                    //modifica la cabecera
                    Double tot = new Double(0);
                    for (Facturadet element: LoginData.getFactura().getFacturadet()) {
                      tot += element.getPrecioVenta() * element.getCantidad();
                    }
                    LoginData.getFactura().setImporte(tot);
                    //actualizar el floating
                    counterFab.setCount(LoginData.getFactura().getFacturadet().size());
                    Toast.makeText(v.getContext(), "Editado. Se actualizara al cerrar esta ventana" , Toast.LENGTH_SHORT).show();

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
        descripcion1.setText(viewHolder.descripcion.getText().toString());
        precio1.setText(viewHolder.precio.getText().toString());
        monto1.setText(viewHolder.monto.getText().toString());
        cantidad1.setText(viewHolder.cantidad.getText().toString());


        MaterialIconView down = (MaterialIconView) layout.findViewById(R.id.aa_down);
        MaterialIconView up = (MaterialIconView) layout.findViewById(R.id.aa_up);

        down.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int c = Integer.parseInt(cantidad1.getText().toString());
            c = c-1;
            if(c > 0){
              cantidad1.setText(String.valueOf(c));
              int m = 0;
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
            m = Integer.parseInt(precio1.getText().toString().replace(".", "")) * c;
            monto1.setText(Utilities.stringNumberFormat(String.valueOf(m)));
          }
        });

      }
    });

    Context context = parent.getContext();
    /*switch (position) {
      case 0:
        viewHolder.textView.setText(context.getString(R.string.google_plus_title));
        viewHolder.imageView.setImageResource(R.drawable.ic_google_plus_icon);
        break;
      case 1:
        viewHolder.textView.setText(context.getString(R.string.google_maps_title));
        viewHolder.imageView.setImageResource(R.drawable.ic_google_maps_icon);
        break;
      default:
        viewHolder.textView.setText(context.getString(R.string.google_messenger_title));
        viewHolder.imageView.setImageResource(R.drawable.ic_google_messenger_icon);
        break;
    }*/

    return view;
  }

  static class ViewHolder {
    TextView descripcion;
    TextView edit;
    TextView delete;
    TextView precio;
    TextView cantidad;
    TextView monto;
    //ImageView imageView;
  }

  public void removeItem(int position) {
    list.remove(position);
    notifyDataSetChanged();

  }
}
