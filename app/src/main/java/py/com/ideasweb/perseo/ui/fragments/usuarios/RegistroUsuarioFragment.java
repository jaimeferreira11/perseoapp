package py.com.ideasweb.perseo.ui.fragments.usuarios;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.androidbuts.multispinnerfilter.MultiSpinner;
import com.androidbuts.multispinnerfilter.MultiSpinnerListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;
import com.yash.tongaonkar.multiselectspinner.MultiSelectSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import py.com.ideasweb.R;
import py.com.ideasweb.perseo.constructor.ConstructorArticulos;
import py.com.ideasweb.perseo.constructor.ConstructorPerfil;
import py.com.ideasweb.perseo.constructor.ConstructorUsuario;
import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Perfilusuario;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.ConstantesRestApi;
import py.com.ideasweb.perseo.restApi.manager.UsuarioManager;
import py.com.ideasweb.perseo.restApi.pojo.CredentialValues;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import py.com.ideasweb.perseo.utilities.UtilLogger;
import py.com.ideasweb.perseo.utilities.Utilities;
import py.com.ideasweb.perseo.utilities.Validation;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroUsuarioFragment extends Fragment {

    @BindView(R.id.text_busqueda)
    EditText busqueda;

    @BindView(R.id.buscar)
    ImageView btnBuscar;

    @BindView(R.id.codigo)
    EditText idusuario;
    @BindView(R.id.login)
    EditText login;
    @BindView(R.id.nombre)
    EditText nombre;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.documento)
    EditText documento;
    @BindView(R.id.perfiles)
    MultiSelectSpinner perfiles;
    @BindView(R.id.checkEstado)
    CheckBox checkEstado;
    @BindView(R.id.grabar)
    Button btngGrabar;


    Unbinder unbinder;
    Usuario usuario;

    public RegistroUsuarioFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_usuario, container, false);
        unbinder = ButterKnife.bind(this, view);

        final List<Object> lista = new ArrayList<>();
        lista.add("ADMINISTRACION");
        lista.add("FACTURACION");
        lista.add("COBRANZAS");

        perfiles.setItems(lista, "");
        perfiles.setSelection(new int[]{0});
        usuario = new Usuario();


        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Validation.hasText(busqueda, getContext())){
                    buscar(busqueda.getText().toString().trim());
                }
            }
        });


        btngGrabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkValidation()){

                    if(Utilities.isNetworkConnected(getContext())){


                        usuario.setIdUsuario(Integer.parseInt(idusuario.getText().toString()));
                        usuario.setNombreApellido(nombre.getText().toString());
                        usuario.setLogin(login.getText().toString());
                        usuario.setNroDocumento(documento.getText().toString());
                        usuario.setActivo(checkEstado.isChecked());
                        usuario.setIdEmpresa(ConstantesRestApi.ID_EMPRESA);
                        if(password.getText().length() > 0)
                            usuario.setPassword(password.getText().toString());

                        if(perfiles.getSelectedObjects().size() > 0){

                            List<Perfilusuario> list = new ArrayList<>();
                            for (Object desc: perfiles.getSelectedObjects() ) {
                                String convert= String.valueOf(desc);
                                switch (convert){
                                    case "ADMINISTRACION":
                                        Perfilusuario p = new Perfilusuario();
                                        Perfil per = new Perfil();
                                        per.setIdPerfil(1);
                                        per.setDescripcion("ADMINISTRACION");
                                        p.setPerfil(per);
                                        list.add(p);
                                        break;
                                    case "FACTURACION":
                                        Perfilusuario p1 = new Perfilusuario();
                                        Perfil per1 = new Perfil();
                                        per1.setIdPerfil(2);
                                        per1.setDescripcion("FACTURACION");
                                        p1.setPerfil(per1);
                                        list.add(p1);
                                        break;
                                    case "COBRANZAS":
                                        Perfilusuario p2 = new Perfilusuario();
                                        Perfil per2 = new Perfil();
                                        per2.setIdPerfil(3);
                                        per2.setDescripcion("COBRANZAS");
                                        p2.setPerfil(per2);
                                        list.add(p2);
                                        break;
                                        default:
                                            break;
                                }

                            }
                            usuario.setPerfiles(list);
                        }

                        grabar(usuario);


                    }else{
                        Utilities.sendToast(getContext(), "Necesitas conexion a internet para realizar esta accion", "error");

                    }
                }
            }
        });




        return  view;
    }



    public void buscar(String param){

        AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        ConstructorUsuario ca = new ConstructorUsuario();


        final List<Usuario> busqueda = ca.getUsuarioByParam(param);


        if(busqueda.isEmpty()){
            new MaterialDialog.Builder(getContext())
                    .icon(getResources().getDrawable(R.drawable.attention_48))
                    .title("Atencion!")
                    .content("No se encontro el usuario.")
                    .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .positiveText("Aceptar")
                    .show();
        }else{

            new MaterialDialog.Builder(getContext())
                    .title("Resultado de la Busqueda")
                    .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                    .items(busqueda)
                    .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                        @Override
                        public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {

                            //setear el cliente
                            setData(busqueda.get(which));
                            UtilLogger.info(which + " - " + text);

                            return true;
                        }

                    })

                    .positiveText("Aceptar")
                    .negativeText("Cancelar")
                    .show();
        }



        dialog.dismiss();

    }

    private void setData(Usuario u) {

        this.usuario = u;
        idusuario.setText(String.valueOf(usuario.getIdUsuario()));
        login.setText(usuario.getLogin());
        nombre.setText(usuario.getNombreApellido());
        documento.setText(usuario.getNroDocumento());
        checkEstado.setChecked(usuario.getActivo());

        final List<String> lista = new ArrayList<>();
        for (Perfilusuario p: usuario.getPerfiles() ) {
            lista.add(p.getDescripcion());
        }
        perfiles.setSelection(lista);
    }


    public void grabar(final Usuario usuario){


        final AlertDialog dialog = new SpotsDialog.Builder()
                .setContext(getContext())
                .setTheme(R.style.spots)
                .setMessage(R.string.aguarde)
                .build();
        dialog.show();


        System.out.println("cantidad de perfiles del usuario: " + usuario.getPerfiles().size());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    UsuarioManager manager = new UsuarioManager();
                    Respuesta respuesta = manager.grabarUsuario(usuario);

                    if(respuesta.getEstado().equalsIgnoreCase("OK")){

                        ConstructorUsuario ca = new ConstructorUsuario();
                        Usuario newUsuario = (Usuario) respuesta.getDatos();
                        if(usuario.getId() > 0){
                            newUsuario.setId(usuario.getId());
                        }
                        ca.insertarUsuario(newUsuario);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();

                                new MaterialDialog.Builder(getContext())
                                        .icon(getResources().getDrawable(R.drawable.checked_48))
                                        .title(getResources().getString(R.string.procesoExitoso))
                                        .content("Usuario registrado!")
                                        .titleColor(getContext().getResources().getColor(R.color.colorPrimaryDark))
                                        .positiveText("Aceptar")
                                        .show();

                                clear();
                            }
                        });

                    }else{

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                Utilities.sendToast(getContext(), "Ocurrio un error, intente mas tarde", "error");

                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();







    }


    public void clear(){
        idusuario.setText("0");
        login.setText(null);
        password.setText(null);
        documento.setText(null);
        nombre.setText(null);
        //iva.setText(TIPOIVA[0]);
        checkEstado.setChecked(true);

        busqueda.setText(null);
        perfiles.setSelection(new int[]{0});

        usuario = new Usuario();

    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(login, getContext())) ret = false;
        if (!Validation.hasText(nombre, getContext())) ret = false;
        //if (!Validation.hasText(documento, getContext())) ret = false;

        if(idusuario.getText().length() == 0 ){
            if (!Validation.hasText(password, getContext())) ret = false;
        }



        if(perfiles.getSelectedItemsAsString().length() == 0){
            Utilities.sendToast(getContext(), "Selecciona algun perfil", "warning");
            ret = false;
        }

        UtilLogger.info(ret + "");
        return ret;
    }

}
