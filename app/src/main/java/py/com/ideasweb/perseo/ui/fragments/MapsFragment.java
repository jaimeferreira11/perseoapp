package py.com.ideasweb.perseo.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.models.Tracking;
import py.com.ideasweb.perseo.utilities.MiUbicacion;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mapa;


    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_maps, container, false);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);  //use SuppoprtMapFragment for using in fragment instead of activity  MapFragment = activity   SupportMapFragment = fragment


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapFragment.getMapAsync(this);


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapa = googleMap;


        List<LatLng> listaPuntos = new ArrayList<>();
        List<Tracking> allSongs = LitePal.findAll(Tracking.class);

        for (Tracking t: allSongs) {

            listaPuntos.add(new LatLng(Double.parseDouble(t.getCoordenadas().split(",")[0].trim()),
                    Double.parseDouble(t.getCoordenadas().split(",")[1].trim())));
        }

        PolylineOptions ruta = new PolylineOptions();

        if (!listaPuntos.isEmpty()) {
            ruta.addAll(listaPuntos).color(Color.BLUE);

        }

        mapa.addPolyline(ruta); // Se añade una ruta.

        // Se añade un punto en el mapa.
        mapa.addMarker(new MarkerOptions().position(new LatLng(MiUbicacion.getLatitud(), MiUbicacion.getLongitud())).title("hola"));

        // Se mueve la cámara a la última posición.
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom( new LatLng(MiUbicacion.getLatitud(), MiUbicacion.getLongitud()), 15));
    }
}
