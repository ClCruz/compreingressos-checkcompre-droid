package br.compreingressos.checkcompre;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import br.compreingressos.checkcompre.model.Lugar;


public class LugarActivity extends Activity {

    Lugar lugar;
    TextView txtCliente, txtContato, txtDocumentoRg, txtDocumentoCpf, txtLugar, txtSetor, txtSala;
    TextView txtTipo, txtValor, txtUtilizado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lugar);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        Intent intent = getIntent();
        this.lugar = (Lugar) intent.getSerializableExtra("lugar");

        preencheCampos(this.lugar);
    }

    public void preencheCampos(Lugar lugar){
        txtCliente = (TextView) findViewById(R.id.txtCliente);
        txtContato = (TextView) findViewById(R.id.txtContato);
        txtDocumentoRg = (TextView) findViewById(R.id.txtDocumentoRg);
        txtDocumentoCpf = (TextView) findViewById(R.id.txtDocumentoCpf);

        txtLugar = (TextView) findViewById(R.id.txtLugar);
        txtSetor = (TextView) findViewById(R.id.txtSetor);
        txtSala = (TextView) findViewById(R.id.txtSala);

        txtTipo = (TextView) findViewById(R.id.txtTipo);
        txtValor = (TextView) findViewById(R.id.txtValor);
        txtUtilizado = (TextView) findViewById(R.id.txtUtilizado);

        txtCliente.setText(lugar.getCliente());
        txtContato.setText(lugar.getTelefone());
        txtDocumentoRg.setText(lugar.getDocumento());
        txtDocumentoCpf.setText(lugar.getCpf());

        txtLugar.setText(lugar.getLugar());
        txtSetor.setText(lugar.getSetor());
        txtSala.setText(lugar.getSala());

        txtTipo.setText(lugar.getTipoBilhete());
        txtValor.setText(lugar.getValor());
        txtUtilizado.setText(lugar.getDtEntrada());
    }

}
