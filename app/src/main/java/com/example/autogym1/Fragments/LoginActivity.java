package com.example.autogym1.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.autogym1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Fragment {

    TextView correo, contrasena;
    Button iniciarAdmin;
    private static final String TAG = "EmailPassword";
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_iniciarusuario, container, false);

        mAuth = FirebaseAuth.getInstance();
        correo = (TextView) view.findViewById(R.id.correo);
        contrasena = (TextView) view.findViewById(R.id.contrasena);

        iniciarAdmin = (Button) view.findViewById(R.id.iniciarAdmin);

        iniciarAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(correo.getText().toString().isEmpty() || contrasena.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(), "Favor complete los campos", Toast.LENGTH_SHORT).show();
                }else{
                    iniciarSesion(correo.getText().toString(), contrasena.getText().toString());
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            reload();
        }
    }

    //Metodo iniciar sesion
    private void iniciarSesion(String correo, String contrasena) {

        mAuth.signInWithEmailAndPassword(correo, contrasena).addOnCompleteListener(getActivity(), (OnCompleteListener<AuthResult>) new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Autentificación exitosa", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);

                    //Si inicia sesion como administrador, debe poder hacer CRUD completo
                    Fragment fragment = new ModuloAdmin();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }else{
                    Toast.makeText(getActivity(), "Error en la autentificación \n No cuentas con permisos de administrador", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
        
    }

    private void reload() { }

    private void updateUI(FirebaseUser user) {

    }

}

