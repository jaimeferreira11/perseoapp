package py.com.ideasweb.perseo.ui.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.HomeAdapter;
import py.com.ideasweb.perseo.models.HomeItem;
import py.com.ideasweb.perseo.repo.HomeItemRepo;
import py.com.ideasweb.perseo.utilities.Utilities;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.upload)
    TextView upload;

    @BindView(R.id.download)
    TextView download;

    HomeAdapter adaptador;

    public HomeFragment() {
        // Required empty public constructor
    }

    Unbinder unbinder;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);


        generarLineaLayoutVertical();
        inicializarAdaptadorRV(crearAdaptador(HomeItemRepo.getListaFacturacion()));

        if(!Utilities.getUltDownload(getContext()).equals(""))
            download.setText(Utilities.getUltDownload(getContext()));

        if(!Utilities.getUltUpload(getContext()).equals(""))
            upload.setText(Utilities.getUltUpload(getContext()));

        return view;
    }


    public void generarLineaLayoutVertical() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(llm);
    }

    public HomeAdapter crearAdaptador(List<HomeItem> lista){
        adaptador = new HomeAdapter(lista, getActivity().getParent() , view.getContext());
        return adaptador;
    }

    public void inicializarAdaptadorRV(HomeAdapter adaptador){
        recycler.setAdapter(adaptador);
    }

}
