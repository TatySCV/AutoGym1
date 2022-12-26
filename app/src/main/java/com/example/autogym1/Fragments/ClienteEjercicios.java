package com.example.autogym1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autogym1.ListAdapterEjercicios;
import com.example.autogym1.Objects.Complementos;
import com.example.autogym1.Objects.Ejercicios;
import com.example.autogym1.Objects.Maquinas;
import com.example.autogym1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClienteEjercicios extends Fragment {

    ListAdapterEjercicios adapter;
    List<Ejercicios> lisEjercicios = new ArrayList<>();
    private ListView lvCliEjercicios;

    private Spinner spn_filtroComplemento, spn_filtroMaquina;
    private Button btn_filtroComplemento, btn_filtroMaquina;

    List<Complementos> complementos = new ArrayList<>();
    List<Maquinas> maquinas = new ArrayList<>();

    private String maquina1, complemento1;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cliente_ejercicios, container, false);

        lvCliEjercicios = view.findViewById(R.id.lvCliEjercicios);

        spn_filtroComplemento = view.findViewById(R.id.spn_filtroComplemento);
        spn_filtroMaquina = view.findViewById(R.id.spn_filtroMaquina);
        btn_filtroComplemento = view.findViewById(R.id.btn_filtrarComplemento);
        btn_filtroMaquina = view.findViewById(R.id.btn_filtrarMaquina);

        spn_filtroMaquina.setVisibility(View.INVISIBLE);
        spn_filtroComplemento.setVisibility(View.INVISIBLE);

        listar();
        llenar();

        btn_filtroMaquina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn_filtroMaquina.setVisibility(View.VISIBLE);
                spn_filtroComplemento.setVisibility(View.INVISIBLE);

                spn_filtroMaquina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selec = adapterView.getItemAtPosition(i).toString();

                        filtrarMaquina(selec);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });

        btn_filtroComplemento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spn_filtroComplemento.setVisibility(View.VISIBLE);
                spn_filtroMaquina.setVisibility(View.INVISIBLE);

                spn_filtroComplemento.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        String selec = adapterView.getItemAtPosition(i).toString();

                        filtrarComplemento(selec);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        });


        return view;
    }

    private void listar(){

        //Conexion con la base de datos
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Ejercicios.class.getSimpleName());

        adapter = new ListAdapterEjercicios(getActivity(), R.layout.item_row_maquinas, lisEjercicios);
        lvCliEjercicios.setAdapter(adapter);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Cuando se agregen datos nuevos a la db
                Ejercicios ejercicios = snapshot.getValue(Ejercicios.class);
                lisEjercicios.add(ejercicios);
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

        lvCliEjercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ejercicios ejercicios = lisEjercicios.get(i);
                androidx.appcompat.app.AlertDialog.Builder a = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                a.setCancelable(true);
                a.setTitle("Ejercicio seleccionado");

                String msg= "ID: "+ejercicios.getId_ejercicio()+"\n";
                msg+="NOMBRE: "+ejercicios.getNombre_ejercicio();

                a.setMessage(msg);
                a.show();
            }
        });

    }

    private void llenar(){

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Complementos.class.getSimpleName());

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot x:snapshot.getChildren()){
                        int id = Integer.parseInt(x.child("id_complemento").getValue().toString());
                        String nombre = x.child("nombre_complemento").getValue().toString();
                        complementos.add(new Complementos(id, nombre));
                    }

                    ArrayAdapter<Complementos> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, complementos);
                    spn_filtroComplemento.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference dbref2 = db.getReference(Maquinas.class.getSimpleName());

        dbref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot x:snapshot.getChildren()){
                        int id = Integer.parseInt(x.child("id_maquina").getValue().toString());
                        String nombre = x.child("nombre_maquina").getValue().toString();
                        maquinas.add(new Maquinas(id, nombre));
                    }

                    ArrayAdapter<Maquinas> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, maquinas);
                    spn_filtroMaquina.setAdapter(arrayAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filtrarComplemento(String complemento){

        Toast.makeText(getActivity(), "Filtro Complemento", Toast.LENGTH_SHORT).show();

    }

    private void filtrarMaquina(String maquina){

        Toast.makeText(getActivity(), "Filtro Maquina", Toast.LENGTH_SHORT).show();

    }

}
