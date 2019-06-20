package py.com.ideasweb.perseo.restApi.pojo;

/**
 * Created by root on 30/11/16.
 */

public class CredentialValues {

    private static String accessToken="error";
    //token para los metodos publicos
    private static String public_accessToken="error";

    private static Token tokenData = null;

    private static LoginData loginData = null;
    // private static ArrayList<Clase> menues = new ArrayList<>();

    public static void setAccessTokenString(String accessToken) {
        CredentialValues.accessToken = accessToken;
        CredentialValues.tokenData = null;
        //   CredentialValues.loginData = null;
    }

    public static void setLoginData(LoginData loginData) {
        CredentialValues.loginData = loginData;
    }

    public static void setAccessToken(Token token) throws Exception{

        accessToken = token.getAccess_token();
        tokenData = token;

        //generar el logindata

        //   LoginManager login = new LoginManager();
        //loginData = login.getUserInfo(tokenData);

    }

    public static String getPublic_accessToken() {
        return public_accessToken;
    }

    public static void setPublic_accessToken(Token token) throws Exception{
        CredentialValues.public_accessToken = token.getAccess_token();;
    }

    public static String getAccessTokenString(){
        return accessToken;
    }

    public static Token getAccessTokenData(){
        return tokenData;
    }

    public static LoginData getLoginData( ) {return  loginData; }



}
