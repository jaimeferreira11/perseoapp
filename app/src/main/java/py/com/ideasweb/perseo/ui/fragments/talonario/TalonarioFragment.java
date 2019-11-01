package py.com.ideasweb.perseo.ui.fragments.talonario;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.ParseException;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorTalonario;
import py.com.ideasweb.perseo.models.Talonario;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;
import py.com.ideasweb.perseo.utilities.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalonarioFragment extends Fragment {


    @BindView(R.id.timbrado)
    EditText timbrado;

    @BindView(R.id.fecha_desde)
    EditText fechaDesde;
    @BindView(R.id.fecha_hasta)
    EditText fechaHasta;
    @BindView(R.id.nro_inicial)
    EditText nroInicial;
    @BindView(R.id.nro_final)
    EditText nroFinal;
    @BindView(R.id.establecimiento)
    EditText establecimiento;
    @BindView(R.id.expedicion)
    EditText expedicion;
    @BindView(R.id.nro_actual)
    EditText nroActual;

    @BindView(R.id.guardar)
    Button guardar;


    Talonario talonario;
    Unbinder unbinder;
    final ConstructorTalonario cons = new ConstructorTalonario();

    public TalonarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_talonario, container, false);
        unbinder = ButterKnife.bind(this, view);


        Utilities.maskDate(fechaDesde);
        Utilities.maskDate(fechaHasta);

        talonario = cons.cargarTalonario();

        if(talonario != null){
            cargar();
            if(CredentialValues.getLoginData().getPerfilactual().getIdPerfil() != 1)
                deshabilitar();
        }else{
            talonario = new Talonario();
            nroActual.setText("0");
        }



        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkValidation()){

                    talonario.setTimbrado(timbrado.getText().toString());
                    talonario.setValidoDesde(fechaDesde.getText().toString());
                    talonario.setValidoHasta(fechaHasta.getText().toString());
                    talonario.setNumeroInicial(Integer.parseInt(nroInicial.getText().toString()));
                    talonario.setNumeroFinal(Integer.parseInt(nroFinal.getText().toString()));
                    talonario.setEstablecimiento(Integer.parseInt(establecimiento.getText().toString()));
                    talonario.setPuntoExpedicion(Integer.parseInt(expedicion.getText().toString()));
                    talonario.setNumeroActual(Integer.parseInt(nroActual.getText().toString()));

                    AlertDialog dialog = new SpotsDialog.Builder()
                            .setContext(getContext())
                            .setTheme(R.style.spots)
                            .setMessage(R.string.aguarde)
                            .build();
                    dialog.show();


                    cons.insertar(talonario);

                    dialog.dismiss();
                    deshabilitar();

                    new MaterialDialog.Builder(getContext())
                            .icon(getResources().getDrawable(R.drawable.checked_48))
                            .title(getResources().getString(R.string.procesoExitoso))
                            .content("Talonario Registrado!")
                            .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                            .positiveText("Aceptar")
                            .show();
                }
            }
        });

        return view;
    }

    private void deshabilitar() {

        timbrado.setEnabled(false);
        fechaHasta.setEnabled(false);
        fechaDesde.setEnabled(false);
        establecimiento.setEnabled(false);
        expedicion.setEnabled(false);
        nroInicial.setEnabled(false);
        nroActual.setEnabled(false);
    }


    public void clear(){
        timbrado.setText(null);
        fechaDesde.setText(null);
        fechaHasta.setText(null);
        nroInicial.setText(null);
        nroFinal.setText(null);
        expedicion.setText(null);
        establecimiento.setText(null);
        nroActual.setText(null);

    }

    public void cargar(){
        timbrado.setText(talonario.getTimbrado());
        fechaDesde.setText(talonario.getValidoDesde());
        fechaHasta.setText(talonario.getValidoHasta());
        nroInicial.setText(String.valueOf(talonario.getNumeroInicial()));
        nroFinal.setText(String.valueOf(talonario.getNumeroFinal()));
        nroActual.setText(String.valueOf(talonario.getNumeroActual()));
        establecimiento.setText(String.valueOf(talonario.getEstablecimiento()));
        expedicion.setText(String.valueOf(talonario.getPuntoExpedicion()));
    }



    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(timbrado, getContext())) ret = false;
        if (!Validation.minLenght(timbrado, 8, getContext())) ret = false;
        //if (!Validation.hasText(fechaHasta, getContext())) ret = false;
       // if (!Validation.hasText(fechaDesde, getContext())) ret = false;
        if (!Validation.hasText(expedicion, getContext())) ret = false;
        if (!Validation.hasText(establecimiento, getContext())) ret = false;
        if (!Validation.hasText(nroInicial, getContext())) ret = false;
        if (!Validation.hasText(nroFinal, getContext())) ret = false;
        if (!Validation.hasText(nroInicial, getContext())) ret = false;


        try {
            if (Validation.hasText(fechaHasta, getContext())){
                Date fechaValido = Utilities.toDateFromString(fechaHasta.getText().toString());
                if (fechaValido.before(new Date(System.currentTimeMillis()))) {
                    fechaHasta.setError("Vencido.La fecha no es valido");
                    ret = false;
                }

            }else{
                ret = false;
            }


        }catch (ParseException e){
            fechaHasta.setError("Vencido.La fecha no es valido");
            ret = false;
        }

        try {
            if (Validation.hasText(fechaDesde, getContext())){
                Date fechaInicio = Utilities.toDateFromString(fechaDesde.getText().toString());
            }else{
                ret = false;
            }
        }catch (ParseException e){
            fechaDesde.setError("Vencido.La fecha no es valido");
            ret = false;
        }


        return ret;
    }


}
