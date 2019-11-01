package py.com.ideasweb.perseo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import java.util.Timer;
import java.util.TimerTask;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.utilities.Utilities;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);


        //PerseoDatabase db = new PerseoDatabase(getApplicationContext()); // Important: db instances should always be singletons

        if(Utilities.isNetworkConnected(getApplicationContext())){
           Utilities.sincronizacionInicial();
        }



        //Utilities.finalizarTareaPeriodica();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                // Start the next activity
                Intent mainIntent = new Intent().setClass(SplashActivity.this, LoginActivity.class);
                startActivity(mainIntent);
                finish();

            }
        };

        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }
}
