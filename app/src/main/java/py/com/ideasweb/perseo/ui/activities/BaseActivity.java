package py.com.ideasweb.perseo.ui.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.leakcanary.LeakCanary;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;

public abstract class BaseActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        LitePal.initialize(this);

        Icepick.restoreInstanceState(this, savedInstanceState);
        //logger


        if (LeakCanary.isInAnalyzerProcess(this)) return ;


        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // habilitar navigacion hacia atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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



}
