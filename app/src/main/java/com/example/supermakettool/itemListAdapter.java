package com.example.supermakettool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class itemListAdapter extends BaseAdapter {

    String[][] datos;
    public static LayoutInflater inflater = null;

    Context context;

    public itemListAdapter(Context context, String[][] datos){
        this.datos = datos;
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return datos.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View view = inflater.inflate(R.layout.itemview, null);

        TextView product = (TextView) view.findViewById(R.id.tv_product);
        TextView quantity = (TextView) view.findViewById(R.id.tv_quantity);
        TextView price = (TextView) view.findViewById(R.id.tv_pice);
        TextView tv_um = (TextView) view.findViewById(R.id.tv_um);

        product.setText(datos[position][0]);
        quantity.setText(datos[position][1]);
        price.setText(datos[position][2]);
        tv_um.setText(datos[position][3]);

        return view;
    }
}
