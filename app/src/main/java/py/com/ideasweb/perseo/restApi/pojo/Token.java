package py.com.ideasweb.perseo.restApi.pojo;

/**
 * Created by root on 30/11/16.
 */

public class Token {

    //response model
    /*{
        "access_token":"ec3d503e-e546-412f-b42a-d1257e62b8e8",
            "token_type":"bearer",
            "refresh_token":"c00224c5-1588-4ae0-86bc-6a76b142d9d0",
            "expires_in":1799,
            "scope":"read write"
    }*/

    String access_token;
    String token_type;
    String refresh_token;
    Long   expires_in;
    String scope;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

}
