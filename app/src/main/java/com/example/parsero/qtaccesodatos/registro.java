package com.example.parsero.qtaccesodatos;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parsero.qtaccesodatos.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class registro extends AppCompatActivity {

    Button btnRegistro, btnCancelar;
    EditText etEmail, etPassword, etUserName, etNombre, etApellidos, etDireccion;
    ListView listadoUsuarios;
    private FirebaseAuth mAuth;
    DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellidos = (EditText) findViewById(R.id.etApellidos);
        etDireccion = (EditText) findViewById(R.id.etDireccion);
        listadoUsuarios = (ListView) findViewById(R.id.listadoUsuarios);

        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(registro.this, MainActivity.class);
                startActivity(i);
            }
        });


        btnRegistro = (Button) findViewById(R.id.btnRegistrar);
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();


                registro(email, password);
//                registroBbdd(username, nombre, apellidos, direccion);
            }
        });

        //Referencia a la bbdd :
        bbdd = FirebaseDatabase.getInstance().getReference("usuarios");

        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//NOS CREAMOS UN ARRAY ADAPTER PARA MOSTRARLO SOBRE EL LIST VIEW:
                ArrayAdapter<String> adapter;
                ArrayList<String> listado = new ArrayList<>();

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {

                    Usuario usuario = datasnapshot.getValue(Usuario.class);

                    String nombreUsuario = usuario.getUserName();
                    String nombre = usuario.getNombre();
                    String apellidos = usuario.getApellidos();
                    String direccion = usuario.getDireccion();

                    listado.add("NOMBRE USUARIO:" + nombreUsuario);
                    listado.add("NOMBRE: " + nombre);
                    listado.add("APELLIDOS: " + apellidos);
                    listado.add("DIRECCION: " + direccion);
                    listado.add("-------------------------------------");
                }
                adapter = new ArrayAdapter<String>(registro.this, android.R.layout.simple_list_item_1, listado);
                listadoUsuarios.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //ESTE ES EL REGISTRO PARA ACCEDER A LA APLICACIÓN (AUTENTICACIÓN)
    public void registro(String email, String password) {

        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            String username = etUserName.getText().toString();
                            String nombre = etNombre.getText().toString();
                            String apellidos = etApellidos.getText().toString();
                            String direccion = etDireccion.getText().toString();

                            FirebaseUser user = mAuth.getCurrentUser();
                            String clave = user.getUid();
                            Toast.makeText(registro.this, "La clave es " + clave, Toast.LENGTH_SHORT).show();

                            registroBbdd(username, nombre, apellidos, direccion, clave);

                            Toast.makeText(registro.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(registro.this, "Authentication failed. Ya existe un usuario con este correo",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });


    }

    //ESTO ES PARA REGISTRAR DATOS EN LA BASE DE DATOS
    public void registroBbdd(String username, String nombre, String apellidos, String direccion, String clave) {
        if (clave != null) {
            username = "ID-"+username;
            String claveUsuario = clave;
            Toast.makeText(this, "La clave es " + clave, Toast.LENGTH_SHORT).show();
            Usuario u1 = new Usuario(claveUsuario,username, nombre, apellidos, direccion);

            bbdd.child(clave).setValue(u1);

        }
    }

}
