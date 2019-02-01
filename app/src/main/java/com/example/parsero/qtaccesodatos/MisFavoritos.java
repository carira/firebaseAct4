package com.example.parsero.qtaccesodatos;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.parsero.qtaccesodatos.model.Producto;
import com.example.parsero.qtaccesodatos.model.Usuario;
import com.example.parsero.qtaccesodatos.model.pFavoritos;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MisFavoritos extends AppCompatActivity {

    ListView lvProductos , lvProductoFavoritos;
    DatabaseReference bbdd;
    EditText etNombreFavorito;
    Button btnAnyadirFavoritos,btnMostrarFavoritos;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_favoritos);
        bbdd = FirebaseDatabase.getInstance().getReference("productos");

        lvProductos = (ListView) findViewById(R.id.lvProductos);
        lvProductoFavoritos =(ListView) findViewById(R.id.lvProductoFavoritos);

        etNombreFavorito = (EditText) findViewById(R.id.etNombreFavorito);

        //AÑADIMOS UN PRODUCTO A FAVORITOS:
        btnAnyadirFavoritos = (Button) findViewById(R.id.btnAnyadirFavoritos);
        btnAnyadirFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = etNombreFavorito.getText().toString();
                Query q = bbdd.orderByChild("nombre").equalTo(nombre);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                            Producto p1 = datasnapshot.getValue(Producto.class);
                            String nombre = p1.getNombre();
                            String precio = p1.getPrecio();
                            String descripcion = p1.getDescripcion();
                            String categoria = p1.getCategoría();
                            String userId = p1.getNombreUsuario();

                            anyadirFavorito(nombre,precio,descripcion,categoria,userId);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        //LIST VIEW
        bbdd.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adapter;
                ArrayList<String> listado = new ArrayList<>();

                for (DataSnapshot datasnapshot : dataSnapshot.getChildren()) {
                    Producto p1 = datasnapshot.getValue(Producto.class);

                    String nombreProducto = p1.getNombre();
                    String descripcionProducto = p1.getDescripcion();
                    String categoria = p1.getCategoría();
                    String precio = p1.getPrecio();


                    listado.add("NOMBRE PRODUCTO" + nombreProducto);
                    listado.add("DESCRIPCION PRODUCTO" + descripcionProducto);
                    listado.add("PRECIO PRODUCTO" + categoria);
                    listado.add("CATEGORIA PRODUCTO" + precio);
                    listado.add("--------------------------");

                }

                adapter = new ArrayAdapter<String>(MisFavoritos.this, android.R.layout.simple_list_item_1, listado);
                lvProductos.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //MIS PRODUCTOS FAVORITOS

        btnMostrarFavoritos = (Button) findViewById(R.id.btnMostrarFavoritos);
        btnMostrarFavoritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bbdd = FirebaseDatabase.getInstance().getReference("usuarios");
                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String claveUser = user.getUid();
                Query q = bbdd.orderByChild("claveUser").equalTo(claveUser);
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot datasnapshot : dataSnapshot.getChildren()){
                        Usuario u1 = datasnapshot.getValue(Usuario.class);
                        String userName1 = u1.getUserName();
                            cargarFavoritosPorUsername(userName1);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }

    public void anyadirFavorito(String nombre, String precio, String descripcion, String categoria, String userId){
        bbdd = FirebaseDatabase.getInstance().getReference("misFavoritos");
        String nombrep = nombre;
        String clave = bbdd.push().getKey();
        pFavoritos pf1 = new pFavoritos(nombre,precio,descripcion,categoria,userId);
        bbdd.child(clave).setValue(pf1);
        Toast.makeText(this, "Product añadido a favoritos", Toast.LENGTH_SHORT).show();
    }

public void cargarFavoritosPorUsername(String username){
    bbdd = FirebaseDatabase.getInstance().getReference("misFavoritos");

    ArrayAdapter<String> adapter;
    ArrayList<String> listado = new ArrayList<>();

    Query q = bbdd.orderByChild("userId").equalTo(username);
    q.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayAdapter<String> adapter;
            ArrayList<String> listado = new ArrayList<>();

            for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                pFavoritos pf2 = datasnapshot.getValue(pFavoritos.class);
                String nombre = pf2.getNombre();
                String precio = pf2.getPrecio();
                String descripcion = pf2.getDescripcion();
                String categoria = pf2.getCategoria();

                listado.add("NOMBRE PRODUCTO" + nombre);
                listado.add("DESCRIPCION PRODUCTO" + descripcion);
                listado.add("PRECIO PRODUCTO" + precio);
                listado.add("CATEGORIA PRODUCTO" + categoria );
                listado.add("--------------------------");
            }
            adapter = new ArrayAdapter<String>(MisFavoritos.this, android.R.layout.simple_list_item_1, listado);
            lvProductoFavoritos.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }
}
