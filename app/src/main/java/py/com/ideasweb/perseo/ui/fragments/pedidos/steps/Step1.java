package py.com.ideasweb.perseo.ui.fragments.pedidos.steps;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.PedidoDetalle;
import py.com.ideasweb.perseo.ui.activities.LoginActivity;
import py.com.ideasweb.perseo.ui.activities.MainActivity;
import py.com.ideasweb.perseo.ui.activities.MainStepper;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Validation;

/**
 * Created by jaime on 09/11/17.
 */

public class Step1 extends AbstractStep {

    View view;
    //MaterialBetterSpinner tipopersona;
    ImageView buscar;
    EditText nombres;
    EditText id ;
    EditText  busqueda;
    EditText direccion;
    EditText telefono ;
    EditText ciudad;
    EditText barrio;
    FloatingActionButton fab;


    String[] TIPODOC = {"CI", "RUC", "DNI"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.layout_step1, container, false);


        buscar = (ImageView) view.findViewById(R.id.cliente_buscar);
        nombres = (EditText) view.findViewById(R.id.regNombres);
        id = (EditText) view.findViewById(R.id.regID);
        busqueda = (EditText) view.findViewById(R.id.text_busqueda);

        direccion = (EditText) view.findViewById(R.id.regDireccion);
        telefono = (EditText) view.findViewById(R.id.regTelefono);
        ciudad = (EditText) view.findViewById(R.id.regCiudad);
        barrio = (EditText) view.findViewById(R.id.regBarrio);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        // SI SE EDITA UN PEDIDO EXISTENTE
        if(LoginData.getFactura().getIdCliente() != null){

            nombres.setText(LoginData.getFactura().getNroDocumentoCliente());
            direccion.setText(LoginData.getFactura().getDireccionCliente());
            telefono.setText(LoginData.getFactura().getTelefonoCliente());
            id.setText(String.valueOf(LoginData.getFactura().getIdCliente()));

        }else{

            LoginData.getFactura().setFacturadet(new ArrayList<Facturadet>());
        }

        //dapatador
       /* ArrayAdapter<String> tipoAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, TIPODOC);
        tipopersona = (MaterialBetterSpinner) view.findViewById(R.id.regTipoPersona);
        tipopersona.setAdapter(tipoAdapter);
        tipopersona.setText(TIPODOC[0]);*/

        buscar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if(Validation.hasText(busqueda, getContext())){

                    buscar(busqueda.getText().toString().trim());

                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                ((MainStepper)view.getContext()).finish();

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


        final List<Cliente> busqueda = LitePal.where("upper(nombreApellido) like ?  or nroDocumento like ?" , "%"+param.toUpperCase().trim()+"%", "%"+param.trim()+"%").find(Cliente.class);


        if(busqueda.isEmpty()){
            new MaterialDialog.Builder(getContext())
                    .icon(getResources().getDrawable(R.drawable.attention_48))
                    .title("Atencion!")
                    .content("No se encontro el cliente.")
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
                            setCliente(busqueda.get(which));
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

    public void setCliente(Cliente cliente){



        LoginData.getFactura().setIdCliente(cliente.getIdCliente());
        LoginData.getFactura().setNombreCliente(cliente.getNombreApellido());
        LoginData.getFactura().setNroDocumentoCliente(cliente.getNroDocumento());
        LoginData.getFactura().setTelefonoCliente(cliente.getTelefono());
        LoginData.getFactura().setDireccionCliente(cliente.getDireccion());



        nombres.setText(cliente.getNombreApellido());
        direccion.setText(cliente.getDireccion());
        telefono.setText(cliente.getTelefono());
        id.setText(String.valueOf(cliente.getIdCliente()));

        if(cliente.getCiudad() != null){
            ciudad.setText(cliente.getCiudad());
        }
        if(cliente.getBarrio() != null){
            barrio.setText(cliente.getBarrio());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);

    }

    @Override
    public String name() {
        return "Tab " + getArguments().getInt("position", 0);
    }

    @Override
    public boolean isOptional() {
        return false;
    }


    @Override
    public void onStepVisible() {

    }


    @Override
    public void onNext() {
        super.onNext();
    }

    @Override
    public void onPrevious() {
        System.out.println("onPrevious");
    }

    @Override
    public String optional() {
        return "You can skip";
    }

    @Override
    public boolean nextIf() {

        return checkValidation();
    }

    @Override
    public String error() {
        return "<b>Favor, Seleccione el Cliente</b>";
    }

    public boolean checkValidation() {

        if(LoginData.getFactura().getIdCliente() == null){
            return false;
        }
        return true;
    }



}
