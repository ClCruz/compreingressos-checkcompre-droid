package br.compreingressos.checkcompre;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class BorderoActivity extends Fragment {
    ProgressDialog mProgress;
    private final String url = "https://compra.compreingressos.com/mobile/redirect.php?";
    private static String FILENAME = "user-data";

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_bordero, container, false);

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

        String param = "ID_USUARIO="+ idUser +"&CodPeca="+ evento +"&DataIni="+ apresentacao +"&DataFim="+ apresentacao +"&HorSessao="+ horario +"&Sala=TODOS&local="+local;

        mProgress = ProgressDialog.show(getActivity(), "Aguarde", "Processando os dados do evento...");

        WebView web = (WebView) view.findViewById(R.id.webView);
        web.getSettings().setLoadWithOverviewMode(true);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setBuiltInZoomControls(true);
        web.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }

            public void onPageFinished(WebView view, String url) {
                if(mProgress.isShowing()) {
                    mProgress.dismiss();
                }
            }
        });
        web.loadUrl(url+param);

        return view;
    }

    public static BorderoActivity newInstance(Bundle bundle){
        BorderoActivity borderoActivity = new BorderoActivity();
        borderoActivity.setArguments(bundle);
        return borderoActivity;
    }
}
