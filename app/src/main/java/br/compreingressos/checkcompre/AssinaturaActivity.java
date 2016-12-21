package br.compreingressos.checkcompre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

import br.compreingressos.checkcompre.model.Assinatura;
import br.compreingressos.checkcompre.util.Util;

public class AssinaturaActivity extends Activity {

    private Spinner cboAssinatura;
    private Button btnAssinatura;

    private String idUser;
    private String local;
    private String evento;
    private String apresentacao;
    private String horario;

    private Util util = new Util();
    private final String url = "https://compra.compreingressos.com/mobile/assinatura.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assinatura);

        Bundle p = getIntent().getExtras();
        idUser = p.getString("idUser");
        //local = p.getString("local");
//        evento = p.getString("evento");
//        apresentacao = p.getString("apresentacao");
//        horario = p.getString("horario");

        cboAssinatura = (Spinner) findViewById(R.id.cboAssinatura);
        btnAssinatura = (Button) findViewById(R.id.btnAssinatura);

        btnAssinatura.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                venda();
            }
        });

        addAssinaturas();
    }

    public void venda(){
        if(cboAssinatura.getSelectedItemPosition() != 0) {
            Intent i = new Intent(this, VendaActivity.class);
            i.putExtra("idUser", idUser);
            i.putExtra("idAssinatura", ((Assinatura) cboAssinatura.getSelectedItem()).id);
            startActivity(i);
        }  else {
            Toast.makeText(this, R.string.cbo_assinatura, Toast.LENGTH_LONG).show();
        }
    }

    public void addAssinaturas(){
        List<Assinatura> list = new ArrayList<Assinatura>();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);

        String param = "action=consulta";
        String response = util.makeRequest(url, param);

        try {
            JSONArray json = new JSONArray(response);
            list.add(new Assinatura("0", "Selecione a Assinatura"));
            for (int i = 0; i < json.length(); i++) {
                JSONObject item = json.getJSONObject(i);
                Assinatura l = new Assinatura(item.getString("id"), item.getString("value"));
                list.add(l);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }

        ArrayAdapter<Assinatura> dataAdapter = new ArrayAdapter<Assinatura>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cboAssinatura.setAdapter(dataAdapter);

        cboAssinatura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Assinatura l = (Assinatura) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assinatura, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_sair:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
