package br.compreingressos.checkcompre;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.compreingressos.checkcompre.model.Apresentacao;
import br.compreingressos.checkcompre.model.Evento;
import br.compreingressos.checkcompre.model.Horario;
import br.compreingressos.checkcompre.model.Local;
import br.compreingressos.checkcompre.model.Setor;
import br.compreingressos.checkcompre.util.Util;

public class LocalActivity extends Activity {

    private Spinner cboLocal, cboEvento, cboApresentacao, cboHorario, cboSetor;
    private Button btnLeitura;
    private Util util = new Util();
    private String idUser;
    private final String url = "https://compra.compreingressos.com/mobile/consulta.php";
    private final String urlAutenticacao = "https://compra.compreingressos.com/mobile/autenticacao.php";
    String FILENAME = "user-data";
    MenuItem menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local);

        Bundle bundle = getIntent().getExtras();
        idUser = bundle.getString("idUser");
        cboLocal = (Spinner) findViewById(R.id.cboLocal);
        btnLeitura = (Button)findViewById(R.id.btnLeitura);
        addLocais();

        btnLeitura.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                leitura(v);
            }
        });

    }

    public boolean isOperador(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "id_usuario="+ idUser;
        boolean retorno = false;
        try {
            String response = util.makeRequest(urlAutenticacao, param);
            if (!response.isEmpty()) {
                JSONObject json = new JSONObject(response);
                Integer resposta = json.getInt("operador");
                retorno = (resposta == 1);
            }
        } catch (JSONException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("LocalActivity", e.getMessage());
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("LocalActivity", e.getMessage());
        }
        return retorno;
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_local, container, false);
        idUser = getArguments().getString("idUser");
        cboLocal = (Spinner) view.findViewById(R.id.cboLocal);
        btnLeitura = (Button)view.findViewById(R.id.btnLeitura);
        addLocais();

        btnLeitura.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                leitura(v);
            }
        });

        return view;
    }
    */

    /*
    public static LocalActivity newInstance(Bundle bundle) {
        LocalActivity localActivity = new LocalActivity();
        localActivity.setArguments(bundle);
        return localActivity;
    }
    */

    public void addLocais(){
        List<Local> list = new ArrayList<Local>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=cboTeatro&admin="+ idUser;
        String response = util.makeRequest(url, param);

        try {
            JSONArray json = new JSONArray(response);
            list.add(new Local("0", "Selecione o Local"));
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                Local l = new Local(item.getString("id"), item.getString("value"));
                list.add(l);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        ArrayAdapter<Local> dataAdapter = new ArrayAdapter<Local>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboLocal.setAdapter(dataAdapter);

        cboLocal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Local l = (Local) parent.getItemAtPosition(position);
                addEvento(l);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addEvento(final Local local){
        List<Evento> eventos = new ArrayList<Evento>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=cboPeca&admin="+ idUser +"&cboTeatro="+ local.id;
        String response = util.makeRequest(url, param);

        try {
            JSONArray json = new JSONArray(response);
            eventos.add(new Evento("0", "Selecione o Evento"));
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                Evento l = new Evento(item.getString("id"), item.getString("value"));
                eventos.add(l);
            }

        }catch(JSONException e){
            e.printStackTrace();
        }

        cboEvento = (Spinner) findViewById(R.id.cboEvento);

        ArrayAdapter<Evento> dataAdapter = new ArrayAdapter<Evento>(this, android.R.layout.simple_spinner_item, eventos);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboEvento.setAdapter(dataAdapter);

        cboEvento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Evento e = (Evento) parent.getItemAtPosition(position);
                addApresentacao(e, local);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addApresentacao(final Evento evento, final Local local){
        List<Apresentacao> apresentacao = new ArrayList<Apresentacao>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=cboApresentacao&admin="+ idUser +"&cboTeatro="+ local.id+"&cboPeca="+evento.id;
        String response = util.makeRequest(url, param);

        try {
            JSONArray json = new JSONArray(response);
            apresentacao.add(new Apresentacao("0", "Selecione a Apresentação"));
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                Apresentacao l = new Apresentacao(item.getString("id"), item.getString("value"));
                apresentacao.add(l);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        cboApresentacao = (Spinner) findViewById(R.id.cboApresentacao);

        ArrayAdapter<Apresentacao> dataAdapter = new ArrayAdapter<Apresentacao>(this, android.R.layout.simple_spinner_item, apresentacao);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboApresentacao.setAdapter(dataAdapter);

        cboApresentacao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Apresentacao a = (Apresentacao) parent.getItemAtPosition(position);
                addHorario(evento, local, a);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addHorario(final Evento evento, final Local local, final Apresentacao apresentacao){
        List<Horario> horarios = new ArrayList<Horario>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=cboHorario&admin="+ idUser +"&cboTeatro="+ local.id+"&cboPeca="+evento.id+"&cboApresentacao="+apresentacao.id;
        String response = util.makeRequest(url, param);

        try {
            JSONArray json = new JSONArray(response);
            horarios.add(new Horario("0", "Selecione o Horário"));
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                Horario l = new Horario(item.getString("id"), item.getString("value"));
                horarios.add(l);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        cboHorario = (Spinner) findViewById(R.id.cboHorario);

        ArrayAdapter<Horario> dataAdapter = new ArrayAdapter<Horario>(this, android.R.layout.simple_spinner_item, horarios);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboHorario.setAdapter(dataAdapter);

        cboHorario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Horario horario = (Horario) parent.getItemAtPosition(position);
                addSetor(evento, local, apresentacao, horario);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addSetor(Evento evento, Local local, Apresentacao apresentacao, Horario horario){
        List<Setor> setores = new ArrayList<>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=cboSetor&admin="+ idUser +"&cboTeatro="+ local.id+"&cboPeca="+evento.id+"&cboApresentacao="+apresentacao.id+"&cboHorario="+horario.id;
        System.out.println(param);
        String response = util.makeRequest(url, param);

        try {
            JSONArray json = new JSONArray(response);
            setores.add(new Setor("0", "Todos os setores"));
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                Setor s = new Setor(item.getString("id"), item.getString("value"));
                setores.add(s);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cboSetor = (Spinner) findViewById(R.id.cboSetor);

        ArrayAdapter<Setor> dataAdapter = new ArrayAdapter<Setor>(this, android.R.layout.simple_spinner_item, setores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboSetor.setAdapter(dataAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_local, menu);
        this.menu = (MenuItem) menu.findItem(R.id.action_assinante);
        this.menu.setEnabled(isOperador());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_assinante:
                Intent i = new Intent(this, AssinaturaActivity.class);
                i.putExtra("idUser", idUser);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void leitura(View v){
        if(validarCampos()) {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("idUser", idUser);
            i.putExtra("local", ((Local) cboLocal.getSelectedItem()).id);
            i.putExtra("evento", ((Evento) cboEvento.getSelectedItem()).id);
            i.putExtra("apresentacao", ((Apresentacao) cboApresentacao.getSelectedItem()).id);
            i.putExtra("horario", ((Horario) cboHorario.getSelectedItem()).id);
            i.putExtra("setor", ((Setor) cboSetor.getSelectedItem()).id);
            startActivity(i);
        }
    }

    private boolean validarCampos() {
        if(cboLocal.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.cbo_local, Toast.LENGTH_LONG).show();
            return false;
        }else if(cboEvento.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.cbo_evento, Toast.LENGTH_LONG).show();
            return false;
        }else if(cboApresentacao.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.cbo_apresentacao, Toast.LENGTH_LONG).show();
            return false;
        }else if(cboHorario.getSelectedItemPosition() == 0) {
            Toast.makeText(this, R.string.cbo_horario, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
