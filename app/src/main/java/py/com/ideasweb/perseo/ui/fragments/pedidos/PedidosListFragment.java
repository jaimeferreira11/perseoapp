package py.com.ideasweb.perseo.ui.fragments.pedidos;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.PageAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorFactura;
import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosListFragment extends Fragment {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SwipeRefreshLayout refreshLayout;
    private int count1=0;
    private int count2=0;
    private int count3=0;
    public PedidosListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =inflater.inflate(R.layout.fragment_pedidos_realizados, container, false);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPagerPedidos);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.indicadorRefresh);


        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        ConstructorFactura constructorFact = new ConstructorFactura();


        count1 = constructorFact.getPendientes().size();
        count2 = constructorFact.getSincronizados().size();
        count3 = constructorFact.getAnulados().size();



        setUpViewPager();

        dialog.dismiss();


        return rootView;
    }

    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments =  new ArrayList<>();
        fragments.add(new PedidosRealizadosPendientesFragment());
        fragments.add(new PedidosRealizadosConfirmadosFragment());
        fragments.add(new PedidosRealizadosAnuladosFragment());

        return fragments;

    }

    private void setUpViewPager(){
        viewPager.setAdapter(new PageAdapter(this.getFragmentManager(), agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        ;
        tabLayout.getTabAt(0).setText(getResources().getString(R.string.pendientes) +" (" +count1+")");
        tabLayout.getTabAt(1).setText(getResources().getString(R.string.confrimados)+" (" +count2+")");
        tabLayout.getTabAt(2).setText(getResources().getString(R.string.anulados)+" (" +count3+")");


    }



}
