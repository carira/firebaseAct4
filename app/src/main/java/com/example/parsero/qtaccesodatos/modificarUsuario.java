package com.example.parsero.qtaccesodatos;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parsero.qtaccesodatos.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class modificarUsuario extends AppCompatActivity implements View.OnClickListener {

    EditText etNombre, etApellidos, etDireccion, etUserName;

    Button btnEditar, btnCancelar, btnEliminar;

    ListView listViewRegistrados;

    private FirebaseAuth mAuth;
    DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);

        bbdd = FirebaseDatabase.getInstance().getReference("usuarios");

        etUserName = (EditText) findViewById(R.id.etUserName);
        etNombre = (EditText) findViewById(R.id.etNombre);
        etApellidos = (EditText) findViewById(R.id.etApellidos);
        etDireccion = (EditText) findViewById(R.id.etDireccion);

        listViewRegistrados = (ListView) findViewById(R.id.listViewRegistrados);


        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(this);

        //BOTÓN EDITAR USUARIO POR USERNAME...-->
        btnEditar = (Button) findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //IGUALAMOS LA QUERY ALO QUE INTRODUCIMOS EN EL CAMPO USERNAME (PARA BUSCAR POR LO INTRODUCIDO)
                String query = etUserName.getText().toString();

                if (!TextUtils.isEmpty(query)) {

                    Query q = bbdd.orderByChild("userName").equalTo(query);

                    //DE LO QUE RECUPERAMOS CON LA QUERY "q" HACEMOS UN LISTENER
                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //RECORREMOS EL ARRAYLIST DE SNAPSHOTS
                            for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                //SACAMOS LA CLAVE
                                String clave = datasnapshot.getKey();
                                //Y DE LA CLAVE SACAMOS UN NODO DE NOMBRES
                                bbdd.child(clave).child("nombre").setValue(etNombre.getText().toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(modificarUsuario.this, "El campo nombre se ha editado con exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(modificarUsuario.this, "Ha habido un fallo en la edición, prueba otro Username", Toast.LENGTH_SHORT).show();
                }
            }

        });

        //BOTÓN PARA ELIMINAR USUARIO POR USERNAME...-->
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = etUserName.getText().toString();

                if (!TextUtils.isEmpty(query)) {
                    Query q = bbdd.orderByChild("userName").equalTo(query);

                    q.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                                String clave = datasnapshot.getKey();
                                DatabaseReference ref = bbdd.child(clave);
                                ref.removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(modificarUsuario.this, "El usuario " + query + " se ha borrado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(modificarUsuario.this, "El usuario " + query + " no existe", Toast.LENGTH_SHORT).show();
                }
            }
        });

//CARGAMOS EL LIST VIEW PARA SABER EN TODO MOMENTO EL LISTADO DE USUARIOS....-->
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
                adapter = new ArrayAdapter<String>(modificarUsuario.this, android.R.layout.simple_list_item_1, listado);
                listViewRegistrados.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCancelar:
                cancelar();
                break;
        }
    }

    public void cancelar() {
        Intent i = new Intent(modificarUsuario.this, menuPrincipal.class);
        startActivity(i);
    }


}
