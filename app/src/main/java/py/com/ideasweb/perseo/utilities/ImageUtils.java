package py.com.ideasweb.perseo.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by cbustamante on 14/12/15.
 */
public class ImageUtils {
    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            UtilLogger.info("file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            UtilLogger.info("io exception");
            e.printStackTrace();
        }

    }
    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            UtilLogger.info("file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            UtilLogger.info("io exception");
            e.printStackTrace();
        }
        return b;
    }
}
