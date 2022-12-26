package com.example.autogym1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.autogym1.Objects.Complementos;
import com.example.autogym1.Objects.Maquinas;

import java.util.List;

public class ListAdapterComplementos extends ArrayAdapter<Complementos>{

    private List<Complementos> mList;
    private Context mContext;
    private int resourceLayout;

    public ListAdapterComplementos(@NonNull Context context, int resource, List<Complementos> objects) {
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

        Complementos complementos = mList.get(position);
        TextView txt_nombre = view.findViewById(R.id.txt_nombre);
        txt_nombre.setText(complementos.getNombre_complemento());
        TextView txt_id = view.findViewById(R.id.txt_id);
        txt_id.setText(String.valueOf(complementos.getId_complemento()));

        return view;
    }
}
