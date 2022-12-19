package com.example.autogym1.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.autogym1.ListAdapterMaquinas;
import com.example.autogym1.Objects.Maquinas;
import com.example.autogym1.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MaquinasActivity extends Fragment{

    private EditText txt_idMaquina, txt_nombreMaquina;
    private Button btn_modificarMaquinas, btn_registrarMaquinas, btn_EliminarMaquinas;
    private ImageButton btn_buscarMaquinas;
    private ListView lvMaquinas;
    ListAdapterMaquinas adapter;
    List<Maquinas> lisMaquinas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragments_maquinas, container, false);

        txt_idMaquina   = (EditText) view.findViewById(R.id.txt_idMaquina);
        txt_nombreMaquina  = (EditText) view.findViewById(R.id.txt_nombreMaquina);
        btn_buscarMaquinas  = (ImageButton)   view.findViewById(R.id.btn_buscarMaquinas);
        btn_modificarMaquinas  = (Button)   view.findViewById(R.id.btn_modificarMaquinas);
        btn_registrarMaquinas  = (Button)   view.findViewById(R.id.btn_RegistrarMaquinas);
        btn_EliminarMaquinas  = (Button)   view.findViewById(R.id.btn_EliminarMaquinas);
        lvMaquinas = (ListView) view.findViewById(R.id.lvMaquinas);

        botonBuscar();
        botonModificar();
        botonRegistrar();
        botonEliminar();
        listarMaquinas();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //codigo programable
    }

    private void botonBuscar(){
        btn_buscarMaquinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idMaquina.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para realizar busqueda", Toast.LENGTH_SHORT).show();
                }else{
                    int id=Integer.parseInt(txt_idMaquina.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux=Integer.toString(id);
                            boolean res=false;
                            for (DataSnapshot x:snapshot.getChildren()) {
                                if(aux.equalsIgnoreCase(x.child("id_maquina").getValue().toString())){
                                    res=true;
                                    ocultarTeclado();
                                    txt_nombreMaquina.setText(x.child("nombre_maquina").getValue().toString());
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
        btn_registrarMaquinas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                if(txt_idMaquina.getText().toString().trim().isEmpty() || txt_nombreMaquina.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes", Toast.LENGTH_SHORT).show();
                }else{
                    int id_maquina=Integer.parseInt(txt_idMaquina.getText().toString());
                    String nombre_maquina=txt_nombreMaquina.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_maquina);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_maquina").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    Toast.makeText(getActivity(), "Error, el ID ("+id_maquina+") ya está registrado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            boolean res2 = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("nombre_maquina").getValue().toString().equals(nombre_maquina)){
                                    res2=true;
                                    ocultarTeclado();
                                    Toast.makeText(getActivity(), "Advertencia: El Nombre ("+nombre_maquina+") ya está registrado", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }


                            if(res==false){
                                Maquinas maquina = new Maquinas(id_maquina, nombre_maquina);
                                dbref.push().setValue(maquina);
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show();
                                txt_idMaquina.setText("");
                                txt_nombreMaquina.setText("");
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
        btn_modificarMaquinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idMaquina.getText().toString().trim().isEmpty() || txt_nombreMaquina.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Complete los datos faltantes para actualizar registro", Toast.LENGTH_SHORT).show();
                }else{
                    int id_maquina=Integer.parseInt(txt_idMaquina.getText().toString());
                    String nombre_maquina=txt_nombreMaquina.getText().toString();

                    //Conexion a firebase
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

                    //Evento de firebase para la inserción
                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String aux=Integer.toString(id_maquina);
                            boolean res = false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                if(x.child("id_maquina").getValue().toString().equalsIgnoreCase(aux)){
                                    res=true;
                                    ocultarTeclado();
                                    x.getRef().child("nombre_maquina").setValue(nombre_maquina);
                                    txt_nombreMaquina.setText("");
                                    txt_idMaquina.setText("");
                                    listarMaquinas();
                                    break;
                                }
                            }

                            if(res==false){
                                ocultarTeclado();
                                Toast.makeText(getActivity(), "Id: "+aux+" no encontrado", Toast.LENGTH_SHORT).show();
                                txt_idMaquina.setText("");
                                txt_nombreMaquina.setText("");
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
        btn_EliminarMaquinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txt_idMaquina.getText().toString().trim().isEmpty()){
                    ocultarTeclado();
                    Toast.makeText(getActivity(), "Ingrese ID para eliminar", Toast.LENGTH_SHORT).show();
                }else {
                    int id = Integer.parseInt(txt_idMaquina.getText().toString());
                    //Conexion con la base de datos
                    FirebaseDatabase db = FirebaseDatabase.getInstance();
                    DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String aux = Integer.toString(id);
                            final boolean[] res = {false};
                            for (DataSnapshot x : snapshot.getChildren()) {
                                if (aux.equalsIgnoreCase(x.child("id_maquina").getValue().toString())) {

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
                                            listarMaquinas();

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

    private void listarMaquinas(){
        //Conexion con la base de datos
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbref = db.getReference(Maquinas.class.getSimpleName());

        adapter = new ListAdapterMaquinas(getActivity(), R.layout.item_row_maquinas, lisMaquinas);
        lvMaquinas.setAdapter(adapter);

        dbref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Cuando se agregen datos nuevos a la db
                Maquinas maquinas = snapshot.getValue(Maquinas.class);
                lisMaquinas.add(maquinas);
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

        lvMaquinas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Maquinas maquinas = lisMaquinas.get(i);
                AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
                a.setCancelable(true);
                a.setTitle("Maquina seleccionada");

                String msg= "ID: "+maquinas.getId_maquina()+"\n";
                msg+="NOMBRE: "+maquinas.getNombre_maquina();

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


