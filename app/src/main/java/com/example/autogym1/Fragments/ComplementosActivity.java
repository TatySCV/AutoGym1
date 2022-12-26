package com.example.autogym1.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.autogym1.ListAdapterComplementos;
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

public class ComplementosActivity extends Fragment {

    private EditText txt_idComplemento, txt_nombreComplemento;
    private Button btn_modificarComplementos, btn_registrarComplementos, btn_EliminarComplementos;
    private ImageButton btn_buscarComplementos;
    private ListView lvComplementos;

    ListAdapterComplementos adapter;
    List<Complementos> lisComplementos = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragments_complementos, container, false);

        txt_idComplemento = (EditText) view.findViewById(R.id.txt_idComplemento);
        txt_nombreComplemento  = (EditText) view.findViewById(R.id.txt_nombreComplemento);
        btn_buscarComplementos  = (ImageButton)   view.findViewById(R.id.btn_buscarComplemento);
        btn_modificarComplementos  = (Button)   view.findViewById(R.id.btn_modificarComplementos);
        btn_registrarComplementos = (Button)   view.findViewById(R.id.btn_RegistrarComplementos);
        btn_EliminarComplementos  = (Button)   view.findViewById(R.id.btn_EliminarComplementos);
        lvComplementos = (ListView) view.findViewById(R.id.lvComplementos);


        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        listarComplementos();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //codigo programable

    }

    private void botonBuscar(){
        btn_buscarComplementos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idComplemento.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para realizar busqueda", Toast.LENGTH_SHORT).show();
                }else{
                    int id=Integer.parseInt(txt_idComplemento.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Complementos.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux=Integer.toString(id);
                            boolean res=false;
                            for (DataSnapshot x:snapshot.getChildren()) {
                                if(aux.equalsIgnoreCase(x.child("id_complemento").getValue().toString())){
                                    res=true;
                                    ocultarTeclado();
                                    txt_nombreComplemento.setText(x.child("nombre_complemento").getValue().toString());
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
        btn_registrarComplementos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txt_idComplemento.getText().toString().trim().isEmpty() || txt_nombreComplemento.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes", Toast.LENGTH_SHORT).show();
                }else{
                    int id_complemento=Integer.parseInt(txt_idComplemento.getText().toString());
                    String nombre_complemento=txt_nombreComplemento.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Complementos.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_complemento);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_complemento").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    Toast.makeText(getActivity(), "Error, el ID ("+id_complemento+") ya está registrado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(res==false){
                                Complementos complementos = new Complementos(id_complemento, nombre_complemento);
                                dbref.push().setValue(complementos);
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                                txt_idComplemento.setText("");
                                txt_nombreComplemento.setText("");
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
        btn_modificarComplementos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idComplemento.getText().toString().trim().isEmpty() || txt_nombreComplemento.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes para actualizar registro", Toast.LENGTH_SHORT).show();
                }else{
                    int id_complemento=Integer.parseInt(txt_idComplemento.getText().toString());
                    String nombre_complemento=txt_nombreComplemento.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Complementos.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_complemento);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_complemento").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    x.getRef().child("nombre_complemento").setValue(nombre_complemento);
                                    txt_nombreComplemento.setText("");
                                    txt_idComplemento.setText("");
                                    listarComplementos();
                                    break;
                                }
                            }

                            if(res==false){
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Id: "+aux+" no encontrado", Toast.LENGTH_SHORT).show();
                                txt_idComplemento.setText("");
                                txt_nombreComplemento.setText("");
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
        btn_EliminarComplementos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idComplemento.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para eliminar", Toast.LENGTH_SHORT).show();
                }else {
                    int id = Integer.parseInt(txt_idComplemento.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Complementos.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(id);
                            final boolean[] res = {false};
                            for (DataSnapshot x : snapshot.getChildren()) {
                                if (aux.equalsIgnoreCase(x.child("id_complemento").getValue().toString())) {

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
                                            listarComplementos();

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

    private void listarComplementos(){
        //Conexion con la base de datos
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Complementos.class.getSimpleName());

        adapter = new ListAdapterComplementos(getActivity(), R.layout.item_row_maquinas, lisComplementos);
        lvComplementos.setAdapter(adapter);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Cuando se agregen datos nuevos a la db
                Complementos complementos = snapshot.getValue(Complementos.class);
                lisComplementos.add(complementos);
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

        lvComplementos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Complementos complementos = lisComplementos.get(i);
                androidx.appcompat.app.AlertDialog.Builder a = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
                a.setCancelable(true);
                a.setTitle("Complemento seleccionado");

                String msg= "ID: "+complementos.getId_complemento()+"\n";
                msg+="NOMBRE: "+complementos.getNombre_complemento();

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


