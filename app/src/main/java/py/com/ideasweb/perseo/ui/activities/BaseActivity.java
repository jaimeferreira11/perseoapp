package py.com.ideasweb.perseo.ui.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.leakcanary.LeakCanary;

import org.ankit.gpslibrary.ADLocation;
import org.ankit.gpslibrary.MyTracker;
import org.litepal.LitePal;
import org.litepal.tablemanager.callback.DatabaseListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.utilities.GPSTracker2;
import py.com.ideasweb.perseo.utilities.MiUbicacion;

public abstract class BaseActivity extends AppCompatActivity  implements MyTracker.ADLocationListener{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
       // LitePal.initialize(this);

        Icepick.restoreInstanceState(this, savedInstanceState);
        //logger


        if (LeakCanary.isInAnalyzerProcess(this)) return ;


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // habilitar navigacion hacia atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // TRAKING
       /* if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //ask for permission
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
        else{
            findLoc();
        }*/



        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                // fill some initial data
                System.out.println("Creando la base de datos");
            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {
                // upgrade data in db
                System.out.println("Actualizando la base de datos");
            }
        });

    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public abstract int getLayoutId();

    public abstract void inicializar();

    public abstract void setListeners();

    public void loading(AlertDialog dialog){
        if(dialog != null)
        if ((dialog.isShowing())) {
            dialog.dismiss();
        } else {
            dialog.show();
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mabout:
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(this)
                        .icon(getResources().getDrawable(R.drawable.ideas_web_logo))
                        .customView(R.layout.layout_acercade, wrapInScrollView)
                        .positiveText("OK")
                        .show();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }


    private void findLoc(){
        new MyTracker(getApplicationContext(),this).track();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            findLoc();
        }
    }
    @Override
    public void whereIAM(ADLocation loc) {
        System.out.println("LUGAR: " + loc);
        MiUbicacion.lugar = loc;
    }


}
