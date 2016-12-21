package br.compreingressos.checkcompre;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import br.compreingressos.checkcompre.util.Util;

public class LoginActivity extends Activity {

    // Componentes de tela
    private EditText txtLogin;
    private EditText txtSenha;
    private TextView txtMensagem;
    private CheckBox chkLembrar;
    private Button btnLogin;
    private ProgressBar progress;
    // Recursos auxiliares
    private Util util;
    private static final String FILENAME = "user-data";
    private String URLServer = "https://compra.compreingressos.com/mobile/autenticacao.php";
    private String mensagem = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ActionBar actionBar = getActionBar();
        //actionBar.setLogo(R.mipmap.ic_logo);
        //actionBar.setDisplayUseLogoEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        setContentView(R.layout.activity_login);
        txtLogin = (EditText)findViewById(R.id.txtLogin);
        txtSenha = (EditText)findViewById(R.id.txtSenha);
        txtMensagem = (TextView)findViewById(R.id.txtMensagem);
        chkLembrar = (CheckBox)findViewById(R.id.chkLembrar);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        progress = (ProgressBar)findViewById(R.id.progress_bar);
        progress.setVisibility(View.INVISIBLE);
        util = new Util();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login(v);
            }
        });
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_login, container, false);
        return view;
    }
    */

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences settings = getSharedPreferences(FILENAME, 0);
        String id = settings.getString("idUser", "0");
        if(id != null && Integer.parseInt(id.trim()) > 0) {
            Intent intent = new Intent(this, LocalActivity.class);
            intent.putExtra("idUser", id);
            startActivity(intent);
        }
    }

    public void login(View v){
        progress.setVisibility(View.VISIBLE);
        disableFields(false);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                fazLogin();
                progress.post(new Runnable() {
                    @Override
                    public void run() {
                        progress.setVisibility(View.INVISIBLE);
                        txtMensagem.setText(mensagem);
                        disableFields(true);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    public void disableFields(boolean option){
        txtLogin.setEnabled(option);
        txtSenha.setEnabled(option);
        chkLembrar.setEnabled(option);
        btnLogin.setEnabled(option);
    }

    public void fazLogin(){
        if(validar()) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            String param = "usuario=" + txtLogin.getText().toString() + "&senha=" + txtSenha.getText().toString();
            try {
                String response = util.makeRequest(URLServer, param);
                if (!response.isEmpty()) {
                    JSONObject json = new JSONObject(response);
                    String resposta = json.getString("retorno");

                    if (resposta.equals("OK")) {
                        String id = json.getString("id");
                        if (chkLembrar.isChecked()) {
                            SharedPreferences settings = getSharedPreferences(FILENAME, 0);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString("idUser", id);
                            editor.commit();
                        }
                        Intent intent = new Intent(this, LocalActivity.class);
                        intent.putExtra("idUser", id);
                        startActivity(intent);
                    } else {
                        mensagem = resposta;
                    }
                }
                mensagem = "Usuário ou Senha inválidos!";
            } catch (JSONException e) {
                mensagem = getString(R.string.txtMensagem);
                e.printStackTrace();
            } catch (Exception e) {
                mensagem = getString(R.string.txtMensagem);
                e.printStackTrace();
            }
        }
    }

    private boolean validar() {
        if(txtLogin.getText().length() == 0){
            mensagem = "Digite seu Login!";
            return false;
        }else if(txtSenha.getText().length() == 0){
            mensagem = "Digite sua Senha!";
            return false;
        }
        return true;
    }

}
