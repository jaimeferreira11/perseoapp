package py.com.ideasweb.perseo.ui.fragments;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorUsuario;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.manager.UsuarioManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;
import py.com.ideasweb.perseo.utilities.Validation;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.documento)
    EditText documento;
    @BindView(R.id.nombre)
    EditText nombre;
    @BindView(R.id.celular)
    EditText celular;
    @BindView(R.id.correo)
    EditText correo;

    @BindView(R.id.pbLoaderCard)
    ProgressBar progressBar;

    @BindView(R.id.tvCambiarFoto)
    TextView mOptionButton;

    @BindView(R.id.btnPerfilActualizar)
    Button actualizar;

    Unbinder unbinder;
    private CircleImageView mSetImage;
    private CircleImageView homeProfile;
    private String mPath;
    private Uri file;
    private Boolean flag = false;

    private static String APP_DIRECTORY;
    private static String MEDIA_DIRECTORY;

    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;

    View view;
    File mediaStorageDir;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        mSetImage = (CircleImageView) view.findViewById(R.id.imagCircular);
        homeProfile = (CircleImageView) getActivity().findViewById(R.id.imageViewPictureMain);



        if(mayRequestStoragePermission())
            mOptionButton.setEnabled(true);
        else
            mOptionButton.setEnabled(false);

        mOptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });


        mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Perseo");


        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if(Utilities.isNetworkConnected(view.getContext())){

                        if(checkValidation()){
                            ConstructorUsuario cu = new ConstructorUsuario();
                            Usuario user = CredentialValues.getLoginData().getUsuario();
                            user.setNombreApellido(nombre.getText().toString());
                            user.setNroDocumento(documento.getText().toString());
                            user.setTelefono(celular.getText().toString());
                            cu.update(user);

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        UsuarioManager manager = new UsuarioManager();
                                        Respuesta respuesta = manager.grabarUsuario(CredentialValues.getLoginData().getUsuario());
                                        if(respuesta.getEstado().equalsIgnoreCase("OK")){
                                            CredentialValues.getLoginData().setUsuario((Usuario) respuesta.getDatos());
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();

                            if(flag){
                                //setear en el home
                                Picasso.with(getContext())
                                        .load(mediaStorageDir.getPath() + File.separator +"PROFILE_"+ CredentialValues.getLoginData().getUsuario().getLogin() + ".jpg")
                                        .error(R.drawable.user)
                                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                                        .into(homeProfile, new com.squareup.picasso.Callback() {
                                            @Override
                                            public void onSuccess() {
                                            }

                                            @Override
                                            public void onError() {
                                            }
                                        });
                            }
                        }
                    }else{
                        Utilities.sendToast(getContext(), "Necesitas conexion a internet para realizar esta accion", "error");
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        setPerfilesData();


        return  view;
    }


    private void setPerfilesData() {

        documento.setText(CredentialValues.getLoginData().getUsuario().getNroDocumento());
        nombre.setText(CredentialValues.getLoginData().getUsuario().getNombreApellido());
        celular.setText(CredentialValues.getLoginData().getUsuario().getTelefono());


        //setear la imagen
        Picasso.with(getContext())
                .load(mediaStorageDir.getPath() + File.separator +"PROFILE_"+ CredentialValues.getLoginData().getUsuario().getLogin() + ".jpg")
                .error(R.drawable.user)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .into(mSetImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onError() {
                        System.out.println("Error");
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });


    }

    private void showOptions() {
        final CharSequence[] option = {getResources().getString(R.string.tomarFoto), getResources().getString(R.string.ElegirGaleria), getResources().getString(R.string.cancelar)};
        final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle(getResources().getString(R.string.ElegirGaleria));
        builder.setItems(option, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(option[which] == getResources().getString(R.string.tomarFoto)){
                    openCamera();
                }else if(option[which] == getResources().getString(R.string.ElegirGaleria)){
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, getResources().getString(R.string.imgperfil)), SELECT_PICTURE);
                }else {
                    dialog.dismiss();
                }
            }
        });

        builder.show();
    }

    private void openCamera() {


        file = Uri.fromFile(getOutputMediaFile());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, PHOTO_CODE);
        //  }
    }

    public File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Perseo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        File file = new File(mediaStorageDir.getPath() + File.separator +"PROFILE_"+ CredentialValues.getLoginData().getUsuario().getLogin() + ".jpg");
        mPath= file.getAbsolutePath();
        System.out.println("EL PATH DE LA IMAGEN ES: " +file.getAbsolutePath());
        System.out.println(file.getName());

        return file;
    }

    private boolean mayRequestStoragePermission() {

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;

        if((view.getContext().checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (view.getContext().checkSelfPermission(CAMERA) == PackageManager.PERMISSION_GRANTED))
            return true;

        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) || (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(view, "Los permisos son necesarios para poder usar la aplicación",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok, new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
                }
            });
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, MY_PERMISSIONS);
        }

        return false;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path", mPath);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UtilLogger.info("REQUEST CODE: " + requestCode);
        if(resultCode == RESULT_OK){
            flag = true;
            switch (requestCode){
                case PHOTO_CODE:

                    try {
                        Bitmap mBitmap = MediaStore.Images.Media.getBitmap(view.getContext().getContentResolver(), file);

                        float aspectRatio = mBitmap.getWidth() / (float) mBitmap.getHeight();
                        int width = 480;
                        int height = Math.round(width / aspectRatio);


                        mBitmap = Bitmap.createScaledBitmap( mBitmap, width, height, false);


                        mSetImage.setImageBitmap(mBitmap);
                        // mSetImage.setScaleType(ImageView.ScaleType.CENTER_CROP);



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case SELECT_PICTURE:
                    Uri path = data.getData();
                    mSetImage.setImageURI(path);
                    mSetImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    mPath = getRealPathFromURI(path);
                    System.out.println("EL PATH DE LA IMAGEN ES: " +mPath);

                    break;

            }

        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = view.getContext().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(view.getContext(), "Permisos aceptados", Toast.LENGTH_SHORT).show();
                mOptionButton.setEnabled(true);
            }
        }else{
            showExplanation();
        }
    }

    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app necesitas aceptar los permisos");
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", view.getContext().getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        builder.show();
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(documento, getContext())) ret = false;
        if (!Validation.hasText(nombre, getContext())) ret = false;
        if (Validation.hasText(correo, getContext())){
            if (!Validation.isEmailAddress(correo,true, getContext())) ret = false;
        }


        return ret;
    }

}
