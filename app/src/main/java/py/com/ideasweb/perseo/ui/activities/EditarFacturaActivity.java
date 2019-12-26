package py.com.ideasweb.perseo.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.appyvet.materialrangebar.RangeBar;

import java.util.Formatter;
import java.util.List;

import butterknife.BindView;
import me.toptas.fancyshowcase.FancyShowCaseView;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.FacturaDetLogAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorFacturaLog;
import py.com.ideasweb.perseo.models.Facturacablog;
import py.com.ideasweb.perseo.models.Facturadetlog;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

public class EditarFacturaActivity extends BaseActivity {

    private RecyclerView tasks;
    TextView cliente;
    TextView doc;
    TextView fecha;
    TextView total;
    TextView value;
    private RangeBar rangebar;
    RadioButton radioCO;
    RadioButton radioCR;
    FloatingActionButton fab;
    View view;

    public static Facturacablog factura = null;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inicializar();

        tasks = (RecyclerView) findViewById(R.id.rvDetallePedido);
        cliente = (TextView) findViewById(R.id.step3Cliente);
        doc = (TextView) findViewById(R.id.step3Doc);
        fecha = (TextView) findViewById(R.id.step3Fecha);
        total = (TextView) findViewById(R.id.step3Total);
        value = (TextView) findViewById(R.id.setValue);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        radioCO = (RadioButton) findViewById(R.id.radioCO);
        radioCR = (RadioButton) findViewById(R.id.radioCR);

        Formatter fmt = new Formatter();
        cliente.setText(fmt.format("%08d", factura.getNumeroFactura())+"-"+factura.getNombreCliente());
        doc.setText("CI/RUC: "+factura.getNroDocumentoCliente());
        factura.setTipoFactura(radioCO.getText().toString());

        final String sFecha = "<b>Fecha: </b>"+ Utilities.getCurrentDate();
        fecha.setText(Html.fromHtml(sFecha));
        String sTotal = "<b>Total: </b>"+Utilities.toStringFromDoubleWithFormat(factura.getImporte());
        total.setText(Html.fromHtml(sTotal));
        UtilLogger.info("DET SIZE: " +factura.getFacturadet().size() );
        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador(factura.getFacturadet()));

        rangebar = (RangeBar) findViewById(R.id.range);

        rangebar.setSeekPinByIndex(0);
        rangebar.setTemporaryPins(false);


        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {

                UtilLogger.info("Rigth value : " + rightPinValue);

                value.setText(rightPinValue + " %");

                float desc = (100- Float.parseFloat(rightPinValue)) / 100;

                float t = (float) (factura.getImporte()*desc);

                UtilLogger.info("Desc: " + desc);

                UtilLogger.info("Total : " + t);

                String sTotal = "<b>Total: </b>"+Utilities.toStringFromFloatWithFormat(t);

                total.setText(Html.fromHtml(sTotal));
               factura.setPorcDescuento(new Double(rightPinValue));

            }
        });


        radioCO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   factura.setTipoFactura(radioCO.getText().toString());
                    radioCR.setChecked(false);
                }
            }
        });

        radioCR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                   factura.setTipoFactura(radioCR.getText().toString());
                    radioCO.setChecked(false);
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkValidation()){

                    new MaterialDialog.Builder(EditarFacturaActivity.this)
                            .title("Desea guardar la factura?")
                            .content("Se aplicaran los cambios")
                            .icon(getApplicationContext().getDrawable(R.drawable.help_48))
                            .positiveText(getApplicationContext().getString(R.string.aceptar))
                            .negativeText(getApplicationContext().getString(R.string.cancelar))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                    editar();
                                    dialog.dismiss();
                                }
                            })
                            .show();


                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_editar_factura;
    }

    @Override
    public void inicializar() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        view = getCurrentFocus();
    }

    @Override
    public void setListeners() {

    }

    private void editar() {

        ConstructorFacturaLog clog = new ConstructorFacturaLog();
        clog.actualizar(factura);


        new MaterialDialog.Builder(EditarFacturaActivity.this)
                .title("Factura Actualizada")
                .content("Marque como 'No sincronizada' para volver a subir")
                .icon(getResources().getDrawable(R.drawable.checked_48))
                .positiveText(getResources().getString(R.string.aceptar))
                .autoDismiss(false)
                .cancelable(false)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        factura = null;
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();



    }


    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tasks.setLayoutManager(llm);
    }


    public FacturaDetLogAdapter crearAdaptador(List<Facturadetlog> taskList) {
        FacturaDetLogAdapter adaptador = new FacturaDetLogAdapter(getApplicationContext(), taskList, EditarFacturaActivity.this);
        return adaptador;
    }

    public void inicializarAdaptadorRV(FacturaDetLogAdapter adapatador) {
        tasks.setAdapter(adapatador);

    }

    public boolean checkValidation() {

        if(factura.getFacturadet().size() == 0){
            Toast.makeText(getApplicationContext(), "No se puede guardar sin detalles" , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        factura = null;
        finish();
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case android.R.id.home:
                onBackPressed();

                break;
            case R.id.mabout:
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(this)
                        .icon(getResources().getDrawable(R.drawable.ideas_web_logo))
                        .customView(R.layout.layout_acercade, wrapInScrollView)
                        .positiveText("OK")
                        .show();
                break;
            case R.id.ayuda:
                new FancyShowCaseView.Builder(this)
                        .focusOn(findViewById(item.getItemId()))
                        .title("Pulsando aquí en cada pantalla, encontrarás un poco de ayuda :)")
                        .build()
                        .show();
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}
