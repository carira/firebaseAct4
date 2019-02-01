package com.example.parsero.qtaccesodatos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.parsero.qtaccesodatos.model.Producto;
import com.example.parsero.qtaccesodatos.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class consultaSimple extends AppCompatActivity {
    ListView lvProductosByCategoria,lvProductosByName;
    EditText etNombreProducto;
    Button btnConsultaCategoria,btnConsultaByName;
    private FirebaseAuth mAuth;
    DatabaseReference bbdd;
    Spinner spCategorias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_simple);

        etNombreProducto = (EditText) findViewById(R.id.etNombreProducto);
        bbdd = FirebaseDatabase.getInstance().getReference("productos");

        spCategorias = (Spinner) findViewById(R.id.spCategorias);
        cargaSpinner();

        lvProductosByCategoria = (ListView) findViewById(R.id.lvProductosByCategoria);

        lvProductosByName = (ListView) findViewById(R.id.lvProductosByName);

        btnConsultaCategoria = (Button) findViewById(R.id.btnConsultaCategoria);
        btnConsultaCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String categoria = spCategorias.getSelectedItem().toString();
                Query q = bbdd.orderByChild("categoría").equalTo(categoria);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayAdapter<String> adapter;
                        ArrayList<String> listado = new ArrayList<>();

                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            Producto p1 = datasnapshot.getValue(Producto.class);
                            String nombre = p1.getNombre();
                            String descripcion = p1.getDescripcion();
                            String precio = p1.getPrecio();


                            listado.add("NOMBRE PRODUCTO:" + nombre);
                            listado.add("DESCRIPCION PRODUCTO: " + descripcion);
                            listado.add("PRECIO: " + precio);

                            listado.add("-------------------------------------");
                        }
                        adapter = new ArrayAdapter<String>(consultaSimple.this, android.R.layout.simple_list_item_1, listado);
                        lvProductosByCategoria.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnConsultaByName = (Button) findViewById(R.id.btnConsultaByName);
        btnConsultaByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = etNombreProducto.getText().toString();

                Query q = bbdd.orderByChild("nombre").equalTo(nombre);

                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayAdapter<String> adapter;
                        ArrayList<String> listado = new ArrayList<>();

                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            Producto p1 = datasnapshot.getValue(Producto.class);
                            String nombre = p1.getNombre();
                            String descripcion = p1.getDescripcion();
                            String preccio = p1.getPrecio();


                            listado.add("NOMBRE PRODUCTO:" + nombre);
                            listado.add("DESCRIPCION PRODUCTO: " + descripcion);
                            listado.add("PRECIO: " + preccio);

                            listado.add("-------------------------------------");
                        }
                        adapter = new ArrayAdapter<String>(consultaSimple.this, android.R.layout.simple_list_item_1, listado);
                        lvProductosByName.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    public void cargaSpinner() {
        ArrayAdapter<String> adaptador;
        ArrayList<String> listadoCategorias = new ArrayList<String>();
        listadoCategorias.add("Null");
        listadoCategorias.add("Coches");
        listadoCategorias.add("Tecnología");
        listadoCategorias.add("Hogar");

        adaptador = new ArrayAdapter<String>(consultaSimple.this, android.R.layout.simple_list_item_1, listadoCategorias);
        spCategorias.setAdapter(adaptador);

    }
}
