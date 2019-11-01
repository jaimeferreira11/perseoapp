package py.com.ideasweb.perseo.ui.fragments.pedidos.steps;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appyvet.materialrangebar.RangeBar;
import com.github.fcannizzaro.materialstepper.AbstractStep;

import java.util.Formatter;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.FacturaDetAdapter;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.restApi.pojo.PedidoDetalle;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 09/11/17.
 */

public class Step3 extends AbstractStep {

    private RecyclerView tasks;
    TextView cliente;
    TextView doc;
    TextView fecha;
    TextView total;
    TextView value;
    View view;
    private RangeBar rangebar;
    RadioButton radioCO;
    RadioButton radioCR;
    /*RadioButton radioTreinta;
    RadioButton radioCuarenta;*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.layout_step3, container, false);

        tasks = (RecyclerView) view.findViewById(R.id.rvDetallePedido);





        return view;

    }


    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tasks.setLayoutManager(llm);
    }


    public FacturaDetAdapter crearAdaptador(List<Facturadet> taskList) {
        FacturaDetAdapter adaptador = new FacturaDetAdapter(getContext(), taskList, view);
        return adaptador;
    }

    public void inicializarAdaptadorRV(FacturaDetAdapter adapatador) {
        tasks.setAdapter(adapatador);

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
        cliente = (TextView) view.findViewById(R.id.step3Cliente);
        doc = (TextView) view.findViewById(R.id.step3Doc);
        fecha = (TextView) view.findViewById(R.id.step3Fecha);
        total = (TextView) view.findViewById(R.id.step3Total);
        value = (TextView) view.findViewById(R.id.setValue);

        radioCO = (RadioButton) view.findViewById(R.id.radioCO);
        radioCR = (RadioButton) view.findViewById(R.id.radioCR);

        Formatter fmt = new Formatter();
        cliente.setText(fmt.format("%08d",LoginData.getTalonario().getNumeroActual() + 1)+"-"+LoginData.getFactura().getNombreCliente());
        doc.setText("CI/RUC: "+LoginData.getFactura().getNroDocumentoCliente());
        LoginData.getFactura().setTipoFactura(radioCO.getText().toString());

        final String sFecha = "<b>Fecha: </b>"+Utilities.getCurrentDate();
        fecha.setText(Html.fromHtml(sFecha));
        String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getImporte());
        total.setText(Html.fromHtml(sTotal));
        UtilLogger.info("DET SIZE: " +LoginData.getFactura().getFacturadet().size() );
        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador(LoginData.getFactura().getFacturadet()));


        //final Double aux = LoginData.getPedido().getImporteTotal();


        rangebar = (RangeBar) view.findViewById(R.id.range);
        // login = (Button) findViewById(R.id.loginAcceder);

        rangebar.setSeekPinByIndex(0);
        rangebar.setTemporaryPins(false);


        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                UtilLogger.info("Rigth value : " + rightPinValue);

                value.setText(rightPinValue + " %");

                float desc = (100- Float.parseFloat(rightPinValue)) / 100;

                float t = (float) (LoginData.getFactura().getImporte()*desc);

                UtilLogger.info("Desc: " + desc);

                UtilLogger.info("Total : " + t);

                String sTotal = "<b>Total: </b>"+Utilities.toStringFromFloatWithFormat(t);

                total.setText(Html.fromHtml(sTotal));
                LoginData.getFactura().setPorcDescuento(new Double(rightPinValue));

            }
        });


        radioCO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    LoginData.getFactura().setTipoFactura(radioCO.getText().toString());
                    radioCR.setChecked(false);
                }
            }
        });

        radioCR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    LoginData.getFactura().setTipoFactura(radioCR.getText().toString());
                    radioCO.setChecked(false);
                }
            }
        });

        /*radioTreinta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(aux*0.7);
                    total.setText(Html.fromHtml(sTotal));
                    LoginData.getPedido().setPorcDescuento(new Double(30));
                }
            }
        });
        radioCuarenta.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                    String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(aux*0.6);
                    total.setText(Html.fromHtml(sTotal));
                    LoginData.getPedido().setPorcDescuento(new Double(40));
                }
            }
        });*/
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
        return "<b>Favor, vuelva a agregar articulos</b>";
    }

    public boolean checkValidation() {

        if(LoginData.getFactura().getFacturadet().size() == 0){
            return false;
        }
        return true;
    }


}
