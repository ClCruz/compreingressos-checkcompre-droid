package br.compreingressos.checkcompre;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import br.compreingressos.checkcompre.util.Util;

/**
 * Created by edicarlosbarbosa on 18/11/15.
 */
public class ConvidadoCreateActivity extends Activity {

    ProgressDialog mProgress;
    Handler handler;
    AlertDialog.Builder builder;
    AlertDialog dialog;

    private TextView txtConvidado, txtCelular, txtCpf, txtEmail, txtConvidadoPor, txtTipoConvite, txtQtdeLugares;
    private Button btnCadastrar;

    private final String URLServer = "https://compra.compreingressos.com/mobile/convidado.php?";
    private Util util = new Util();

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;
    private String mensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convidado_create);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        handler = new Handler();

        //Instancia campos da activity
        txtConvidado = (EditText) findViewById(R.id.txtConvidado);
        txtCelular = (EditText) findViewById(R.id.txtCelular);
        txtCpf = (EditText) findViewById(R.id.txtCpf);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtConvidadoPor = (EditText) findViewById(R.id.txtConvidadoPor);
        txtTipoConvite = (EditText) findViewById(R.id.txtTipoConvite);
        txtQtdeLugares = (EditText) findViewById(R.id.txtQtdeIngressos);

        btnCadastrar = (Button) findViewById(R.id.btnCadConvidado);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cadastrar(v);
            }
        });

        //Obtem valores da activity anterior
        Intent intent = getIntent();
        Bundle p = intent.getExtras();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

    }

    public void cadastrar(View v) {
        if(validar()) {
            mProgress = ProgressDialog.show(this, "Aguarde", "Processando os dados...");
            disableFields(false);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    cadastrarConvidado();
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

    public void cadastrarConvidado(){
        if(validar()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            String param = "action=add";
            param += "&cboTeatro=" + local +"&cboPeca="+ evento +"&cboApresentacao="+ apresentacao
                    +"&cboHorario="+ horario.replace(":", "h") +"&idUsuario="+ idUser
                    +"&convidado="+ txtConvidado.getText().toString()
                    +"&email="+ txtEmail.getText().toString()
                    +"&celular="+ txtCelular.getText().toString()
                    +"&cpf="+ txtCpf.getText().toString()
                    +"&convidadoPor=" + txtConvidadoPor.getText().toString()
                    +"&tipoConvite=" + txtTipoConvite.getText().toString()
                    +"&qtdeIngresso=" + txtQtdeLugares.getText().toString();
            try {
                final String response = util.makeRequest(URLServer, param);
                if (!response.isEmpty()) {
                    JSONObject json = new JSONObject(response);
                    final String resposta = json.getString("retorno");
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
        if(txtConvidado.getText().length() == 0) {
            onMessage("Alerta", "Preencha o campo Convidado.");
            return false;
        }else if(txtEmail.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo E-Mail.");
            return false;
        }else if(txtCelular.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Celular.");
            return false;
        }else if(txtConvidadoPor.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Convidado Por.");
            return false;
        }else if(txtTipoConvite.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Tipo de Convite.");
            return false;
        }else if(txtQtdeLugares.getText().length() == 0){
            onMessage("Alerta", "Preencha o campo Qtde. de Ingressos por Sessão.");
            return false;
        }
        return true;
    }

    public void onMessage(String title, String mensagem){
        builder = new AlertDialog.Builder(this);
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
        txtConvidado.setEnabled(option);
        txtCelular.setEnabled(option);
        txtEmail.setEnabled(option);
        txtCpf.setEnabled(option);
        txtConvidadoPor.setEnabled(option);
        txtTipoConvite.setEnabled(option);
        txtQtdeLugares.setEnabled(option);
        btnCadastrar.setEnabled(option);
    }
}
