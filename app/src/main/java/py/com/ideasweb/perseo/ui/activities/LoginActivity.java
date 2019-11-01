package py.com.ideasweb.perseo.ui.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.fingerprint.FingerprintManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.iml.ActionProcessButton;
import com.multidots.fingerprintauth.AuthErrorCodes;
import com.multidots.fingerprintauth.FingerPrintAuthCallback;
import com.multidots.fingerprintauth.FingerPrintAuthHelper;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;

import butterknife.BindView;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorUsuario;
import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.utilities.Utilities;
import py.com.ideasweb.perseo.utilities.Validation;

public class LoginActivity extends BaseActivity  implements FingerPrintAuthCallback {
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


    //finger
    boolean fingerSuccess = false;
    private FingerPrintAuthHelper mFingerPrintAuthHelper;
    boolean fingerPrintAvailability = true;
    ImageView imgHuella;
    TextView textHuella;
    MaterialDialog dialogPrint;


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

        btnSignIn.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSignIn.setProgress(0);


        SwRecordad.setChecked(true);


        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "font/Roboto-Medium.ttf");
        tEdit.setTypeface(tf);




        SwRecordad.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    recordarCrenciales();
                    Snackbar.make(getCurrentFocus(), R.string.guardarUsuario, Snackbar.LENGTH_LONG).show();
                }else{
                    borrarUsuario();
                    Snackbar.make(getCurrentFocus(),R.string.noGuardarUsuario, Snackbar.LENGTH_LONG).show();
                }
            }
        });


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


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void inicializar() {

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            mFingerPrintAuthHelper = FingerPrintAuthHelper.getHelper(this, this);

        tvuser.setText(usuarioGuardado());
        tEdit.setText(passwordGuardado());
        if (recordarCrenciales().equals("no")){
            SwRecordad.setChecked(false);
        }else{
            SwRecordad.setChecked(true);

        }

        if(!passwordGuardado().equals("")){

            if( mFingerPrintAuthHelper != null){
                mFingerPrintAuthHelper.startAuth();
                if (fingerPrintAvailability){

                    fingerprint(); // el otro dialog
                }
            }
        }

    }

    @Override
    public void setListeners() {

    }

    private void fingerprint(){

        dialogPrint = new MaterialDialog.Builder(LoginActivity.this)
                .title("Ingresar con huella digital")
                .customView(R.layout.layout_fingerprint,true)
                .titleColor(getResources().getColor(R.color.colorAccent))
                // .positiveText(v.getContext().getResources().getString(R.string.aceptar))
                .negativeText(getResources().getString(R.string.cancelar))
                //    .autoDismiss(false)

                .build();

        View layout = dialogPrint.getCustomView();
        imgHuella = (ImageView) layout.findViewById(R.id.imgHuella);
        textHuella = (TextView) layout.findViewById(R.id.textHuella);


        if(!dialogPrint.isShowing()){
            dialogPrint.show();
        }
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

        if(checkValidation()){

            // inicio de la animacion
            btnSignIn.setProgress(1);


            ConstructorUsuario cu = new ConstructorUsuario();

            Usuario u = new Usuario();
            u.setLogin(tvuser.getText().toString().toUpperCase());
            u.setPassword(tEdit.getText().toString().trim());

            if (cu.login(u)){

                //recordad credenciales
                if(SwRecordad.isChecked()){
                    recordarUsuario(tvuser.getText().toString() , tEdit.getText().toString().trim());
                }else{
                    borrarUsuario();
                }
                btnSignIn.setProgress(100);
                selectPerfil(view);

                if(dialogPrint != null)
                if(dialogPrint.isShowing()){
                    dialogPrint.dismiss();
                }

            }else{
                btnSignIn.setProgress(-1);
            }

        }


    }

    private void irHome(String path){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        if(path != null)
            intent.putExtra("url", path);
        startActivity(intent);
        finish();
    }

    private void selectPerfil(View view){

        Perfil perfilactual = new Perfil();
        if(CredentialValues.getLoginData().getUsuario().getPerfiles().size() == 0){
            perfilactual.setIdPerfil(2);
            perfilactual.setDescripcion("FACTURACION");
            CredentialValues.getLoginData().setPerfilactual(perfilactual);
            irHome(null);
            return;
        }else{
            if(CredentialValues.getLoginData().getUsuario().getPerfiles().size() == 1){
                perfilactual.setIdPerfil(CredentialValues.getLoginData().getUsuario().getPerfiles().get(0).getIdperfil());
                perfilactual.setDescripcion(CredentialValues.getLoginData().getUsuario().getPerfiles().get(0).getDescripcion());
                CredentialValues.getLoginData().setPerfilactual(perfilactual);
                if(perfilactual.getIdPerfil() == 1){
                    // si es administrador
                    irHome("py.com.ideasweb.perseo.ui.fragments.LoadingFragment");
                }else{
                    irHome(null);
                }

                return;
            }else{


                new MaterialDialog.Builder(view.getContext())
                        .cancelable(false)
                        .title("Seleccione el Perfil")
                        .cancelable(false)
                        .titleColor(view.getContext().getResources().getColor(R.color.colorPrimaryDark))
                        .items(CredentialValues.getLoginData().getUsuario().getPerfiles())
                        .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                                Perfil perfilactual = new Perfil(Integer.parseInt(Utilities.obtenerIdPerfil(text.toString(),0)),Utilities.obtenerIdPerfil(text.toString(),1));
                                CredentialValues.getLoginData().setPerfilactual(perfilactual);

                                if(perfilactual.getIdPerfil() == 1){
                                    // si es administrador
                                    irHome("py.com.ideasweb.perseo.ui.fragments.LoadingFragment");
                                }else{
                                    irHome(null);
                                }

                                return true;
                            }


                        })

                        .positiveText("Aceptar")
                        .negativeText("Cancelar")
                        .show();
            }
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



    private String recordarCrenciales() {
        SharedPreferences sharedPreferences = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        String usuario= "no";
        usuario = sharedPreferences.getString("guardar-user", "no");
        return usuario;
    }
    private String passwordGuardado() {
        SharedPreferences sharedPreferences = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    private String usuarioGuardado() {
        SharedPreferences sharedPreferences = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    public void  recordarUsuario(String username, String password){
        SharedPreferences SPUbicacion = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("guardad-user", "si");
        editor.commit();
    }
    public void  borrarUsuario(){
        SharedPreferences SPUbicacion = getSharedPreferences("perseo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = SPUbicacion.edit();
        //editor.putString("username", "no");
        //editor.putString("password", "");
        editor.putString("guardad-user", "no");
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


    @Override
    public void onNoFingerPrintHardwareFound() {
        fingerPrintAvailability = false;
        System.out.println("NO HAY HARDWARE DE FINGERPRINT");
    }

    @Override
    public void onNoFingerPrintRegistered() {

        fingerPrintAvailability = false;
        System.out.println("NO HAY HUELLAS DE FINGERPRINT REGISTRADAS");
    }

    @Override
    public void onBelowMarshmallow() {

        fingerPrintAvailability = false;
        System.out.println("ES INFERIOR A MARSHMELLOW");
    }

    @Override
    public void onAuthSuccess(FingerprintManager.CryptoObject cryptoObject) {

        fingerSuccess = true;

        tvuser.setText(getSharedPreferences("perseo", MODE_PRIVATE)
                    .getString("username", ""));

        tEdit.setText(getSharedPreferences("perseo", MODE_PRIVATE)
                .getString("password", ""));


        imgHuella.setImageDrawable(getDrawable(R.drawable.checked_48));
        textHuella.setText("Huella digital reconocida.");
        textHuella.setTextColor(getResources().getColor(R.color.colorPrimary));
        logueo(getCurrentFocus());

    }

    @Override
    public void onAuthFailed(int errorCode, String errorMessage) {

        switch (errorCode) {
            case AuthErrorCodes.CANNOT_RECOGNIZE_ERROR:
                textHuella.setText("No se puede reconocer su huella digital. Inténtalo de nuevo.");
                break;
            case AuthErrorCodes.NON_RECOVERABLE_ERROR:
                textHuella.setText("No se puede inicializar la autenticación de huella digital. Ingrese con su contraseña");
                break;
            case AuthErrorCodes.RECOVERABLE_ERROR:
                textHuella.setText(errorMessage);
                break;
        }
    }
}
