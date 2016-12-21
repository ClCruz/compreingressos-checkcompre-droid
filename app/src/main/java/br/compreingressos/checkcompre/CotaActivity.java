package br.compreingressos.checkcompre;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import br.compreingressos.checkcompre.util.Util;


public class CotaActivity extends Fragment {

    ProgressDialog mProgress;
    Button btnCadCota;
    Handler handler;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    private EditText txtConvidadoPor;
    private EditText txtTipoConvite;
    private EditText txtQtdeIngressos;
    private String id_cota_convite;

    private final String URLServer = "https://compra.compreingressos.com/mobile/cota.php?";
    private static String FILENAME = "user-data";
    private Util util = new Util();

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    private String mensagem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_cota, container, false);

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

        txtConvidadoPor = (EditText) view.findViewById(R.id.txtConvidadoPor);
        txtTipoConvite = (EditText) view.findViewById(R.id.txtTipoConvite);
        txtQtdeIngressos = (EditText) view.findViewById(R.id.txtQtdeIngressos);
        btnCadCota = (Button) view.findViewById(R.id.btnCadCota);

        btnCadCota.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cadastrar(v);
            }
        });

        id_cota_convite = "";

        handler = new Handler();

        load();

        return view;
    }

    public void load(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=load&cboLocal=" + local +"&cboEvento="+ evento +"&cboApresentacao="+ apresentacao +"&cboHorario="+ horario.replace(":", "h");
        try {
            String response = util.makeRequest(URLServer, param);
            if (!response.isEmpty()) {
                JSONObject json = new JSONObject(response);

                id_cota_convite = json.getString("id_cota_convite");
                txtConvidadoPor.setText(json.getString("nm_convidado_por"));
                txtTipoConvite.setText(json.getString("ds_tipo_convite"));
                txtQtdeIngressos.setText(json.getString("qt_ingresso"));
                btnCadCota.setText("ALTERAR");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cadastrar(View v){
        if(validar()) {
            mProgress = ProgressDialog.show(getActivity(), "Aguarde", "Processando os dados...");
            disableFields(false);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    cadastrarCota();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mProgress.isShowing()) {
                                mProgress.dismiss();
                            }
                            disableFields(true);
                        }
                    });
                }
            };
            new Thread(runnable).start();
        }
    }

    public void cadastrarCota(){
        if(validar()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            String param = (id_cota_convite.equals("")) ? "action=add" : "action=edit&id_cota_convite="+ id_cota_convite;

            param += "&cboLocal=" + local +"&cboEvento="+ evento +"&cboApresentacao="+ apresentacao +"&cboHorario="+ horario.replace(":", "h") +"&idUsuario="+ idUser +"&convidadoPor=" + txtConvidadoPor.getText().toString() + "&tipoConvite=" + txtTipoConvite.getText().toString() +"&qtdeIngresso=" + txtQtdeIngressos.getText().toString();
            try {
                String response = util.makeRequest(URLServer, param);
                if (!response.isEmpty()) {
                    JSONObject json = new JSONObject(response);
                    String resposta = json.getString("retorno");
                    mensagem = json.getString("mensagem");

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            onMessage("Mensagem", mensagem);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validar() {
        if(txtConvidadoPor.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Convidado Por.");
            return false;
        }else if(txtTipoConvite.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Tipo de Convite.");
            return false;
        }else if(txtQtdeIngressos.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Qtde. de Ingressos por Sessão.");
            return false;
        }
        return true;
    }

    public void onMessage(String title, String mensagem){
        builder = new AlertDialog.Builder(getActivity());
        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setMessage(mensagem);
        builder.setTitle(title);
        dialog = builder.create();
        dialog.show();
    }

    public void disableFields(boolean option){
        txtConvidadoPor.setEnabled(option);
        txtTipoConvite.setEnabled(option);
        txtQtdeIngressos.setEnabled(option);
        btnCadCota.setEnabled(option);
    }

    public void eraseFields(){
        txtConvidadoPor.setText("");
        txtTipoConvite.setText("");
        txtQtdeIngressos.setText("");
    }

    public static CotaActivity newInstance(Bundle bundle){
        CotaActivity cotaActivity = new CotaActivity();
        cotaActivity.setArguments(bundle);
        return cotaActivity;
    }
}
