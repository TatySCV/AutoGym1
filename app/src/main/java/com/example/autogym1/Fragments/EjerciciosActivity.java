package com.example.autogym1.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autogym1.ListAdapterEjercicios;
import com.example.autogym1.ListAdapterMaquinas;
import com.example.autogym1.Objects.Ejercicios;
import com.example.autogym1.Objects.Maquinas;
import com.example.autogym1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EjerciciosActivity extends Fragment {

    private EditText txt_idEjercicio, txt_nombreEjercicio;
    private Button btn_modificarEjercicios, btn_registrarEjercicios, btn_EliminarEjercicios, btn_selComplemento, btn_selRutina;
    private ImageButton btn_buscarEjercicios;
    private ListView lvEjercicios;
    ListAdapterEjercicios adapter;
    List<Ejercicios> lisEjercicios = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragments_ejercicios, container, false);

        txt_idEjercicio = (EditText) view.findViewById(R.id.txt_idEjercicio);
        txt_nombreEjercicio  = (EditText) view.findViewById(R.id.txt_nombreEjercicio);
        btn_buscarEjercicios  = (ImageButton)   view.findViewById(R.id.btn_buscarEjercicios);
        btn_modificarEjercicios  = (Button)   view.findViewById(R.id.btn_modificarEjercicios);
        btn_registrarEjercicios  = (Button)   view.findViewById(R.id.btn_RegistrarEjercicios);
        btn_EliminarEjercicios  = (Button)   view.findViewById(R.id.btn_EliminarEjercicios);
        lvEjercicios = (ListView) view.findViewById(R.id.lvEjercicios);
        btn_selComplemento = view.findViewById(R.id.btn_selComplemento);
        btn_selRutina= view.findViewById(R.id.btn_selRutina);

        //Boton complemento

        //Boton rutina

        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        listarEjercicios();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //codigo programable

    }

    private void botonBuscar(){
        btn_buscarEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idEjercicio.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para realizar busqueda", Toast.LENGTH_SHORT).show();
                }else{
                    int id=Integer.parseInt(txt_idEjercicio.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Ejercicios.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux=Integer.toString(id);
                            boolean res=false;
                            for (DataSnapshot x:snapshot.getChildren()) {
                                if(aux.equalsIgnoreCase(x.child("id_ejercicio").getValue().toString())){
                                    res=true;
                                    ocultarTeclado();
                                    txt_nombreEjercicio.setText(x.child("nombre_ejercicio").getValue().toString());
                                    break;
                                }
                            }
                            if(res==false){
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "ID: "+aux+" no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }

    private void botonRegistrar(){
        btn_registrarEjercicios.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txt_idEjercicio.getText().toString().trim().isEmpty() || txt_nombreEjercicio.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes", Toast.LENGTH_SHORT).show();
                }else{
                    int id_ejercicio=Integer.parseInt(txt_idEjercicio.getText().toString());
                    String nombre_ejercicio=txt_nombreEjercicio.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Ejercicios.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_ejercicio);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_ejercicio").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    Toast.makeText(getActivity(), "Error, el ID ("+id_ejercicio+") ya está registrado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(res==false){
                                Ejercicios ejercicio = new Ejercicios(id_ejercicio, nombre_ejercicio);
                                dbref.push().setValue(ejercicio);
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                                txt_idEjercicio.setText("");
                                txt_nombreEjercicio.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //Para error
                            Toast.makeText(getActivity(), "Error: "+error, Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
    }

    private void botonModificar(){
        btn_modificarEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idEjercicio.getText().toString().trim().isEmpty() || txt_nombreEjercicio.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes para actualizar registro", Toast.LENGTH_SHORT).show();
                }else{
                    int id_ejercicio=Integer.parseInt(txt_idEjercicio.getText().toString());
                    String nombre_ejercicio=txt_nombreEjercicio.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Ejercicios.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_ejercicio);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_ejercicio").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    x.getRef().child("nombre_ejercicio").setValue(nombre_ejercicio);
                                    txt_nombreEjercicio.setText("");
                                    txt_idEjercicio.setText("");
                                    listarEjercicios();
                                    break;
                                }
                            }

                            if(res==false){
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Id: "+aux+" no encontrado", Toast.LENGTH_SHORT).show();
                                txt_idEjercicio.setText("");
                                txt_nombreEjercicio.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //Para error
                            Toast.makeText(getActivity(), "Error: "+error, Toast.LENGTH_SHORT).show();

                        }
                    });

                }

            }
        });
    }

    private void botonEliminar(){
        btn_EliminarEjercicios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idEjercicio.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para eliminar", Toast.LENGTH_SHORT).show();
                }else {
                    int id = Integer.parseInt(txt_idEjercicio.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Ejercicios.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(id);
                            final boolean[] res = {false};
                            for (DataSnapshot x : snapshot.getChildren()) {
                                if (aux.equalsIgnoreCase(x.child("id_ejercicio").getValue().toString())) {

                                    androidx.appcompat.app.AlertDialog.Builder a = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                                    a.setCancelable(false);
                                    a.setTitle("Eliminar registro");
                                    a.setMessage("¿Está seguro que desea eliminar el registro?");
                                    a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });
                                    a.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            res[0] = true;
                                            ocultarTeclado();
                                            x.getRef().removeValue();
                                            listarEjercicios();

                                        }
                                    });
                                    a.show();
                                    break;
                                }
                            }
                            if (res[0] == false) {
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "ID: " + aux + " no encontrado", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });
    }

    private void listarEjercicios(){
        //Conexion con la base de datos
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Ejercicios.class.getSimpleName());

        adapter = new ListAdapterEjercicios(getActivity(), R.layout.item_row_maquinas, lisEjercicios);
        lvEjercicios.setAdapter(adapter);

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

        lvEjercicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ejercicios ejercicios = lisEjercicios.get(i);
                androidx.appcompat.app.AlertDialog.Builder a = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                a.setCancelable(true);
                a.setTitle("Ejercicio seleccionada");

                String msg= "ID: "+ejercicios.getId_ejercicio()+"\n";
                msg+="NOMBRE: "+ejercicios.getNombre_ejercicio();

                a.setMessage(msg);
                a.show();
            }
        });
    }

    public void ocultarTeclado(){
        View view = getActivity().getCurrentFocus();
        if(view != null){
            InputMethodManager input = (InputMethodManager) (getActivity().getSystemService(Context.INPUT_METHOD_SERVICE));
            input.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
