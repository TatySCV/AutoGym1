package com.example.autogym1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.autogym1.Objects.Ejercicios;
import com.example.autogym1.Objects.Musculos;

import java.util.List;

public class ListAdapterMusculos extends ArrayAdapter<Musculos>{

    private List<Musculos> mList;
    private Context mContext;
    private int resourceLayout;

    public ListAdapterMusculos(@NonNull Context context, int resource, List<Musculos> objects) {
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

        Musculos musculo = mList.get(position);
        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        txt_nombre.setText(musculo.getNombre_musculo());
        TextView txt_id = view.findViewById(R.id.txt_id);
        txt_id.setText(String.valueOf(musculo.getId_musculo()));

        return view;
    }
}
