package py.com.ideasweb.perseo.ui.fragments.pedidos.steps;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andremion.counterfab.CounterFab;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.github.fcannizzaro.materialstepper.AbstractStep;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;

import java.util.ArrayList;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.ArticuloAdapter;
import py.com.ideasweb.perseo.adapter.DialogAdapter;
import py.com.ideasweb.perseo.adapter.RvSearchDemoAdapter;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Facturadet;
import py.com.ideasweb.perseo.restApi.pojo.PedidoDetalle;

/**
 * Created by jaime on 09/11/17.
 */

public class Step2BackupBuscador extends AbstractStep {

    private RecyclerView tasks;
    private ArrayList<Articulo> list = new ArrayList<>();
    SwipeRefreshLayout refresh;
    FloatingSearchView mSearchView;
    CounterFab counterFab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_step2_backup_buscador, container, false);


        tasks = (RecyclerView) view.findViewById(R.id.rvArticulos);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        counterFab = (CounterFab) view.findViewById(R.id.fabCart);



        //armar el reciclerview
      //  list.add(new PedidoCabecera(new Long(1), "Nro 001-001-125154"));
      //  list.add(new PedidoCabecera(new Long(2), "Nro 001-001-999999"));

        //generarLineaLayoutVertical();
       // inicializarAdaptadorRV(crearAdaptador(list));

        RecyclerView rvItems = (RecyclerView) view.findViewById(R.id.rvArticulos);
        rvItems.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<String> countryList = new ArrayList<>();
        /*for (String s : Locale.getISOCountries()) {
            countryList.add(new Locale("", s).getDisplayCountry(Locale.ENGLISH));
        }*/

        final RvSearchDemoAdapter adapter = new RvSearchDemoAdapter(getContext(), countryList);
        rvItems.setAdapter(adapter);


        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                mSearchView.showProgress();
                //get suggestions based on newQuery
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                }

                //pass them on to the search view
                adapter.filter(newQuery);

                mSearchView.hideProgress();

            }
        });



        // refrescar
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                //volver a traer la lista
             //   list.add(new PedidoCabecera(new Long(1), "Nro 001-001-125154"));
             //   list.add(new PedidoCabecera(new Long(2), "Nro 001-001-999999"));

                generarLineaLayoutVertical();
                inicializarAdaptadorRV(crearAdaptador(list));

                refresh.setRefreshing(false);
            }
        });


        final ArrayList<Facturadet> list = new ArrayList<>();
        // fab
        counterFab.setCount(10);
        counterFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             //   View contentView = getLayoutInflater().inflate(R.layout.content2, null);
                DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                        .setGravity(Gravity.BOTTOM)
                        .setAdapter(new DialogAdapter(getContext(), list, counterFab ))
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

                dialogPlus.show();




            }
        });


        return view;

    }


    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tasks.setLayoutManager(llm);
    }


    public ArticuloAdapter crearAdaptador(ArrayList<Articulo> taskList) {
        //ArticuloAdapter adaptador = new ArticuloAdapter(getContext(), taskList);
        return null;
    }

    public void inicializarAdaptadorRV(ArticuloAdapter adapatador) {
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
        return "<b>"+getResources().getString(R.string.complete_campos)+"</b>";
    }

    public boolean checkValidation() {

        return true;
    }

}
