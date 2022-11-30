package com.example.autogym1.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autogym1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MaquinasActivity extends Fragment{

    private TextView txt_idMaquina, txt_nombreMaquina, txt_imagenMaquina;
    private Button btn_registrar;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragments_maquinas, container, false);

        txt_nombreMaquina = view.findViewById(R.id.txt_nombreMaquina);
        btn_registrar = view.findViewById(R.id.btn_registrar);

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mostrarJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //codigo programable

    }

    public String leerJson()  {

        String json = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("maquinas.json");
            int size = inputStream.available();
            byte[] byteArray = new byte[size];
            inputStream.read(byteArray);
            inputStream.close();
            json = new String(byteArray, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    public void mostrarJson() throws JSONException {

        String nombres="";
        JSONArray arreglo = new JSONArray(leerJson());
        //JSONObject object = new JSONObject(leerJson());

        for(int i=0; i<arreglo.length(); i++){
            JSONObject maquina=arreglo.getJSONObject(i);
            String id = maquina.getString("id");
            String name = maquina.getString("nombre");

            nombres += name + ", ";


        }

        txt_nombreMaquina.setText(nombres);

        /*String id = object.getString("id");
        String name = object.getString("nombre");
        Log.d("NOMBRE: ", name);*/

        }


}


