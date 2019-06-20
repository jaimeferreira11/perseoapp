package py.com.ideasweb.perseo.constructor;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import py.com.ideasweb.perseo.restApi.pojo.Menuapp;


/**
 * Created by jaime on 31/03/17.
 */

public class ConstructorMenu {
    private Context context;

    public ConstructorMenu(Context context) {
        this.context = context;
    }

    /*public ArrayList<Menuapp> obtenerMenus(){
        DatabaseHelper db = new DatabaseHelper(context);
        return db.getAllMenuFromBD();

    }*/


    /*public void insertarMenu(ArrayList<Menuapp> menuapps){
        DatabaseHelper db = new DatabaseHelper(context);
        int cont = 0;

        for (int i = 0; i < menuapps.size() ; i++) {

            ContentValues contentValues =  new ContentValues();
            contentValues.put(DbModelStruct.DF_MENU.idmenu.toString(), menuapps.get(i).getIdmenuapp());
            contentValues.put(DbModelStruct.DF_MENU.descripcion.toString() , menuapps.get(i).getDescripcion());
            contentValues.put(DbModelStruct.DF_MENU.orden.toString() , menuapps.get(i).getOrden());
            contentValues.put(DbModelStruct.DF_MENU.tipo.toString() , menuapps.get(i).getTipo());
            int active = 0;
            if(menuapps.get(i).getEstado()) active = 1;
            contentValues.put(DbModelStruct.DF_MENU.estado.toString() , active);
            contentValues.put(DbModelStruct.DF_MENU.path.toString() , menuapps.get(i).getPath());
            contentValues.put(DbModelStruct.DF_MENU.idversion.toString() , menuapps.get(i).getIdversion());


            cont = cont + db.insertarMenus(contentValues);
        }
        System.out.println("Menus insertados: " + cont);

    }
*/
}
