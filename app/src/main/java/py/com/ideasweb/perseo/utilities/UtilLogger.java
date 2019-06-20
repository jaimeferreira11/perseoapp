package py.com.ideasweb.perseo.utilities;

import android.util.Log;

/**
 * Created by cbustamante on 06/12/15.
 */
public class UtilLogger {
    public static void info(String log)
    {
        Log.d("EncuestApp |INFO |", log);
    }
    public static void error(String log)
    {
        Log.d("EncuestApp |ERROR |> ", log);
    }
    public static void api(String log)
    {
        Log.d("EncuestApp |API-REST|> ", log);
    }
    public static void db(String log)
    {
        Log.d("EncuestApp |DATABASE|> ", log);
    }
    public static void error(String log, Exception ex)
    {

        String errorMsg="";
        if(ex instanceof NullPointerException)
        {
            NullPointerException n = (NullPointerException)ex;
            StackTraceElement stackTrace = n.getStackTrace()[0];
            errorMsg= ex.getMessage() + "Exception at Class:" + stackTrace.getClassName() + " method:" +stackTrace.getMethodName() + " line:" + stackTrace.getLineNumber();
        }
        else
        {
            errorMsg=ex.toString();
        }
        Log.d("EncuestApp |ERROR |> ", errorMsg);
    }
}
