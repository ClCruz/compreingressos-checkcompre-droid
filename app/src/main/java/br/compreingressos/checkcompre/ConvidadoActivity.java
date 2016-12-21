package br.compreingressos.checkcompre;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.compreingressos.checkcompre.model.Convidado;
import br.compreingressos.checkcompre.util.Util;

/**
 * Created by edicarlosbarbosa on 16/11/15.
 */
public class ConvidadoActivity extends Fragment implements AdapterView.OnItemClickListener {

    ProgressBar progress;
    private Util util = new Util();
    private final String url = "https://compra.compreingressos.com/mobile/convidado.php";

    private ListView listView;
    private AdapterConvidadoListView adapterListView;
    private List<Convidado> itens;

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    private TextView txtVazio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_lista_convidado, container, false);

        Bundle p = getArguments();
        idUser = p.getString("idUser");
        local = p.getString("local");
        evento = p.getString("evento");
        apresentacao = p.getString("apresentacao");
        horario = p.getString("horario");

        progress = (ProgressBar)view.findViewById(R.id.progress_bar);
        progress.setVisibility(View.INVISIBLE);

        txtVazio = (TextView) view.findViewById(R.id.txtVazio);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setOnItemClickListener(this);

        obtemDados();
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        obtemDados();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.convidado, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(getActivity(), ConvidadoCreateActivity.class);
                intent.putExtra("idUser", idUser);
                intent.putExtra("local", local);
                intent.putExtra("evento", evento);
                intent.putExtra("apresentacao", apresentacao);
                intent.putExtra("horario", horario);
                getActivity().startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public static ConvidadoActivity newInstance(Bundle bundle){
        ConvidadoActivity convidadoActivity = new ConvidadoActivity();
        convidadoActivity.setArguments(bundle);
        return convidadoActivity;
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
                        if(itens.size() == 0) {
                            txtVazio.setVisibility(View.VISIBLE);
                        }else{
                            txtVazio.setVisibility(View.INVISIBLE);
                        }
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    private List<Convidado> load() {
        List<Convidado> convidados = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=load&cboTeatro="+ local +"&cboPeca="+ evento + "&cboApresentacao="+ apresentacao + "&cboHorario="+ horario.replace(":", "h");
        String response = util.makeRequest(url, param);
        try {
            if(!response.isEmpty()) {
                JSONArray json = new JSONArray(response);
                for (int i = 0; i < json.length(); i++) {
                    JSONObject item = json.getJSONObject(i);

                    Convidado convidado = new Convidado(item.getString("NOME"), item.getString("CPF"));
                    convidado.setCelular(item.getString("CELULAR"));
                    convidado.setConvidadoPor(item.getString("CONVIDADOPOR"));
                    convidado.setEmail(item.getString("EMAIL"));
                    convidado.setId(item.getString("ID"));
                    convidado.setQtdeLugares(item.getString("QTDELUGARES"));
                    convidado.setTipoConvite(item.getString("TIPOCONVITE"));
                    convidado.setConfirmado(item.getString("CONFIRMADO"));
                    convidados.add(convidado);

                }
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return convidados;
    }

    private void createListView(List<Convidado> itens)
    {
        //Cria o adapter
        adapterListView = new AdapterConvidadoListView(getActivity(), itens);

        //Define o Adapter
        listView.setAdapter(adapterListView);
        //Cor quando a lista é selecionada para ralagem.
        listView.setCacheColorHint(Color.TRANSPARENT);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
    {
        Convidado item = adapterListView.getItem(arg2);
        Intent intent = new Intent(getActivity(), ConvidadoDetalheActivity.class);
        intent.putExtra("convidado", item);
        getActivity().startActivity(intent);
    }
}
