package py.com.ideasweb.perseo.utilities;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by jaime on 29/11/16.
 */

public class Share {



    public static void shareRedeSociales(Activity activity, String linkShare, String titleShare){

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, titleShare);
        intent.putExtra(Intent.EXTRA_TEXT, linkShare);

        activity.startActivity(Intent.createChooser(intent, "Compartir"));
    }

}
