package com.example.autogym1.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autogym1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class EjerciciosActivity extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragments_ejercicios, container, false);
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
            InputStream inputStream = getActivity().getAssets().open("ejercicios.json");
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

        JSONArray arreglo = new JSONArray(leerJson());
        //JSONObject object = new JSONObject(leerJson());

        for(int i=0; i<arreglo.length(); i++){
            JSONObject maquina=arreglo.getJSONObject(i);
            String id = maquina.getString("id");
            String nombre = maquina.getString("nombre");
            Log.d("NOMBRE: ", nombre);
        }

        /*String id = object.getString("id");
        String name = object.getString("nombre");
        Log.d("NOMBRE: ", name);*/

    }

}
