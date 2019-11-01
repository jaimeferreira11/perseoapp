package py.com.ideasweb.perseo.ui.fragments.marcacion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import py.com.ideasweb.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TipoMarcacionFragment extends Fragment {


    Unbinder unbinder;
    public TipoMarcacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tipo_marcacion, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }

}
