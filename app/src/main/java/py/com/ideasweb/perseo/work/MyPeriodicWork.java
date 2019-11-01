package py.com.ideasweb.perseo.work;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.sql.Date;

import py.com.ideasweb.perseo.ui.activities.BaseActivity;
import py.com.ideasweb.perseo.utilities.FechaUtils;
import py.com.ideasweb.perseo.utilities.GPSTracker2;

public class MyPeriodicWork extends Worker {

    private static final String TAB = MyPeriodicWork.class.getSimpleName();
    private Context context;

    public MyPeriodicWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.e(TAB,"PeriodicWork in BackGround");
        System.out.println("Se ejecuta la tarea periodica");

        System.out.println("Que dia de la semana es? " + FechaUtils.getDiaSemana(new Date(System.currentTimeMillis())));
        System.out.println("Que dia de la semana es? " + FechaUtils.getDiaSemanaName(new Date(System.currentTimeMillis())));
        System.out.println("Que hora es? " + FechaUtils.getHora(new Date(System.currentTimeMillis())));



     //   BaseActivity.gps.toggleLocationUpdates(true);

        return Result.success();
    }


}
