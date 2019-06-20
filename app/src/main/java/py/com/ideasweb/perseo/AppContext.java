package py.com.ideasweb.perseo;

import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;


public class AppContext extends Application {

    private static final String TAG = AppContext.class.getName();

    private static AppContext instance;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        LitePal.initialize(this);
    }



}
