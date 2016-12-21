package br.compreingressos.checkcompre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.compreingressos.checkcompre.model.Lugar;

/**
 * Created by edicarlosbarbosa on 25/05/15.
 */
public class AdapterListView extends BaseAdapter
{
    private LayoutInflater mInflater;
    private List<Lugar> itens;

    public AdapterListView(Context context, List<Lugar> itens)
    {
        this.itens = itens;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return itens.size();
    }

    public Lugar getItem(int position)
    {
        return itens.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent)
    {
        Lugar item = itens.get(position);
        view = mInflater.inflate(R.layout.search_item, null);
        ((TextView) view.findViewById(R.id.txtCliente)).setText(item.getCliente());
        ((TextView) view.findViewById(R.id.txtLugar)).setText(item.getLugar());

        return view;
    }
}
