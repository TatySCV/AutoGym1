package com.example.autogym1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.autogym1.Objects.Maquinas;

import java.util.List;

public class ListAdapterMaquinas extends ArrayAdapter<Maquinas>{

    private List<Maquinas> mList;
    private Context mContext;
    private int resourceLayout;

    public ListAdapterMaquinas(@NonNull Context context, int resource, List<Maquinas> objects) {
        super(context, resource, objects);
        this.mList = objects;
        this.mContext = context;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view = LayoutInflater.from(mContext).inflate(R.layout.item_row_maquinas, null);

        Maquinas maquina = mList.get(position);
        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        txt_nombre.setText(maquina.getNombre_maquina());
        TextView txt_id = view.findViewById(R.id.txt_id);
        txt_id.setText(String.valueOf(maquina.getId_maquina()));

        return view;
    }
}
