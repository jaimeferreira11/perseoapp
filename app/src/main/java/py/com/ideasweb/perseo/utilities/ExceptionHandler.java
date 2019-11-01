package py.com.ideasweb.perseo.utilities;

import android.app.Activity;
import android.os.Build;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import py.com.ideasweb.R;


/**
 * Created by jaime on 09/08/17.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final Activity myContext;
    private final String LINE_SEPARATOR = "\n";

    public ExceptionHandler(Activity context) {
        myContext = context;
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {

        e.printStackTrace();
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();


        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        errorReport.append("************ USUARIO************\n");
     //   errorReport.append(CredentialValues.getLoginData().getUsuario().getIdusuario() + " " + CredentialValues.getLoginData().getUsuario().getUsername());
        errorReport.append(LINE_SEPARATOR);

        errorReport.append("\n************ CAUSE OF ERROR ************\n\n");
        errorReport.append("Fecha: " + sd.format(new Timestamp(System.currentTimeMillis())));
        errorReport.append(LINE_SEPARATOR);
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ APP VERSION ***********\n");
        errorReport.append("Version: ");
        errorReport.append(myContext.getResources().getString(R.string.version_name));
        errorReport.append(LINE_SEPARATOR);


        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);




        /*Intent intent = new Intent(myContext, ErrorActivity.class);
        intent.putExtra("error", errorReport.toString());
        myContext.startActivity(intent);*/

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);


    }


}
