package py.com.ideasweb.perseo.ui.fragments.cliente;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.litepal.LitePal;

import java.util.List;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.manager.ClienteManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.MiUbicacion;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;
import py.com.ideasweb.perseo.utilities.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroClienteFragment extends Fragment {

    View view;
    EditText nombres;
    EditText  cedula;
    EditText direccion;
    EditText telefono ;
    EditText ciudad ;
    EditText barrio ;
    MaterialBetterSpinner regTipoPersona;
    Button grabar;







    //SearchableSpinner ciudad;
    //SearchableSpinner dpto;
    // SearchableSpinner barrio;

    //ArrayAdapter<Ciudad> ciudadAdapter;
    //ArrayAdapter<Departamento> dptoAdapter;
    //ArrayAdapter<Barrio> barrioAdapter;

   // Ciudad ciudadAux;
    //Departamento dptoAux;
   // Barrio barrioAux;

    Cliente cliente;

    String[] TIPOSPER =  {"CI", "RUC", "DNI"};


    public RegistroClienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registro_cliente, container, false);

        nombres = (EditText) view.findViewById(R.id.regNombres);
        cedula = (EditText) view.findViewById(R.id.regCedula);
        direccion = (EditText) view.findViewById(R.id.regDireccion);
        telefono = (EditText) view.findViewById(R.id.regTelefono);
        ciudad = (EditText) view.findViewById(R.id.regCiudad);
        barrio = (EditText) view.findViewById(R.id.regBarrio);

        grabar = (Button) view.findViewById(R.id.regGuardar);

        regTipoPersona = (MaterialBetterSpinner) view.findViewById(R.id.regTipoPersona) ;


        ArrayAdapter<String> tipoAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_dropdown_item_1line, TIPOSPER);
        regTipoPersona.setAdapter(tipoAdapter);
        regTipoPersona.setText(TIPOSPER[0]);


        cliente = new Cliente();

        cedula.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(Validation.hasText(cedula, getContext())){

                        buscar(cedula.getText().toString());

                    }
                }
            }
        });



        grabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValidation()){


                    cliente.setNroDocumento(cedula.getText().toString());
                    cliente.setNombreApellido(nombres.getText().toString());
                    cliente.setDireccion(direccion.getText().toString());
                    cliente.setTelefono(telefono.getText().toString());
                    cliente.setCiudad(ciudad.getText().toString());
                    cliente.setBarrio(barrio.getText().toString());
                    cliente.setCodTipoDocumento(regTipoPersona.getText().toString());
                    cliente.setCoordenadas(MiUbicacion.getCoordenadasActual());
                    cliente.setSincronizar(true);
                    cliente.setIdEmpresa(ConstantesRestApi.ID_EMPRESA);

                    if(CredentialValues.getLoginData().getPerfilactual().getIdPerfil() == 1){
                        //si es administrador
                        if(Utilities.isNetworkConnected(getContext())){
                            subirCliente(cliente);
                        }else{
                            Utilities.sendToast(getContext(), "Necesitas conexion a internet para realizar esta accion", "error");
                        }

                    }else{
                        grabar(cliente);
                    }

                }
            }
        });


        return view;
    }

    public void clear(){
        cedula.setText(null);
        nombres.setText(null);
        direccion.setText(null);
        telefono.setText(null);
        barrio.setText(null);
        ciudad.setText(null);
        regTipoPersona.setText(TIPOSPER[0]);
        //ciudad.setSelection(-1);
        //dpto.setText("");
        cliente = new Cliente();
    }

    public void grabar(Cliente cliente){


        AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();

        cliente.save();

        dialog.dismiss();

        new MaterialDialog.Builder(getContext())
                .icon(getResources().getDrawable(R.drawable.checked_48))
                .title(getResources().getString(R.string.procesoExitoso))
                .content("El cliente registrado!")
                .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                .positiveText("Aceptar")
                .show();

        clear();

    }

    private void subirCliente(final Cliente cliente){

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        final ConstructorCliente cc = new ConstructorCliente();
        final ClienteManager manager = new ClienteManager();
        new Thread(new Runnable() {
            public void run() {
                try {
                    Respuesta respuesta = manager.addCliente(cliente);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    });

                    if(respuesta.getEstado() == "OK"){

                        if(cliente.getId() == 0){
                            cc.grabar((Cliente) respuesta.getDatos());
                        }else{
                            cc.update(cliente);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.checked_48))
                                        .title(getResources().getString(R.string.procesoExitoso))
                                        .content("El cliente registrado!")
                                        .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                                        .positiveText("Aceptar")
                                        .show();

                                clear();
                            }
                        });


                    }else{

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utilities.sendToast(view.getContext(), "Ocurrio un error", "error");
                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    public void buscar(String nrodoc){

        AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        final List<Cliente> busqueda = LitePal.where("nroDocumento like ? " , nrodoc).find(Cliente.class);

        if(busqueda.size() > 0){

            if(CredentialValues.getLoginData().getPerfilactual().getIdPerfil() == 1){

                grabar.setEnabled(true);

                new MaterialDialog.Builder(getContext())
                        .title("Clientes encontrados")
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

            }else{
                new MaterialDialog.Builder(getContext())
                        .icon(getResources().getDrawable(R.drawable.attention_48))
                        .title("Atencion!")
                        .content("El cliente ya se encuentra registrado.")
                        .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                        .positiveText("Aceptar")
                        .show();
            }


        }else{
            grabar.setEnabled(true);
        }

        dialog.dismiss();

    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(nombres, getContext())) ret = false;
        if (!Validation.hasText(cedula, getContext())) ret = false;
        if (!Validation.hasText(direccion, getContext())) ret = false;
        if (!Validation.hasText(telefono, getContext())) ret = false;

        /*if(ciudad.getSelectedItem().toString().length() == 0){
            ret = false;
            //ciudad.setError(getContext().getResources().getString(R.string.required));

        }


        if(dpto.getSelectedItem().toString().length() == 0){
            ret = false;
            //dpto.setError(getContext().getResources().getString(R.string.required));
        }

        if(barrio.getSelectedItem().toString().length() == 0){
            ret = false;
            //dpto.setError(getContext().getResources().getString(R.string.required));
        }*/

        UtilLogger.info(ret + "");
        return ret;
    }


    public void setCliente(Cliente c){


        this.cliente = c;


        cedula.setText(cliente.getNroDocumento());
        nombres.setText(cliente.getNombreApellido());
        direccion.setText(cliente.getDireccion());
        telefono.setText(cliente.getTelefono());
        regTipoPersona.setText(cliente.getCodTipoDocumento());


        if(cliente.getCiudad() != null){
            ciudad.setText(cliente.getCiudad());
        }
        if(cliente.getBarrio() != null){
            barrio.setText(cliente.getBarrio());
        }
    }




}
