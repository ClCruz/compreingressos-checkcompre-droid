package br.compreingressos.checkcompre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.compreingressos.checkcompre.model.Convidado;
import br.compreingressos.checkcompre.model.Lugar;

/**
 * Created by edicarlosbarbosa on 16/11/15.
 */
public class AdapterConvidadoListView extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Convidado> itens;

    public AdapterConvidadoListView(Context context, List<Convidado> itens)
    {
        this.itens = itens;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return itens.size();
    }

    public Convidado getItem(int position)
    {
        return itens.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        Convidado item = itens.get(position);
        view = mInflater.inflate(R.layout.activity_lista_convidado_item, null);
        ((TextView) view.findViewById(R.id.txtConvidado)).setText(item.getNome());

        if(item.getConfirmado().equals("Não"))
            view.findViewById(R.id.imgConfirmado).setVisibility(View.INVISIBLE);

        return view;
    }
}
