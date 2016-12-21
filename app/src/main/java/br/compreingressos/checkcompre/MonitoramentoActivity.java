package br.compreingressos.checkcompre;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.compreingressos.checkcompre.model.Ingresso;
import br.compreingressos.checkcompre.util.Util;


public class MonitoramentoActivity extends Fragment {
    ProgressBar progress;
    private static String FILENAME = "user-data";
    private Util util = new Util();
    private final String url = "https://compra.compreingressos.com/mobile/monitoramento.php";

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    private String totalQtde;
    private String totalValor;

    private TableLayout tl;

    List<Ingresso> listaIngressos = new ArrayList<Ingresso>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_monitoramento, container, false);

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

        progress = (ProgressBar)view.findViewById(R.id.progress_bar);
        progress.setVisibility(View.INVISIBLE);

        tl = (TableLayout) view.findViewById(R.id.table);

        obtemDados();

        return view;
    }

    public static MonitoramentoActivity newInstance(Bundle bundle){
        MonitoramentoActivity monitoramentoActivity = new MonitoramentoActivity();
        monitoramentoActivity.setArguments(bundle);
        return monitoramentoActivity;
    }

    private void obtemDados(){
        progress.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                listaIngressos = load();
                progress.post(new Runnable() {
                    @Override
                    public void run() {
                        loadTable(listaIngressos);
                        loadFooter();
                        progress.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    private List<Ingresso> load() {
        List<Ingresso> ingressos = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "admin="+ idUser +"&cboTeatro="+ local +"&cboPeca="+ evento + "&cboApresentacao="+ apresentacao + "&cboHorario="+ horario + "&cboSala=TODOS";
        String response = util.makeRequest(url, param);
        try {
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                JSONObject item = json.getJSONObject(i);
                if(item.has("TOTALQTDE")){
                    totalQtde = item.getString("TOTALQTDE");
                    totalValor = item.getString("TOTALVALOR");
                } else {
                    Ingresso ingresso = new Ingresso(item.getString("TIPBILHETE"), item.getInt("QTDE"), item.getString("VALORUNITARIO"), item.getString("TOTAL"));
                    ingressos.add(ingresso);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return ingressos;
    }

    private void loadTable(List<Ingresso> ingressos) {

        for(Ingresso ingresso : ingressos)
        {
            TableRow tr = new TableRow(getActivity());
            tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));

            TableRow.LayoutParams layout = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);

            TextView t = new TextView(getActivity());
            t.setText(ingresso.getTipoBilhete());
            t.setGravity(Gravity.LEFT);
            t.setPadding(4, 4, 4, 4);
            tr.addView(t, layout);

            TextView t2 = new TextView(getActivity());
            t2.setText(Integer.toString(ingresso.getQuantidade()));
            t2.setGravity(Gravity.RIGHT);
            t2.setPadding(4, 4, 4, 4);
            tr.addView(t2, layout);

            TextView t3 = new TextView(getActivity());
            t3.setText(ingresso.getValor());
            t3.setGravity(Gravity.RIGHT);
            t3.setPadding(4, 4, 4, 4);
            tr.addView(t3, layout);

            TextView t4 = new TextView(getActivity());
            t4.setText(ingresso.getTotal());
            t4.setGravity(Gravity.RIGHT);
            t4.setPadding(4, 4, 4, 4);
            tr.addView(t4, layout);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private void loadFooter(){

        TableRow tr = new TableRow(getActivity());
        tr.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.FILL_PARENT));
        tr.setBackgroundColor(getResources().getColor(R.color.box_value));

        TableRow.LayoutParams layout = new TableRow.LayoutParams(0, TableRow.LayoutParams.MATCH_PARENT, 1.0f);

        TextView t = new TextView(getActivity());
        t.setText("Total");
        t.setGravity(Gravity.LEFT);
        t.setTextColor(Color.WHITE);
        t.setPadding(4, 4, 4, 4);
        tr.addView(t, layout);

        TextView t2 = new TextView(getActivity());
        t2.setText(totalQtde);
        t2.setGravity(Gravity.RIGHT);
        t2.setTextColor(Color.WHITE);
        t2.setPadding(4, 4, 4, 4);
        tr.addView(t2, layout);

        TextView t3 = new TextView(getActivity());
        t3.setText(" ");
        t3.setGravity(Gravity.RIGHT);
        t3.setTextColor(Color.WHITE);
        t3.setPadding(4, 4, 4, 4);
        tr.addView(t3, layout);

        TextView t4 = new TextView(getActivity());
        t4.setText(totalValor);
        t4.setGravity(Gravity.RIGHT);
        t4.setTextColor(Color.WHITE);
        t4.setPadding(4, 4, 4, 4);
        tr.addView(t4, layout);

        tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));

    }

}
