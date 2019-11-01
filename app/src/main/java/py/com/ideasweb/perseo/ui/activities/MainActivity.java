package py.com.ideasweb.perseo.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import butterknife.BindView;
import dmax.dialog.SpotsDialog;
import me.toptas.fancyshowcase.FancyShowCaseView;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.adapter.DrawerAdapter;
import py.com.ideasweb.perseo.constructor.ConstructorTalonario;
import py.com.ideasweb.perseo.constructor.ConstructorUsuario;
import py.com.ideasweb.perseo.models.Facturacab;
import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Tracking;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.manager.UsuarioManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.LoginData;
import py.com.ideasweb.perseo.ui.elements.DrawerItem;
import py.com.ideasweb.perseo.ui.elements.ItemClickSupport;
import py.com.ideasweb.perseo.utilities.GPSTracker;
import py.com.ideasweb.perseo.utilities.GPSTracker2;
import py.com.ideasweb.perseo.utilities.MiUbicacion;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;
import py.com.ideasweb.perseo.utilities.Validation;
import py.com.ideasweb.perseo.work.LocationJobService;


public class MainActivity extends BaseActivity implements GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,  Runnable {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.textViewName)
    TextView user;

    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    RelativeLayout relativeLayoutScrollViewChild;
    ScrollView scrollViewNavigationDrawerContent;
    ViewTreeObserver viewTreeObserverNavigationDrawerScrollView;
    ViewTreeObserver.OnScrollChangedListener onScrollChangedListener;
    RecyclerView recyclerViewDrawer1, recyclerViewDrawerSettings;
    RecyclerView.Adapter drawerAdapter1, drawerAdapterSettings;
    ArrayList<DrawerItem> drawerItems1, drawerItemsSettings;
    float drawerHeight, scrollViewHeight;
    LinearLayoutManager linearLayoutManager,  linearLayoutManagerSettings;
    ItemClickSupport itemClickSupport1, itemClickSupport3;
    int colorPrimary, textColorPrimary, colorControlHighlight, colorBackground;


    //location
    private GoogleApiClient apiClient;
    GPSTracker gps;
    private static final String LOGTAG = "android-localizacion";
    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    private static final long SPLASH_SCREEN_DELAY = 3000;

    private static final int INTERVALO = 2000; //2 segundos para salir
    private long tiempoPrimerClick;

    public static boolean inHome;

    //password
    EditText passAnterior ;
    EditText pass1 ;
    EditText pass2 ;

    //ayuda
    MenuItem menuAyuda;

    // IMPRESION
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private ProgressDialog mBluetoothConnectProgressDialog;
    public static BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    protected static final String TAG = "PRINT";
    private UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    Facturacab factura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inicializar();

        //Utilities.iniciarTareaPeriodica();

        // TRACKING
       /* System.out.println("INICIALIZAR ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("registered"," on start service");
            startBackgroundService();
        }else{
            Toast.makeText(getBaseContext(),"service for pre lollipop will be available in next update",Toast.LENGTH_LONG).show();
        }*/



    }

    public static final String JOB_STATE_CHANGED = "jobStateChanged";
    public static final String LOCATION_ACQUIRED = "locAcquired";
    boolean registered = false, isServiceStarted=false;

    private BroadcastReceiver jobStateChanged = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction()==null){
                return;
            }
            if(intent.getAction().equals(JOB_STATE_CHANGED)) {
                //changeServiceButton(intent.getExtras().getBoolean("isStarted"));
            }else if (intent.getAction().equals(LOCATION_ACQUIRED)){
                if(intent.getExtras()!=null){
                    Bundle b = intent.getExtras();
                    Location l = b.getParcelable("location");
                    updateUI(l);
                }else{
                    Log.d("intent","null");
                }
            }
        }
    };



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startBackgroundService() {
        if(!registered) {
            IntentFilter i = new IntentFilter(JOB_STATE_CHANGED);
            i.addAction(LOCATION_ACQUIRED);
            LocalBroadcastManager.getInstance(this).registerReceiver(jobStateChanged, i);
        }
        JobScheduler jobScheduler =
                (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        assert jobScheduler != null;
        jobScheduler.schedule(new JobInfo.Builder(LocationJobService.LOCATION_SERVICE_JOB_ID,
                new ComponentName(this, LocationJobService.class))
                .setOverrideDeadline(500)
                .setPersisted(true)
                .setRequiresDeviceIdle(false)
                .build());
    }

    private void stopBackgroundService() {
        if(getSharedPreferences("PERSEO",MODE_PRIVATE).getBoolean("isServiceStarted",false)){
            Log.d("registered"," on stop service");
            Intent stopJobService = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                stopJobService = new Intent(LocationJobService.ACTION_STOP_JOB);
                LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(stopJobService);
            }else{
                Toast.makeText(getApplicationContext(),"yet to be coded - stop service",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void inicializar() {

        //Drawer
        setupNavigationDrawer();

        if (CredentialValues.getLoginData().getUsuario() != null) {
            user.setText(CredentialValues.getLoginData().getUsuario().getNombreApellido());
        }

        ConstructorTalonario constructorTal = new ConstructorTalonario();
        constructorTal.cargarTalonario();

        if(Utilities.isNetworkConnected(getApplicationContext())){
            if(Utilities.getUltDownload(getApplicationContext()).equals("")){

                System.out.println(Utilities.getUltDownload(getApplicationContext()));
                System.out.println("DESCARGANDO............");
                final AlertDialog dialog = new SpotsDialog.Builder()
                        .setContext(this)
                        .setTheme(R.style.spots)
                        .setMessage("Descargando datos...")
                        .build();
                dialog.show();

                Utilities.bajarDatos(getApplicationContext());

                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {

                        dialog.dismiss();

                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, SPLASH_SCREEN_DELAY);

            }
        }

        Bundle param = getIntent().getExtras();

        if (param != null) {
            inHome = false;
            //Fragment frags = Fragment.instantiate(getApplicationContext(), "py.com.ideasweb.ui.fragments." +  param.getString("back"));
            Fragment frags = Fragment.instantiate(getApplicationContext(), param.getString("url"));
            frags.setArguments(param);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, frags);
            fragmentTransaction.commit();


        }else{
            inHome = true;


            //infla el home
            Fragment fragshome = Fragment.instantiate(getApplicationContext(), "py.com.ideasweb.perseo.ui.fragments.HomeFragment");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragshome);
            fragmentTransaction.commit();

            //getPedidos();
        }

        //location
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        gps = new GPSTracker(getApplicationContext());


        //ayuda
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    new FancyShowCaseView.Builder(MainActivity.this)
                            .focusOn(findViewById(menuAyuda.getItemId()))
                            .title("Pulsando aquí en cada pantalla, encontrarás un poco de ayuda :)")
                            .build()
                            .show();
                }catch (Exception e){

                }

            }
        };

        // showcase primera vez instalado
        Boolean isFirstRun = getSharedPreferences("perseo", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if(isFirstRun){
            // hacer o mostrar algo aqui...
            Timer timer = new Timer();
            timer.schedule(task, SPLASH_SCREEN_DELAY);
        }
        // desabilitar primer ingreso
        getSharedPreferences("perseo", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();


    }

    @Override
    public void setListeners() {

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        System.out.println("inHome: " + inHome);
        if(inHome){

            if (tiempoPrimerClick + INTERVALO > System.currentTimeMillis()){
                super.onBackPressed();
                return;
            }else {
                Toast.makeText(this, getResources().getString(R.string.cerra_app), Toast.LENGTH_SHORT).show();
            }
            tiempoPrimerClick = System.currentTimeMillis();

        }else{
            inHome = true;
            Fragment frags = Fragment.instantiate(getApplicationContext(), "py.com.ideasweb.perseo.ui.fragments.HomeFragment");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body,frags);
            fragmentTransaction.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        menuAyuda = menu.findItem(R.id.ayuda);

        return true;
    }
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
            case R.id.ayuda:
                new FancyShowCaseView.Builder(this)
                        .focusOn(findViewById(item.getItemId()))
                        .title("Pulsando aquí en cada pantalla, encontrarás un poco de ayuda :)")
                        .build()
                        .show();
            default:
                break;

        }

        return super.onOptionsItemSelected(item);
    }



    public void setupNavigationDrawer() {

        // Setup Navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        // Setup Drawer Icon
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerToggle.syncState();


        // Hide Settings and Feedback buttons when navigation drawer is scrolled
        hideNavigationDrawerSettingsAndFeedbackOnScroll();

        // Setup RecyclerViews inside drawer
        setupNavigationDrawerRecyclerViews();
    }

    private void hideNavigationDrawerSettingsAndFeedbackOnScroll() {

        scrollViewNavigationDrawerContent = (ScrollView) findViewById(R.id.scrollViewNavigationDrawerContent);
        relativeLayoutScrollViewChild = (RelativeLayout) findViewById(R.id.relativeLayoutScrollViewChild);
        viewTreeObserverNavigationDrawerScrollView = relativeLayoutScrollViewChild.getViewTreeObserver();

        if (viewTreeObserverNavigationDrawerScrollView.isAlive()) {
            viewTreeObserverNavigationDrawerScrollView.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT > 16) {
                        relativeLayoutScrollViewChild.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        relativeLayoutScrollViewChild.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    drawerHeight = relativeLayoutScrollViewChild.getHeight();
                    scrollViewHeight = scrollViewNavigationDrawerContent.getHeight();

                }
            });
        }

        onScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollViewNavigationDrawerContent.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
            }
        };

        scrollViewNavigationDrawerContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewTreeObserver observer;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        observer = scrollViewNavigationDrawerContent.getViewTreeObserver();
                        observer.addOnScrollChangedListener(onScrollChangedListener);
                        break;
                    case MotionEvent.ACTION_UP:
                        observer = scrollViewNavigationDrawerContent.getViewTreeObserver();
                        observer.addOnScrollChangedListener(onScrollChangedListener);
                        break;
                }
                return false;
            }
        });
    }

    private void setupNavigationDrawerRecyclerViews() {

        // RecyclerView 1
        recyclerViewDrawer1 = (RecyclerView) findViewById(R.id.recyclerViewDrawer1);
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerViewDrawer1.setLayoutManager(linearLayoutManager);

        drawerItems1 = new ArrayList<>();
        //material icons
        Drawable homeDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.HOME) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build

        drawerItems1.add(new DrawerItem(homeDrawable, getResources().getString(R.string.home), "py.com.ideasweb.perseo.ui.fragments.HomeFragment", "F"));

        Drawable yourDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.LABEL) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build


        /*for (HomeItem item: HomeItemRepo.getModulos()) {
            drawerItems1.add(new DrawerItem(yourDrawable, item.getTexto(), item.getUrl(), item.getTipo()));

        }*/


        drawerAdapter1 = new DrawerAdapter(drawerItems1);
        recyclerViewDrawer1.setAdapter(drawerAdapter1);

        recyclerViewDrawer1.setMinimumHeight(convertDpToPx(144));
        recyclerViewDrawer1.setHasFixedSize(true);

        // RecyclerView 2


        // RecyclerView Settings
        recyclerViewDrawerSettings = (RecyclerView) findViewById(R.id.recyclerViewDrawerSettings);
        linearLayoutManagerSettings = new LinearLayoutManager(MainActivity.this);
        recyclerViewDrawerSettings.setLayoutManager(linearLayoutManagerSettings);

        drawerItemsSettings = new ArrayList<>();

        Drawable profileDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.ACCOUNT) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        drawerItemsSettings.add(new DrawerItem(profileDrawable, getResources().getString(R.string.miperfil), "", ""));

        Drawable passwordDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.LOCK) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        drawerItemsSettings.add(new DrawerItem(passwordDrawable,"Cambiar Contraseña", "", ""));

        Drawable perfilDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.REPEAT) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        drawerItemsSettings.add(new DrawerItem(perfilDrawable,"Cambiar Perfil", "", ""));


        //material icons
        Drawable logoutDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.POWER_SETTINGS) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        drawerItemsSettings.add(new DrawerItem(logoutDrawable, getResources().getString(R.string.logout), "", ""));
        //material icons
        Drawable aboutDrawable = MaterialDrawableBuilder.with(getApplicationContext()) // provide a context
                .setIcon(MaterialDrawableBuilder.IconValue.HELP_CIRCLE_OUTLINE) // provide an icon
                .setColor(getResources().getColor(R.color.colorPrimaryDark)) // set the icon color
                .setToActionbarSize() // set the icon size
                .build(); // Finally call build
        drawerItemsSettings.add(new DrawerItem(getResources().getDrawable(R.drawable.ideas_web_logo), getResources().getString(R.string.nosotros), "", ""));

        drawerItemsSettings.add(new DrawerItem(aboutDrawable, "Ayuda", "", ""));

        drawerAdapterSettings = new DrawerAdapter(drawerItemsSettings);
        recyclerViewDrawerSettings.setAdapter(drawerAdapterSettings);

        recyclerViewDrawerSettings.setMinimumHeight(convertDpToPx(96));
        recyclerViewDrawerSettings.setHasFixedSize(true);

        //colores
        //color del texto seleccionado
        colorPrimary = getResources().getColor(R.color.colorPrimaryDark);
        //color del texto no seleecionado
        textColorPrimary = getResources().getColor(R.color.secondary_text);
        //fondo seleccionado
        colorControlHighlight = getResources().getColor(R.color.colorPrimaryLight);

        // Set icons alpha at start
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after some time
                for (int i = 0; i < recyclerViewDrawer1.getChildCount(); i++) {
                    ImageView imageViewDrawerItemIcon = (ImageView) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.imageViewDrawerItemIcon);
                    TextView textViewDrawerItemTitle = (TextView) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);

                    LinearLayout linearLayoutItem = (LinearLayout) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.linearLayoutItem);

                    if (i == 0) {
                        //  imageViewDrawerItemIcon.setColorFilter(colorPrimary);
                        textViewDrawerItemTitle.setTextColor(colorPrimary);
                        linearLayoutItem.setBackgroundColor(colorControlHighlight);
                    } else {
                        //   imageViewDrawerItemIcon.setColorFilter(textColorPrimary);
                        textViewDrawerItemTitle.setTextColor(textColorPrimary);
                        linearLayoutItem.setBackgroundColor(colorBackground);
                    }
                }


                for (int i = 0; i < recyclerViewDrawerSettings.getChildCount(); i++) {
                    ImageView imageViewDrawerItemIcon = (ImageView) recyclerViewDrawerSettings.getChildAt(i).findViewById(R.id.imageViewDrawerItemIcon);
                    TextView textViewDrawerItemTitle = (TextView) recyclerViewDrawerSettings.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                    LinearLayout linearLayoutItem = (LinearLayout) recyclerViewDrawerSettings.getChildAt(i).findViewById(R.id.linearLayoutItem);
                    // imageViewDrawerItemIcon.setColorFilter(textColorPrimary);
                    textViewDrawerItemTitle.setTextColor(textColorPrimary);
                    //linearLayoutItem.setBackgroundColor(colorBackground);;
                }
            }
        }, 500);

        itemClickSupport1 = ItemClickSupport.addTo(recyclerViewDrawer1);
        itemClickSupport1.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, long id) {
                for (int i = 0; i < recyclerViewDrawer1.getChildCount(); i++) {
                    ImageView imageViewDrawerItemIcon = (ImageView) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.imageViewDrawerItemIcon);
                    TextView textViewDrawerItemTitle = (TextView) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.textViewDrawerItemTitle);
                    TextView urlActivity = (TextView) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.drawerActivity);
                    TextView typeMenu = (TextView) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.drawerType);
                    LinearLayout linearLayoutItem = (LinearLayout) recyclerViewDrawer1.getChildAt(i).findViewById(R.id.linearLayoutItem);
                    if (i == position) {
                        inHome = false;
                        if (Build.VERSION.SDK_INT > 15) {
                            imageViewDrawerItemIcon.setImageAlpha(255);
                        } else {
                            imageViewDrawerItemIcon.setAlpha(255);
                        }
                        textViewDrawerItemTitle.setTextColor(colorPrimary);
                        linearLayoutItem.setBackgroundColor(colorControlHighlight);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        //si se seleecciona
                        //recargar el reciclerview con el parametro enviado

                        if (typeMenu.getText().toString().trim().equals("A")) {

                            if(textViewDrawerItemTitle.getText().toString().equals("Registar Factura")){
                                UtilLogger.info("Ingresando a registro de factura");
                                if(LoginData.getTalonario() == null){
                                    System.out.println("El Talonario es nulo");

                                    Toast.makeText(getApplicationContext(), "Agregue un talonario antes de registrar alguna factura", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }

                                Intent intent = new Intent();
                                intent.setClassName(getApplicationContext(),  urlActivity.getText().toString());
                                startActivity(intent);
                                finish();

                        } else if (typeMenu.getText().toString().trim().equals("F")) {

                            Fragment frags = Fragment.instantiate(getApplicationContext(), urlActivity.getText().toString());

                            if(textViewDrawerItemTitle.getText().toString().equals(getResources().getString(R.string.home))){
                                inHome = true;
                            }
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.container_body, frags);
                            fragmentTransaction.commit();


                        }


                    } else {
                        //    imageViewDrawerItemIcon.setColorFilter(textColorPrimary);
                        textViewDrawerItemTitle.setTextColor(textColorPrimary);
                        linearLayoutItem.setBackgroundColor(colorBackground);
                    }
                }

            }
        });


        //setings click listener
        itemClickSupport3 = ItemClickSupport.addTo(recyclerViewDrawerSettings);
        itemClickSupport3.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, final View view, int position, long id) {
                switch (position) {
                    case 0:
                        inHome = false;

                        Fragment frags2 = Fragment.instantiate(getApplicationContext(),"py.com.ideasweb.perseo.ui.fragments.ProfileFragment");
                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                        fragmentTransaction2.replace(R.id.container_body,frags2);
                        fragmentTransaction2.commit();

                        drawerLayout.closeDrawer(GravityCompat.START);

                        break;
                    case 1:
                        inHome = true;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        cambiarPass(view);
                        break;
                    case 2:
                        inHome = true;
                        drawerLayout.closeDrawer(GravityCompat.START);
                        if(CredentialValues.getLoginData().getUsuario().getPerfiles().size() > 1){

                            new MaterialDialog.Builder(MainActivity.this)
                                    .title("Seleccione el Perfil")
                                    .titleColor(getResources().getColor(R.color.colorPrimaryDark))
                                    .items(CredentialValues.getLoginData().getUsuario().getPerfiles())
                                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                                        @Override
                                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {


                                            Perfil perfilactual = new Perfil(Integer.parseInt(Utilities.obtenerIdPerfil(text.toString(),0)),Utilities.obtenerIdPerfil(text.toString(),1));
                                            CredentialValues.getLoginData().setPerfilactual(perfilactual);

                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                            finish();

                                            return true;

                                        }
                                    })

                                    .positiveText("Aceptar")
                                    .negativeText("Cancelar")
                                    .show();

                        }else{

                            Utilities.sendToast(getApplicationContext(), "Solo tienes este perfil", "info");
                        }
                        break;

                    case 3:
                        inHome = false;
                        new MaterialDialog.Builder(MainActivity.this)
                                .title(R.string.logout)
                                .content(R.string.dialog_logout)
                                .positiveText(R.string.si)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                        stopBackgroundService();
                                        Utilities.deleteLoginData();
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();


                                    }
                                })
                                .negativeText(R.string.no)
                                .show();

                        break;
                    case 4:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        boolean wrapInScrollView = true;
                        new MaterialDialog.Builder(view.getContext())
                                .icon(getResources().getDrawable(R.mipmap.ic_launcher))
                                .customView(R.layout.layout_acercade, wrapInScrollView)
                                .positiveText("OK")
                                .show();
                        break;
                    case 5:
                        inHome = false;
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        startActivity(intent);
                       // finish();
                        break;



                }
            }
        });

    }


    public int convertDpToPx(int dp) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (dp * scale + 0.5f);
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
           /* Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/juan-emilio-espinola-874716125"));
            startActivity(intent);*/
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra("url", "https://www.linkedin.com/in/juan-emilio-espinola-874716125");
            startActivity(intent);
        }
    }

    public void irPagina(View v) {
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        intent.putExtra("url", "http://www.ideasweb.com.py/ideasweb/index");
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PETICION_PERMISO_LOCALIZACION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                gps = new GPSTracker(getApplicationContext());
                //Permiso concedido
                @SuppressWarnings("MissingPermission")

                Location lastLocation =gps.getLocation();

                //updateUI(lastLocation);


            } else {
                //Permiso denegado:
                //Deberíamos deshabilitar toda la funcionalidad relativa a la localización.

                Log.e(LOGTAG, "Permiso denegado");
            }
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
        } else {

            Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(apiClient);

            //updateUI(lastLocation);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

        UtilLogger.info( "Se ha interrumpido la conexión con Google Play Services");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        UtilLogger.error( "Error grave al conectar con Google Play Services");
    }

    private void updateUI(Location loc) {

        if (loc != null) {

            if(CredentialValues.getLoginData() != null){

                System.out.println("Agregando nuevo punto: " + String.valueOf(loc.getLatitude()) + ","+String.valueOf(loc.getLongitude()));

                Tracking t = new Tracking();
                t.setCoordenadas(String.valueOf(loc.getLatitude()) + ","+String.valueOf(loc.getLongitude()));
                t.setFechaHora(Utilities.getCurrentDateTimeBD());
                t.setIdusuario(CredentialValues.getLoginData().getUsuario().getIdUsuario());
                if(MiUbicacion.lugar != null){
                    t.setDireccion(MiUbicacion.lugar.address);
                    t.setCiudad(MiUbicacion.lugar.city);
                }

                t.save();


                LatLng punto = new LatLng(loc.getLatitude(), loc.getLongitude());
                MiUbicacion.listaPuntos.add(punto); // Se añade un punto a la lista, para crear una ruta después.
            }


            Log.e(LOGTAG, "Actualizando coordenadas " + loc.getLatitude() + loc.getLongitude());
            MiUbicacion.setLatitud(loc.getLatitude());
            MiUbicacion.setLongitud(loc.getLongitude());
            MiUbicacion.setBan(true);
            MiUbicacion.guardarUbicacion(getApplicationContext());


        } else {
            MiUbicacion.setBan(false);
            gps = new GPSTracker(getApplicationContext());

            if (!gps.canGetLocation()) {
//                gps.showSettingsAlert();
            }
            if (Utilities.isNetworkConnected(getApplicationContext()))
                gps.getLocation();
        }

    }

    // IMPRESION

    @Override
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
            System.out.println("RUN");

        } catch (IOException eConnectException) {
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);

            return;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(MainActivity.this, "Impresora conectada. Imprimiendo", Toast.LENGTH_SHORT).show();
            Utilities.imprimirFactura(factura, getApplicationContext());
        }
    };

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();

            if(mBluetoothConnectProgressDialog.isShowing())
                mBluetoothConnectProgressDialog.dismiss();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            Log.d(TAG, "CouldNotCloseSocket");
        }
    }

    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);
                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();
                    // pairToDevice(mBluetoothDevice); This method is replaced by
                    // progress dialog with thread
                    System.out.println("REQUEST_CONNECT_DEVICE");
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(MainActivity.this,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    System.out.println("REQUEST_ENABLE_BT");

                    //grabar();

                } else {
                    Toast.makeText(MainActivity.this, "onActivityResult REQUEST_ENABLE_BT", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    public void buscarImpresora(Facturacab cab){

        this.factura = cab;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(MainActivity.this, "No tiene soporte de bluetooht", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                ListPairedDevices();
                Intent connectIntent = new Intent(MainActivity.this,
                        DeviceListActivity.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
        }
    }


    private void cambiarPass(View v){

        if(Utilities.isNetworkConnected(getApplicationContext())){

            MaterialDialog dialog1 = new MaterialDialog.Builder(v.getContext())
                    .title("Cambiar contraseña")
                    .customView(R.layout.layout_cambiar_password,true)
                    .titleColor(v.getContext().getResources().getColor(R.color.colorPrimary))
                    .positiveText(v.getContext().getResources().getString(R.string.aceptar))
                    .negativeText(v.getContext().getResources().getString(R.string.cancelar))
                    .autoDismiss(false)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull final MaterialDialog d, @NonNull DialogAction which) {

                            if (checkValidation()){

                                if(Utilities.isNetworkConnected(getApplicationContext())){

                                    ConstructorUsuario cu = new ConstructorUsuario();
                                    Usuario user = new Usuario();
                                    user.setLogin(CredentialValues.getLoginData().getUsuario().getLogin());
                                    user.setPassword(passAnterior.getText().toString());
                                    if(cu.login(user)){
                                        CredentialValues.getLoginData().getUsuario().setPassword(user.getPassword());
                                        cu.update(CredentialValues.getLoginData().getUsuario());

                                        new MaterialDialog.Builder(MainActivity.this)
                                                .title(getResources().getString(R.string.pwdupdate))
                                                .titleColor(getResources().getColor(R.color.colorPrimary))
                                                .icon(getResources().getDrawable(R.drawable.checked_48))
                                                .positiveText(R.string.aceptar)
                                                .autoDismiss(false)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                                        startActivity(intent);
                                                        finish();

                                                    }
                                                })
                                                .show();

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    UsuarioManager manager = new UsuarioManager();
                                                    manager.grabarUsuario(CredentialValues.getLoginData().getUsuario());

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();




                                    }else{
                                        passAnterior.setError("La contraseña no coincide");
                                    }


                                }else{
                                    Utilities.sendToast(getApplicationContext(), "Necesitas conexion a internet para realizar esta accion", "error");
                                }


                            }

                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .build();

            View layout = dialog1.getCustomView();
            passAnterior = (EditText) layout.findViewById(R.id.PwdAnterior);
            pass1 = (EditText) layout.findViewById(R.id.PwdUno);
            pass2 = (EditText) layout.findViewById(R.id.PwdDos);

            dialog1.show();
        }else{
            Toast.makeText(getApplicationContext(), "Debes estar conectado a internet para realizar esta accion", Toast.LENGTH_LONG).show();
        }

    }


    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(pass1, getApplicationContext())) ret = false;
        if (!Validation.hasText(pass2, getApplicationContext())) ret = false;
        if (!Validation.hasText(passAnterior, getApplicationContext())) ret = false;

        if(Validation.hasText(pass1, getApplicationContext()) &&
                Validation.hasText(pass2, getApplicationContext())){
            if(!pass1.getText().toString().equals(pass2.getText().toString())){
                ret =  false;
                pass1.setError(getResources().getString(R.string.nocoincidenpwd));
                pass2.setError(getResources().getString(R.string.nocoincidenpwd));

            }else{
                pass1.setError(null);
                pass2.setError(null);
            }
        }

        return ret;
    }


}
