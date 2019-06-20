package py.com.ideasweb.perseo.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.iml.ActionProcessButton;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import butterknife.BindView;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorUsuario;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.utilities.Validation;

public class LoginActivity extends BaseActivity  {
   // private Toolbar mToolbar;
    /*private SwitchCompat SwRecordad;
    TextView tvuser;
    TextView tvpass;*/

    @BindView(R.id.loginUsuario)
    TextView tvuser;
    @BindView(R.id.loginPwd)
    ShowHidePasswordEditText tEdit;
    @BindView(R.id.loginRecordar)
    SwitchCompat SwRecordad;
    @BindView(R.id.btnSignIn)
    ActionProcessButton btnSignIn;


   // Button login;
  //  ShowHidePasswordEditText tEdit;
  //  ActionProcessButton btnSignIn;
    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;
    private static final long SPLASH_SCREEN_DELAY = 1000;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static final String EXTRAS_ENDLESS_MODE = "EXTRAS_ENDLESS_MODE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        // se agrega el toolbar
        /*mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //se oculta el title por defecto
        getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        // des/habilitar navigacion hacia atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


        inicializar();
        setListeners();

     //   SwRecordad = (SwitchCompat) findViewById(R.id.loginRecordar);
     //   tvuser = (TextView) findViewById(R.id.loginUsuario);
     //   tvpass = (TextView) findViewById(R.id.loginPwd);





     //   btnSignIn = (ActionProcessButton) findViewById(R.id.btnSignIn);
        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setProgress(0);


        SwRecordad.setChecked(true);
        String usuarioguardado= UsuarioGuardado();

    //    tEdit = ( ShowHidePasswordEditText ) findViewById(R.id.loginPwd);

        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Medium.ttf");
        tEdit.setTypeface(tf);


        if (usuarioguardado.equals("no")){
            SwRecordad.setChecked(false);
        }else{
            SwRecordad.setChecked(true);
            tEdit.requestFocus(); // (java)
            //request al loginPwd
            tvuser.setText(usuarioguardado);
        }

        SwRecordad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //  Snackbar.make(getCurrentFocus(), R.string.guardarUsuario, Snackbar.LENGTH_SHORT).show();

                    Snackbar.make(getCurrentFocus(), R.string.guardarUsuario, Snackbar.LENGTH_LONG).show();

                    recordarUsuario(tvuser.getText().toString());
                }else{
                    // Snackbar.make(getCurrentFocus(), R.string.noGuardarUsuario, Snackbar.LENGTH_SHORT).show();

                    Snackbar.make(getCurrentFocus(),R.string.noGuardarUsuario, Snackbar.LENGTH_LONG).show();

                    borrarUsuario();
                }
            }
        });

        // Permiso para almacenamiento interno
        //verifyStoragePermissions(this);


        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.CAMERA};

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        //new SpotsDialog(this).show();

        //getParametros();


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void inicializar() {

    }

    @Override
    public void setListeners() {

    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void logueo(View view){

        /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();*/


        if(checkValidation()){

            if(SwRecordad.isChecked()){
                recordarUsuario(tvuser.getText().toString());
            }
            // inicio de la animacion
            btnSignIn.setProgress(1);


            ConstructorUsuario cu = new ConstructorUsuario();

            Usuario u = new Usuario();
            u.setLogin(tvuser.getText().toString().toUpperCase());
            u.setPassword(tEdit.getText().toString().trim());

            if (cu.login(u)){
                btnSignIn.setProgress(100);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                btnSignIn.setProgress(-1);
            }



           // AlertDialog dialog = new SpotsDialog(this, "Custom message & style", R.style.spots);
           // dialog.show();

                /*new Thread(new Runnable() {
                    public void run() {
                        try {
                            LoginManager manager = new LoginManager();
                            Usuario u = new Usuario();
                            u.setUsuario(tvuser.getText().toString().toUpperCase());
                            u.setPassword(tEdit.getText().toString());
                            final Respuesta respuesta =manager.login(u);

                            if(respuesta.getEstado().equals("OK")){

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnSignIn.setProgress(100);
                                    }
                                });
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else{

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        btnSignIn.setProgress(-1);
                                    }
                                });
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

                }).start();*/

               // dialog.dismiss();


        }


    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    //pulsar dos veces para cerrar apps
    @Override
    public void onBackPressed(){
        if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else {
            Toast.makeText(this, getResources().getString(R.string.cerra_app), Toast.LENGTH_SHORT).show();
        }
        tiempoPrimerClick = System.currentTimeMillis();
    }

    //infla en el activity el menu de opciones
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // menu de opciones
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mabout:
                boolean wrapInScrollView = true;
                new MaterialDialog.Builder(this)
                        .icon(getResources().getDrawable(R.drawable.ideas_web_logo))
                        .customView(R.layout.layout_acercade, wrapInScrollView)
                        .positiveText("OK")
                        .show();
                break;
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    private String UsuarioGuardado() {
        SharedPreferences sharedPreferences = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        String usuario= "no";
        usuario = sharedPreferences.getString("username", "no");
        return usuario;
    }

    public void  recordarUsuario(String username){
        SharedPreferences SPUbicacion = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        editor.putString("username", username);
        editor.commit();
    }
    public void  borrarUsuario(){
        SharedPreferences SPUbicacion = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        editor.putString("username", "no");
        editor.commit();
    }

    // validacion de formulario
    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText((EditText) findViewById(R.id.loginUsuario), getApplicationContext())) ret = false;
        if (!Validation.hasText((EditText) findViewById(R.id.loginPwd), getApplicationContext())) ret = false;

        return ret;
    }

    //Link redes sociales
    public void irLinkJaime(View v) {

        try {
            v.getContext().getPackageManager().getPackageInfo("com.linkedin.android", 0);
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://jaimeferreira91"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/jaimeferreira91"));
            startActivity(intent);
        }
    }

    //Link redes sociales
    public void irLinkMarcos(View v) {

        try {
            v.getContext().getPackageManager().getPackageInfo("com.linkedin.android", 0);
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://marcos-cespedes-8aa92319"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/marcos-cespedes-8aa92319"));
            startActivity(intent);
        }
    }

    public void irLinkEmilio(View v) {

        try {
            v.getContext().getPackageManager().getPackageInfo("com.linkedin.android", 0);
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://juan-emilio-espinola-874716125"));
            startActivity(intent);
        } catch (Exception e) {
            Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/juan-emilio-espinola-874716125"));
            startActivity(intent);
        }
    }


}
