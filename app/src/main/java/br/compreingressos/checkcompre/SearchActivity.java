package br.compreingressos.checkcompre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.compreingressos.checkcompre.model.Lugar;
import br.compreingressos.checkcompre.util.Util;


public class SearchActivity extends Fragment implements AdapterView.OnItemClickListener{
    ProgressBar progress;
    private Util util = new Util();
    private final String url = "https://compra.compreingressos.com/mobile/lugares.php";

    private ListView listView;
    private AdapterListView adapterListView;
    private List<Lugar> itens;

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_search, container, false);

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

        progress = (ProgressBar)view.findViewById(R.id.progress_bar);
        progress.setVisibility(View.INVISIBLE);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        obtemDados();

        return view;
    }

    public static SearchActivity newInstance(Bundle bundle){
        SearchActivity searchActivity = new SearchActivity();
        searchActivity.setArguments(bundle);
        return searchActivity;
    }

    private void obtemDados(){
        progress.setVisibility(View.VISIBLE);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                itens = load();
                progress.post(new Runnable() {
                    @Override
                    public void run() {
                        createListView(itens);
                        progress.setVisibility(View.INVISIBLE);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    private List<Lugar> load() {
        List<Lugar> ingressos = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
       StrictMode.setThreadPolicy(policy);

        String param = "cboTeatro="+ local +"&cboPeca="+ evento + "&cboApresentacao="+ apresentacao + "&cboHorario="+ horario;
        String response = util.makeRequest(url, param);
        try {
            JSONArray json = new JSONArray(response);
            for(int i = 0; i < json.length(); i++){
                JSONObject item = json.getJSONObject(i);
                if(item.has("TOTALQTDE")){
//                    totalQtde = item.getString("TOTALQTDE");
//                    totalValor = item.getString("TOTALVALOR");
                } else {
                    Lugar lugar = new Lugar(item.getString("DSNOME"), item.getString("DSLUGAR"));
                    lugar.setDocumento(item.getString("CDRG"));
                    lugar.setCpf(item.getString("CDCPF"));
                    lugar.setSetor(item.getString("DSSETOR"));
                    lugar.setSala(item.getString("DSSALA"));
                    lugar.setTelefone(item.getString("CDTELEFONE"));
                    lugar.setTipoBilhete(item.getString("DSTIPOBILHETE"));
                    lugar.setValor(item.getString("VALPAGTO"));
                    lugar.setDtEntrada(item.getString("DATHRENTRADA"));
                    ingressos.add(lugar);
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return ingressos;
    }

    private void createListView(List<Lugar> itens)
    {
        //Cria o adapter
        adapterListView = new AdapterListView(getActivity(), itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        Lugar item = adapterListView.getItem(arg2);
        Intent intent = new Intent(getActivity(), LugarActivity.class);
        intent.putExtra("lugar", item);
        getActivity().startActivity(intent);
    }

}
