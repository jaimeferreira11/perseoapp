package py.com.ideasweb.perseo.ui.fragments.pedidos.steps;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.ArticuloAdapter;
import py.com.ideasweb.perseo.adapter.DialogAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.activities.LectorCodigoBarraActivity;
import py.com.ideasweb.perseo.utilities.ConstantesMensajes;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;

/**
 * Created by jaime on 09/11/17.
 */

public class Step2 extends AbstractStep {

    private RecyclerView tasks;
    private ArrayList<Articulo> list = new ArrayList<>();
    SwipeRefreshLayout refresh;
    FloatingSearchView mSearchView;
    CounterFab counterFab;
    FloatingActionButton fabScan;
    RecyclerView rvItems;


    View footerView;

    ConstructorArticulos articuloRepo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_step2, container, false);

        tasks = (RecyclerView) view.findViewById(R.id.rvArticulos);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        counterFab = (CounterFab) view.findViewById(R.id.fabCart);
        fabScan = (FloatingActionButton) view.findViewById(R.id.fabScan);
        rvItems = (RecyclerView) view.findViewById(R.id.rvArticulos);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));

        articuloRepo = new ConstructorArticulos();

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                UtilLogger.info("onFocus");
                mSearchView.clearQuery();
            }

            @Override
            public void onFocusCleared() {
                UtilLogger.info("onFocusCleared " + mSearchView.getQuery());

                if(!mSearchView.getQuery().isEmpty()){

                    clearAdapter();
                    getArticulos(getContext() , mSearchView.getQuery().trim() );

                }

            }
        });


        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                mSearchView.showProgress();
                //get suggestions based on newQuery
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                }

                //pass them on to the search view
                //adapter.filter(newQuery);

                mSearchView.hideProgress();

            }
        });



        // refrescar
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getArticulos(getContext() , mSearchView.getQuery() );
                refresh.setRefreshing(false);
            }
        });


        // fab
        if(LoginData.getFactura().getFacturadet().size() > 0)
            counterFab.setCount(LoginData.getFactura().getFacturadet().size());

        counterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginData.getFactura().getFacturadet()!= null){

                    if(!LoginData.getFactura().getFacturadet().isEmpty()){

                        final DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                                .setGravity(Gravity.BOTTOM)
                                .setAdapter(new DialogAdapter(getContext(), LoginData.getFactura().getFacturadet(), counterFab ))
                                .setContentHolder(new ListHolder())
                                .setHeader(R.layout.dialog_header)
                                .setFooter(R.layout.dialog_footer)
                                .setExpanded(true)
                                .setCancelable(true)
                                .setOnDismissListener(new OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogPlus dialog) {

                                    }
                                })
                                .setOnCancelListener(new OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogPlus dialog) {

                                    }
                                })
                                .setOnBackPressListener(new OnBackPressListener() {
                                    @Override
                                    public void onBackPressed(DialogPlus dialogPlus) {

                                    }
                                })
                                .create();

                        footerView = dialogPlus.getFooterView();
                        TextView footer_tot = (TextView) footerView.findViewById(R.id.footer_tot);
                        footer_tot.setText("Total: "+Utilities.toStringFromDoubleWithFormat(LoginData.getFactura().getImporte()) +" Gs.");
                        Button close = (Button) footerView.findViewById(R.id.footer_close_button);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogPlus.dismiss();
                            }
                        });


                        View headerView = dialogPlus.getHeaderView();
                        TextView header_cant = (TextView) headerView.findViewById(R.id.header_cant);
                        header_cant.setText(LoginData.getFactura().getFacturadet().size() + " Articulos");


                        dialogPlus.show();


                    }else {
                        Toast.makeText(getContext(), "Agregue articulo al carro", Toast.LENGTH_SHORT).show();
                    }


                }
                //   View contentView = getLayoutInflater().inflate(R.layout.content2, null);


            }
        });

        // scan
        fabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LectorCodigoBarraActivity.class);
                view.getContext().startActivity(intent);

            }
        });


        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("STEP2 ONRESUMEN");
        if(LoginData.getCodigoBarra() != null){
                getArticulosByCodigoBarra(getContext() , LoginData.getCodigoBarra());

        }
    }



    @Override
    public void onStop() {
        super.onStop();
        System.out.println("STEP2 ONSTOP");
    }

    @Override
    public void onPause() {
        System.out.println("STEP2 ONPAUSE");
        super.onPause();
    }


    private void getArticulos(final Context context , final String param){

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        list = (ArrayList<Articulo>) articuloRepo.getArticuloByParam(param);

      //  LoginData.setCodigoBarra(null);

        if(!list.isEmpty()){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArticuloAdapter adapter = new ArticuloAdapter(context, list, counterFab);
                    rvItems.setAdapter(adapter);
                }
            });

        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, ConstantesMensajes.ARTICULO_NO_EXISTE , Toast.LENGTH_LONG).show();
                }
            });
        }

        dialog.dismiss();

    }


    private void getArticulosByCodigoBarra(final Context context , final String param){

        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(context)
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();

        list = (ArrayList<Articulo>) articuloRepo.getArticulosByCodigoBarra(param);

        LoginData.setCodigoBarra(null);

        if(!list.isEmpty()){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ArticuloAdapter adapter = new ArticuloAdapter(context, list, counterFab);
                    rvItems.setAdapter(adapter);
                }
            });

        }else{
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, ConstantesMensajes.ARTICULO_NO_EXISTE , Toast.LENGTH_LONG).show();
                }
            });
        }

        dialog.dismiss();


    }


    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tasks.setLayoutManager(llm);
    }


    public ArticuloAdapter crearAdaptador(ArrayList<Articulo> taskList) {
        ArticuloAdapter adaptador = new ArticuloAdapter(getContext(), taskList, counterFab);
        return adaptador;
    }

    public void inicializarAdaptadorRV(ArticuloAdapter adapatador) {
        tasks.setAdapter(adapatador);

    }

    public void clearAdapter(){
        ArticuloAdapter adapter = new ArticuloAdapter(getContext(), new ArrayList<Articulo>(), counterFab);
        rvItems.setAdapter(adapter);
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
        if(LoginData.getFactura().getFacturadet()!= null)
        counterFab.setCount(LoginData.getFactura().getFacturadet().size());
    }


    @Override
    public void onNext() {

        //LoginData.getFactura().setPorcDescuento(new Double(0));

        /*LoginData.getFactura().setSincronizadoCore(false);
        LoginData.getFactura().setUsuario(CredentialValues.getLoginData().getUsuario());*/

        //super.onNext();
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
        return "<b>Favor, agregue articulos al carro</b>";
    }

    public boolean checkValidation() {

        if( LoginData.getFactura().getFacturadet().size() == 0 ){
            return false;
        }
        return true;
    }

}
