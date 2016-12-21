package br.compreingressos.checkcompre;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import br.compreingressos.checkcompre.model.Convidado;
import br.compreingressos.checkcompre.util.Util;

/**
 * Created by edicarlosbarbosa on 17/11/15.
 */
public class ConvidadoDetalheActivity extends Activity {

    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    private Convidado convidado;
    private Button btnRemoverConvidado;
    private TextView txtConvidado, txtCelular, txtCpf, txtEmail, txtConvidadoPor, txtTipoConvite, txtQtdeLugares, txtConfirmado;

    private final String URLServer = "https://compra.compreingressos.com/mobile/convidado.php?";
    private Util util = new Util();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convidado);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        this.convidado = (Convidado) intent.getSerializableExtra("convidado");

        btnRemoverConvidado = (Button) findViewById(R.id.btnRemoverConvidado);
        btnRemoverConvidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remover(v);
            }
        });

        preencheCampos(this.convidado);
    }

    public void preencheCampos(Convidado convidado){
        txtConvidado = (TextView) findViewById(R.id.txtConvidado);
        txtCelular = (TextView) findViewById(R.id.txtContato);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        txtCpf = (TextView) findViewById(R.id.txtCpf);

        txtConvidadoPor = (TextView) findViewById(R.id.txtConvidadoPor);
        txtTipoConvite = (TextView) findViewById(R.id.txtTipoConvite);
        txtQtdeLugares = (TextView) findViewById(R.id.txtQtdeIngressos);
        txtConfirmado = (TextView) findViewById(R.id.txtConfirmado);

        txtConvidado.setText(convidado.getNome());
        txtCelular.setText(convidado.getCelular());
        txtEmail.setText(convidado.getEmail());
        txtCpf.setText(convidado.getCpf());

        txtConvidadoPor.setText(convidado.getConvidadoPor());
        txtTipoConvite.setText(convidado.getTipoConvite());
        txtQtdeLugares.setText(convidado.getQtdeLugares());
        txtConfirmado.setText(convidado.getConfirmado());
    }

    public void remover(View v){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=del&id_convidado="+ convidado.getId();
        try {
            final String response = util.makeRequest(URLServer, param);
            if (!response.isEmpty()) {
                JSONObject json = new JSONObject(response);
                final String resposta = json.getString("retorno");
                String mensagem = json.getString("mensagem");
                onMessage("Mensagem", mensagem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMessage(String title, String mensagem){
        builder = new AlertDialog.Builder(this);
        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setMessage(mensagem);
        builder.setTitle(title);
        dialog = builder.create();
        dialog.show();
    }

}
