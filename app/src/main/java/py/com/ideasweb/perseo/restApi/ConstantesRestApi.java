package py.com.ideasweb.perseo.restApi;

/**
 * Created by jaime on 31/03/17.
 */

public final class ConstantesRestApi {

     public static final String urlBase = "http://181.121.129.218";
  //  public static final String urlBase = "http://10.0.10.174";
    public static final String port    = ":8085";
    public static final String context = "/perseo-rest/";

   /*public static final String urlBase = "http://www.fundacionparaguaya.org.py";
   public static final String port    = "";
   public static final String context = "/semaforo-rest/";*/

    public static final String ROOT_URL = urlBase+port+context;

    // prefijo para publico y privado
    private static final String api_version = "api/v1";
   public static final String api_key_login = "fc878fef-696d-4d08-a86f-9f6acd426a38";
   public static final String api_key_metodos = "17de429c-5620-4ae0-abc9-30b6bcb6d5d9";


   // EMPRESA
    public static final int ID_EMPRESA = 2;


    /**
     * URL login to WebServices Oauth2
     */
    public static final String OAUTH_CLIENT_ID ="client";
    public static final String OAUTH_CLIENT_SECRET ="secret";
    public static final String OAUTH_TOKEN_LOGIN = "oauth/token?";
    public static final String OAUTH_REVOKE_TOKEN = "oauth/revoke-token";
    public static String userpass = ConstantesRestApi.OAUTH_CLIENT_ID + ":" + ConstantesRestApi.OAUTH_CLIENT_SECRET;
    public static final String basicAuth = "Basic " + new String(new org.apache.commons.codec.binary.Base64().encode(userpass.getBytes()));



    public static boolean isSync;
    public static String imgStorage= "/Android/data/py.com.ideasweb.perseo";


    public static final String API_DOWNLOAD_APK ="https://play.google.com/store/apps/details?id=py.org.fundacionparaguaya.appdynamicsemaforo";

   /*Email*/
   public static final String EMAIL ="ideaswebpy@gmail.com";
    public static final String PASSWORD ="ideasweb2017";
    //remitente de correo
    public static final String REMITENTE_EMAIL ="ideaswebpy@gmail.com";

    public final static String NOMBRE_DIRECTORIO = "Perseo";
    public final static String ETIQUETA_ERROR = "ERROR al generar PDF";

    /**
     * URL login to WebServices Oauth2
     */
/*    public static final String OAUTH_CLIENT_ID ="client";
    public static final String OAUTH_CLIENT_SECRET ="secret";*/

   // public static String userpass = ConstantesRestApi.OAUTH_CLIENT_ID + ":" + ConstantesRestApi.OAUTH_CLIENT_SECRET;
    //public static final String basicAuth = "Basic " + new String(new org.apache.commons.codec.binary.Base64().encode(userpass.getBytes()));

    /*
   * metodos
   * */

    //usuario
    public static final String API_GET_ALL_USUARIOS = api_version +"/usuarios";
    public static final String API_GET_USUARIOS_BY_EMPRESA = api_version +"/usuarios/empresa/{idEmpresa}";
    public static final String API_GET_USER_INFO = api_version +"/user/user/{username}";
    public static final String API_GET_VERSION_APP = api_version + "/menu/version-app";
    public static final String API_POST_GRABAR_USUARIO = api_version + "/usuarios";


 //articulos
    public static final String API_GET_ALL_ARTICULOS= api_version + "/articulos";
    public static final String API_GET_ARTICULO_BY_CODIGO = api_version + "/articulos/codigo";
    public static final String API_GET_ARTICULO_BY_CODIGO_EAN = api_version + "/articulos/codigo-ean";
    public static final String API_POST_ADD_ARTICULO= api_version + "/articulos/";
    public static final String API_GET_ARTICULOS_BY_EMPRESA= api_version + "/articulos/empresa/{idEmpresa}";

    //clientes
    public static final String API_GET_ALL_CLIENTE= api_version + "/clientes";
    public static final String API_GET_CLIENTE_BY_USUARIO= api_version + "/clientes/{idUsuario}";
    public static final String API_GET_CLIENTE_BY_EMPRESA= api_version + "/clientes/empresa/{idEmpresa}";
    public static final String API_GET_CLIENTE_BY_DOC= api_version + "/clientes/{tipoDoc}/{nroDoc}/";
    public static final String API_GET_CLIENTE_BY_CODE= api_version + "/clientes/codigo/{codigo}";
    public static final String API_POST_ADD_CLIENTE= api_version + "/clientes/";
    public static final String API_POST_ADD_LISTA_CLIENTE= api_version + "/clientes/grabar-lista";

    //facturas
    public static final String API_GET_TALONARIO = api_version + "/facturas/talonario";
    public static final String API_POST_GRABAR_TALONARIO = api_version + "/facturas/talonario";
    public static final String API_GET_FACTURAS = api_version + "/facturas";
    public static final String API_GET_FACTURAS_BY_USUARIO = api_version + "/facturas/usuario/{idUsuario}";
    public static final String API_POST_GRABAR_FACTURA = api_version + "/facturas";
    public static final String API_POST_GRABAR_LISTA_FACTURA = api_version + "/facturas/grabar-lista";

    //pedidos
    public static final String API_POST_ADD_PEDIDO = api_version + "/pedidos/";
    public static final String API_DELETE_PEDIDO = api_version + "/pedidos/{codPedido}";
    public static final String API_GET_ALL_PEDIDOS = api_version + "/pedidos/";

    public static final String API_GET_ALL_PEDIDOS_BY_USUARIO = api_version + "/pedidos/usuario/{codUsuario}";


    //parametros
    public static final String API_GET_CIUDADES= api_version + "/localidades/ciudades/";
    public static final String API_GET_DPTOS= api_version + "/localidades/departamentos/";
    public static final String API_GET_BARRIOS= api_version + "/localidades/barrios/";
    public static final String API_GET_TRANSPORTADORAS= api_version + "/transportadoras";


    //perfiles
    public static final String API_GET_PERFILES= api_version + "/perfiles";

    /*Impresion de comprobantes*/




}
