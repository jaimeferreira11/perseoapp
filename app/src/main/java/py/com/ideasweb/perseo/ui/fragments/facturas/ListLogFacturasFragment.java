package py.com.ideasweb.perseo.ui.fragments.facturas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.ClienteAdapter;
import py.com.ideasweb.perseo.adapter.FacturaAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorCliente;
import py.com.ideasweb.perseo.constructor.ConstructorFacturaLog;
import py.com.ideasweb.perseo.models.Facturacab;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListLogFacturasFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;


    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    Unbinder unbinder;
    FacturaAdapter adaptador;

    public ListLogFacturasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_log_facturas, container, false);

        unbinder = ButterKnife.bind(this, view);


        ConstructorFacturaLog cc = new ConstructorFacturaLog();
        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador((ArrayList<Facturacab>) cc.getAll(), ""));


        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                mSearchView.showProgress();
                adaptador.filter(null);
                mSearchView.hideProgress();
                mSearchView.clearSuggestions();
                mSearchView.clearSearchFocus();
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

                System.out.println("onSuggestionClicked");
            }

            @Override
            public void onSearchAction(String newQuery) {

                try {

                    System.out.println(newQuery);

                    mSearchView.showProgress();
                    //get suggestions based on newQuery
                   /* if (!oldQuery.equals("") && newQuery.equals("")) {
                        mSearchView.clearSuggestions();
                    }*/

                    //pass them on to the search view
                    adaptador.filter(newQuery);

                    mSearchView.hideProgress();
                    mSearchView.clearSuggestions();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return  view;
    }




    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
    }


    public FacturaAdapter crearAdaptador(ArrayList<Facturacab> taskList, String state) {
        adaptador = new FacturaAdapter(getContext(), taskList, state);
        return adaptador;
    }

    public void inicializarAdaptadorRV(FacturaAdapter adapatador) {
        recycler.setAdapter(adapatador);

    }

}
