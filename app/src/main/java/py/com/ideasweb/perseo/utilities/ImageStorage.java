package py.com.ideasweb.perseo.utilities;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;

import py.com.ideasweb.perseo.restApi.ConstantesRestApi;


/**
 * Created by cbustamante on 14/12/15.
 */
public class ImageStorage {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    public static File getMainDirectory() {

        return Environment.getExternalStorageDirectory();
    }

    public static String saveToSdCard(Bitmap bitmap, String filename) {
            String stored = null;
            UtilLogger.info("Save sdCard File>" + filename);
            try
            {

                File sdcard = getMainDirectory() ;
                //File sdcard = Environment.getDataDirectory() ;
                File folder = new File(sdcard.getAbsoluteFile(), ConstantesRestApi.imgStorage );//the dot makes this directory hidden to the user
                folder.mkdir();

                File file = new File(folder.getAbsoluteFile(), filename ) ;
                if (file.exists())
                    return stored ;

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                    stored = "success";
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        catch(Exception ex)
        {
            UtilLogger.error("Save to SD ", ex);
        }
        return stored;
    }

    public static File getImage(String imagename) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        File mediaImage = null;
        try {
            String root = getMainDirectory().toString()+ConstantesRestApi.imgStorage;
            //String root = Environment.getDataDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists())
            {
                UtilLogger.info ("getImage > No existe el directorio > " + myDir);
                myDir.mkdirs();
                //return null;
            }
           // UtilLogger.info("getImage > obtenindoArchivo > " + myDir.getPath() +  imagename);
            mediaImage = new File(myDir.getPath() + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            UtilLogger.error("getImage > Error obtenindoArchivo > " + ConstantesRestApi.imgStorage + imagename, e);
        }
        return mediaImage;
    }
    public static boolean checkDir(Activity activity) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        boolean result=false;
        File mediaImage = null;
        try {
            String root = getMainDirectory().toString();
            //String root = Environment.getDataDirectory().toString();

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );


            File myDir = new File(root);
            if (!myDir.exists())
            {
                UtilLogger.info ("checkDir > No existe el directorio > " + myDir);
                return result;
            }
            else
            {
                UtilLogger.info ("checkDir > Existe el directorio > " + myDir);
            }
            UtilLogger.info ("checkDir > obtenindoArchivo > " + myDir.getPath() + "/" + ConstantesRestApi.imgStorage );

            File mydirAndroid = new File(myDir.getPath() + "/Android");
            if (!mydirAndroid.exists()) {
                UtilLogger.info ("checkDir > No existe el directorio creando> " + mydirAndroid);
                mydirAndroid.mkdir();

            }
            else
            {
                UtilLogger.info ("checkDir > Existe el directorio > " + mydirAndroid);
            }

            File mydirData= new File(myDir.getPath() + "/Android/data");
            if (!mydirData.exists()) {
                UtilLogger.info("checkDir > No existe el directorio, creando> " + mydirData);
                mydirData.mkdir();
            }
            else
            {
                UtilLogger.info("checkDir > Existe el directorio > " + mydirData);
            }

            File mydirStorage= new File(myDir.getPath() + "/" +  ConstantesRestApi.imgStorage );
            if (!mydirStorage.exists()) {
                UtilLogger.info("checkDir >No existe el directorio - creando> " + mydirStorage);

                mydirStorage.mkdir();
            }
            else
            {
                UtilLogger.info("checkDir > Existe el directorio > " + mydirStorage);
            }

            File mydirResources= new File(myDir.getPath() + "/" + ConstantesRestApi.imgStorage  );

            if (!mydirResources.exists()) {
                UtilLogger.info("checkDir > No existe el directorio creando> " + mydirResources);
                mydirResources.mkdir();
            }
            else
            {
                UtilLogger.info("checkDir > Existe el directorio > " + mydirResources);
            }

            File mydirPictures= new File(myDir.getPath() + "/" + ConstantesRestApi.imgStorage );

            if (!mydirPictures.exists()) {
                UtilLogger.info("checkDir > No existe el directorio - creando > " + mydirPictures);
                mydirPictures.mkdir();
            }
            else
            {
                UtilLogger.info("checkDir > Existe el directorio > " + mydirPictures);
            }


            File myDir2 = new File(myDir.getPath()  + "/" +  ConstantesRestApi.imgStorage);
            if (!myDir2.exists()) {

            }

            //mediaImage = new File(myDir.getPath() + "/" +  ActiveSettings.imgStorage + imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            UtilLogger.error("checkDir > Error checkDir > " + ConstantesRestApi.imgStorage , e);
        }
        return result;
    }

    public static boolean checkifImageExists(String imagename)
    {
        Bitmap b = null ;
        File file = ImageStorage.getImage("/"+imagename);
        String path = file.getAbsolutePath();

        if (path != null)
            b = BitmapFactory.decodeFile(path);

        if(b == null ||  b.equals(""))
        {
            return false ;
        }
        return true ;
    }




}