package py.com.ideasweb.perseo.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jaime on 03/12/16.
 */

public class MiUbicacion {
    private static  Double latitud;
    private  static  Double Longitud;
    private static boolean ban = false;


    private static  Double latitudGPS;
    private  static  Double LongitudGPS;

    public MiUbicacion() {
    }


    public static Double getLatitudGPS() {
        return latitudGPS;
    }

    public static void setLatitudGPS(Double latitudGPS) {
        MiUbicacion.latitudGPS = latitudGPS;
    }

    public static Double getLongitudGPS() {
        return LongitudGPS;
    }

    public static void setLongitudGPS(Double longitudGPS) {
        LongitudGPS = longitudGPS;
    }

    public static Double getLatitud() {
        return latitud;
    }

    public static void setLatitud(Double latitud) {
        MiUbicacion.latitud = latitud;
    }

    public static Double getLongitud() {
        return Longitud;
    }

    public static void setLongitud(Double longitud) {
        Longitud = longitud;
    }
    public static boolean getBan() {
        return ban;
    }

    public static void setBan(boolean ban) {
        MiUbicacion.ban = ban;
    }

    public static void guardarUbicacion(Context context){
        SharedPreferences SPUbicacion = context.getSharedPreferences("perseo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        editor.putString("latitud", String.valueOf(MiUbicacion.getLatitud()));
        editor.putString("longitud", String.valueOf(MiUbicacion.getLongitud()));
        editor.commit();
    }

    public static String getCoordenadasActual(){
        return String.valueOf(getLatitud()) +","+String.valueOf(getLongitud());
    }

}
