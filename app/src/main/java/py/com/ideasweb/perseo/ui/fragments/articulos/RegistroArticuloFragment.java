package py.com.ideasweb.perseo.ui.fragments.articulos;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroArticuloFragment extends Fragment {


    @BindView(R.id.text_busqueda)
    EditText busqueda;

    @BindView(R.id.buscar)
    ImageView btnBuscar;

    @BindView(R.id.codigo)
    EditText codigo;
    @BindView(R.id.descripcion)
    EditText descripcion;
    @BindView(R.id.codigobarra)
    EditText codigobarra;
    @BindView(R.id.precio)
    EditText precio;
    @BindView(R.id.cantidad)
    EditText cantidad;
    @BindView(R.id.iva)
    MaterialBetterSpinner iva;
    @BindView(R.id.checkEstado)
    CheckBox checkEstado;
    @BindView(R.id.grabar)
    Button btngGrabar;


    Articulo articulo;
    Unbinder unbinder;

    String[] TIPOIVA =  {"10%", "5%", "EXENTA"};
    public RegistroArticuloFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_articulo, container, false);
        unbinder = ButterKnife.bind(this, view);


        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, TIPOIVA);
        iva.setAdapter(tipoAdapter);
        iva.setText(TIPOIVA[0]);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Validation.hasText(busqueda, getContext())){

                    buscar(busqueda.getText().toString().trim());

                }
            }
        });


        btngGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()){


                    articulo.setDescripcion(descripcion.getText().toString());
                    articulo.setCodigoBarra(codigobarra.getText().toString());
                    articulo.setCantidad(Double.parseDouble(cantidad.getText().toString()));
                    articulo.setPrecioVenta(Double.parseDouble(precio.getText().toString()));
                    articulo.setSincronizar(true);
                    articulo.setEstado(checkEstado.isChecked());

                    switch (iva.getText().toString()){
                        case "10%":
                            articulo.setTasaIva(new Double(10));
                            break;
                        case "5%":
                            articulo.setTasaIva(new Double(5));
                            break;
                        case "EXENTA":
                            articulo.setTasaIva(new Double(0));
                            break;
                        default:
                            break;

                    }





                    grabar(articulo);

                }
            }
        });


        return view;
    }

    public void buscar(String param){

        AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        ConstructorArticulos ca = new ConstructorArticulos();


        final List<Articulo> busqueda = ca.getArticuloByParam(param);


        if(busqueda.isEmpty()){
            new MaterialDialog.Builder(getContext())
                    .icon(getResources().getDrawable(R.drawable.attention_48))
                    .title("Atencion!")
                    .content("No se encontro el articulo.")
                    .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .positiveText("Aceptar")
                    .show();
        }else{

            new MaterialDialog.Builder(getContext())
                    .title("Resultado de la Busqueda")
                    .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .items(busqueda)
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            //setear el cliente
                            setData(busqueda.get(which));
                            UtilLogger.info(which + " - " + text);

                            return true;
                        }

                    })

                    .positiveText("Aceptar")
                    .negativeText("Cancelar")
                    .show();
        }



        dialog.dismiss();

    }

    private void setData(Articulo articulo) {

        codigo.setText(articulo.getIdArticulo());
        codigobarra.setText(articulo.getCodigoBarra());
        cantidad.setText(String.valueOf(articulo.getCantidad()));
        precio.setText(String.valueOf(articulo.getPrecioVenta()));
        descripcion.setText(articulo.getDescripcion());
        checkEstado.setChecked(articulo.getEstado());

        switch (articulo.getTasaIva().intValue()){
            case 10:
                iva.setText(TIPOIVA[0]);
                break;
            case 5:
                iva.setText(TIPOIVA[1]);
                break;
            case 0:
                iva.setText(TIPOIVA[2]);
                break;
                default:
                    break;

        }
    }


    public void grabar(Articulo articulo){


        AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();

        articulo.save();

        dialog.dismiss();

        new MaterialDialog.Builder(getContext())
                .icon(getResources().getDrawable(R.drawable.checked_48))
                .title(getResources().getString(R.string.procesoExitoso))
                .content("Articulo registrado!")
                .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                .positiveText("Aceptar")
                .show();

        clear();

    }


    public void clear(){
        codigo.setText("0");
        descripcion.setText(null);
        codigo.setText(null);
        precio.setText(null);
        cantidad.setText(null);
        iva.setText(TIPOIVA[0]);
        checkEstado.setChecked(true);
        //ciudad.setSelection(-1);
        //dpto.setText("");
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(descripcion, getContext())) ret = false;
        if (!Validation.hasText(codigo, getContext())) ret = false;
        if (!Validation.hasText(codigobarra, getContext())) ret = false;
        if (!Validation.hasText(precio, getContext())) ret = false;
        if (!Validation.hasText(cantidad, getContext())) ret = false;


        if(iva.getText().toString().length() == 0){
            ret = false;
        }

        UtilLogger.info(ret + "");
        return ret;
    }

}
