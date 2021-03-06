package py.com.ideasweb.perseo.restApi;

import java.util.List;

import okhttp3.RequestBody;
import py.com.ideasweb.perseo.models.Articulo;
import py.com.ideasweb.perseo.models.Cliente;
import py.com.ideasweb.perseo.models.Empresa;
import py.com.ideasweb.perseo.models.FacturaCab;
import py.com.ideasweb.perseo.models.Perfil;
import py.com.ideasweb.perseo.models.Talonario;
import py.com.ideasweb.perseo.models.Usuario;
import py.com.ideasweb.perseo.restApi.pojo.Respuesta;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import se.bjurr.jmib.anotations.GenerateMethodInvocationBuilder;

/**
 * Created by jaime on 31/03/17.
 */

@GenerateMethodInvocationBuilder
public interface Endpoints {

    //login
    @FormUrlEncoded
    @POST(ConstantesRestApi.OAUTH_TOKEN_LOGIN)
    Call<Respuesta> loginOauthToken(@Field("usuario") String usuario,
                                @Field("password") String password);

    //logout
    @POST(ConstantesRestApi.OAUTH_REVOKE_TOKEN)
    Call<Respuesta> revokeOauthToken(@Field("cod-usuario") Long codUsuario);


    // USUARIOS

    @GET(ConstantesRestApi.API_GET_USER_INFO)
    Call<Respuesta> getUserInfo(@Path("username") String username);

    @GET(ConstantesRestApi.API_GET_ALL_USUARIOS)
    Call<List<Usuario>> getAllUsuarios();

    @GET(ConstantesRestApi.API_GET_USUARIOS_BY_EMPRESA)
    Call<List<Usuario>> getUsuariosByEmpresa(@Path("idEmpresa") Integer idEmpresa);

    @POST(ConstantesRestApi.API_POST_GRABAR_USUARIO)
    Call<Usuario> grabarUsuario(@Body Usuario usuario);


    // CLIENTES

    @GET(ConstantesRestApi.API_GET_ALL_CLIENTE)
    Call<List<Cliente>> getAllClientes();

    @GET(ConstantesRestApi.API_GET_CLIENTE_BY_USUARIO)
    Call<List<Cliente>> getClientesByUsuario(@Path("idUsuario") Integer idUsuario);

    @GET(ConstantesRestApi.API_GET_CLIENTE_BY_EMPRESA)
    Call<List<Cliente>> getClientesByEmpresa(@Path("idEmpresa") Integer idEmpresa);

    @POST(ConstantesRestApi.API_POST_ADD_CLIENTE)
    Call<Cliente> addCliente(@Body RequestBody cliente);

    @POST(ConstantesRestApi.API_POST_ADD_LISTA_CLIENTE)
    Call<Void> grabarListaCliente(@Body List<Cliente> clientes);

    @GET(ConstantesRestApi.API_GET_CLIENTE_BY_DOC)
    Call<List<Cliente>> getClienteByDoc(@Path("tipoDoc") String tipoDoc, @Path("nroDoc") String nroDoc);

    @GET(ConstantesRestApi.API_GET_CLIENTE_BY_CODE)
    Call<List<Cliente>> getClienteByCode(@Path("codigo") String codigo);


    // ARTCULOS

    @GET(ConstantesRestApi.API_GET_ALL_ARTICULOS)
    Call<List<Articulo>> getAllArticulos();

    @GET(ConstantesRestApi.API_GET_ARTICULOS_BY_EMPRESA)
    Call<List<Articulo>> getArticulosByEmpresa(@Path("idEmpresa") Integer idEmpresa);

    @FormUrlEncoded
    @POST(ConstantesRestApi.API_GET_ARTICULO_BY_CODIGO)
    Call<List<Articulo>> getArticulosByCod(@Field("codigo") String codigo);

    @FormUrlEncoded
    @POST(ConstantesRestApi.API_GET_ARTICULO_BY_CODIGO_EAN)
    Call<List<Articulo>> getArticulosByCodBarra(@Field("codigo") String codigo);

    @POST(ConstantesRestApi.API_POST_ADD_ARTICULO)
    Call<Articulo> addArticulo(@Body RequestBody articulo);


    // FACTURAS

    @GET(ConstantesRestApi.API_GET_TALONARIO)
    Call<Talonario> getTalonario();

    @POST(ConstantesRestApi.API_POST_GRABAR_TALONARIO)
    Call<Talonario> grabarTalonario(@Body Talonario talonario);

    @GET(ConstantesRestApi.API_GET_FACTURAS)
    Call<List<FacturaCab>> getFacturas();

    @GET(ConstantesRestApi.API_GET_FACTURAS_BY_USUARIO)
    Call<List<FacturaCab>> getFacturasByUsuario(@Path("idUsuario") Integer idUsuario);


    @POST(ConstantesRestApi.API_POST_GRABAR_FACTURA)
    Call<Void> grabarFactura(@Body FacturaCab facturacab);

    @POST(ConstantesRestApi.API_POST_GRABAR_LISTA_FACTURA)
    Call<Void> grabarListaFactura(@Body List<FacturaCab> facturas);





    // CIUDADES
    @GET(ConstantesRestApi.API_GET_CIUDADES)
    Call<Respuesta> getCiudades();

    //DEPARTAMENTOS
    @GET(ConstantesRestApi.API_GET_DPTOS)
    Call<Respuesta> getDptos();

    // BARRIOS
    @GET(ConstantesRestApi.API_GET_BARRIOS)
    Call<Respuesta> getBarrios();


    // PEDIDOS
    @POST(ConstantesRestApi.API_POST_ADD_PEDIDO)
    Call<Respuesta> addPedido(@Body RequestBody pedidoCabecera);

    @GET(ConstantesRestApi.API_GET_ALL_PEDIDOS)
    Call<Respuesta> getPedidos();

    @GET(ConstantesRestApi.API_GET_ALL_PEDIDOS_BY_USUARIO)
    Call<Respuesta> getPedidosByUsuario(@Path("codUsuario") Integer codUsuario);

    @DELETE(ConstantesRestApi.API_DELETE_PEDIDO)
    Call<Respuesta> deletePedido(@Path("codPedido") Integer codPedido);

    @GET(ConstantesRestApi.API_GET_TRANSPORTADORAS)
    Call<Respuesta> getTransportadoras();

    //version de la app
    @GET(ConstantesRestApi.API_GET_VERSION_APP)
    Call<Respuesta> getVersionApp();

    // PERFILES
    @GET(ConstantesRestApi.API_GET_PERFILES)
    Call<List<Perfil>> getPerfiles();


    // EMPRESAS
    @GET(ConstantesRestApi.API_GET_EMPRESA_BY_ID)
    Call<Empresa> getEmpresaById(@Path("idEmpresa") Integer idEmpresa);
}
