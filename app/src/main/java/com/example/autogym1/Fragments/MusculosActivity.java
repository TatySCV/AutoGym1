package com.example.autogym1.Fragments;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.autogym1.ListAdapterMusculos;
import com.example.autogym1.Objects.Musculos;
import com.example.autogym1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MusculosActivity extends Fragment {

    private EditText txt_idMusculo, txt_nombreMusculo;
    private Button btn_modificarMusculos, btn_registrarMusculos, btn_EliminarMusculos;
    private ImageButton btn_buscarMusculos;
    private ListView lvMusculos;
    ListAdapterMusculos adapter;
    List<Musculos> lisMusculos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragments_musculos, container, false);

        txt_idMusculo   = (EditText) view.findViewById(R.id.txt_idMusculo);
        txt_nombreMusculo  = (EditText) view.findViewById(R.id.txt_nombreMusculo);
        btn_buscarMusculos  = (ImageButton)   view.findViewById(R.id.btn_buscarMusculo);
        btn_modificarMusculos  = (Button)   view.findViewById(R.id.btn_modificarMusculos);
        btn_registrarMusculos  = (Button)   view.findViewById(R.id.btn_RegistrarMusculos);
        btn_EliminarMusculos  = (Button)   view.findViewById(R.id.btn_EliminarMusculos);
        lvMusculos = (ListView) view.findViewById(R.id.lvMusculos);

        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        listarMusculos();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //codigo programable
    }

    private void botonBuscar(){
        btn_buscarMusculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idMusculo.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para realizar busqueda", Toast.LENGTH_SHORT).show();
                }else{
                    int id=Integer.parseInt(txt_idMusculo.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Musculos.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux=Integer.toString(id);
                            boolean res=false;
                            for (DataSnapshot x:snapshot.getChildren()) {
                                if(aux.equalsIgnoreCase(x.child("id_musculo").getValue().toString())){
                                    res=true;
                                    ocultarTeclado();
                                    txt_nombreMusculo.setText(x.child("nombre_musculo").getValue().toString());
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
        btn_registrarMusculos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txt_idMusculo.getText().toString().trim().isEmpty() || txt_nombreMusculo.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes", Toast.LENGTH_SHORT).show();
                }else{
                    int id_musculo=Integer.parseInt(txt_idMusculo.getText().toString());
                    String nombre_musculo=txt_nombreMusculo.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Musculos.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_musculo);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_musculo").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    Toast.makeText(getActivity(), "Error, el ID ("+id_musculo+") ya está registrado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            boolean res2 = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("nombre_musculo").getValue().toString().equals(nombre_musculo)){
                                    res2=true;
                                    ocultarTeclado();
                                    Toast.makeText(getActivity(), "Advertencia: El Nombre ("+nombre_musculo+") ya está registrado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }


                            if(res==false){
                                Musculos musculo = new Musculos(id_musculo, nombre_musculo);
                                dbref.push().setValue(musculo);
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                                txt_idMusculo.setText("");
                                txt_nombreMusculo.setText("");
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
        btn_modificarMusculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idMusculo.getText().toString().trim().isEmpty() || txt_nombreMusculo.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes para actualizar registro", Toast.LENGTH_SHORT).show();
                }else{
                    int id_musculo=Integer.parseInt(txt_idMusculo.getText().toString());
                    String nombre_musculo=txt_nombreMusculo.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Musculos.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_musculo);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_musculo").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    x.getRef().child("nombre_musculo").setValue(nombre_musculo);
                                    txt_nombreMusculo.setText("");
                                    txt_idMusculo.setText("");
                                    listarMusculos();
                                    break;
                                }
                            }

                            if(res==false){
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Id: "+aux+" no encontrado", Toast.LENGTH_SHORT).show();
                                txt_idMusculo.setText("");
                                txt_nombreMusculo.setText("");
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
        btn_EliminarMusculos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idMusculo.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para eliminar", Toast.LENGTH_SHORT).show();
                }else {
                    int id = Integer.parseInt(txt_idMusculo.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Musculos.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(id);
                            final boolean[] res = {false};
                            for (DataSnapshot x : snapshot.getChildren()) {
                                if (aux.equalsIgnoreCase(x.child("id_musculo").getValue().toString())) {

                                    AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
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
                                            listarMusculos();

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

    private void listarMusculos(){
        //Conexion con la base de datos
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Musculos.class.getSimpleName());

        adapter = new ListAdapterMusculos(getActivity(), R.layout.item_row_maquinas, lisMusculos);
        lvMusculos.setAdapter(adapter);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Cuando se agregen datos nuevos a la db
                Musculos musculos = snapshot.getValue(Musculos.class);
                lisMusculos.add(musculos);
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

        lvMusculos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Musculos musculos = lisMusculos.get(i);
                AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
                a.setCancelable(true);
                a.setTitle("Maquina seleccionada");

                String msg= "ID: "+musculos.getId_musculo()+"\n";
                msg+="NOMBRE: "+musculos.getNombre_musculo();

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