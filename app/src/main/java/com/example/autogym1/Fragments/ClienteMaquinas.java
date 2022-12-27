package com.example.autogym1.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autogym1.ListAdapterEjercicios;
import com.example.autogym1.ListAdapterMaquinas;
import com.example.autogym1.ListAdapterMusculos;
import com.example.autogym1.Objects.Complementos;
import com.example.autogym1.Objects.Ejercicios;
import com.example.autogym1.Objects.Maquinas;
import com.example.autogym1.Objects.Musculos;
import com.example.autogym1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClienteMaquinas extends Fragment {

    ListAdapterMaquinas adapter, adap;
    List<Maquinas> lisMaquinas = new ArrayList<>();
    private ListView lvCliMaquina;

    private Spinner spn_filtroMusculo;
    private Button btn_filtroMusculo, btn_limpiarFiltro;

    List<Musculos> musculos = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cliente_maquinas, container, false);

        lvCliMaquina = view.findViewById(R.id.lvCliMaquina);

        spn_filtroMusculo = view.findViewById(R.id.spn_filtroMusculo);
        btn_filtroMusculo = view.findViewById(R.id.btn_filtrarMusculo);
        btn_limpiarFiltro = view.findViewById(R.id.btn_limpiarFiltro);

        spn_filtroMusculo.setVisibility(View.INVISIBLE);

        listar();
        llenar();

        btn_filtroMusculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn_filtroMusculo.setVisibility(View.VISIBLE);

                spn_filtroMusculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selec = adapterView.getItemAtPosition(i).toString();

                        filtrarMusculo(selec);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

        btn_limpiarFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                spn_filtroMusculo.setVisibility(View.INVISIBLE);

                lisMaquinas.clear();
                adapter = new ListAdapterMaquinas(getActivity(), R.layout.item_row_maquinas, lisMaquinas);
                lvCliMaquina.setAdapter(adapter);

                listar();
            }
        });

        return view;
    }

    private void listar(){

        //Conexion con la base de datos
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

        adapter = new ListAdapterMaquinas(getActivity(), R.layout.item_row_maquinas, lisMaquinas);
        lvCliMaquina.setAdapter(adapter);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Cuando se agregen datos nuevos a la db
                Maquinas maquina = snapshot.getValue(Maquinas.class);
                lisMaquinas.add(maquina);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Cuando se producen cambios
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //Cuando se elimina un dato
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lvCliMaquina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Maquinas maquina = lisMaquinas.get(i);
                androidx.appcompat.app.AlertDialog.Builder a = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                a.setCancelable(true);
                a.setTitle("Maquina seleccionado");

                String msg= "ID: "+maquina.getId_maquina()+"\n";
                msg+="NOMBRE: "+maquina.getNombre_maquina();

                a.setMessage(msg);
                a.show();
            }
        });

    }  //Metodo para listar los ejercicios

    private void llenar(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Musculos.class.getSimpleName());

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot x:snapshot.getChildren()){
                        int id = Integer.parseInt(x.child("id_musculo").getValue().toString());
                        String nombre = x.child("nombre_musculo").getValue().toString();
                        musculos.add(new Musculos(id, nombre));
                    }

                    ArrayAdapter<Musculos> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, musculos);
                    spn_filtroMusculo.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } //Metodo para llenar los spinner

    private void filtrarMusculo(String musculo){

        lisMaquinas.clear();
        adapter = new ListAdapterMaquinas(getActivity(), R.layout.item_row_maquinas, lisMaquinas);
        lvCliMaquina.setAdapter(adapter);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

        Query filtroMusculo = dbref.orderByChild("musculo").equalTo(musculo);

        filtroMusculo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot x:snapshot.getChildren()){
                    Maquinas maquinas = snapshot.getValue(Maquinas.class);
                    Maquinas maq = x.getValue(Maquinas.class);
                    lisMaquinas.add(new Maquinas(maq.getId_maquina(), maq.getNombre_maquina()));
                }

                adap = new ListAdapterMaquinas(getActivity(), R.layout.item_row_maquinas, lisMaquinas);
                lvCliMaquina.setAdapter(adap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
