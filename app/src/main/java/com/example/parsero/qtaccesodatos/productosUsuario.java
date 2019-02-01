package com.example.parsero.qtaccesodatos;

import android.app.DownloadManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.parsero.qtaccesodatos.model.Producto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class productosUsuario extends AppCompatActivity {
    Button btnConsultarProductos;
    EditText etNombreUsuario;
    ListView lvProductos;
    DatabaseReference bbdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_usuario);

        bbdd = FirebaseDatabase.getInstance().getReference("productos");

        etNombreUsuario = (EditText) findViewById(R.id.etNombreUsuario);
        lvProductos = (ListView) findViewById(R.id.lvProductos);

        btnConsultarProductos = (Button) findViewById(R.id.btnConsultarProductos);
        btnConsultarProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreUsuario = etNombreUsuario.getText().toString();
                Query q = bbdd.orderByChild("nombreUsuario").equalTo(nombreUsuario);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayAdapter<String> adapter;
                        ArrayList<String> listado = new ArrayList<>();

                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                            Producto p1 = datasnapshot.getValue(Producto.class);

                            String nombre = p1.getNombre();
                            String categoria = p1.getCategoría();
                            String descripcion = p1.getDescripcion();
                            String precio = p1.getPrecio();


                            listado.add("NOMBRE PRODUCTO:" + nombre);
                            listado.add("DESCRIPCION PRODUCTO: " + descripcion);
                            listado.add("PRECIO: " + precio);
                            listado.add("Categoría"+categoria);
                            listado.add("-------------------------------------");
                        }
                        adapter = new ArrayAdapter<String>(productosUsuario.this, android.R.layout.simple_list_item_1, listado);
                        lvProductos.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
