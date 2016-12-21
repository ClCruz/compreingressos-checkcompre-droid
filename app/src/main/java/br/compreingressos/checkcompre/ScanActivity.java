package br.compreingressos.checkcompre;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import br.compreingressos.checkcompre.util.Util;

public class ScanActivity extends Fragment {

    public static final int REQUEST_CODE = 0;
    private static final long VIBRATE_DURATION = 500L;
	private TextView txResult;
    private TextView txtDisponiveis;
    private Button button;
    private Button btnLeitor;
    private Spinner cboSentido;
    private TextView txtCodigo;
    private Util util = new Util();
    private final String url = "https://compra.compreingressos.com/mobile/validar.php";
    private String URLServer = "https://compra.compreingressos.com/mobile/consulta.php";

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;
    private String setor;

    private boolean leitorManual;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_scan, container, false);

        txResult = (TextView) view.findViewById(R.id.txtResult);
        txtDisponiveis = (TextView) view.findViewById(R.id.txtDisponiveis);
        cboSentido = (Spinner) view.findViewById(R.id.cboSentido);
        txtCodigo = (TextView) view.findViewById(R.id.txtCodigo);
        btnLeitor = (Button) view.findViewById(R.id.btnLeitor);

        leitorManual = false;

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");
        setor = p.getString("setor");
        button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                callZXing(v);
            }
        });
        txtCodigo.setVisibility(View.GONE);

        btnLeitor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(leitorManual) {
                    button.setVisibility(View.VISIBLE);
                    btnLeitor.setText("INICIAR LEITURA MANUAL");
                    txtCodigo.setVisibility(View.GONE);
                    leitorManual = false;
                }else{
                    button.setVisibility(View.GONE);
                    txtCodigo.setVisibility(View.VISIBLE);
                    btnLeitor.setText("PARAR LEITURA MANUAL");
                    leitorManual = true;
                }
            }
        });

        txtCodigo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 22){
                    validarCodigo(txtCodigo.getText().toString());
                    txtCodigo.setText("");
                }
            }
        });

        return view;
    }

    public static ScanActivity newInstance(Bundle bundle){
        ScanActivity scanActivity = new ScanActivity();
        scanActivity.setArguments(bundle);
        return scanActivity;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDisponiveis();
    }

    private void updateDisponiveis() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=conDisponiveis&cboTeatro=" + local + "&cboApresentacao="+ apresentacao +"&cboHorario="+ horario +"&cboPeca="+ evento +"&cboSetor="+ setor;
        try {
            String response = util.makeRequest(URLServer, param);
            JSONObject json = new JSONObject(response);
            String Total = json.getString("Total");
            String Utilizados = json.getString("Utilizados");
            txtDisponiveis.setText(Utilizados + " de " + Total + " disponíveis");
        } catch (Exception e) {
            txtDisponiveis.setText(R.string.txtMensagem);
            e.printStackTrace();
        }
    }

    public void callZXing(View view){
        Intent it = new Intent(getActivity().getApplicationContext(), com.google.zxing.client.android.CaptureActivity.class);
        startActivityForResult(it, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == -1) {
                String contents = data.getStringExtra("SCAN_RESULT");
                validarCodigo(contents);
            }
        }
    }

    private void validarCodigo(String codigo) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        String sentido = "";
        switch (cboSentido.getSelectedItemPosition()){
            case 0:
                sentido = "entrada";
                break;
            case 1:
                sentido = "saida";
                break;
            default:
                sentido = "entrada";
                break;
        }
        String param = "codigo="+ codigo +"&admin="+ idUser +"&cboTeatro="+ local+"&cboPeca="+evento+"&cboApresentacao="+apresentacao+"&cboHorario="+horario+"&sentido="+sentido+"&cboSetor="+setor;
        try {
            String response = util.makeRequest(url, param);
            JSONObject json = new JSONObject(response);
            String resposta = json.getString("class");
            String mensagem = json.getString("mensagem");
            if(resposta.equals("sucesso")){
                MediaPlayer song = MediaPlayer.create(getActivity(), R.raw.beep);
                song.start();
                txResult.setBackgroundColor(getResources().getColor(R.color.result_text_sucess));
            }else{
                MediaPlayer song = MediaPlayer.create(getActivity(), R.raw.sirene);
                song.start();
                Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VIBRATE_DURATION);
                txResult.setBackgroundColor(getResources().getColor(R.color.result_text_fail));
            }
            txResult.setText(mensagem);
        } catch (Exception e){
            Toast.makeText(getActivity(), R.string.txtMensagem, Toast.LENGTH_LONG).show();
        }
    }
}
