package br.compreingressos.checkcompre;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import br.compreingressos.checkcompre.util.Util;


public class PainelActivity extends Fragment {

    private Util util = new Util();
    private static String FILENAME = "user-data";
    private final String url = "https://compra.compreingressos.com/mobile/dashboard.php";
    private TextView txtIngressosValidados, txtIngressosValidadosPercentual,txtPublicoPresente,
            txtPublicoPresentePercentual, txtPublicoAusente, txtPublicoAusentePercentual,
            txtPublicoPagante, txtPublicoNaoPagante;
    private TextView txtDuracao, txtPrimeiraLeitura, txtUltimaLeitura;
    private TextView txtCheckIn, txtCheckOn, txtCheckOut;

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_painel, container, false);

        txtIngressosValidados = (TextView) view.findViewById(R.id.txtIngressosValidados);
        txtIngressosValidadosPercentual = (TextView) view.findViewById(R.id.txtIngressosValidadosPercentual);
        txtPublicoPresentePercentual = (TextView) view.findViewById(R.id.txtPublicoPresentePercentual);
        txtPublicoPresente = (TextView) view.findViewById(R.id.txtPublicoPresente);
        txtPublicoAusentePercentual = (TextView) view.findViewById(R.id.txtPublicoAusentePercentual);
        txtPublicoAusente = (TextView) view.findViewById(R.id.txtPublicoAusente);
        txtPublicoPagante = (TextView) view.findViewById(R.id.txtPublicoPaganteValor);
        txtPublicoNaoPagante = (TextView) view.findViewById(R.id.txtPublicoNaoPaganteValor);

        txtDuracao = (TextView) view.findViewById(R.id.txtDuracaoValor);
        txtPrimeiraLeitura = (TextView) view.findViewById(R.id.txtPrimeiraLeituraValor);
        txtUltimaLeitura = (TextView) view.findViewById(R.id.txtUltimaLeituraValor);

        txtCheckIn = (TextView) view.findViewById(R.id.txtCheckInValor);
        txtCheckOn = (TextView) view.findViewById(R.id.txtCheckOnValor);
        txtCheckOut = (TextView) view.findViewById(R.id.txtCheckOutValor);

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

        loadDashboard();

        return view;
    }

    public static PainelActivity newInstance(Bundle bundle){
        PainelActivity painelActivity = new PainelActivity();
        painelActivity.setArguments(bundle);
        return painelActivity;
    }

    public void loadDashboard() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "admin=" + idUser +"&cboTeatro="+ local +"&cboPeca="+ evento +
                "&cboApresentacao="+ apresentacao +"&cboHorario="+ horario +"&boSala=TODOS";
        String response = util.makeRequest(url, param);
        try {
            if (!response.isEmpty()) {
                JSONObject json = new JSONObject(response);
                try{
                    String resultado = json.getString("resultado");
                    new AlertDialog.Builder(getActivity()).setTitle("Informação").setMessage(resultado)
                            .setNeutralButton("Fechar Mensagem", null).show();
                }catch (JSONException e){
                    int QTDINGTOTAL = json.getInt("QTDINGTOTAL");
                    int QTDINGLIDOS = json.getInt("QTDINGLIDOS");
                    int QTDINGAUSENTE = QTDINGTOTAL - QTDINGLIDOS;
                    int QTDINGPAGANTE = json.getInt("QTDINGPAGANTE");
                    int QTDINGCONVITE = json.getInt("QTDINGCONVITE");
                    String HRLEITURADURACAO = json.getString("HRLEITURADURACAO");
                    String HRLEITURAINI = json.getString("HRLEITURAINI");
                    String HRLEITURAFIM = json.getString("HRLEITURAFIM");
                    int CHECKIN = json.getInt("CHECKIN");
                    int CHECKON = json.getInt("CHECKON");
                    int CHECKOUT = json.getInt("CHECKOUT");

                    float ingValidadosPercentual = ((float) QTDINGLIDOS / (float) QTDINGTOTAL) * 100;
                    float publicoPresentePercentual = ((float) QTDINGLIDOS / (float) QTDINGTOTAL) * 100;
                    float publicoAusentePercentual = ((float) QTDINGAUSENTE / (float) QTDINGTOTAL) * 100;

                    DecimalFormat df = new DecimalFormat("0.00");
                    txtIngressosValidados.append(" " + Integer.toString(QTDINGLIDOS) + " de " + Integer.toString(QTDINGTOTAL));
                    txtIngressosValidadosPercentual.setText(df.format(ingValidadosPercentual) + "%");
                    txtPublicoPresentePercentual.setText(df.format(publicoPresentePercentual) + "%");
                    txtPublicoPresente.append(" " + Integer.toString(QTDINGLIDOS));
                    txtPublicoAusentePercentual.setText(df.format(publicoAusentePercentual) + "%");
                    txtPublicoAusente.append(" " + Integer.toString(QTDINGAUSENTE));
                    txtPublicoPagante.setText(Integer.toString(QTDINGPAGANTE));
                    txtPublicoNaoPagante.setText(Integer.toString(QTDINGCONVITE));

                    txtDuracao.setText(HRLEITURADURACAO);
                    txtPrimeiraLeitura.setText(HRLEITURAINI);
                    txtUltimaLeitura.setText(HRLEITURAFIM);

                    txtCheckIn.setText(" "+ Integer.toString(CHECKIN));
                    txtCheckOn.setText(" "+ Integer.toString(CHECKON));
                    txtCheckOut.setText(" "+ Integer.toString(CHECKOUT));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
