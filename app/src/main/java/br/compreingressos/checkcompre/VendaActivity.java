package br.compreingressos.checkcompre;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by edicarlosbarbosa on 17/10/16.
 */

public class VendaActivity extends Activity {

    ProgressDialog mProgress;
    WebView web;
    private final String url = "https://compra.compreingressos.com/mobile/assinatura.php?";
    private static String FILENAME = "user-data";
    private static final String TAG = "VendaActivity";

    private String idUser;
    private String idAssinatura;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        Bundle p = getIntent().getExtras();
        idUser = p.getString("idUser");
        idAssinatura = p.getString("idAssinatura");

        String param = "action=venda&assinatura="+ idAssinatura +"&id_usuario="+idUser;

        mProgress = ProgressDialog.show(this, "Aguarde", "Processando os dados do evento...");
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        web = (WebView) findViewById(R.id.webView);
        WebSettings settings = web.getSettings();
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setBuiltInZoomControls(true);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.i(TAG, "onPageStarted Started for URL "+ url);
                super.onPageStarted(view, url, favicon);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.i(TAG, "Override URL");
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                if (mProgress.isShowing())
                    mProgress.dismiss();

                web.loadUrl("javascript:document.getElementById('novo_menu').style.display='none';void(0);");
                web.loadUrl("javascript:document.getElementById('content').style.overflowX='hidden';void(0);");

                if (url.contains("/comprar/etapa3_2.php")) {
                    web.loadUrl("javascript:document.getElementById('checkbox_sms').style.display='none';void(0);");
                    web.loadUrl("javascript:document.getElementById('checkbox_politica').style.display='none';void(0);");
                }

                if (url.contains("/comprar/assinatura.php")) {
                    web.loadUrl("javascript:document.getElementById('botoes').style.paddingBottom='50px';void(0);");
                } else {
                    web.loadUrl("javascript:document.getElementById('content').style.paddingBottom='300px';void(0);");
                }

                web.loadUrl("javascript:document.getElementById('footer').style.display='none';void(0);");

                if (url.contains("/comprar/assinatura_ok.php")) {
                    web.loadUrl("javascript:document.getElementsByClassName('nova_venda').item(0).style.display='none';void(0);");
                    web.loadUrl("javascript:for(i = 0; i < document.getElementsByTagName('a').length; i++){ document.getElementsByTagName('a').item(i).style.color='black';}void(0);");
                }

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                //Toast.makeText(, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        web.loadUrl(url+param);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assinatura, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sair:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
