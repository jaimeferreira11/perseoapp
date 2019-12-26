package py.com.ideasweb.perseo.ui.fragments.pedidos;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.FacturaAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.models.FacturaCab;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosRealizadosPendientesFragment extends Fragment {

    private RecyclerView tasks;

    SwipeRefreshLayout refresh;

    public PedidosRealizadosPendientesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedidos_realizados_pendientes, container, false);

        tasks = (RecyclerView) view.findViewById(R.id.rvPP);
        refresh = (SwipeRefreshLayout) view.findViewById(R.id.ppRefresh);

        final ConstructorFactura cf = new ConstructorFactura();

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refresh.setRefreshing(true);

                generarLineaLayoutVertical();
                inicializarAdaptadorRV(crearAdaptador((ArrayList<FacturaCab>) cf.getPendientes(), getResources().getString(R.string.pendientes)));

                refresh.setRefreshing(false);
            }
        });


        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador((ArrayList<FacturaCab>) cf.getPendientes(), getResources().getString(R.string.pendientes)));


        return view;
    }


    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        tasks.setLayoutManager(llm);
    }


    public FacturaAdapter crearAdaptador(ArrayList<FacturaCab> taskList, String state) {
        FacturaAdapter adaptador = new FacturaAdapter(getContext(), taskList, state);
        return adaptador;
    }

    public void inicializarAdaptadorRV(FacturaAdapter adapatador) {
        tasks.setAdapter(adapatador);

    }

}
