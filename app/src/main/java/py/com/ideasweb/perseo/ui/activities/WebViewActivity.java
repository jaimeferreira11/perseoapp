package py.com.ideasweb.perseo.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import py.com.ideasweb.R;
import py.com.ideasweb.perseo.utilities.UtilLogger;


public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = (WebView) findViewById(R.id.activity_main_webview);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new WebViewClient());

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        Bundle param = getIntent().getExtras();

        if(param != null){
            UtilLogger.info(param.getString("url"));
            if(param.getString("url")!= null)
            mWebView.loadUrl(param.getString("url"));
        }else{
            mWebView.loadUrl("file:///android_asset/www/manual_usuario.html");
        }




    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
