package py.com.ideasweb.perseo.ui.fragments.marcacion;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.PageAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabMarcacionFragment extends Fragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    Unbinder unbinder;
    public TabMarcacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab_marcacion, container, false);
        unbinder = ButterKnife.bind(this, view);


        setUpViewPager();

        return view;
    }


    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments =  new ArrayList<>();
        fragments.add(new TipoMarcacionFragment());
        fragments.add(new MisMarcacionesFragment());
        //    fragments.add(new PrestamosActivosFragment());
        //  fragments.add(new PrestamosCanceladosFragment());

        return fragments;

    }
    private void setUpViewPager(){
        viewPager.setAdapter(new PageAdapter(this.getFragmentManager(), agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Registrar Marcacion");
        tabLayout.getTabAt(1).setText("Mis Marcaciones");
    }

}
